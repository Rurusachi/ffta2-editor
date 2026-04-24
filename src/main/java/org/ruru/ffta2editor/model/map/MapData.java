package org.ruru.ffta2editor.model.map;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

public class MapData {
    public int id;
    
    public int width;
    public int height;
    public byte[] texture;
    public byte[] palette;

    BufferedImage cachedImage;
    BufferedImage cachedTexture;

    public MapData(ByteBuffer mapBytes, ByteBuffer textureBytes, ByteBuffer paletteBytes, int id) {
        this.id = id;
        texture = new byte[textureBytes.remaining()];
        textureBytes.get(texture);
        
        palette = new byte[paletteBytes.remaining()];
        paletteBytes.get(palette);
        
        width = 256;
        height = texture.length / 256;
    }

    public BufferedImage getTexture() {
        if (texture.length == 0) return new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_INDEXED, new IndexColorModel(8, 256, new byte[256], new byte[256], new byte[256], 0));
        if (cachedTexture != null) return cachedTexture;

        ByteBuffer paletteBytes = ByteBuffer.wrap(palette).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer imageBytes = ByteBuffer.wrap(texture).order(ByteOrder.LITTLE_ENDIAN);

        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        int numColors = 256;
        for (int i = 0; i < numColors; i++) {
            short color = paletteBytes.getShort();
            byte red = (byte)(color & 0x1F);
            byte green = (byte)((color >> 5) & 0x1F);
            byte blue = (byte)((color >> 10) & 0x1F);
            reds[i] = (byte)(red*255 / 31);
            greens[i] = (byte)(green*255 / 31);
            blues[i] = (byte)(blue*255 / 31);
        }
        IndexColorModel indexColorModel = new IndexColorModel(8, 256, reds, greens, blues, 0);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, indexColorModel);

        for (int i = 0; i < width*height; i++) {
            int x = i % width;
            int y = i / width;

            int pixel = Byte.toUnsignedInt(imageBytes.get());
            bufferedImage.setRGB(x, y, indexColorModel.getRGB(pixel));
        }
        cachedTexture = bufferedImage;
        return bufferedImage;
    }
}
