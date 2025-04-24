package org.ruru.ffta2editor.utility;

import javafx.beans.property.BooleanProperty;
import javafx.util.StringConverter;

public class ByteStringConverter extends StringConverter<Byte> {
    boolean signed;

    public ByteStringConverter() {
        this.signed = false;
    }

    public ByteStringConverter(boolean signed) {
        this.signed = signed;
    }

    public ByteStringConverter(BooleanProperty signed) {
        this.signed = signed.getValue();
        signed.addListener((observable, oldValue, newValue) -> this.signed = newValue);
    }


    @Override
    public String toString(Byte value) {
        if (signed) {
            return Integer.toString(value);
        } else {
            return Integer.toString(Byte.toUnsignedInt(value));
        }
    }

    @Override
    public Byte fromString(String string) {
        return (byte)Integer.parseInt(string);
    }
        
}