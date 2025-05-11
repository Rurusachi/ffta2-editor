package org.ruru.ffta2editor.model.stringTable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.TextController.StringWithId;
import org.ruru.ffta2editor.utility.FFTA2Charset;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StringTable {
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");
    public StringProperty name;
    public int id;
    public boolean hasLines;
    public ArrayList<Integer> numLinesList = new ArrayList<>();

    public SimpleListProperty<StringWithId> strings = new SimpleListProperty<>();

    public StringTable(ByteBuffer bytes, StringProperty name, int id) throws Exception {
        this.name = name;
        this.id = id;
        ObservableList<StringWithId> stringList = FXCollections.observableArrayList();

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
                numLinesList.add(Short.toUnsignedInt(numLines));
                hasLines |= numLines != 0;
                String s = FFTA2Charset.decode(bytes.slice(offset, stringLength));
                stringList.add(new StringWithId(i, new SimpleStringProperty(s)));
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to decode entry %d in table \"%s\"", i, name));
                System.err.println(e);
                throw e;
            }
        }
        strings.set(stringList);
    }
    
    private int align16(int d) {
        return (d + 0x1f) & ~0x1f;
    }
    
    private int align4(int d) {
        return (d + 3) & ~3;
    }

    public byte[] toBytes() throws Exception {
        return toBytes(true);
    }

    public byte[] toBytes(boolean aligned16) throws Exception {
        List<String> stringList = strings.getValue().stream().map(x -> x.string().getValue()).toList();
        if (stringList.isEmpty()) return new byte[0];
        ByteBuffer tableBytes = ByteBuffer.allocate(stringList.stream().mapToInt(x -> (x.length()+1)*2).sum() + 2 + stringList.size()*10 + 0x20).order(ByteOrder.LITTLE_ENDIAN);
        int offset = 2 + stringList.size()*10;
        tableBytes.putShort((short)stringList.size());
        byte[] zeroes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int stringIndex = 0; stringIndex < stringList.size(); stringIndex++) {
        //for (String s : stringList) {
            String s = stringList.get(stringIndex);
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
                if (hasLines) {
                    if (numLinesList.get(stringIndex) != maxLines) {
                        logger.warning(String.format("Counted %d lines instead of %d in \"%s\"", numLinesList.get(stringIndex), maxLines, s));
                    }
                    tableBytes.putShort((short)Math.max(1, maxLines));
                } else {
                    tableBytes.putShort((short)0);
                }

                tableBytes.put(offset, bytes);
                offset += bytes.length;
            } catch (Exception e) {
                throw new Exception(String.format("%x: %s\n%s", id, name.getValue(), e.getMessage()), e.getCause());
            }
        }
        if (aligned16) {
            offset += align16(offset) - offset;
            tableBytes.put(zeroes, 0, align16(offset) - offset);
        } else {
            offset += align4(offset) - offset;
            tableBytes.put(zeroes, 0, align4(offset) - offset);
        }
        return Arrays.copyOf(tableBytes.array(), offset);
    }
}
