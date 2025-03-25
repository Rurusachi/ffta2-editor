package org.ruru.ffta2editor.model.stringTable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.utility.FFTA2Charset;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StringTable {
    public String name;
    public int id;

    public SimpleListProperty<StringProperty> strings = new SimpleListProperty<>();

    public StringTable(ByteBuffer bytes, String name, int id) {
        this.name = name;
        this.id = id;

        bytes.order(ByteOrder.LITTLE_ENDIAN);
        int numEntries = bytes.getShort();
        ObservableList<StringProperty> stringList = FXCollections.observableArrayList();
        for (int i = 0; i < numEntries; i++) {
            try {
                int stringLength = bytes.getInt();
                int offset = bytes.getInt();
                short numLines = bytes.getShort(); // Includes <~>?
                String s = FFTA2Charset.decode(bytes.slice(offset, stringLength));
                stringList.add(new SimpleStringProperty(s));
            } catch (Exception e) {
                System.err.println(e);
                return;
            }
        }
        strings.set(stringList);
    }

    public byte[] toBytes() {
        List<String> stringList = strings.getValue().stream().map(x -> x.getValue()).toList();
        ByteBuffer tableBytes = ByteBuffer.allocate(stringList.stream().mapToInt(x -> (x.length()+1)*2).sum() + 2 + stringList.size()*10).order(ByteOrder.LITTLE_ENDIAN);
        int offset = 2 + stringList.size()*10;
        tableBytes.putShort((short)stringList.size());
        for (String s : stringList) {
            try {
                byte[] bytes = FFTA2Charset.encode(s);
                
                //short numLines = (short)IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).filter(b -> b == (byte)0xC0 || b == (byte)0xC1 || b == (byte)0xC7).mapToInt(b -> (int)b).count();
                // This may not be 100% correct
                ArrayList<byte[]> pages = new ArrayList<>();
                int start = 0;
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] == (byte)0xC1) {
                        pages.add(Arrays.copyOfRange(bytes, start, i+2));
                        start = i+2;
                    }
                }
                int maxLines = 0;
                for (byte[] page : pages) {
                    int numLines = (short)IntStream.range(0, page.length).mapToObj(i -> page[i]).filter(b -> b == (byte)0xC0 || b == (byte)0xC2 || b == (byte)0xC8).mapToInt(b -> (int)b).count();
                    maxLines = Math.max(maxLines, numLines);
                }
                //short numLines = (short)IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).filter(b -> (Byte.toUnsignedInt(b) > 0xC1 && Byte.toUnsignedInt(b) < 0xCA)).mapToInt(b -> (int)b).count();
                tableBytes.putInt(bytes.length);
                tableBytes.putInt(offset);
                tableBytes.putShort((short)maxLines);

                tableBytes.put(offset, bytes);
                offset += bytes.length;
            } catch (Exception e) {
                System.err.println(e);
                return null;
            }
        }
        return Arrays.copyOf(tableBytes.array(), offset);
    }
}
