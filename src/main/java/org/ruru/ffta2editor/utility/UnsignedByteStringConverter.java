package org.ruru.ffta2editor.utility;

import javafx.util.StringConverter;

public class UnsignedByteStringConverter extends StringConverter<Byte> {

    @Override
    public String toString(Byte value) {
        return Integer.toString(Byte.toUnsignedInt(value));
    }

    @Override
    public Byte fromString(String string) {
        return (byte)Integer.parseInt(string);
    }
        
}