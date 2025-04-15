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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class StringTable {
    public StringProperty name;
    public int id;

    public SimpleListProperty<StringProperty> strings = new SimpleListProperty<>();

    public StringTable(ByteBuffer bytes, StringProperty name, int id) {
        this.name = name;
        this.id = id;
        ObservableList<StringProperty> stringList = FXCollections.observableArrayList();

        if (bytes == null || bytes.rewind().remaining() == 0) {
            strings.set(stringList);
            this.name.set("Empty");
            return;
        }

        bytes.order(ByteOrder.LITTLE_ENDIAN);
        int numEntries = bytes.getShort();
        for (int i = 0; i < numEntries; i++) {
            try {
                int stringLength = bytes.getInt();
                int offset = bytes.getInt();
                short numLines = bytes.getShort();
                String s = FFTA2Charset.decode(bytes.slice(offset, stringLength));
                stringList.add(new SimpleStringProperty(s));
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.err.println(e);
                return;
            }
        }
        strings.set(stringList);
    }

    public byte[] toBytes() throws Exception {
        List<String> stringList = strings.getValue().stream().map(x -> x.getValue()).toList();
        if (stringList.isEmpty()) return new byte[0];
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
                    //int numLines = (short)IntStream.range(0, page.length).mapToObj(i -> page[i]).filter(b -> b == (byte)0xC0 || b == (byte)0xC2 || b == (byte)0xC8 || b == (byte)0xC9).mapToInt(b -> (int)b).count();
                    //int numLines = (short)IntStream.range(0, page.length).mapToObj(i -> page[i]).filter(b -> b == (byte)0xC0 || b == (byte)0xC2 || b == (byte)0xC8 || b == (byte)0xC9).mapToInt(b -> (int)b).count();
                    int numLines = (int)IntStream.range(0, page.length).mapToObj(i -> page[i]).filter(b -> b == (byte)0xC0 || b == (byte)0xC1).mapToInt(b -> (int)b).count();
                    int numOptions = (int)IntStream.range(0, page.length).mapToObj(i -> page[i]).filter(b -> b == (byte)0xC8).count();
                    if (numOptions > 0) numLines += numOptions - 1;
                    maxLines = Math.max(maxLines, numLines);
                }
                //short numLines = (short)IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).filter(b -> (Byte.toUnsignedInt(b) > 0xC1 && Byte.toUnsignedInt(b) < 0xCA)).mapToInt(b -> (int)b).count();
                tableBytes.putInt(bytes.length);
                tableBytes.putInt(offset);
                tableBytes.putShort((short)Math.max(1, maxLines));

                tableBytes.put(offset, bytes);
                offset += bytes.length;
            } catch (Exception e) {
                throw new Exception(String.format("%x: %s\n%s", id, name.getValue(), e.getMessage()), e.getCause());
            }
        }
        return Arrays.copyOf(tableBytes.array(), offset);
    }
}
