package org.ruru.ffta2editor.model.unitSst;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.ruru.ffta2editor.utility.BinaryTree;
import org.ruru.ffta2editor.utility.BinaryTreeNode;
import org.ruru.ffta2editor.utility.LZSS;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UnitSst extends BinaryTree<byte[]> {
    public UnitSst(ByteBuffer bytes) {
        super(bytes);
        // Populate nodes with values
        traverseInOrder(node -> {
            data.position(node.offset*0x10+4);
            
            try {
                var value = LZSS.decode(data);
                byte[] compressedValue = new byte[value.compressedSize+4];
                data.position(node.offset*0x10);
                data.get(compressedValue);
                node.value = compressedValue;
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.err.println(e);
                node.value = null;
            }

        });
    }

    public UnitAnimation getAnimation(int key) {
        var node = find(key);
        ByteBuffer temp = ByteBuffer.wrap(node.value).order(ByteOrder.LITTLE_ENDIAN);
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
        return node.value;
    }

    public void setCompressedValue(int key, ByteBuffer value) {
        var node = find(key);
        //node.compressedValue = value;
        
        ByteBuffer newCompressedValue = ByteBuffer.allocate(value.capacity()+4).order(ByteOrder.LITTLE_ENDIAN);
        newCompressedValue.putShort(value.getShort(1));
        for (int j = 2; j < 4; j++) {
            newCompressedValue.put(node.value[j]);
        }
        newCompressedValue.put(value);
        node.value = newCompressedValue.array();
    }

    public byte[] toBytes() {
        return toByteBuffer().array();
    }

    public ByteBuffer toByteBuffer() {
        ArrayList<BinaryTreeNode<byte[]>> nodeList = asList();
        List<byte[]> compressedData = nodeList.stream().map(node -> getCompressedValue(node.key)).toList();
        
        int headerSize = size % 2 == 1 ? (size+1)*8 : (size+2)*8;
        //ByteBuffer newSstHeader = ByteBuffer.allocate(headerSize).order(ByteOrder.LITTLE_ENDIAN);
        //ByteBuffer newSstData = ByteBuffer.allocate().order(ByteOrder.LITTLE_ENDIAN);

        

        ByteBuffer newSst = ByteBuffer.allocate(headerSize + compressedData.stream().mapToInt(x -> x.length + (x.length % 16 != 0 ? 16 - (x.length % 16) : 0)).sum()).order(ByteOrder.LITTLE_ENDIAN);

        int currOffset = headerSize;
        for (int i = 0; i < nodeList.size(); i++) {
            BinaryTreeNode<byte[]> node = nodeList.get(i);
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
