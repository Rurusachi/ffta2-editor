package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ruru.ffta2editor.EquipmentController.ItemCell;
import org.ruru.ffta2editor.model.item.ConsumableData;
import org.ruru.ffta2editor.utility.ShortChangeListener;
import org.ruru.ffta2editor.utility.UnsignedShortStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class ConsumableController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    @FXML ListView<ConsumableData> consumableList;

    @FXML TextField consumableName;
    @FXML TextArea consumableDescription;

    @FXML TextField buy;
    @FXML TextField sell;
    @FXML TextField _0x4;
    @FXML TextField storyProgress;

    private ObjectProperty<ConsumableData> consumableProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        
        consumableList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindEquipmentData();
            consumableProperty.setValue(newValue);
            if (newValue != null) bindEquipmentData();
        });

        // Data validators
        buy.textProperty().addListener(new ShortChangeListener(buy));
        sell.textProperty().addListener(new ShortChangeListener(sell));
        storyProgress.textProperty().addListener(new ShortChangeListener(storyProgress));
        _0x4.textProperty().addListener(new ShortChangeListener(_0x4));
    }

    private void unbindEquipmentData() {
        consumableName.textProperty().unbindBidirectional(consumableProperty.getValue().name);
        consumableDescription.textProperty().unbindBidirectional(consumableProperty.getValue().description);

        buy.textProperty().unbindBidirectional(consumableProperty.getValue().buy);
        sell.textProperty().unbindBidirectional(consumableProperty.getValue().sell);
        storyProgress.textProperty().unbindBidirectional(consumableProperty.getValue().storyProgress);
        _0x4.textProperty().unbindBidirectional(consumableProperty.getValue()._0x4);
    }

    private void bindEquipmentData() {
        consumableName.textProperty().bindBidirectional(consumableProperty.getValue().name);
        consumableDescription.textProperty().bindBidirectional(consumableProperty.getValue().description);

        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(buy.textProperty(), consumableProperty.getValue().buy, unsignedShortConverter);
        Bindings.bindBidirectional(sell.textProperty(), consumableProperty.getValue().sell, unsignedShortConverter);
        Bindings.bindBidirectional(storyProgress.textProperty(), consumableProperty.getValue().storyProgress, unsignedShortConverter);
        Bindings.bindBidirectional(_0x4.textProperty(), consumableProperty.getValue()._0x4, unsignedShortConverter);
    }

    public void loadConsumables() throws Exception {
        if (App.archive != null) {
            consumableList.setItems(FXCollections.observableArrayList(App.consumableList.subList(1, App.consumableList.size())));
            consumableList.setCellFactory(x -> new ItemCell<>());
        }
    }

    public void saveConsumables() {
        if (App.archive != null) {
        List<ConsumableData> consumable = consumableList.getItems();
        ByteBuffer newConsumableDatabytes = ByteBuffer.allocate(consumable.size()*0x8).order(ByteOrder.LITTLE_ENDIAN);

        logger.info("Saving Consumables");
        for (int i = 0; i < consumable.size(); i++) {
            try {
                newConsumableDatabytes.put(consumable.get(i).toBytes());
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Consumable %d \"%s\"", i, consumable.get(i).name.getValue()));
                throw e;
            }
        }
        newConsumableDatabytes.rewind();
        App.sysdata.setFile(17, newConsumableDatabytes);
        }
    }

}
