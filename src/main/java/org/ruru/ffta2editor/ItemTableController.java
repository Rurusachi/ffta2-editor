package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ruru.ffta2editor.EquipmentController.ItemCell;
import org.ruru.ffta2editor.model.item.ItemData;
import org.ruru.ffta2editor.model.item.ItemTable;
import org.ruru.ffta2editor.utility.AutoCompleteComboBox;
import org.ruru.ffta2editor.utility.ShortChangeListener;
import org.ruru.ffta2editor.utility.UnsignedShortStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class ItemTableController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    @FXML ListView<ItemTable> itemTableList;

    @FXML AutoCompleteComboBox<ItemData> item1;
    @FXML AutoCompleteComboBox<ItemData> item2;
    @FXML AutoCompleteComboBox<ItemData> item3;
    @FXML AutoCompleteComboBox<ItemData> item4;
    
    @FXML TextField gil1;
    @FXML TextField gil2;
    @FXML TextField gil3;
    @FXML TextField gil4;

    private ObjectProperty<ItemTable> itemTableProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        
        itemTableList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindLootTable();
            itemTableProperty.setValue(newValue);
            if (newValue != null) bindLootTable();
        });

        // Data validators
        gil1.textProperty().addListener(new ShortChangeListener(gil1));
        gil2.textProperty().addListener(new ShortChangeListener(gil2));
        gil3.textProperty().addListener(new ShortChangeListener(gil3));
        gil4.textProperty().addListener(new ShortChangeListener(gil4));
        
        item1.setVisible(false);
        item2.setVisible(false);
        item3.setVisible(false);
        item4.setVisible(false);
        gil1.setVisible(true);
        gil2.setVisible(true);
        gil3.setVisible(true);
        gil4.setVisible(true);
    }

    private void unbindLootTable() {

        if (itemTableProperty.getValue().id < 61 || itemTableProperty.getValue().id > 80) {
            itemTableProperty.getValue().item1.unbind();
            itemTableProperty.getValue().item2.unbind();
            itemTableProperty.getValue().item3.unbind();
            itemTableProperty.getValue().item4.unbind();
            //item1.valueProperty().unbindBidirectional(itemTableProperty.getValue().item1);
            //item2.valueProperty().unbindBidirectional(itemTableProperty.getValue().item2);
            //item3.valueProperty().unbindBidirectional(itemTableProperty.getValue().item3);
            //item4.valueProperty().unbindBidirectional(itemTableProperty.getValue().item4);
        } else {
            gil1.textProperty().unbindBidirectional(itemTableProperty.getValue().item1);
            gil2.textProperty().unbindBidirectional(itemTableProperty.getValue().item2);
            gil3.textProperty().unbindBidirectional(itemTableProperty.getValue().item3);
            gil4.textProperty().unbindBidirectional(itemTableProperty.getValue().item4);
        }
    }

    private void bindLootTable() {

        if (itemTableProperty.getValue().id < 61 || itemTableProperty.getValue().id > 80) {
            item1.setVisible(true);
            item2.setVisible(true);
            item3.setVisible(true);
            item4.setVisible(true);
            gil1.setVisible(false);
            gil2.setVisible(false);
            gil3.setVisible(false);
            gil4.setVisible(false);

            item1.clearFilter();
            item1.getSelectionModel().select(Short.toUnsignedInt(itemTableProperty.getValue().item1.getValue()));
            itemTableProperty.getValue().item1.bind(new ObjectBinding<Short>() {
                {bind(item1.valueProperty());}
                @Override
                protected Short computeValue() {
                    return (short)item1.valueProperty().getValue().id;
                }

            });
            
            item2.clearFilter();
            item2.getSelectionModel().select(Short.toUnsignedInt(itemTableProperty.getValue().item2.getValue()));
            itemTableProperty.getValue().item2.bind(new ObjectBinding<Short>() {
                {bind(item2.valueProperty());}
                @Override
                protected Short computeValue() {
                    return (short)item2.valueProperty().getValue().id;
                }

            });

            item3.clearFilter();
            item3.getSelectionModel().select(Short.toUnsignedInt(itemTableProperty.getValue().item3.getValue()));
            itemTableProperty.getValue().item3.bind(new ObjectBinding<Short>() {
                {bind(item3.valueProperty());}
                @Override
                protected Short computeValue() {
                    return (short)item3.valueProperty().getValue().id;
                }

            });

            item4.clearFilter();
            item4.getSelectionModel().select(Short.toUnsignedInt(itemTableProperty.getValue().item4.getValue()));
            itemTableProperty.getValue().item4.bind(new ObjectBinding<Short>() {
                {bind(item4.valueProperty());}
                @Override
                protected Short computeValue() {
                    return (short)item4.valueProperty().getValue().id;
                }

            });

            //item1.valueProperty().bindBidirectional(itemTableProperty.getValue().item1);
            //item2.valueProperty().bindBidirectional(itemTableProperty.getValue().item2);
            //item3.valueProperty().bindBidirectional(itemTableProperty.getValue().item3);
            //item4.valueProperty().bindBidirectional(itemTableProperty.getValue().item4);
        } else {
            item1.setVisible(false);
            item2.setVisible(false);
            item3.setVisible(false);
            item4.setVisible(false);
            gil1.setVisible(true);
            gil2.setVisible(true);
            gil3.setVisible(true);
            gil4.setVisible(true);
            StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
            Bindings.bindBidirectional(gil1.textProperty(), itemTableProperty.getValue().item1, unsignedShortConverter);
            Bindings.bindBidirectional(gil2.textProperty(), itemTableProperty.getValue().item2, unsignedShortConverter);
            Bindings.bindBidirectional(gil3.textProperty(), itemTableProperty.getValue().item3, unsignedShortConverter);
            Bindings.bindBidirectional(gil4.textProperty(), itemTableProperty.getValue().item4, unsignedShortConverter);
        }

        
    }

    public void loadItemTables() throws Exception {
        if (App.archive != null) {
            
            ByteBuffer itemTableDataBytes = App.sysdata.getFile(24);

            if (itemTableDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Item Table data is null");
            }
            itemTableDataBytes.rewind();

            ObservableList<ItemTable> itemTableDataList = FXCollections.observableArrayList();

            logger.info("Loading Item Tables");
            int numItemTables = Short.toUnsignedInt(App.arm9.getShort(0x000cb5fc))+1;
            for (int i = 0; i < numItemTables; i++) {
                try {
                    ItemTable itemTable = new ItemTable(itemTableDataBytes, i);
                    itemTableDataList.add(itemTable);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Item Table %d", i));
                    throw e;
                }
            }
            App.itemTableList = itemTableDataList;
            itemTableList.setItems(itemTableDataList);

            itemTableDataBytes.rewind();

            item1.setData(App.itemList);
            item1.setCellFactory(x -> new ItemCell<>());
            item2.setData(App.itemList);
            item2.setCellFactory(x -> new ItemCell<>());
            item3.setData(App.itemList);
            item3.setCellFactory(x -> new ItemCell<>());
            item4.setData(App.itemList);
            item4.setCellFactory(x -> new ItemCell<>());
        }
    }

    public void saveItemTables() {
        if (App.archive != null) {
            List<ItemTable> itemTable = itemTableList.getItems();
            ByteBuffer newItemTableDatabytes = ByteBuffer.allocate(itemTable.size()*0x8).order(ByteOrder.LITTLE_ENDIAN);
            
            logger.info("Saving Item Tables");
            for (int i = 0; i < itemTable.size(); i++) {
                try {
                    newItemTableDatabytes.put(itemTable.get(i).toBytes());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to save Item Table %d", i));
                    throw e;
                }
            }
            newItemTableDatabytes.rewind();
            App.sysdata.setFile(24, newItemTableDatabytes);
        }
    }

}
