package org.ruru.ffta2editor.model.unitSst;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class SpriteData {
    public static record SpritePiece(int offsetX, int offsetY,
                                 int width, int height) {}
    public static record SpriteMap(short unkownValue, int spriteOffset, SpritePiece[] pieces) {}
    public ArrayList<SpriteMap> spriteMaps = new ArrayList<>();

    public SpriteData(ByteBuffer bytes) {
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        int count = bytes.getShort(0) >>> 3;

        for (int i = 0; i < count; i++) {
            int pieceOffset = bytes.getShort();
            short unknownValue = bytes.getShort(); // unknown
            int spriteOffset = bytes.getInt();
            bytes.mark();
            bytes.position(pieceOffset);
            SpritePiece[] pieces = new SpritePiece[bytes.getInt()];
            for (int j = 0; j < pieces.length; j++) {
                pieces[j] = new SpritePiece(bytes.getShort(), bytes.getShort(), bytes.getShort(), bytes.getShort());
            }
            spriteMaps.add(new SpriteMap(unknownValue, spriteOffset, pieces));
            bytes.reset();
        }
    }

    public ByteBuffer toByteBuffer() {
        ByteBuffer bytes = ByteBuffer.allocate(spriteMaps.stream().mapToInt(x -> 8 + 4 + x.pieces().length*8).sum()).order(ByteOrder.LITTLE_ENDIAN);

        int currPieceOffset = spriteMaps.size() * 8;
        for (SpriteMap spriteMap : spriteMaps) {
            bytes.putShort((short)currPieceOffset);
            bytes.putShort(spriteMap.unkownValue());
            bytes.putInt(spriteMap.spriteOffset());
            bytes.mark();
            bytes.position(currPieceOffset);
            bytes.putInt(spriteMap.pieces().length);
            for (SpritePiece piece : spriteMap.pieces()) {
                bytes.putShort((short)piece.offsetX());
                bytes.putShort((short)piece.offsetY());
                bytes.putShort((short)piece.width());
                bytes.putShort((short)piece.height());
            }
            currPieceOffset = bytes.position();
            bytes.reset();
        }

        return bytes.rewind();
    }

    public byte[] toBytes() {
        return toByteBuffer().array();
    }
}
