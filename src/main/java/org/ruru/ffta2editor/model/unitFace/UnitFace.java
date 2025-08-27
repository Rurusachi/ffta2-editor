package org.ruru.ffta2editor.model.unitFace;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class UnitFace {

    public class PieceMapping {
        public short sourceX;
        public short sourceY;
        public short sourceWidth;
        public short sourceHeight;

        public short offsetX;
        public short offsetY;
        public short targetWidth;
        public short targetHeight;

        public PieceMapping() {

        }

        public void setSource(ByteBuffer bytes) {
            sourceX = bytes.getShort();
            sourceY = bytes.getShort();
            sourceWidth = bytes.getShort();
            sourceHeight = bytes.getShort();
        }

        public void setTarget(ByteBuffer bytes) {
            offsetX = bytes.getShort();
            offsetY = bytes.getShort();
            targetWidth = bytes.getShort();
            targetHeight = bytes.getShort();
        }
    }

    public int id;

    public byte _0x0;
    public byte _0x1;
    public short width;
    public short height;
    public byte _0x6;
    
    public PieceMapping[] pieces;

    public byte[] image;
    public byte[] palette;
    byte[] remainingBytes;

    public boolean hasChanged = false;
    BufferedImage cachedImage;
    BufferedImage cachedTexture;

    public UnitFace(ByteBuffer bytes, int id) {
        this.id = id;
        
        _0x0 = bytes.get();
        _0x1 = bytes.get();
        width = bytes.getShort();
        height = bytes.getShort();
        _0x6 = bytes.get();

        int numPieces = bytes.get();
        pieces = new PieceMapping[numPieces];
        for (int i = 0; i < numPieces; i++) {
            pieces[i] = new PieceMapping();
            pieces[i].setSource(bytes);
        }
        for (int i = 0; i < numPieces; i++) {
            pieces[i].setTarget(bytes);
            if (pieces[i].sourceWidth != pieces[i].targetWidth || pieces[i].sourceHeight != pieces[i].targetHeight) {
                System.out.println(id);
            }
        }


        if (_0x0 != 0) {
            System.out.println(String.format("%d: 0x0 is %02X", id, _0x0));
        }

        if (_0x1 != 2) {
            System.out.println(String.format("%d: _0x1 is  %02X", id, _0x1));
        }
        if (_0x6 != 2) {
            System.out.println(String.format("%d: _0x6 is  %02X", id, _0x6));
        }

        

        image = new byte[width*height];
        bytes.get(image);

        palette = new byte[0x200];
        bytes.get(palette);

        remainingBytes = new byte[bytes.remaining()];
        bytes.get(remainingBytes);
    }

    public UnitFace(int id) {
        this.id = id;

        _0x0 = 0;
        _0x1 = 0;
        width = 0;
        height = 0;
        _0x6 = 0;

        pieces = new PieceMapping[0];
        image = new byte[0];
        palette = new byte[0];

        remainingBytes = new byte[0];
    }

    public BufferedImage getImage(int scale) {
        BufferedImage image = getImage();
        if (scale == 1) return image;
        
        BufferedImage scaledImage = new BufferedImage(image.getWidth()*scale, image.getHeight()*scale, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)image.getColorModel());
        Graphics2D graphics = scaledImage.createGraphics();
        graphics.drawImage(image.getScaledInstance(image.getWidth()*scale, image.getHeight()*scale, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        graphics.dispose();
        return scaledImage;
    }

    public BufferedImage getImage() {
        if (image.length == 0) return new BufferedImage(64, 96, BufferedImage.TYPE_BYTE_INDEXED, new IndexColorModel(8, 256, new byte[256], new byte[256], new byte[256], 0));
        if (cachedImage != null) return cachedImage;

        BufferedImage sourceImage = getTexture();

        PieceMapping leftMost = Arrays.stream(pieces).min(Comparator.comparingInt(x -> x.offsetX)).get();
        PieceMapping rightMost = Arrays.stream(pieces).max(Comparator.comparingInt(x -> x.offsetX + x.sourceWidth)).get();
        PieceMapping topMost = Arrays.stream(pieces).min(Comparator.comparingInt(x -> -x.offsetY)).get();
        PieceMapping bottomMost = Arrays.stream(pieces).max(Comparator.comparingInt(x -> x.sourceHeight - x.offsetY)).get();
        int fullWidth = (rightMost.offsetX + rightMost.sourceWidth) - (leftMost.offsetX);
        int fullHeight = (bottomMost.sourceHeight - bottomMost.offsetY) + (topMost.offsetY);

        BufferedImage fullImage = new BufferedImage(fullWidth, fullHeight, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)sourceImage.getColorModel());
        
        for (int j = 0; j < pieces.length; j++) {
            PieceMapping piece = pieces[j];
            
            byte[] pixels = (byte[])sourceImage.getRaster().getDataElements(piece.sourceX, piece.sourceY, piece.sourceWidth, piece.sourceHeight, null);
            int targetY = topMost.offsetY - piece.offsetY;
            int targetX = piece.offsetX - leftMost.offsetX;
            fullImage.getRaster().setDataElements(targetX, targetY, piece.sourceWidth, piece.sourceHeight, pixels);
        }
        cachedImage = fullImage;
        return fullImage;
    }

    public BufferedImage getTexture() {
        if (image.length == 0) return new BufferedImage(64, 96, BufferedImage.TYPE_BYTE_INDEXED, new IndexColorModel(8, 256, new byte[256], new byte[256], new byte[256], 0));
        if (cachedTexture != null) return cachedTexture;

        ByteBuffer paletteBytes = ByteBuffer.wrap(palette).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer imageBytes = ByteBuffer.wrap(image).order(ByteOrder.LITTLE_ENDIAN);

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

    private record PackingCell(boolean occupied, int width, int pieceIndex){}

    public int setTexture(BufferedImage newImage) {
        var pixelSets = getSetCover(newImage);
        pixelSets.forEach(x -> System.out.println(String.format("Pos (%d, %d) : Size (%d * %d)", x.startIndex % newImage.getWidth(), x.startIndex / newImage.getWidth(), x.width, x.height)));
        
        pixelSets.sort(Comparator.comparingInt(x -> -x.height));
        pixelSets.sort(Comparator.comparingInt(x -> -x.width));
        int newWidth = pixelSets.getFirst().width;
        if (8 < newWidth && newWidth <= 16) {
            newWidth = 16;
        } else if (16 < newWidth && newWidth <= 32) {
            newWidth = 32;
        } else if (32 < newWidth && newWidth <= 64) {
            newWidth = 64;
        }


        int maxHeight = pixelSets.stream().mapToInt(x -> x.height).sum();

        LinkedList<Pair<Integer, LinkedList<PackingCell>>> box = new LinkedList<>();
        box.add(new Pair<Integer,LinkedList<PackingCell>>(maxHeight, new LinkedList<>()));
        box.get(0).getValue().add(new PackingCell(false, newWidth, -1));
        for (int i = 0; i < pixelSets.size(); i++) {
            PixelSet pixelSet = pixelSets.get(i);
            boolean placed = false;
            int y = 0;
            int x = 0;
            int x2 = 0;
            int y2 = 0;
            int remainingWidth = 0;
            int remainingHeight = 0;
            int availableWidth = 0;
            int availableHeight = 0;
            for (y = 0; y < box.size(); y++) {
                var column = box.get(y);
                for (x = 0; x < column.getValue().size(); x++) {
                    if (!column.getValue().get(x).occupied) {
                        availableWidth = column.getValue().get(x).width;
                        availableHeight = column.getKey();
                        for (x2 = x+1; x2 < column.getValue().size() && availableWidth < pixelSet.width; x2++) {
                            if (column.getValue().get(x2).occupied) break;
                            availableWidth += column.getValue().get(x2).width;
                        }
                        for (y2 = y+1; y2 < box.size() && availableHeight < pixelSet.height; y2++) {
                            if (box.get(y2).getValue().get(x).occupied) break;
                            availableHeight += box.get(y2).getKey();
                        }
                        if (pixelSet.width <= availableWidth && pixelSet.height <= availableHeight) {
                            placed = true;
                            for (int y3 = y; y3 < y2; y3++) {
                                for (int x3 = x; x3 < x2; x3++) {
                                    if (box.get(y3).getValue().get(x3).occupied) {
                                        placed = false;
                                        break;
                                    }
                                }
                                if (!placed) break;
                            }
                            if (placed) {
                                remainingWidth = availableWidth - pixelSet.width;
                                break;
                            }
                        }
                    }
                }
                if (placed) { 
                    remainingHeight = availableHeight - pixelSet.height;
                    break;
                }
            }
            if (remainingWidth != 0) {
                for (int y3 = 0; y3 < box.size(); y3++) {
                    var column = box.get(y3);
                    var cell = column.getValue().get(x2-1);
                    column.getValue().set(x2-1, new PackingCell(cell.occupied, cell.width - remainingWidth, cell.pieceIndex));
                    if (x2 == column.getValue().size()) {
                        column.getValue().add(new PackingCell(column.getValue().get(x2-1).occupied, remainingWidth, -1));
                    } else {
                        column.getValue().add(x2, new PackingCell(column.getValue().get(x2-1).occupied, remainingWidth, -1));
                    }
                }
            }
            

            if (remainingHeight != 0) {
                LinkedList<PackingCell> newRow = new LinkedList<>();
                var column = box.get(y2-1);
                
                box.set(y2-1, new Pair<Integer,LinkedList<PackingCell>>(column.getKey() - remainingHeight, column.getValue()));

                for (int x3 = 0; x3 < column.getValue().size(); x3++) {
                    PackingCell cell = column.getValue().get(x3);
                    newRow.add(new PackingCell(cell.occupied, cell.width, -1));
                }
                if (y2 == box.size()) {
                    box.add(new Pair<Integer,LinkedList<PackingCell>>(remainingHeight, newRow));
                } else {
                    box.add(y2, new Pair<Integer,LinkedList<PackingCell>>(remainingHeight, newRow));
                }
            }

            for (int y3 = y; y3 < y2; y3++) {
                for (int x3 = x; x3 < x2; x3++) {
                    var column = box.get(y3);
                    var cell = column.getValue().get(x3);
                    if (x3 == x && y3 == y) {
                        column.getValue().set(x3, new PackingCell(true, cell.width, i));
                    } else if (x3 == x2-1) {
                        column.getValue().set(x3, new PackingCell(true, cell.width, -1));
                    } else {
                        column.getValue().set(x3, new PackingCell(true, cell.width, -1));
                    }
                }
            }
            if (placed == false) System.out.println("Error placing");
        }

        PieceMapping[] newPieces = new PieceMapping[pixelSets.size()];

        int numPieces = 0;
        int currY = 0;
        for (int y = 0; y < box.size(); y++) {
            var column = box.get(y);
            int currX = 0;
            for (int x = 0; x < column.getValue().size(); x++) {
                if (column.getValue().get(x).occupied && column.getValue().get(x).pieceIndex != -1) {
                    numPieces++;
                    PieceMapping newPiece = new PieceMapping();
                    newPiece.sourceX = (short)currX;
                    newPiece.sourceY = (short)currY;
                    newPiece.sourceWidth = (short)pixelSets.get(column.getValue().get(x).pieceIndex).width;
                    newPiece.sourceHeight = (short)pixelSets.get(column.getValue().get(x).pieceIndex).height;

                    newPiece.targetWidth = (short)pixelSets.get(column.getValue().get(x).pieceIndex).width;
                    newPiece.targetHeight = (short)pixelSets.get(column.getValue().get(x).pieceIndex).height;
                    newPieces[column.getValue().get(x).pieceIndex] = newPiece;
                }
                currX += column.getValue().get(x).width;
            }
            currY += column.getKey();
        }
        System.out.println(numPieces == newPieces.length);
        int newHeight = Arrays.stream(newPieces).mapToInt(x -> x.sourceY + x.sourceHeight).max().getAsInt();

        BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)newImage.getColorModel());
        for (int i = 0; i < newPieces.length; i++) {
            PieceMapping piece = newPieces[i];
            PixelSet pixelSet = pixelSets.get(i);
            for (int y = 0; y < piece.sourceHeight; y++) {
                for (int x = 0; x < piece.sourceWidth; x++) {
                    int xPos = x + pixelSet.startIndex % newImage.getWidth();
                    int yPos = y + pixelSet.startIndex / newImage.getWidth();
                    byte[] pixel = (byte[])newImage.getRaster().getDataElements(xPos, yPos, null);
                    byte b = pixel[0];
                    bufferedImage.setRGB(piece.sourceX + x, piece.sourceY + y, ((IndexColorModel)newImage.getColorModel()).getRGB(b));
                }
            }
        }

        pixelSets.stream().map(x -> x.startIndex % newImage.getWidth());
        for (int i = 0; i < newPieces.length; i++) {
            PieceMapping piece = newPieces[i];
            PixelSet pixelSet = pixelSets.get(i);
            piece.offsetX = (short)(pixelSet.startIndex % newImage.getWidth());
            piece.offsetY = (short)(newImage.getHeight() - pixelSet.startIndex / newImage.getWidth());
        }
        
        ByteBuffer spriteBytes = ByteBuffer.allocate(newWidth*newHeight).order(ByteOrder.LITTLE_ENDIAN);
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                byte[] b = (byte[])bufferedImage.getRaster().getDataElements(x, y, null);
                spriteBytes.put(b[0]);
            }
        }

        if (spriteBytes.array().length > 8192) {
            return 8192 - spriteBytes.array().length;
        }

        this.width = (short)newWidth;
        this.height = (short)newHeight;
        this.pieces = newPieces;
        System.out.println(String.format("Size: %d -> %d", image.length, spriteBytes.array().length));
        this.image = spriteBytes.array();

        hasChanged = true;
        cachedImage = null;
        cachedTexture = null;

        return 0;
    }

    private class PixelSet implements Comparable<PixelSet> {
        int startIndex;
        int width;
        int height;

        HashSet<Integer> pixels = new HashSet<>();
        int numEmpty = 0;

        float weight;

        public PixelSet(int i, int w, int h) {
            this.startIndex = i;
            this.width = w;
            this.height = h;
        }

        @Override
        public int compareTo(PixelSet other) {
            int result;
            result = Float.compare(other.weight, weight);
            //result = Integer.compare(pixels.size(), other.pixels.size());
            if (result != 0) return result;
            result = Integer.compare(numEmpty, other.numEmpty);
            if (result != 0) return result;
            return Integer.compare(width * height, other.width * other.height);
        }


    }

    private ArrayList<PixelSet> getSetCover(BufferedImage sourceImage) {
        HashSet<Integer> elements = new HashSet<>();
        HashSet<Integer> coveredElements = new HashSet<>();
        List<PixelSet> pixelSets = new ArrayList<>();
        {
            int i = 0;
            int x1 = i % sourceImage.getWidth();
            int y1 = i / sourceImage.getWidth();
            while(i < sourceImage.getWidth()*sourceImage.getHeight()) {
                for (int y2 = 0; y2 < 8; y2++) {
                    for (int x2 = 0; x2 < 8; x2++) {
                        if (((byte[])sourceImage.getRaster().getDataElements(x1+x2, y1+y2, null))[0] != 0) {
                            elements.add(i);
                        }
                    }
                }
                i += 8;
                x1 = i % sourceImage.getWidth();
                if (x1 == 0) i += sourceImage.getWidth() * 7;
                y1 = i / sourceImage.getWidth();
            }
        }
        HashSet<Integer> elementsInSets = new HashSet<>();
        {
            int i = 0;
            int x1 = i % sourceImage.getWidth();
            int y1 = i / sourceImage.getWidth();
            while(i < sourceImage.getWidth()*sourceImage.getHeight()) {
                int currHeight = 8;
                while (currHeight <= 32 && y1 + currHeight <= sourceImage.getHeight()) {
                    int currWidth = 8;
                    while (currWidth <= 32 && x1 + currWidth <= sourceImage.getWidth()) {
                        PixelSet newSet = new PixelSet(i, currWidth, currHeight);
                        for (int y = 0; y < currHeight; y+=8) {
                            for (int x = 0; x < currWidth; x+=8) {
                                int index = i + x + y * sourceImage.getWidth();
                                if (elements.contains(index)) {
                                    newSet.pixels.add(index);
                                    elementsInSets.add(index);
                                } else {
                                    newSet.numEmpty++;
                                }
                            }
                        }
                        newSet.weight = newSet.pixels.size();
                        if (newSet.numEmpty == 0) {
                            pixelSets.add(newSet);
                        }
                        currWidth += 8;
                    }
                    currHeight += 8;
                }
                i += 8;
                x1 = i % sourceImage.getWidth();
                if (x1 == 0) i += sourceImage.getWidth() * 7;
                y1 = i / sourceImage.getWidth();
            }
        }
        System.out.println(String.format("%d PixelSets", pixelSets.size()));

        if (!elementsInSets.containsAll(elements)) {
            System.err.println("Not covered:");
            elements.removeAll(elementsInSets);
            elements.forEach(x -> System.out.println(String.format("(%d, %d)", x % sourceImage.getWidth(), x / sourceImage.getWidth())));

            return null;
        }
        pixelSets.sort(null);
        List<PixelSet> sets = pixelSets.stream().sorted().collect(Collectors.toList());


        ArrayList<PixelSet> chosenSets = new ArrayList<>();
        
        while (coveredElements.size() < elements.size()) {
            PixelSet bestSet;
            bestSet = sets.getFirst();

            chosenSets.add(bestSet);
            coveredElements.addAll(bestSet.pixels);

            sets = sets.stream().filter(x -> x.pixels.stream().allMatch(p -> !coveredElements.contains(p))).sorted().collect(Collectors.toList());
        }
        return chosenSets;
    }

    public void setImage(BufferedImage newImage) {
        PieceMapping leftMost = Arrays.stream(pieces).min(Comparator.comparingInt(x -> x.offsetX)).get();
        PieceMapping topMost = Arrays.stream(pieces).min(Comparator.comparingInt(x -> -x.offsetY)).get();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel)newImage.getColorModel());
        Graphics2D graphics = bufferedImage.createGraphics();
        for (int j = 0; j < pieces.length; j++) {
            PieceMapping piece = pieces[j];
            int targetX = piece.offsetX - leftMost.offsetX;
            int targetY = topMost.offsetY - piece.offsetY;
            graphics.drawImage(newImage.getSubimage(targetX, targetY, piece.targetWidth, piece.targetHeight), piece.sourceX, piece.sourceY, null);
        }
        graphics.dispose();

        ByteBuffer spriteBytes = ByteBuffer.allocate(bufferedImage.getWidth()*bufferedImage.getHeight()).order(ByteOrder.LITTLE_ENDIAN);
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                byte[] b = (byte[])bufferedImage.getRaster().getDataElements(x, y, null);
                spriteBytes.put(b[0]);
            }
        }
        image = spriteBytes.array();
        hasChanged = true;
        cachedImage = null;
        cachedTexture = null;
    }
    
    public void setPalette(BufferedImage image) {
        IndexColorModel colorModel = (IndexColorModel)image.getColorModel();
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        colorModel.getReds(reds);
        colorModel.getGreens(greens);
        colorModel.getBlues(blues);
        ByteBuffer paletteBytes = ByteBuffer.allocate(0x200).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 256; i++) {
            int color = (((Byte.toUnsignedInt(blues[i])*31)/255) << 10) | (((Byte.toUnsignedInt(greens[i])*31)/255) << 5) | ((Byte.toUnsignedInt(reds[i])*31)/255);
            paletteBytes.putShort((short)color);
        }

        palette = paletteBytes.array();
        hasChanged = true;
        cachedImage = null;
        cachedTexture = null;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x8 + 0x10*pieces.length + image.length + palette.length + remainingBytes.length).order(ByteOrder.LITTLE_ENDIAN);
        
        buffer.put(_0x0);
        buffer.put(_0x1);
        buffer.putShort(width);
        buffer.putShort(height);
        buffer.put(_0x6);

        buffer.put((byte)pieces.length);

        for (PieceMapping piece : pieces) {
            buffer.putShort(piece.sourceX);
            buffer.putShort(piece.sourceY);
            buffer.putShort(piece.sourceWidth);
            buffer.putShort(piece.sourceHeight);
        }
        for (PieceMapping piece : pieces) {
            buffer.putShort(piece.offsetX);
            buffer.putShort(piece.offsetY);
            buffer.putShort(piece.targetWidth);
            buffer.putShort(piece.targetHeight);
        }

        buffer.put(image);
        buffer.put(palette);
        buffer.put(remainingBytes);

        return buffer.array();
    }
}
