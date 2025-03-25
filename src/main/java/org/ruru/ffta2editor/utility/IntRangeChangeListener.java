package org.ruru.ffta2editor.utility;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class IntRangeChangeListener implements ChangeListener<String> {
    TextField field;
    int min;
    int max;

    public IntRangeChangeListener(TextField field, int min, int max) {
        this.field = field;
        this.min = min;
        this.max = max;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        try {
            int i = Integer.parseInt(newValue);
            if (i < min) this.field.setText(Integer.toString(min));
            else if (i > max) this.field.setText(Integer.toString(max));
            //if (i < min || i > max) throw new Exception();
        } catch (Exception e) {
            this.field.setText(oldValue);
        }
    }

}
