package org.ruru.ffta2editor.utility;

import javafx.util.StringConverter;

public class UnsignedShortStringConverter extends StringConverter<Short> {

    @Override
    public String toString(Short value) {
        return Integer.toString(Short.toUnsignedInt(value));
    }

    @Override
    public Short fromString(String string) {
        return (short)Integer.parseInt(string);
    }
        
}