package org.ruru.ffta2editor.model.map;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Logger;

public class MapData {
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    public int id;
    
    public int width;
    public int height;
    public byte[] texture;
    public byte[][] palettes;

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

    HashMap<Integer, BufferedImage> cachedImages = new HashMap<>();
    HashMap<Integer, BufferedImage> cachedTextures = new HashMap<>();

    private static final byte[] PRTS = "PRTS".getBytes(StandardCharsets.UTF_8);
    private static final byte[] PLTT = "PLTT".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ANIM = "ANIM".getBytes(StandardCharsets.UTF_8); // TODO: Reverse engineer section
    private static final byte[] PANL = "PANL".getBytes(StandardCharsets.UTF_8); // TODO: Reverse engineer section
    private static final byte[] TEX  = "TEX ".getBytes(StandardCharsets.UTF_8); // TODO: Reverse engineer section
    public MapData(ByteBuffer mapBytes, ByteBuffer textureBytes, ByteBuffer mapCtrlBytes, int id) {
        this.id = id;
        texture = new byte[textureBytes.remaining()];
        textureBytes.get(texture);
        

        int mapCtrlBytesPos = mapCtrlBytes.position();
        mapCtrlBytes.getShort(); // ??
        int numTextures = mapCtrlBytes.get(); // Always 1?
        int numPalettes = mapCtrlBytes.get();
        mapCtrlBytes.position(mapCtrlBytesPos + 0x14);

        palettes = new byte[numPalettes][512];
        
        width = 256;
        height = texture.length / 256;

        readPRTS(mapBytes);
        readPLTT(mapBytes);
    }

    private ByteBuffer getSection(ByteBuffer mapBytes, byte[] name) {
        mapBytes.rewind();

        byte[] temp = new byte[4];
        boolean foundSection = false;
        while (mapBytes.remaining() >= 4) {
            mapBytes.get(temp);
            if (Arrays.equals(temp, name)) {
                //logger.info(String.format("Map %d: PRTS at %d", id, mapBytes.position()-4));
                foundSection = true;
                break;
            }
            mapBytes.position(mapBytes.position()-3);
        }
        if (foundSection) {
            int pos = mapBytes.position();
            int lenParts = mapBytes.getInt();
            return mapBytes.slice(pos, lenParts-4).order(ByteOrder.LITTLE_ENDIAN);
        }
        return null;
    }

    private void readPRTS(ByteBuffer mapBytes) {
        ByteBuffer PRTSBytes = getSection(mapBytes, PRTS);
        if (PRTSBytes != null) {
            int lenParts = PRTSBytes.getInt();
            int numParts = PRTSBytes.getInt();

            //logger.info(String.format("Map %d: PRTS: length=%d num=%d", id, lenParts, numParts));

            parts = new Part[numParts];

            for (int i = 0; i < parts.length; i++) {
                parts[i] = new Part(PRTSBytes);
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

    private void readPLTT(ByteBuffer mapBytes) {
        ByteBuffer PLTTBytes = getSection(mapBytes, PLTT);
        if (PLTTBytes != null) {
            int lenParts = PLTTBytes.getInt();
            int numParts = PLTTBytes.getInt();

            // TODO: Figure out what this section does

        } else {
            logger.info(String.format("Map %d: PLTT not found", id));
        }

    }

    public void loadPalette(ByteBuffer paletteBytes, int i) {
        paletteBytes.get(palettes[i]);
    }

    public BufferedImage getImage(int paletteIndex) {
        if (texture.length == 0) return new BufferedImage(64, 96, BufferedImage.TYPE_BYTE_INDEXED, new IndexColorModel(8, 256, new byte[256], new byte[256], new byte[256], 0));
        BufferedImage cachedImage = cachedImages.getOrDefault(paletteIndex, null);
        if (cachedImage != null) return cachedImage;

        BufferedImage sourceImage = getTexture(paletteIndex);

        Part leftMost = Arrays.stream(parts).min(Comparator.comparingInt(x -> x.target[0].x)).get();
        Part rightMost = Arrays.stream(parts).max(Comparator.comparingInt(x -> x.target[2].x)).get();
        Part topMost = Arrays.stream(parts).min(Comparator.comparingInt(x -> x.target[0].y)).get();
        Part bottomMost = Arrays.stream(parts).max(Comparator.comparingInt(x -> x.target[2].y)).get();
        int fullWidth = rightMost.target[2].x - leftMost.target[0].x;
        int fullHeight = bottomMost.target[2].y - topMost.target[0].y;

        logger.info(String.format("Map %d: (%d - %d = %d, %d - %d = %d)", id, rightMost.target[2].x, leftMost.target[0].x, fullWidth, bottomMost.target[2].y, topMost.target[0].y, fullHeight));

        BufferedImage fullImage = new BufferedImage(fullWidth, fullHeight, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)sourceImage.getColorModel());

        for (int j = 0; j < parts.length; j++) {
            Part piece = parts[j];

            if (piece.isFX) continue; // TODO: Figure out how these should be drawn

            byte[] pixels = (byte[])sourceImage.getRaster().getDataElements(piece.source[0].x, piece.source[0].y, piece.source[2].x - piece.source[0].x, piece.source[2].y - piece.source[0].y, null);
            int targetY = piece.target[0].y - topMost.target[0].y;
            int targetX = piece.target[0].x - leftMost.target[0].x;

            logger.info(String.format("%d: (%d, %d) %d x %d", j, targetX, targetY, piece.target[2].x - piece.target[0].x, piece.target[2].y - piece.target[0].y));


            int width = piece.source[2].x - piece.source[0].x;
            int height = piece.source[2].y - piece.source[0].y;

            // Manually copy in pixels one at a time to be able to skip transparent pixels
            BufferedImage subImage = fullImage.getSubimage(targetX, targetY, piece.target[2].x - piece.target[0].x, piece.target[2].y - piece.target[0].y);
            byte[] temp = new byte[1];
            for (int p = 0; p < pixels.length; p++) {
                if (pixels[p] != 0) {
                    temp[0] = pixels[p];
                    subImage.getRaster().setDataElements(p % width, p / width, temp);
                }
            }
        }

        cachedImages.put(paletteIndex, fullImage);
        return fullImage;
    }

    public BufferedImage getTexture(int paletteIndex) {
        if (texture.length == 0) return new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_INDEXED, new IndexColorModel(8, 256, new byte[256], new byte[256], new byte[256], 0));
        BufferedImage cachedTexture = cachedTextures.getOrDefault(paletteIndex, null);
        if (cachedTexture != null) return cachedTexture;

        ByteBuffer paletteBytes = ByteBuffer.wrap(palettes[paletteIndex]).order(ByteOrder.LITTLE_ENDIAN);
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
        
        cachedTextures.put(paletteIndex, bufferedImage);
        return bufferedImage;
    }
}
