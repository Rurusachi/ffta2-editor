package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ruru.ffta2editor.EquipmentController.ItemCell;
import org.ruru.ffta2editor.model.ability.AbilityElement;
import org.ruru.ffta2editor.model.item.ConsumableData;
import org.ruru.ffta2editor.model.item.LootCategory;
import org.ruru.ffta2editor.model.item.LootData;
import org.ruru.ffta2editor.model.item.LootRank;
import org.ruru.ffta2editor.utility.AutoCompleteComboBox;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.ByteStringConverter;
import org.ruru.ffta2editor.utility.ShortChangeListener;
import org.ruru.ffta2editor.utility.UnsignedShortStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class LootController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    @FXML ListView<LootData> lootList;

    @FXML TextField lootName;
    @FXML TextArea lootDescription;

    @FXML TextField buy;
    @FXML TextField sell;
    @FXML TextField _0x6;
    
    @FXML AutoCompleteComboBox<LootRank> rank;
    @FXML AutoCompleteComboBox<LootCategory> category;

    private ObjectProperty<LootData> lootProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        
        lootList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindEquipmentData();
            lootProperty.setValue(newValue);
            if (newValue != null) bindEquipmentData();
        });
        ObservableList<LootCategory> lootCategoryEnums = FXCollections.observableArrayList(LootCategory.values());
        category.setData(lootCategoryEnums);
        ObservableList<LootRank> lootRankEnums = FXCollections.observableArrayList(LootRank.values());
        rank.setData(lootRankEnums);

        // Data validators
        buy.textProperty().addListener(new ShortChangeListener(buy));
        sell.textProperty().addListener(new ShortChangeListener(sell));
        _0x6.textProperty().addListener(new ShortChangeListener(_0x6));
    }

    private void unbindEquipmentData() {
        lootName.textProperty().unbindBidirectional(lootProperty.getValue().name);
        lootDescription.textProperty().unbindBidirectional(lootProperty.getValue().description);

        buy.textProperty().unbindBidirectional(lootProperty.getValue().buy);
        sell.textProperty().unbindBidirectional(lootProperty.getValue().sell);
        _0x6.textProperty().unbindBidirectional(lootProperty.getValue()._0x6);

        rank.valueProperty().unbindBidirectional(lootProperty.getValue().rank);
        category.valueProperty().unbindBidirectional(lootProperty.getValue().category);
    }

    private void bindEquipmentData() {
        lootName.textProperty().bindBidirectional(lootProperty.getValue().name);
        lootDescription.textProperty().bindBidirectional(lootProperty.getValue().description);

        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(buy.textProperty(), lootProperty.getValue().buy, unsignedShortConverter);
        Bindings.bindBidirectional(sell.textProperty(), lootProperty.getValue().sell, unsignedShortConverter);
        Bindings.bindBidirectional(_0x6.textProperty(), lootProperty.getValue()._0x6, unsignedShortConverter);

        rank.valueProperty().bindBidirectional(lootProperty.getValue().rank);
        category.valueProperty().bindBidirectional(lootProperty.getValue().category);
    }

    public void loadLoot() throws Exception {
        if (App.archive != null) {
            lootList.setItems(FXCollections.observableArrayList(App.lootList.subList(1, App.lootList.size())));
            lootList.setCellFactory(x -> new ItemCell<>());
        }
    }

    public void saveLoot() {
        if (App.archive != null) {
        List<LootData> loot = lootList.getItems();
        ByteBuffer newLootDatabytes = ByteBuffer.allocate(loot.size()*0x8).order(ByteOrder.LITTLE_ENDIAN);

        logger.info("Saving Loot");
        for (int i = 0; i < loot.size(); i++) {
            try {
                newLootDatabytes.put(loot.get(i).toBytes());
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Loot %d \"%s\"", i, loot.get(i).name.getValue()));
                throw e;
            }
        }
        newLootDatabytes.rewind();
        App.sysdata.setFile(18, newLootDatabytes);
        }
    }

}
