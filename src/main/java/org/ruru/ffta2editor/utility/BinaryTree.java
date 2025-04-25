package org.ruru.ffta2editor.utility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;

public class BinaryTree<T> {
    public interface TreeTraversalFunc<T> {
        void call(BinaryTreeNode<T> node);
    }

    protected ByteBuffer data;
    public BinaryTreeNode<T> root;
    public int size = 0;
    public boolean hasChanged = false;

    public BinaryTree(ByteBuffer bytes) {
        data = bytes.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        root = new BinaryTreeNode<T>(bytes.slice(0, 8).order(ByteOrder.LITTLE_ENDIAN), 0);
        buildTree(root);
    }

    protected void buildTree(BinaryTreeNode<T> node) {
        size += 1;
        if (node.leftIndex != -1) {
            node.leftChild = new BinaryTreeNode<T>(data.slice(node.leftIndex*8, 8).order(ByteOrder.LITTLE_ENDIAN), node.leftIndex);
            buildTree(node.leftChild);
        } 
        if (node.rightIndex != -1) {
            node.rightChild = new BinaryTreeNode<T>(data.slice(node.rightIndex*8, 8).order(ByteOrder.LITTLE_ENDIAN), node.rightIndex);
            buildTree(node.rightChild);
        }
    }

    public BinaryTreeNode<T> find(int key) {
        BinaryTreeNode<T> currNode = root;
        while (currNode != null && currNode.key != key) {
            if (currNode.key < key) {
                currNode = currNode.leftChild;
            } else {
                currNode = currNode.rightChild;
            }
        }
        return currNode;
    }

    public void insert(BinaryTreeNode<T> newNode) {
        BinaryTreeNode<T> prevNode = null;
        BinaryTreeNode<T> currNode = root;
        while (currNode != null) {
            if (newNode.key > currNode.key) {
                prevNode = currNode;
                currNode = currNode.leftChild;
            } else if (newNode.key < currNode.key) {
                prevNode = currNode;
                currNode = currNode.rightChild;
            } else {
                newNode.leftIndex = currNode.leftIndex;
                newNode.leftChild = currNode.leftChild;

                newNode.rightIndex = currNode.rightIndex;
                newNode.rightChild = currNode.rightChild;
                //return;
                newNode.index = currNode.index;
                size -= 1;
                break;
            }
        }
        size += 1;
        if (newNode.key > prevNode.key) {
            prevNode.leftIndex = (short)newNode.index;
            prevNode.leftChild = newNode;
        } else {
            prevNode.rightIndex = (short)newNode.index;
            prevNode.rightChild = newNode;
        }
    }

    public void traverseInOrder(BinaryTreeNode<T> node, TreeTraversalFunc<T> func) {
        if (node != null) {
            traverseInOrder(node.leftChild, func);
            func.call(node);
            traverseInOrder(node.rightChild, func);
        }
    }
    public void traverseInOrder(TreeTraversalFunc<T> func) {
        traverseInOrder(root, func);
    }

    public ArrayList<BinaryTreeNode<T>> asList() {
        ArrayList<BinaryTreeNode<T>> list = new ArrayList<>();
        traverseInOrder(root, x -> list.add(x));
        list.sort(Comparator.comparingInt(BinaryTreeNode<T>::getIndex));
        return list;
    }

    public byte[] toBytes() {
        return toByteBuffer().array();
    }

    public ByteBuffer toByteBuffer() {
        ArrayList<BinaryTreeNode<T>> nodeList = asList();
        
        int headerSize = size % 2 == 1 ? (size+1)*8 : (size+2)*8;
        //ByteBuffer newSstHeader = ByteBuffer.allocate(headerSize).order(ByteOrder.LITTLE_ENDIAN);
        //ByteBuffer newSstData = ByteBuffer.allocate().order(ByteOrder.LITTLE_ENDIAN);

        

        ByteBuffer newBytes = ByteBuffer.allocate(size*8).order(ByteOrder.LITTLE_ENDIAN);

        //int currOffset = headerSize;
        for (int i = 0; i < nodeList.size(); i++) {
            BinaryTreeNode<T> node = nodeList.get(i);
            //node.offset = currOffset / 16;
            newBytes.put(node.toBytes());
        }
        //newSst.putShort((short)0).putInt(newSst.capacity() / 0x10);

        return newBytes.rewind();
    }
}
