package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import org.ruru.ffta2editor.model.stringTable.MessageId;
import org.ruru.ffta2editor.model.stringTable.StringTable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class TextController {
    
    public static class StringTableCell extends ListCell<StringTable> {
        Label label = new Label();


        public StringTableCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(StringTable table, boolean empty) {
            super.updateItem(table, empty);
            if (table != null) {
                label.setText(String.format("%X: %s", table.id , table.name));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }
    
    public static class StringPropertyCell extends ListCell<StringProperty> {
        Label label = new Label();


        public StringPropertyCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(StringProperty property, boolean empty) {
            super.updateItem(property, empty);
            if (property != null) {
                label.setText(property.getValue());
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    //List<StringTable> messageList;

    @FXML ListView<StringTable> messageList;
    @FXML ListView<StringProperty> stringList;
    @FXML TextArea stringArea;


    private ObjectProperty<StringTable> stringTableProperty = new SimpleObjectProperty<>();
    private StringProperty stringProperty;

    public void initialize() {
        
        messageList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindStringTableData();
            stringTableProperty.setValue(newValue);
            if (newValue != null) bindStringTableData();
        });
        
        stringList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindStringData();
            stringProperty = stringList.getSelectionModel().getSelectedItem();
            if (newValue != null) bindStringData();
        });

    }

    private void unbindStringTableData() {
        stringList.setItems(null);
    }

    private void bindStringTableData() {
        stringList.setItems(stringTableProperty.getValue().strings);
    }

    private void unbindStringData() {
        stringArea.textProperty().unbindBidirectional(stringProperty);
    }

    private void bindStringData() {
        stringArea.textProperty().bindBidirectional(stringProperty);
    }

    public void loadMessages() {
        if (App.archive != null) {
            ObservableList<StringTable> tempMessageList = FXCollections.observableArrayList();

            for (int i = 0; i < App.jdMessage.numFiles(); i++) {
                ByteBuffer stringTableBytes = App.jdMessage.getFile(i);
                //System.out.println(i);
                if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                    //System.err.println(String.format("jdMessage empty %d", i));
                    continue;
                }

                StringTable stringTable = new StringTable(stringTableBytes, MessageId.messageNames[i], i);
                tempMessageList.add(stringTable);

                ObservableList<StringProperty> tempList;
                switch (i) {
                    case 0:
                        App.characterNames = stringTable.strings.getValue();
                        break;
                    case 1:
                        App.jobNames = stringTable.strings.getValue();
                        break;
                    case 2:
                        App.abilitySetNames = stringTable.strings.getValue();
                        break;
                    case 3:
                        App.abilityNames = stringTable.strings.getValue();
                        break;
                    case 4:
                        App.regionNames = stringTable.strings.getValue();
                        break;
                    case 5:
                        App.locationNames = stringTable.strings.getValue();
                        break;
                    case 6:
                        App.questNames = stringTable.strings.getValue();
                        break;
                    case 11:
                        App.itemNames = stringTable.strings.getValue();
                        break;
                    case 14:
                        App.lawNames = stringTable.strings.getValue();
                        break;
                    case 18:
                        tempList = FXCollections.observableArrayList(stringTable.strings.getValue().subList(0x75, 0xC8));
                        tempList.addFirst(new SimpleStringProperty("None"));
                        App.bonusEffects = tempList;
                        break;
                    case 24:
                        App.bazaarSetNames = stringTable.strings.getValue();
                        break;
                    case 30:
                        App.abilitySetDescriptions = stringTable.strings.getValue();
                        break;
                    case 31:
                        App.abilityDescriptions = stringTable.strings.getValue();
                        break;
                    case 35:
                        App.questDescriptions = stringTable.strings.getValue();
                        break;
                    case 38:
                        App.jobDescriptions = stringTable.strings.getValue();
                        break;
                    case 39:
                        App.itemDescriptions = stringTable.strings.getValue();
                        break;
                    case 53:
                        App.bazaarSetDescriptions = stringTable.strings.getValue();
                        break;
                
                    default:
                        break;
                }
            }
            messageList.getItems().setAll(tempMessageList);
            messageList.setCellFactory(x -> new StringTableCell());
            stringList.setCellFactory(x -> new StringPropertyCell());

            //App.characterNames = tempMessageList.get(0).strings.getValue();

            //App.jobNames = tempMessageList.get(1).strings.getValue();
            //App.abilitySetNames = tempMessageList.get(2).strings.getValue();
            //App.abilityNames = tempMessageList.get(3).strings.getValue();

            //App.jobDescriptions = tempMessageList.get(38).strings.getValue();
            //App.abilitySetDescriptions = tempMessageList.get(30).strings.getValue();
            //App.abilityDescriptions = tempMessageList.get(31).strings.getValue();
        }
    }

    public void saveMessages() {
        List<StringTable> messages = messageList.getItems();

        //ArrayList<byte[]> encodedMessages = new ArrayList<>();
        for (StringTable table : messages) {
            //encodedMessages.add(table.toBytes());
            //System.out.println(table.id);
            byte[] tableBytes = table.toBytes();
            //System.out.println(String.format("%d -> %d", App.jdMessage.getFile(table.id).rewind().remaining(), tableBytes.length));
            App.jdMessage.setFile(table.id, ByteBuffer.wrap(tableBytes).order(ByteOrder.LITTLE_ENDIAN));
        }
        
    }
}
