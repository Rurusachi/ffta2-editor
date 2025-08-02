package org.ruru.ffta2editor.utility;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class AutoCompleteComboBox<T> extends ComboBox<T> {
    AutoCompleteComboBoxListener autoCompleteListener;

    public AutoCompleteComboBox() {
        super();
        initialize();
    }

    public AutoCompleteComboBox(ObservableList<T> items) {
        super(items);
        initialize();
    }

    private void initialize() {
        autoCompleteListener = new AutoCompleteComboBoxListener(this);
    }

    public void setData(ObservableList<T> value) {
        autoCompleteListener.data = value;
        setItems(value);
    }

    public ObservableList<T> getData() {
        return autoCompleteListener.data;
    }

    public void clearFilter() {
        setItems(autoCompleteListener.data);
    }

    private class AutoCompleteComboBoxListener implements EventHandler<KeyEvent> {
        private ComboBox<T> comboBox;
        private ObservableList<T> data;
        private boolean moveCaretToPos = false;
        private int caretPos;

        public AutoCompleteComboBoxListener(final ComboBox<T> comboBox) {
            this.comboBox = comboBox;
            data = comboBox.getItems();

            this.comboBox.setEditable(true);
            this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    comboBox.hide();
                }
            });
            this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
            
            this.comboBox.setConverter(new StringConverter<T>() {

                @Override
                public String toString(T value) {
                    if (value != null) {
                        if (StringProperty.class.isInstance(data.get(0))) {
                            System.err.println("AutoComplete toString: StringProperty");
                            return ((StringProperty)value).getValue();
                        } else {
                            return value.toString();
                        }
                    } else {
                        return "";
                    }
                }

                @Override
                public T fromString(String s) {
                    FilteredList<T> filtered;
                    if (StringProperty.class.isInstance(data.get(0))) {
                        System.err.println("AutoComplete fromString: StringProperty");
                        filtered =  data.filtered(x -> ((StringProperty)x).getValue().equals(s));
                    } else {
                        filtered =  data.filtered(x -> x.toString().equals(s));
                    }
                    if (filtered.size() != 1) {
                        if (filtered.size() > 1) System.err.println(String.format("Ambiguous fromString result \"%s\"", s));
                        return data.get(0);
                    }
                    return filtered.get(0);
                    //try {
                    //    return fromString.apply(s);
                    //} catch (Exception e) {
                    //    return data.get(0);
                    //}
                }
                
            });
        }

        @Override
        public void handle(KeyEvent event) {

            if(event.getCode() == KeyCode.UP) {
                caretPos = -1;
                moveCaret(comboBox.getEditor().getText().length());
                return;
            } else if(event.getCode() == KeyCode.DOWN) {
                if(!comboBox.isShowing()) {
                    comboBox.show();
                }
                caretPos = -1;
                moveCaret(comboBox.getEditor().getText().length());
                return;
            } else if(event.getCode() == KeyCode.BACK_SPACE) {
                moveCaretToPos = true;
                caretPos = comboBox.getEditor().getCaretPosition();
            } else if(event.getCode() == KeyCode.DELETE) {
                moveCaretToPos = true;
                caretPos = comboBox.getEditor().getCaretPosition();
            }

            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                    || event.isControlDown() || event.getCode() == KeyCode.HOME
                    || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
                return;
            }

            ObservableList<T> list = FXCollections.observableArrayList();
            for (int i=0; i<data.size(); i++) {
                if(data.get(i).toString().toLowerCase().contains(
                    AutoCompleteComboBoxListener.this.comboBox
                    .getEditor().getText().toLowerCase())) {
                    list.add(data.get(i));
                }
            }
            String t = comboBox.getEditor().getText();

            comboBox.setItems(list);
            comboBox.getEditor().setText(t);
            if(!moveCaretToPos) {
                caretPos = -1;
            }
            moveCaret(t.length());
            if(!list.isEmpty()) {
                comboBox.show();
            }
        }

        private void moveCaret(int textLength) {
            if(caretPos == -1) {
                comboBox.getEditor().positionCaret(textLength);
            } else {
                comboBox.getEditor().positionCaret(caretPos);
            }
            moveCaretToPos = false;
        }

    }
}
