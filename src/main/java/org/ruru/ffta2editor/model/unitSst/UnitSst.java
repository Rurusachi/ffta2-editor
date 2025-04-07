package org.ruru.ffta2editor.model.unitSst;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.ruru.ffta2editor.utility.LZSS;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UnitSst {

    public static class SstHeaderNode {
        public int index;
        public byte dataType;
        public byte animationId;
        public int offset;
        public byte[] compressedValue;

        public short leftIndex = -1;
        public SstHeaderNode leftChild = null;
        public short rightIndex = -1;
        public SstHeaderNode rightChild = null;

        public SstHeaderNode(ByteBuffer bytes, int index) {
            bytes.order(ByteOrder.LITTLE_ENDIAN);
            this.index = index;
            dataType = bytes.get();
            animationId = bytes.get();
            offset = Short.toUnsignedInt(bytes.getShort());
            leftIndex = bytes.getShort();
            rightIndex = bytes.getShort();
        }

        public SstHeaderNode(int index, byte dataType, byte animationId, int offset) {
            this.index = index;
            this.dataType = dataType;
            this.animationId = animationId;
            this.offset = offset;
        }

        public int key() {
            return (Byte.toUnsignedInt(animationId) << 0x8) | Byte.toUnsignedInt(dataType) ;
        }

        public int getIndex(){
            return index;
        }

        public byte[] toBytes() {
            ByteBuffer bytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            bytes.put(dataType);
            bytes.put(animationId);
            bytes.putShort((short)offset);
            bytes.putShort(leftIndex);
            bytes.putShort(rightIndex);
            return bytes.array();
        }
    }
    public interface SstTraversalFunc {
        void call(SstHeaderNode node);
    }

    ByteBuffer data;
    public SstHeaderNode root;
    public int size = 0;
    public boolean hasChanged = false;

    public UnitSst(ByteBuffer bytes) {
        data = bytes.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        root = new SstHeaderNode(bytes.slice(0, 8).order(ByteOrder.LITTLE_ENDIAN), 0);
        buildTree(root);
        
        // Populate nodes with values
        traverseInOrder(node -> {
            data.position(node.offset*0x10+4);
            
            try {
                var value = LZSS.decode(data);
                byte[] compressedValue = new byte[value.compressedSize+4];
                data.position(node.offset*0x10);
                data.get(compressedValue);
                node.compressedValue = compressedValue;
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.err.println(e);
                node.compressedValue = null;
            }

        });
    }

    private void buildTree(SstHeaderNode node) {
        size += 1;
        if (node.leftIndex != -1) {
            node.leftChild = new SstHeaderNode(data.slice(node.leftIndex*8, 8).order(ByteOrder.LITTLE_ENDIAN), node.leftIndex);
            buildTree(node.leftChild);
        } 
        if (node.rightIndex != -1) {
            node.rightChild = new SstHeaderNode(data.slice(node.rightIndex*8, 8).order(ByteOrder.LITTLE_ENDIAN), node.rightIndex);
            buildTree(node.rightChild);
        }
    }

    public SstHeaderNode find(int key) {
        SstHeaderNode currNode = root;
        while (currNode != null && currNode.key() != key) {
            if (currNode.key() < key) {
                currNode = currNode.leftChild;
            } else {
                currNode = currNode.rightChild;
            }
        }
        return currNode;
    }

    public void insert(SstHeaderNode newNode) {
        SstHeaderNode prevNode = null;
        SstHeaderNode currNode = root;
        while (currNode != null) {
            if (newNode.key() > currNode.key()) {
                prevNode = currNode;
                currNode = currNode.leftChild;
            } else if (newNode.key() < currNode.key()) {
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
        if (newNode.key() > prevNode.key()) {
            prevNode.leftIndex = (short)newNode.index;
            prevNode.leftChild = newNode;
        } else {
            prevNode.rightIndex = (short)newNode.index;
            prevNode.rightChild = newNode;
        }
    }

    public void traverseInOrder(SstHeaderNode node, SstTraversalFunc func) {
        if (node != null) {
            traverseInOrder(node.leftChild, func);
            func.call(node);
            traverseInOrder(node.rightChild, func);
        }
    }
    public void traverseInOrder(SstTraversalFunc func) {
        traverseInOrder(root, func);
    }

    public ArrayList<SstHeaderNode> asList() {
        ArrayList<SstHeaderNode> list = new ArrayList<>();
        traverseInOrder(root, x -> list.add(x));
        list.sort(Comparator.comparingInt(SstHeaderNode::getIndex));
        return list;
    }

    public UnitAnimation getAnimation(int key) {
        var node = find(key);
        ByteBuffer temp = ByteBuffer.wrap(node.compressedValue).order(ByteOrder.LITTLE_ENDIAN);
        byte[] animHeader = new byte[4];
        temp.get(animHeader);

        try {
            var anim = LZSS.decode(temp);
            return new UnitAnimation(anim.decodedData, animHeader, key);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.err.println(key);
            System.err.println(e);
            return null;
        }
    }

    public ByteBuffer getPalettes() {
        var node = find(0x00F0);
        data.position(node.offset*0x10);
        data.getShort();
        short count = data.getShort();
        try {
            var palettes = LZSS.decode(data);
            //return new SpritePalettes(palettes.decodedData, count);
            return palettes.decodedData;
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.err.println(e);
            return null;
        }
        
    }

    public SpriteData getSpriteMap() {
        var node = find(0x00FF);
        data.position(node.offset*0x10);
        data.getInt();
        try {
            var spriteMapBytes = LZSS.decode(data);
            return new SpriteData(spriteMapBytes.decodedData);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.err.println(e);
            return null;
        }
    }

    //public byte[] getCompressedValue(int key) {
    //    var node = find(key);
    //    data.position(node.offset*0x10+4);
    //    
    //    try {
    //        var value = LZSS.decode(data);
    //        byte[] compressedValue = new byte[value.compressedSize+4];
    //        data.position(node.offset*0x10);
    //        data.get(compressedValue);
    //        return compressedValue;
    //    } catch (Exception e) {
    //        System.err.println(e);
    //        return null;
    //    }
    //}

    public byte[] getCompressedValue(int key) {
        var node = find(key);
        return node.compressedValue;
    }

    public void setCompressedValue(int key, ByteBuffer value) {
        var node = find(key);
        //node.compressedValue = value;
        
        ByteBuffer newCompressedValue = ByteBuffer.allocate(value.capacity()+4).order(ByteOrder.LITTLE_ENDIAN);
        newCompressedValue.putShort(value.getShort(1));
        for (int j = 2; j < 4; j++) {
            newCompressedValue.put(node.compressedValue[j]);
        }
        newCompressedValue.put(value);
        node.compressedValue = newCompressedValue.array();
    }

    public byte[] toBytes() {
        return toByteBuffer().array();
    }

    public ByteBuffer toByteBuffer() {
        ArrayList<SstHeaderNode> nodeList = asList();
        List<byte[]> compressedData = nodeList.stream().map(node -> getCompressedValue(node.key())).toList();
        
        int headerSize = size % 2 == 1 ? (size+1)*8 : (size+2)*8;
        //ByteBuffer newSstHeader = ByteBuffer.allocate(headerSize).order(ByteOrder.LITTLE_ENDIAN);
        //ByteBuffer newSstData = ByteBuffer.allocate().order(ByteOrder.LITTLE_ENDIAN);

        

        ByteBuffer newSst = ByteBuffer.allocate(headerSize + compressedData.stream().mapToInt(x -> x.length + (x.length % 16 != 0 ? 16 - (x.length % 16) : 0)).sum()).order(ByteOrder.LITTLE_ENDIAN);

        int currOffset = headerSize;
        for (int i = 0; i < nodeList.size(); i++) {
            SstHeaderNode node = nodeList.get(i);
            node.offset = currOffset / 16;
            newSst.put(node.toBytes());
            newSst.mark();
            newSst.position(currOffset);
            newSst.put(compressedData.get(i));
            if (newSst.position() % 16 != 0) {
                newSst.position(newSst.position() + 16 - (newSst.position() % 16));
            }
            currOffset = newSst.position();
            newSst.reset();
        }
        newSst.putShort((short)0).putInt(newSst.capacity() / 0x10);

        return newSst.rewind();
    }

}
