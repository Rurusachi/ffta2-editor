package org.ruru.ffta2editor.utility;

import javafx.scene.control.TextField;

public class ShortChangeListener extends IntRangeChangeListener {
    public ShortChangeListener(TextField field) {
        super(field, 0, 0xFFFF);
    }
}