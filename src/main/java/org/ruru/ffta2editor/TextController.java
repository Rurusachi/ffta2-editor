package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.TextController.StringWithId;
import org.ruru.ffta2editor.model.stringTable.MessageId;
import org.ruru.ffta2editor.model.stringTable.StringSingle;
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
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");
    
    public static class StringTableCell extends ListCell<StringTable> {
        Label label = new Label();


        public StringTableCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(StringTable table, boolean empty) {
            super.updateItem(table, empty);
            if (table != null) {
                label.setText(String.format("%X: %s", table.id , table.name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }
    
    public static class StringSingleCell extends ListCell<StringSingle> {
        Label label = new Label();


        public StringSingleCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(StringSingle single, boolean empty) {
            super.updateItem(single, empty);
            if (single != null) {
                label.setText(String.format("%X: %s", single.id , single.name.getValue()));
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
    
    public record StringWithId(int id, StringProperty string){
        
        @Override
        public String toString() {
            return String.format("%X: %s", id, string.getValue());
        }
    };
    public static class StringWithIdCell extends ListCell<StringWithId> {
        Label label = new Label();


        public StringWithIdCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(StringWithId property, boolean empty) {
            super.updateItem(property, empty);
            if (property != null) {
                label.setText(property.toString());
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    //List<StringTable> messageList;

    @FXML ListView<StringTable> messageList;
    @FXML ListView<StringTable> eventMsgList;
    @FXML ListView<StringSingle> questList;
    @FXML ListView<StringSingle> rumorList;
    @FXML ListView<StringSingle> noticeList;
    @FXML ListView<StringWithId> messageStringList;
    @FXML ListView<StringWithId> eventStringList;
    @FXML TextArea stringArea;


    private ObjectProperty<StringTable> stringTableProperty = new SimpleObjectProperty<>();
    private StringProperty stringProperty;

    public void initialize() {
        
        messageList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindMessageStringTableData();
            stringTableProperty.setValue(newValue);
            if (newValue != null) bindMessageStringTableData();
        });
        
        eventMsgList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindEventStringTableData();
            stringTableProperty.setValue(newValue);
            if (newValue != null) bindEventStringTableData();
        });
        
        questList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (stringProperty != null) unbindStringData();
            stringProperty = newValue.text;
            if (stringProperty != null) bindStringData();
        });
        
        rumorList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (stringProperty != null) unbindStringData();
            stringProperty = newValue.text;
            if (stringProperty != null) bindStringData();
        });
        
        noticeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (stringProperty != null) unbindStringData();
            stringProperty = newValue.text;
            if (stringProperty != null) bindStringData();
        });
        
        messageStringList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (stringProperty != null) unbindStringData();
            stringProperty = newValue != null ? newValue.string() : null;
            if (stringProperty != null) bindStringData();
        });
        
        eventStringList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (stringProperty != null) unbindStringData();
            stringProperty = newValue != null ? newValue.string() : null;
            if (stringProperty != null) bindStringData();
        });

        messageStringList.setCellFactory(x -> new StringWithIdCell());
        eventStringList.setCellFactory(x -> new StringWithIdCell());
    }

    private void unbindMessageStringTableData() {
        messageStringList.setItems(null);
    }

    private void bindMessageStringTableData() {
        messageStringList.setItems(stringTableProperty.getValue().strings);
    }

    private void unbindEventStringTableData() {
        eventStringList.setItems(null);
    }

    private void bindEventStringTableData() {
        eventStringList.setItems(stringTableProperty.getValue().strings);
    }

    private void unbindStringData() {
        stringArea.textProperty().unbindBidirectional(stringProperty);
    }

    private void bindStringData() {
        stringArea.textProperty().bindBidirectional(stringProperty);
    }

    @FXML
    public void addMessageString() {
        if (messageStringList.getItems() != null) {
            int nextIndex = messageStringList.getItems().size();
            messageStringList.getItems().add(new StringWithId(nextIndex, new SimpleStringProperty("")));
            messageStringList.getSelectionModel().selectLast();
        }
    }

    @FXML
    public void removeMessageString() {
        if (messageStringList.getItems().size() > 0) {
            messageStringList.getItems().removeLast();
        }
    }

    @FXML
    public void addEventString() {
        if (eventStringList.getItems() != null) {
            int nextIndex = eventStringList.getItems().size();
            eventStringList.getItems().add(new StringWithId(nextIndex, new SimpleStringProperty("")));
            eventStringList.getSelectionModel().selectLast();
        }
    }

    @FXML
    public void removeEventString() {
        if (eventStringList.getItems().size() > 0) {
            eventStringList.getItems().removeLast();
        }
    }

    public void loadMessages() throws Exception {
        if (App.archive != null) {
            ObservableList<StringTable> tempMessageList = FXCollections.observableArrayList();

            logger.info("Loading jdMessage");
            int currPos = 0;
            for (int i = 0; i < App.jdMessage.numFiles(); i++) {
                ByteBuffer stringTableBytes = App.jdMessage.getFile(i);
                //System.out.println(i);
                if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                    //System.err.println(String.format("jdMessage empty %d", i));
                    continue;
                }

                //System.out.println(String.format("%s: %d to %d", MessageId.messageNames[i],  currPos, currPos+stringTableBytes.remaining()));
                currPos += stringTableBytes.remaining();
                StringTable stringTable = new StringTable(stringTableBytes, new SimpleStringProperty(MessageId.messageNames[i]), i);
                tempMessageList.add(stringTable);

                ObservableList<StringWithId> tempList;
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
                    case 8:
                        App.rumorNames = stringTable.strings.getValue();
                        break;
                    case 10:
                        App.noticeNames = stringTable.strings.getValue();
                        break;
                    case 11:
                        App.itemNames = stringTable.strings.getValue();
                        break;
                    case 14:
                        App.lawNames = stringTable.strings.getValue();
                        break;
                    case 18:
                        tempList = FXCollections.observableArrayList(stringTable.strings.getValue().subList(0x75, 0xC8));
                        tempList.addFirst(new StringWithId(0, new SimpleStringProperty("None")));
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
                    case 47:
                        //ObservableList<StringWithId> abilityHelpStrings = FXCollections.observableArrayList(IntStream.range(0, App.abilityHelpText.size()).mapToObj(i -> new StringWithId(i, App.abilityHelpText.get(i))).toList());
                        App.abilityHelpText = stringTable.strings.getValue();
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

            
            ObservableList<StringTable> tempEventMsgList = FXCollections.observableArrayList();

            logger.info("Loading evMsg");
            for (int i = 0; i < App.evMsg.numFiles(); i++) {
                ByteBuffer stringTableBytes = App.evMsg.getFile(i);

                StringTable stringTable = new StringTable(stringTableBytes, new SimpleStringProperty("Unknown"), i);
                tempEventMsgList.add(stringTable);
                
                switch (i) {
                    case 0x21:
                    stringTable.name.set("First Auction?");
                        break;
                    case 0x22:
                    stringTable.name.set("Bazaar Tutorial + Hurdy?");
                        break;
                    case 0x23:
                    stringTable.name.set("Pub Tutorial + Adelle?");
                        break;
                    case 0x24:
                    stringTable.name.set("Aerodrome");
                        break;
                    case 0x26:
                    stringTable.name.set("Raffle");
                        break;
                    case 0x29:
                    stringTable.name.set("Resting");
                        break;
                    case 0x2a:
                    stringTable.name.set("Intro");
                        break;
                    case 0x2b:
                    stringTable.name.set("Race Descriptions");
                        break;
                    case 0x66:
                    stringTable.name.set("Map Tutorial");
                        break;
                    case 0x72:
                    stringTable.name.set("Entering Auction?");
                        break;
                    case 0x7e:
                    stringTable.name.set("Making Music (shop)");
                        break;
                    case 0xB4:
                    stringTable.name.set("Help! (text)");
                        break;
                    default:
                        break;
                }
            }
            eventMsgList.getItems().setAll(tempEventMsgList);
            eventMsgList.setCellFactory(x -> new StringTableCell());
            
            App.evMsgNames = tempEventMsgList.stream().collect(Collectors.toMap(x -> x.id, x -> x.name));

            ObservableList<StringSingle> tempQuestList = FXCollections.observableArrayList();

            logger.info("Loading jhQuest");
            for (int i = 0; i < App.jhQuest.numFiles(); i++) {
                ByteBuffer stringTableBytes = App.jhQuest.getFile(i);
                //System.out.println(i);
                if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                    //System.err.println(String.format("jdMessage empty %d", i));
                    continue;
                }

                StringSingle stringTable = new StringSingle(stringTableBytes, App.questNames.get(i).string(), i);
                tempQuestList.add(stringTable);
            }
            questList.getItems().setAll(tempQuestList);
            questList.setCellFactory(x -> new StringSingleCell());


            ObservableList<StringSingle> tempRumorList = FXCollections.observableArrayList();

            logger.info("Loading jhRumor");
            for (int i = 0; i < App.jhRumor.numFiles(); i++) {
                ByteBuffer stringTableBytes = App.jhRumor.getFile(i);
                //System.out.println(i);
                if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                    //System.err.println(String.format("jdMessage empty %d", i));
                    continue;
                }

                StringSingle stringTable = new StringSingle(stringTableBytes, App.rumorNames.get(i).string(), i);
                tempRumorList.add(stringTable);
            }
            rumorList.getItems().setAll(tempRumorList);
            rumorList.setCellFactory(x -> new StringSingleCell());


            ObservableList<StringSingle> tempNoticeList = FXCollections.observableArrayList();

            logger.info("Loading jhNotice");
            for (int i = 0; i < App.jhNotice.numFiles(); i++) {
                ByteBuffer stringTableBytes = App.jhNotice.getFile(i);
                //System.out.println(i);
                if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                    //System.err.println(String.format("jdMessage empty %d", i));
                    continue;
                }

                StringSingle stringTable = new StringSingle(stringTableBytes, App.noticeNames.get(i).string(), i);
                tempNoticeList.add(stringTable);
            }
            noticeList.getItems().setAll(tempNoticeList);
            noticeList.setCellFactory(x -> new StringSingleCell());
        }
    }

    public void saveMessages() throws Exception {
        List<StringTable> messages = messageList.getItems();
        //ArrayList<byte[]> encodedMessages = new ArrayList<>();
        logger.info("saving jdMessage");
        int currPos = 0;
        for (StringTable table : messages) {
            //encodedMessages.add(table.toBytes());
            //System.out.println(table.id);
            byte[] tableBytes = table.toBytes();
            System.out.println(String.format("%s: %d to %d", table.name.getValue(), currPos, currPos + tableBytes.length));
            currPos += tableBytes.length;
            if (tableBytes.length == 0) continue;
            //System.out.println(String.format("%d -> %d", App.jdMessage.getFile(table.id).rewind().remaining(), tableBytes.length));
            App.jdMessage.setFile(table.id, ByteBuffer.wrap(tableBytes).order(ByteOrder.LITTLE_ENDIAN));
        }

        logger.info("saving evMsg");
        List<StringTable> events = eventMsgList.getItems();
        for (StringTable table : events) {
            byte[] tableBytes = table.toBytes(false);
            if (tableBytes.length == 0) continue;
            App.evMsg.setFile(table.id, ByteBuffer.wrap(tableBytes).order(ByteOrder.LITTLE_ENDIAN));
        }

        logger.info("saving jhQuest");
        List<StringSingle> quests = questList.getItems();
        for (StringSingle single : quests) {
            byte[] singleBytes = single.toBytes();
            App.jhQuest.setFile(single.id, ByteBuffer.wrap(singleBytes).order(ByteOrder.LITTLE_ENDIAN));
        }
        
        logger.info("saving jhRumor");
        List<StringSingle> rumors = rumorList.getItems();
        for (StringSingle single : rumors) {
            byte[] singleBytes = single.toBytes();
            App.jhRumor.setFile(single.id, ByteBuffer.wrap(singleBytes).order(ByteOrder.LITTLE_ENDIAN));
        }
        
        logger.info("saving jhNotice");
        List<StringSingle> notices = noticeList.getItems();
        for (StringSingle single : notices) {
            byte[] singleBytes = single.toBytes();
            App.jhNotice.setFile(single.id, ByteBuffer.wrap(singleBytes).order(ByteOrder.LITTLE_ENDIAN));
        }
        
    }
}
