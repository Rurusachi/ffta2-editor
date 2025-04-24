package org.ruru.ffta2editor.utility;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TextField;

public class ByteChangeListener extends IntRangeChangeListener {
    public ByteChangeListener(TextField field, BooleanProperty signed) {
        super(field, signed.getValue() ? -128 : 0, signed.getValue() ? 127 : 255);
        signed.addListener((observable, oldValue, newValue) -> {
            this.min = newValue ? -128 : 0;
            this.max = newValue ? 127 : 255;
        });
    }
    public ByteChangeListener(TextField field) {
        super(field, 0, 255);
    }
}