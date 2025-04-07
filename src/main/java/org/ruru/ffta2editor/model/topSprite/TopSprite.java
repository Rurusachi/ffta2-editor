package org.ruru.ffta2editor.model.topSprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TopSprite {
    public int id;

    public byte[] palette;
    public ListProperty<byte[]> sprites = new SimpleListProperty<>();
    byte[] remainingBytes;
    
    public boolean hasChanged = false;
    HashMap<Integer, BufferedImage> cachedImages = new HashMap<>();

    public TopSprite(ByteBuffer bytes, int id) {
        this.id = id;

        palette = new byte[32];
        bytes.get(palette);

        int numSprites = bytes.get();
        //int numSprites = 5;

        ObservableList<byte[]> spriteList = FXCollections.observableArrayList();
        for (int i = 0; i < numSprites; i++) {
            byte[] newSprite = new byte[0x20 * 4 * 5];
            bytes.get(newSprite);
            spriteList.add(newSprite);
        }
        sprites.set(spriteList);

        remainingBytes = new byte[bytes.remaining()];
        bytes.get(remainingBytes);
    }

    public TopSprite(int id) {
        this.id = id;

        palette = new byte[0];

        int numSprites = 0;
        //int numSprites = 5;

        ObservableList<byte[]> spriteList = FXCollections.observableArrayList();
        sprites.set(spriteList);

        remainingBytes = new byte[0];
    }

    public BufferedImage getSprite(int spriteIndex, int scale) {
        BufferedImage image = getSprite(spriteIndex);
        if (scale == 1) return image;
        
        BufferedImage scaledImage = new BufferedImage(image.getWidth()*scale, image.getHeight()*scale, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)image.getColorModel());
        Graphics2D graphics = scaledImage.createGraphics();
        graphics.drawImage(image.getScaledInstance(image.getWidth()*scale, image.getHeight()*scale, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        graphics.dispose();
        return scaledImage;
    }

    public BufferedImage getSprite(int spriteIndex) {
        if (spriteIndex >= sprites.size()) return new BufferedImage(32, 40, BufferedImage.TYPE_BYTE_INDEXED, new IndexColorModel(4, 16, new byte[16], new byte[16], new byte[16], 0));
        ByteBuffer spriteBuffer = ByteBuffer.wrap(sprites.get(spriteIndex)).order(ByteOrder.LITTLE_ENDIAN);

        BufferedImage[] imagePieces = new BufferedImage[4*5];
        for (int i = 0; i < imagePieces.length; i++) {
            imagePieces[i] = toImage(spriteBuffer);
        }

        BufferedImage fullImage = new BufferedImage(32, 40, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)imagePieces[0].getColorModel());
        Graphics2D graphics = fullImage.createGraphics();
        for (int j = 0; j < imagePieces.length; j++) {
            int x = j % 4;
            int y = j / 4;
            graphics.drawImage(imagePieces[j], x*8, y*8, null);
        }
        graphics.dispose();
        return fullImage;
    }

    private BufferedImage toImage(ByteBuffer imageBytes) {
        ByteBuffer paletteBytes = ByteBuffer.wrap(palette).order(ByteOrder.LITTLE_ENDIAN);

        byte[] reds = new byte[16];
        byte[] greens = new byte[16];
        byte[] blues = new byte[16];
        int numColors = 16;
        for (int i = 0; i < numColors; i++) {
            short color = paletteBytes.getShort();
            byte red = (byte)(color & 0x1F);
            byte green = (byte)((color >> 5) & 0x1F);
            byte blue = (byte)((color >> 10) & 0x1F);
            reds[i] = (byte)(red*255 / 31);
            greens[i] = (byte)(green*255 / 31);
            blues[i] = (byte)(blue*255 / 31);
        }
        IndexColorModel indexColorModel = new IndexColorModel(4, 16, reds, greens, blues, 0);

        BufferedImage bufferedImage = new BufferedImage(8, 8, BufferedImage.TYPE_BYTE_INDEXED, indexColorModel);

        int pixelByte = 0;
        int pixel = 0;
        for (int i = 0; i < 8*8; i++) {
            if (i % 2 == 0) {
                pixelByte = Byte.toUnsignedInt(imageBytes.get());
                pixel = pixelByte & 0xf;
            } else {
                pixel = pixelByte >>> 4;
            }
            int x = i % 8;
            int y = i / 8;

            bufferedImage.setRGB(x, y, indexColorModel.getRGB(pixel));
        }
        return bufferedImage;
    }

    public void setSprite(int spriteIndex, BufferedImage newImage) {
        BufferedImage[] imagePieces = new BufferedImage[4*5];
        for (int i = 0; i < imagePieces.length; i++) {
            int x = i % 4;
            int y = i / 4;
            imagePieces[i] = newImage.getSubimage(x*8, y*8, 8, 8);
        }
        ByteBuffer spriteBytes = ByteBuffer.allocate(imagePieces.length*0x20).order(ByteOrder.LITTLE_ENDIAN);
        for (BufferedImage image : imagePieces) {
            for (int i = 0; i < 8*8; i+=2) {
                int x = i % 8;
                int y = i / 8;
                byte[] pixels = (byte[])image.getRaster().getDataElements(x, y, 2, 1, null);
                int b = (pixels[1] << 4) | pixels[0];
                spriteBytes.put((byte)b);
            }
        }
        sprites.set(spriteIndex, spriteBytes.array());
        hasChanged = true;
        cachedImages.remove(spriteIndex);
    }

    public void setPalette(BufferedImage image) {
        IndexColorModel colorModel = (IndexColorModel)image.getColorModel();
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        colorModel.getReds(reds);
        colorModel.getGreens(greens);
        colorModel.getBlues(blues);
        ByteBuffer paletteBytes = ByteBuffer.allocate(0x20).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 16; i++) {
            int color = (((Byte.toUnsignedInt(blues[i])*31)/255) << 10) | (((Byte.toUnsignedInt(greens[i])*31)/255) << 5) | ((Byte.toUnsignedInt(reds[i])*31)/255);
            paletteBytes.putShort((short)color);
        }

        palette = paletteBytes.array();
        hasChanged = true;
        cachedImages.clear();
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x21 + (0x20 * 4 * 5) * sprites.size() + remainingBytes.length).order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(palette);
        buffer.put((byte)sprites.size());

        for (byte[] sprite : sprites) {
            buffer.put(sprite);
        }
        buffer.put(remainingBytes);

        return buffer.array();
    }
}
