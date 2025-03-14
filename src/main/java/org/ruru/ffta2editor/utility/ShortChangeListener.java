package org.ruru.ffta2editor.utility;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ShortChangeListener implements ChangeListener<String> {
    TextField field;

    public ShortChangeListener(TextField field) {
        this.field = field;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        try {
            int i = Integer.parseInt(newValue);
            if (i < 0 || i > 0xFFFF) throw new Exception();
        } catch (Exception e) {
            this.field.setText(oldValue);
        }
    }

}