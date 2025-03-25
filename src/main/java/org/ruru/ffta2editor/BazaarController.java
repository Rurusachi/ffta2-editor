package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import org.ruru.ffta2editor.EquipmentController.ItemCell;
import org.ruru.ffta2editor.model.bazaar.BazaarRecipe;
import org.ruru.ffta2editor.model.bazaar.BazaarSet;
import org.ruru.ffta2editor.model.bazaar.BazaarSet.BazaarSetItem;
import org.ruru.ffta2editor.model.item.EquipmentData;
import org.ruru.ffta2editor.model.item.LootData;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.IntRangeChangeListener;
import org.ruru.ffta2editor.utility.ShortChangeListener;
import org.ruru.ffta2editor.utility.UnsignedByteStringConverter;
import org.ruru.ffta2editor.utility.UnsignedShortStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class BazaarController {
    
    public static class BazaarSetCell extends ListCell<BazaarSet> {
        Label label = new Label();

        public BazaarSetCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(BazaarSet set, boolean empty) {
            super.updateItem(set, empty);
            if (set != null) {
                label.setText(String.format("%X: %s", set.id, set.name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }
    
    public static class BazaarSetItemCell extends ListCell<BazaarSetItem> {
        Label label = new Label();

        public BazaarSetItemCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(BazaarSetItem setItem, boolean empty) {
            super.updateItem(setItem, empty);
            if (setItem != null) {
                label.setText(String.format("%X: %s", setItem.item.getValue().id, setItem.item.getValue().name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    @FXML ListView<BazaarSet> bazaarSetList;
    @FXML ListView<BazaarSetItem> bazaarSetItemList;

    @FXML TextField bazaarSetName;
    @FXML TextArea bazaarSetDescription;

    
    @FXML TextField storyRequirement;
    @FXML TextField flagRequirement;

    @FXML ComboBox<EquipmentData> item;
    @FXML TextField grade;

    @FXML ComboBox<LootData> loot1;
    @FXML ComboBox<LootData> loot2;
    @FXML ComboBox<LootData> loot3;
    

    private ObjectProperty<BazaarSet> bazaarSetProperty = new SimpleObjectProperty<>();
    private ObjectProperty<BazaarSetItem> bazaarSetItemProperty = new SimpleObjectProperty<>();
    private ObjectProperty<BazaarRecipe> bazaarRecipeProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        bazaarSetList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindBazaarSetData();
            bazaarSetProperty.setValue(newValue);
            bazaarSetItemList.getSelectionModel().clearSelection();
            if (newValue != null) bindBazaarSetData();
        });
        bazaarSetItemList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindBazaarSetItemData();
            bazaarSetItemProperty.setValue(newValue);
            item.getSelectionModel().clearSelection();
            if (newValue != null) bindBazaarSetItemData();
        });
        item.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindBazaarRecipeData();
            
            loot1.getSelectionModel().clearSelection();
            loot2.getSelectionModel().clearSelection();
            loot3.getSelectionModel().clearSelection();
            if (newValue != null) {
                bazaarRecipeProperty.setValue(App.bazaarRecipeList.get(newValue.id));
                bindBazaarRecipeData();
            } else {
                bazaarRecipeProperty.setValue(null);
            }
        });
        
        // Data validators
        storyRequirement.textProperty().addListener(new ShortChangeListener(storyRequirement));
        flagRequirement.textProperty().addListener(new ShortChangeListener(flagRequirement));

        grade.textProperty().addListener(new IntRangeChangeListener(grade, 0, 5));
    }

    public void unbindBazaarSetData() {
        storyRequirement.textProperty().unbindBidirectional(bazaarSetProperty.getValue().storyRequirement);
        flagRequirement.textProperty().unbindBidirectional(bazaarSetProperty.getValue().flagRequirement);

        bazaarSetName.textProperty().unbindBidirectional(bazaarSetProperty.getValue().name);
        bazaarSetDescription.textProperty().unbindBidirectional(bazaarSetProperty.getValue().description);

        bazaarSetItemList.setItems(null);
    }

    public void bindBazaarSetData() {
        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(storyRequirement.textProperty(), bazaarSetProperty.getValue().storyRequirement, unsignedShortConverter);
        Bindings.bindBidirectional(flagRequirement.textProperty(), bazaarSetProperty.getValue().flagRequirement, unsignedShortConverter);

        bazaarSetName.textProperty().bindBidirectional(bazaarSetProperty.getValue().name);
        bazaarSetDescription.textProperty().bindBidirectional(bazaarSetProperty.getValue().description);

        bazaarSetItemList.setItems(bazaarSetProperty.getValue().items);
    }

    public void unbindBazaarSetItemData() {
        item.valueProperty().unbindBidirectional(bazaarSetItemProperty.getValue().item);

        grade.textProperty().unbindBidirectional(bazaarSetItemProperty.getValue().grade);
    }

    public void bindBazaarSetItemData() {
        item.valueProperty().bindBidirectional(bazaarSetItemProperty.getValue().item);

        StringConverter<Byte> unsignedByteConverter = new UnsignedByteStringConverter();
        Bindings.bindBidirectional(grade.textProperty(), bazaarSetItemProperty.getValue().grade, unsignedByteConverter);
    }

    public void unbindBazaarRecipeData() {
        loot1.valueProperty().unbindBidirectional(bazaarRecipeProperty.getValue().loot1);
        loot2.valueProperty().unbindBidirectional(bazaarRecipeProperty.getValue().loot2);
        loot3.valueProperty().unbindBidirectional(bazaarRecipeProperty.getValue().loot3);
    }

    public void bindBazaarRecipeData() {
        loot1.valueProperty().bindBidirectional(bazaarRecipeProperty.getValue().loot1);
        loot2.valueProperty().bindBidirectional(bazaarRecipeProperty.getValue().loot2);
        loot3.valueProperty().bindBidirectional(bazaarRecipeProperty.getValue().loot3);
    }

    @FXML
    public void addBazaarSet() {
        if (bazaarSetList.getItems() != null) {
            int newIndex = bazaarSetList.getItems().size();
            bazaarSetList.getItems().add(new BazaarSet("", newIndex));
            bazaarSetList.getSelectionModel().selectLast();;
        }
    }

    @FXML
    public void removeBazaarSet() {
        if (bazaarSetList.getItems().size() > 0) {
            bazaarSetList.getItems().removeLast();
        }
    }

    public void loadBazaar() {
        if (App.archive != null) {

            ByteBuffer bazaarRecipeBytes = App.sysdata.getFile(26);

            if (bazaarRecipeBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            bazaarRecipeBytes.rewind();

            ObservableList<BazaarRecipe> bazaarRecipeDataList = FXCollections.observableArrayList();

            int numBazaarRecipes = Short.toUnsignedInt(App.arm9.getShort(0x000cb684))+1;
            for (int i = 0; i < numBazaarRecipes; i++) {
                BazaarRecipe bazaarRecipeData = new BazaarRecipe(bazaarRecipeBytes, i);
                bazaarRecipeDataList.add(bazaarRecipeData);
            }
            App.bazaarRecipeList = bazaarRecipeDataList;

            bazaarRecipeBytes.rewind();
            

            ByteBuffer bazaarSetBytes = App.sysdata.getFile(25);

            if (bazaarSetBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            bazaarSetBytes.rewind();

            ObservableList<BazaarSet> bazaarSetDataList = FXCollections.observableArrayList();

            int numBazaarSets = Byte.toUnsignedInt(App.arm9.get(0x000cb634))+1;
            for (int i = 0; i < numBazaarSets; i++) {
                BazaarSet bazaarSetData = new BazaarSet(bazaarSetBytes, i);
                bazaarSetDataList.add(bazaarSetData);
            }
            bazaarSetList.setItems(bazaarSetDataList);
            bazaarSetList.setCellFactory(x -> new BazaarSetCell());
            
            bazaarSetItemList.setCellFactory(x -> new BazaarSetItemCell());

            bazaarSetBytes.rewind();

            item.setItems(App.equipmentList);
            item.setCellFactory(x -> new ItemCell<>());
            item.setButtonCell(new ItemCell<>());

            loot1.setItems(App.lootList);
            loot1.setCellFactory(x -> new ItemCell<>());
            loot1.setButtonCell(new ItemCell<>());

            loot2.setItems(App.lootList);
            loot2.setCellFactory(x -> new ItemCell<>());
            loot2.setButtonCell(new ItemCell<>());

            loot3.setItems(App.lootList);
            loot3.setCellFactory(x -> new ItemCell<>());
            loot3.setButtonCell(new ItemCell<>());
        }
    }

    public void saveBazaar() {
        List<BazaarRecipe> bazaarRecipes = App.bazaarRecipeList;
        ByteBuffer newBazaarRecipeDatabytes = ByteBuffer.allocate(bazaarRecipes.size()*0x8).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < bazaarRecipes.size(); i++) {
            newBazaarRecipeDatabytes.put(bazaarRecipes.get(i).toBytes());
        }
        newBazaarRecipeDatabytes.rewind();
        App.sysdata.setFile(26, newBazaarRecipeDatabytes);
        
        List<BazaarSet> bazaarSets = bazaarSetList.getItems();
        ByteBuffer newBazaarSetDatabytes = ByteBuffer.allocate(bazaarSets.size()*0x2c).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < bazaarSets.size(); i++) {
            newBazaarSetDatabytes.put(bazaarSets.get(i).toBytes());
        }
        newBazaarSetDatabytes.rewind();
        App.sysdata.setFile(25, newBazaarSetDatabytes);
        // Patch arm9 code with new Set length
        App.arm9.put(0x000cb634, (byte)(bazaarSets.size()-1));
        App.arm9.put(0x000cb638, (byte)(bazaarSets.size()-1));

        App.overlay8.put(0x171c, (byte)(bazaarSets.size()));
    }

}
