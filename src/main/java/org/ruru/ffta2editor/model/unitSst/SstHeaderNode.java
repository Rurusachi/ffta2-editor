package org.ruru.ffta2editor.model.unitSst;

import java.nio.ByteBuffer;

import org.ruru.ffta2editor.utility.BinaryTreeNode;

public class SstHeaderNode extends BinaryTreeNode<byte[]> {
    public byte dataType;
    public byte animationId;

    public SstHeaderNode(ByteBuffer bytes, int index) {
        super(bytes, index);
        this.dataType = (byte)(key & 0xff);
        this.animationId = (byte)(key >>> 0x8);
    }

    public SstHeaderNode(int index, byte dataType, byte animationId, int offset) {
        super(index, (short)key(dataType, animationId), offset);
        this.dataType = dataType;
        this.animationId = animationId;
    }

    public static int key(byte dataType, byte animationId) {
        return (Byte.toUnsignedInt(animationId) << 0x8) | Byte.toUnsignedInt(dataType);
    }
}