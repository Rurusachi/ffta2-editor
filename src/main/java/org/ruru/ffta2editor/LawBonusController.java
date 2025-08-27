package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ruru.ffta2editor.model.item.ItemData;
import org.ruru.ffta2editor.model.item.LawBonus;
import org.ruru.ffta2editor.utility.AutoCompleteComboBox;
import org.ruru.ffta2editor.utility.ShortChangeListener;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class LawBonusController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    private static final int storyRequirementOffset = 0x0011b650;

    @FXML ListView<LawBonus> lawBonusList;

    @FXML AutoCompleteComboBox<ItemData> item1;
    @FXML AutoCompleteComboBox<ItemData> item2;
    @FXML AutoCompleteComboBox<ItemData> item3;


    
    @FXML TextField storyRequirement1;
    @FXML TextField storyRequirement2;
    @FXML TextField storyRequirement3;
    @FXML TextField storyRequirement4;


    private ObjectProperty<LawBonus> lawBonusProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        
        lawBonusList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindLawBonus();
            lawBonusProperty.setValue(newValue);
            if (newValue != null) bindLawBonus();
        });
        storyRequirement1.textProperty().addListener(new ShortChangeListener(storyRequirement1));
        storyRequirement2.textProperty().addListener(new ShortChangeListener(storyRequirement2));
        storyRequirement3.textProperty().addListener(new ShortChangeListener(storyRequirement3));
        storyRequirement4.textProperty().addListener(new ShortChangeListener(storyRequirement4));
    }

    private void unbindLawBonus() {
        item1.valueProperty().unbindBidirectional(lawBonusProperty.getValue().item1);
        item2.valueProperty().unbindBidirectional(lawBonusProperty.getValue().item2);
        item3.valueProperty().unbindBidirectional(lawBonusProperty.getValue().item3);
    }

    private void bindLawBonus() {
        item1.valueProperty().bindBidirectional(lawBonusProperty.getValue().item1);
        item2.valueProperty().bindBidirectional(lawBonusProperty.getValue().item2);
        item3.valueProperty().bindBidirectional(lawBonusProperty.getValue().item3);
    }

    public void loadLawBonus() throws Exception {
        if (App.archive != null) {
            
            ByteBuffer lawBonusDataBytes = App.sysdata.getFile(45);

            if (lawBonusDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Law Bonus data is null");
            }
            lawBonusDataBytes.rewind();

            ObservableList<LawBonus> lawBonusDataList = FXCollections.observableArrayList();

            logger.info("Loading Law Bonuses");
            int numLawBonus = Short.toUnsignedInt(App.arm9.getShort(0x000cb958))+1;
            for (int i = 0; i < numLawBonus; i++) {
                try {
                    LawBonus lawBonus = new LawBonus(lawBonusDataBytes, i);
                    lawBonusDataList.add(lawBonus);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Law Bonus %d", i));
                    throw e;
                }
            }
            App.lawBonusList = lawBonusDataList;
            lawBonusList.setItems(lawBonusDataList);

            lawBonusDataBytes.rewind();

            storyRequirement1.setText(Integer.toString(Short.toUnsignedInt(App.arm9.getShort(storyRequirementOffset+0))));
            storyRequirement2.setText(Integer.toString(Short.toUnsignedInt(App.arm9.getShort(storyRequirementOffset+2))));
            storyRequirement3.setText(Integer.toString(Short.toUnsignedInt(App.arm9.getShort(storyRequirementOffset+4))));
            storyRequirement4.setText(Integer.toString(Short.toUnsignedInt(App.arm9.getShort(storyRequirementOffset+6))));

            item1.setData(App.itemList);
            item2.setData(App.itemList);
            item3.setData(App.itemList);
        }
    }

    public void saveLawBonus() {
        if (App.archive != null) {
            List<LawBonus> lawBonus = lawBonusList.getItems();
            ByteBuffer newLawBonusDatabytes = ByteBuffer.allocate(lawBonus.size()*0x8).order(ByteOrder.LITTLE_ENDIAN);

            logger.info("Saving Item Tables");
            for (int i = 0; i < lawBonus.size(); i++) {
                try {
                    newLawBonusDatabytes.put(lawBonus.get(i).toBytes());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to save Law Bonus %d", i));
                    throw e;
                }
            }
            newLawBonusDatabytes.rewind();
            App.sysdata.setFile(45, newLawBonusDatabytes);

            App.arm9.putShort(storyRequirementOffset+0, Short.parseShort(storyRequirement1.getText()));
            App.arm9.putShort(storyRequirementOffset+2, Short.parseShort(storyRequirement2.getText()));
            App.arm9.putShort(storyRequirementOffset+4, Short.parseShort(storyRequirement3.getText()));
            App.arm9.putShort(storyRequirementOffset+6, Short.parseShort(storyRequirement4.getText()));
        }
    }

}
