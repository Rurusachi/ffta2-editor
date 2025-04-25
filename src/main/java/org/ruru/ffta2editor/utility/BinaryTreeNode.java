package org.ruru.ffta2editor.utility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinaryTreeNode<T>{
    public int index;
    public short key;
    public int offset;
    public T value;

    public short leftIndex = -1;
    public BinaryTreeNode<T> leftChild = null;
    public short rightIndex = -1;
    public BinaryTreeNode<T> rightChild = null;

    public BinaryTreeNode(ByteBuffer bytes, int index) {
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        this.index = index;
        key = bytes.getShort();
        offset = Short.toUnsignedInt(bytes.getShort());
        leftIndex = bytes.getShort();
        rightIndex = bytes.getShort();
    }

    public BinaryTreeNode(int index, short key, int offset) {
        this.index = index;
        this.key = key;
        this.offset = offset;
    }

    public int getIndex(){
        return index;
    }

    public byte[] toBytes() {
        ByteBuffer bytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        bytes.putShort(key);
        bytes.putShort((short)offset);
        bytes.putShort(leftIndex);
        bytes.putShort(rightIndex);
        return bytes.array();
    }
}