package org.ruru.ffta2editor.utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.model.unitSst.SpriteData;
import org.ruru.ffta2editor.model.unitSst.SpriteData.SpriteMap;
import org.ruru.ffta2editor.model.unitSst.SpriteData.SpritePiece;
import org.ruru.ffta2editor.model.unitSst.SpritePalettes;
import org.ruru.ffta2editor.utility.LZSS.LZSSDecodeResult;

import javafx.util.Pair;

public class UnitSprite {
    public SpriteData spriteData;
    public SpritePalettes spritePalettes;
    public int unitIndex;
    ByteBuffer compressedSpriteBytes;
    ArrayList<ByteBuffer> compressedSpriteList = new ArrayList<>();
    public boolean hasChanged = false;
    ArrayList<HashMap<Integer, BufferedImage>> cachedImages = new ArrayList<>();


    public UnitSprite(int unitIndex, SpriteData spriteData, ByteBuffer spritePaletteBytes, ByteBuffer compressedSpriteBytes) {
        this.unitIndex = unitIndex;
        this.spriteData = spriteData;
        this.compressedSpriteBytes = compressedSpriteBytes.duplicate().order(ByteOrder.LITTLE_ENDIAN);

        try {
            SpriteMap spriteMap = spriteData.spriteMaps.get(0);
            LZSSDecodeResult decodedSprite = LZSS.decode(compressedSpriteBytes.position(spriteMap.spriteOffset()));
            int paletteSize = 32;
            if (spritePaletteBytes.remaining() % 0x100 == 0) {
                while (decodedSprite.decodedData.remaining() > 0) {
                    byte color = decodedSprite.decodedData.get();
                    if (color != 0 && Byte.toUnsignedInt(color) < 224) {
                        System.out.println("128 colors");
                        paletteSize = 128;
                        break;
                    }
                }
            }
            this.spritePalettes = new SpritePalettes(spritePaletteBytes, paletteSize);

            for (SpriteMap sMap : spriteData.spriteMaps) {
                decodedSprite = LZSS.decode(compressedSpriteBytes.position(sMap.spriteOffset()));
                compressedSpriteList.add(compressedSpriteBytes.slice(sMap.spriteOffset(), decodedSprite.compressedSize).order(ByteOrder.LITTLE_ENDIAN));
            }

            for (int i = 0; i < spritePalettes.palettes.size(); i++) {
                cachedImages.add(new HashMap<>());
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public BufferedImage getSprite(int spriteIndex, int paletteIndex, int scale) {
        if (paletteIndex >= spritePalettes.palettes.size()) paletteIndex = 0;
        BufferedImage image = getSprite(spriteIndex, paletteIndex);
        if (scale == 1) return image;
        
        BufferedImage scaledImage = new BufferedImage(image.getWidth()*scale, image.getHeight()*scale, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)image.getColorModel());
        Graphics2D graphics = scaledImage.createGraphics();
        graphics.drawImage(image.getScaledInstance(image.getWidth()*scale, image.getHeight()*scale, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        graphics.dispose();
        return scaledImage;
    }

    public BufferedImage getSprite(int spriteIndex, int paletteIndex) {
        if (paletteIndex >= spritePalettes.palettes.size()) paletteIndex = 0;
        if (paletteIndex < cachedImages.size()) {
            BufferedImage cachedImage = cachedImages.get(paletteIndex).getOrDefault(spriteIndex, null);
            if (cachedImage != null) return cachedImage;
        }
        try {
            ByteBuffer palette = ByteBuffer.wrap(spritePalettes.palettes.get(paletteIndex));
            SpriteMap  spriteMap = spriteData.spriteMaps.get(spriteIndex);
            LZSSDecodeResult decodedSprite = LZSS.decode(compressedSpriteList.get(spriteIndex).rewind());
            BufferedImage[] imagePieces = new BufferedImage[spriteMap.pieces().length];
            for (int i = 0; i < imagePieces.length; i++) {
                palette.rewind();
                SpritePiece piece = spriteData.spriteMaps.get(spriteIndex).pieces()[i];
                imagePieces[i] = toImage(piece.width(), piece.height(), palette, decodedSprite.decodedData);
            }
            SpritePiece leftMost = Arrays.stream(spriteMap.pieces()).min(Comparator.comparingInt(x -> x.offsetX())).get();
            SpritePiece rightMost = Arrays.stream(spriteMap.pieces()).max(Comparator.comparingInt(x -> x.offsetX() + x.width())).get();
            SpritePiece topMost = Arrays.stream(spriteMap.pieces()).min(Comparator.comparingInt(x -> x.offsetY())).get();
            SpritePiece bottomMost = Arrays.stream(spriteMap.pieces()).max(Comparator.comparingInt(x -> x.offsetY()  + x.height())).get();
            int fullWidth = (rightMost.offsetX() + rightMost.width()) - (leftMost.offsetX());
            int fullHeight = (bottomMost.offsetY() + bottomMost.height()) - (topMost.offsetY());
            //System.out.println(String.format("%d, %d (%d, %d)", fullWidth, fullHeight, leftMost.offsetX(), topMost.offsetY()));

            BufferedImage fullImage = new BufferedImage(fullWidth, fullHeight, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)imagePieces[0].getColorModel());
            Graphics2D graphics = fullImage.createGraphics();
            for (int j = 0; j < imagePieces.length; j++) {
                SpritePiece piece = spriteMap.pieces()[j];
                int xPos = piece.offsetX() - leftMost.offsetX();
                int yPos = piece.offsetY() - topMost.offsetY();
                //System.out.println(String.format("x: %d, y: %d (%d, %d)", xPos, yPos, spriteMap.pieces()[j].width(), spriteMap.pieces()[j].height()));
                graphics.drawImage(imagePieces[j], xPos, yPos, null);
            }
            graphics.dispose();
            cachedImages.get(paletteIndex).put(spriteIndex, fullImage);
            return fullImage;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public void setSprite(int spriteIndex, BufferedImage newImage) {
        SpriteMap  spriteMap = spriteData.spriteMaps.get(spriteIndex);
        SpritePiece leftMost = Arrays.stream(spriteMap.pieces()).min(Comparator.comparingInt(x -> x.offsetX())).get();
        SpritePiece topMost = Arrays.stream(spriteMap.pieces()).min(Comparator.comparingInt(x -> x.offsetY())).get();
        BufferedImage[] imagePieces = new BufferedImage[spriteMap.pieces().length];
        for (int i = 0; i < imagePieces.length; i++) {
            SpritePiece piece = spriteMap.pieces()[i];
            int xPos = piece.offsetX() - leftMost.offsetX();
            int yPos = piece.offsetY() - topMost.offsetY();
            imagePieces[i] = newImage.getSubimage(xPos, yPos, piece.width(), piece.height());
        }
        ByteBuffer spriteBytes = ByteBuffer.allocate(IntStream.range(0, imagePieces.length).map(i -> imagePieces[i].getWidth() * imagePieces[i].getHeight()).sum());
        for (BufferedImage image : imagePieces) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    //IndexColorModel colorModel = (IndexColorModel)image.getColorModel();
                    //var rgb = image.getRGB(x, y);
                    byte[] b = (byte[])image.getRaster().getDataElements(x, y, null);
                    spriteBytes.put(b[0]);
                }
            }
        }
        ByteBuffer compressedSprite = LZSS.encode(spriteBytes.rewind(), true);
        //newSprites.add(new Pair<Integer,ByteBuffer>(spriteIndex, compressedSprite));
        compressedSpriteList.set(spriteIndex, compressedSprite);
        hasChanged = true;
        for (HashMap<Integer, BufferedImage> cache : cachedImages) {
            cache.remove(spriteIndex);
        }
    }

    public void setPalette(int paletteIndex, BufferedImage image) {
        IndexColorModel colorModel = (IndexColorModel)image.getColorModel();
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        colorModel.getReds(reds);
        colorModel.getGreens(greens);
        colorModel.getBlues(blues);
        ByteBuffer paletteBytes = ByteBuffer.allocate(spritePalettes.numColors*2).order(ByteOrder.LITTLE_ENDIAN);

        int offset = spritePalettes.numColors == 32 ? 256-32 : 0;
        for (int i = 0; i < spritePalettes.numColors; i++) {
            int color = (((Byte.toUnsignedInt(blues[i+offset])*31)/255) << 10) | (((Byte.toUnsignedInt(greens[i+offset])*31)/255) << 5) | ((Byte.toUnsignedInt(reds[i+offset])*31)/255);
            //System.out.println(String.format("red: %02X, green: %02X, blue: %02X, color: red: %04X", Byte.toUnsignedInt(reds[i+offset])*31/255, Byte.toUnsignedInt(greens[i+offset])*31/255, Byte.toUnsignedInt(blues[i+offset])*31/255, color));
            paletteBytes.putShort((short)color);
        }

        spritePalettes.palettes.set(paletteIndex, paletteBytes.array());
        hasChanged = true;
        cachedImages.get(paletteIndex).clear();
    }

    public void addPalette(BufferedImage image) {
        cachedImages.add(new HashMap<>());
        spritePalettes.palettes.add(null);
        setPalette(spritePalettes.palettes.size()-1, image);
    }

    public ByteBuffer savePalettes() {
        return spritePalettes.toByteBuffer();
    }

    /* 
     * Returns compressed sprite data and compressed sprites (unitsst 00FF and unitcg)
    */
    public Pair<ByteBuffer, ByteBuffer> saveSprites() {
        //if (newSprites.size() == 0) return new Pair<byte[], byte[]>(spriteData.toBytes(), compressedSpriteBytes.array());
        //
        //
        //
        //List<Integer> replacedIndexes = newSprites.stream().mapToInt(x -> x.getKey()).boxed().toList();
        //int combinedCompressedSize = IntStream.range(0, spriteData.spriteMaps.size())
        //                            .filter(i -> !replacedIndexes.contains(i))
        //                            .map(i -> compressedSpriteList.get(i).rewind().remaining())
        //                            .sum(); //+ newSprites.stream().mapToInt(x -> x.getValue().rewind().remaining()).sum();
        //

        int combinedCompressedSize = compressedSpriteList.stream().mapToInt(x -> x.rewind().remaining()).sum();
        ByteBuffer compressedSprites = ByteBuffer.allocate(combinedCompressedSize).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < compressedSpriteList.size(); i++) {
            int newOffset = compressedSprites.position();
            SpriteMap prevMap = spriteData.spriteMaps.get(i);
            SpriteMap newMap = new SpriteMap(prevMap.unkownValue(), newOffset, prevMap.pieces());
            spriteData.spriteMaps.set(i, newMap);
            compressedSprites.put(compressedSpriteList.get(i));
        }
        ByteBuffer compressedSpriteData = LZSS.encode(spriteData.toByteBuffer());
        return new Pair<ByteBuffer, ByteBuffer>(compressedSpriteData, compressedSprites.rewind());
    }

    private BufferedImage toImage(int width, int height, ByteBuffer paletteBytes, ByteBuffer imageBytes) {
        paletteBytes.order(ByteOrder.LITTLE_ENDIAN);
        imageBytes.order(ByteOrder.LITTLE_ENDIAN);

        
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        int numColors = 32;
        if (paletteBytes.remaining() == 0x100) {
            numColors = 128;
        }
        reds[0] = (byte)255;
        greens[0] = (byte)0;
        blues[0] = (byte)255;
        int i = numColors == 32 ? 224 : 0;
        for (; paletteBytes.remaining() > 0; i++) {
            short color = paletteBytes.getShort();
            byte red = (byte)(color & 0x1F);
            byte green = (byte)((color >> 5) & 0x1F);
            byte blue = (byte)((color >> 10) & 0x1F);
            reds[i] = (byte)(red*255 / 31);
            greens[i] = (byte)(green*255 / 31);
            blues[i] = (byte)(blue*255 / 31);
        }
        //i = numColors == 32 ? 0 : 255;
        IndexColorModel indexColorModel = new IndexColorModel(8, 256, reds, greens, blues, 0);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, indexColorModel);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = Byte.toUnsignedInt(imageBytes.get());
                //if (pixel != 0 && numColors == 32) {
                //    System.out.println(pixel);
                //    pixel -= 255;
                //    //System.out.println(indexColorModel.getRGB(pixel));
                //};
                //int rgb = ((reds[pixel]*8) << 16) | ((greens[pixel]*8) << 8) | (blues[pixel]*8);
                bufferedImage.setRGB(x, y, indexColorModel.getRGB(pixel));
            }
        }
        return bufferedImage;
    }
}
