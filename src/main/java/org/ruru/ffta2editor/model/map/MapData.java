package org.ruru.ffta2editor.model.map;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

public class MapData {
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    public int id;
    
    public int width;
    public int height;
    public byte[] texture;
    public byte[] palette;

    public class Part {
        public class Point {
            public short x;
            public short y;
            public Point(short x, short y) {
                this.x = x;
                this.y = y;
            }
        }
        public Point[] source = new Point[4];

        public Point[] target = new Point[4];

        byte primaryIndex;
        byte FXIndex;
        boolean isFX;

        public Part(ByteBuffer bytes) {
            target[0] = new Point(bytes.getShort(), bytes.getShort());
            bytes.getShort();
            source[0] = new Point(bytes.getShort(), bytes.getShort());
            bytes.getShort();

            target[1] = new Point(bytes.getShort(), bytes.getShort());
            bytes.getShort();
            source[1] = new Point(bytes.getShort(), bytes.getShort());
            bytes.getShort();

            target[2] = new Point(bytes.getShort(), bytes.getShort());
            bytes.getShort();
            source[2] = new Point(bytes.getShort(), bytes.getShort());
            bytes.getShort();

            target[3] = new Point(bytes.getShort(), bytes.getShort());
            bytes.getShort();
            source[3] = new Point(bytes.getShort(), bytes.getShort());
            bytes.getShort();

            bytes.getShort();
            bytes.get();
            primaryIndex = bytes.get();
            bytes.getShort();
            bytes.get();
            FXIndex = bytes.get();
            isFX = (bytes.getShort() & 1) == 1;
            bytes.getShort();
        }
    }
    public Part[] parts;

    BufferedImage cachedImage;
    BufferedImage cachedTexture;

    private static final byte[] PRTS = "PRTS".getBytes(StandardCharsets.UTF_8);
    public MapData(ByteBuffer mapBytes, ByteBuffer textureBytes, ByteBuffer paletteBytes, int id) {
        this.id = id;
        texture = new byte[textureBytes.remaining()];
        textureBytes.get(texture);
        
        palette = new byte[paletteBytes.remaining()];
        paletteBytes.get(palette);
        
        width = 256;
        height = texture.length / 256;

        byte[] temp = new byte[4];
        boolean foundPRTS = false;
        while (mapBytes.remaining() >= 4) {
            mapBytes.get(temp);
            if (Arrays.equals(temp, PRTS)) {
                //logger.info(String.format("Map %d: PRTS at %d", id, mapBytes.position()-4));
                foundPRTS = true;
                break;
            }
            mapBytes.position(mapBytes.position()-3);
        }
        if (foundPRTS) {
            int lenParts = mapBytes.getInt();
            int numParts = mapBytes.getInt();

            //logger.info(String.format("Map %d: PRTS: length=%d num=%d", id, lenParts, numParts));

            parts = new Part[numParts];

            for (int i = 0; i < parts.length; i++) {
                parts[i] = new Part(mapBytes);
            }
            int curr = 0;
            Part[] orderedParts = new Part[numParts];
            for (Part p : Arrays.stream(parts).filter(x -> x.isFX).sorted(Comparator.comparingInt(x -> x.FXIndex)).toList().reversed()) {
                orderedParts[curr++] = p;
            };
            for (Part p : Arrays.stream(parts).filter(x -> !x.isFX).sorted(Comparator.comparingInt(x -> x.primaryIndex)).toList().reversed()) {
                orderedParts[curr++] = p;
            };
            parts = orderedParts;
            //Arrays.sort(parts, Comparator.comparingInt(x -> x.primaryIndex));
        } else {
            logger.info(String.format("Map %d: PRTS not found", id));
        }


    }

    public BufferedImage getImage() {
        if (texture.length == 0) return new BufferedImage(64, 96, BufferedImage.TYPE_BYTE_INDEXED, new IndexColorModel(8, 256, new byte[256], new byte[256], new byte[256], 0));
        if (cachedImage != null) return cachedImage;

        BufferedImage sourceImage = getTexture();

        Part leftMost = Arrays.stream(parts).min(Comparator.comparingInt(x -> x.target[0].x)).get();
        Part rightMost = Arrays.stream(parts).max(Comparator.comparingInt(x -> x.target[2].x)).get();
        Part topMost = Arrays.stream(parts).min(Comparator.comparingInt(x -> x.target[0].y)).get();
        Part bottomMost = Arrays.stream(parts).max(Comparator.comparingInt(x -> x.target[2].y)).get();
        int fullWidth = rightMost.target[2].x - leftMost.target[0].x;
        int fullHeight = bottomMost.target[2].y - topMost.target[0].y;

        //fullWidth = 1024;
        //fullHeight = 1024;

        logger.info(String.format("Map %d: (%d - %d = %d, %d - %d = %d)", id, rightMost.target[2].x, leftMost.target[0].x, fullWidth, bottomMost.target[2].y, topMost.target[0].y, fullHeight));

        //logger.info(String.format("Map %d: (%d, %d)", id, fullWidth, fullHeight));

        BufferedImage fullImage = new BufferedImage(fullWidth, fullHeight, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)sourceImage.getColorModel());

        for (int j = 0; j < parts.length; j++) {
            Part piece = parts[j];

            if (piece.isFX) continue;

            byte[] pixels = (byte[])sourceImage.getRaster().getDataElements(piece.source[0].x, piece.source[0].y, piece.source[2].x - piece.source[0].x, piece.source[2].y - piece.source[0].y, null);
            int targetY = piece.target[0].y - topMost.target[0].y;
            int targetX = piece.target[0].x - leftMost.target[0].x;

            logger.info(String.format("%d: (%d, %d) %d x %d", j, targetX, targetY, piece.target[2].x - piece.target[0].x, piece.target[2].y - piece.target[0].y));


            int width = piece.source[2].x - piece.source[0].x;
            int height = piece.source[2].y - piece.source[0].y;

            BufferedImage subImage = fullImage.getSubimage(targetX, targetY, piece.target[2].x - piece.target[0].x, piece.target[2].y - piece.target[0].y);
            byte[] temp = new byte[1];
            for (int p = 0; p < pixels.length; p++) {
                if (pixels[p] != 0) {
                    temp[0] = pixels[p];
                    subImage.getRaster().setDataElements(p % width, p / width, temp);
                }
            }
            //fullImage.getRaster().setDataElements(targetX, targetY, piece.target[2].x - piece.target[0].x, piece.target[2].y - piece.target[0].y, pixels);
        }

        cachedImage = fullImage;
        return fullImage;
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
