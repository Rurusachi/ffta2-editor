package org.ruru.ffta2editor.utility;

import javafx.scene.control.TextField;

public class ByteChangeListener extends IntRangeChangeListener {
    public ByteChangeListener(TextField field) {
        super(field, 0, 0xFF);
    }
}