package org.ruru.ffta2editor.model.stringTable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.utility.FFTA2Charset;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringSingle {
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");
    public StringProperty name;
    public int id;

    public ObjectProperty<Short> _0x00 = new SimpleObjectProperty<>();
    public ObjectProperty<Short> _0x02 = new SimpleObjectProperty<>();
    public IntegerProperty stringLength = new SimpleIntegerProperty();
    public IntegerProperty _0x08 = new SimpleIntegerProperty();
    public ObjectProperty<Short> numLines = new SimpleObjectProperty<>();

    public StringProperty text = new SimpleStringProperty();

    public StringSingle(ByteBuffer bytes, StringProperty name, int id) throws Exception {
        this.name = name;
        this.id = id;

        bytes.order(ByteOrder.LITTLE_ENDIAN);
        _0x00.set(bytes.getShort());
        _0x02.set(bytes.getShort());
        stringLength.set(bytes.getInt());
        _0x08.set(bytes.getInt());
        numLines.set(bytes.getShort());
        try {
            text.set(FFTA2Charset.decode(bytes.slice(bytes.position(), stringLength.getValue())));
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Failed to decode \"%s\"", name));
            System.err.println(e);
            throw e;
        }
    }

    public byte[] toBytes() throws Exception {
        ByteBuffer stringBytes = ByteBuffer.allocate(0xe + (text.getValue().length()+1)*2).order(ByteOrder.LITTLE_ENDIAN);
        stringBytes.position(0xe);
        int endPosition;
        try {
            byte[] bytes = FFTA2Charset.encode(text.getValue());
            
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
                //int numLines = (short)IntStream.range(0, page.length).mapToObj(i -> page[i]).filter(b -> b == (byte)0xC0 || b == (byte)0xC8).mapToInt(b -> (int)b).count() + 1;
                int numLines = (int)IntStream.range(0, page.length).mapToObj(i -> page[i]).filter(b -> b == (byte)0xC0 || b == (byte)0xC1).mapToInt(b -> (int)b).count();
                int numOptions = (int)IntStream.range(0, page.length).mapToObj(i -> page[i]).filter(b -> b == (byte)0xC8).count();
                if (numOptions > 0) numLines += numOptions - 1;
                maxLines = Math.max(maxLines, numLines);
            }
            //short numLines = (short)IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).filter(b -> (Byte.toUnsignedInt(b) > 0xC1 && Byte.toUnsignedInt(b) < 0xCA)).mapToInt(b -> (int)b).count();
            stringLength.set(bytes.length);
            numLines.set((short)Math.max(1, maxLines));
            stringBytes.put(bytes);
            endPosition = stringBytes.position();
        } catch (Exception e) {
            throw new Exception(String.format("%x: %s\n%s", id, name.getValue(), e.getMessage()), e.getCause());
        }
        stringBytes.rewind();
        stringBytes.putShort(_0x00.getValue());
        stringBytes.putShort(_0x02.getValue());
        stringBytes.putInt(stringLength.getValue());
        stringBytes.putInt(_0x08.getValue());
        stringBytes.putShort(numLines.getValue());
        return Arrays.copyOf(stringBytes.array(), endPosition);
    }
}
