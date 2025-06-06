package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.AbilityController.AbilityCell;
import org.ruru.ffta2editor.JobController.JobSpriteCell;
import org.ruru.ffta2editor.JobGroupController.JobGroupCell;
import org.ruru.ffta2editor.TextController.StringPropertyCell;
import org.ruru.ffta2editor.TextController.StringWithId;
import org.ruru.ffta2editor.TextController.StringWithIdCell;
import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.ability.AbilityElement;
import org.ruru.ffta2editor.model.item.ConsumableData;
import org.ruru.ffta2editor.model.item.EquipmentData;
import org.ruru.ffta2editor.model.item.EquipmentLocation;
import org.ruru.ffta2editor.model.item.EquipmentType;
import org.ruru.ffta2editor.model.item.ItemData;
import org.ruru.ffta2editor.model.item.LootData;
import org.ruru.ffta2editor.model.job.JobGroup;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.ShortChangeListener;
import org.ruru.ffta2editor.utility.UnitSprite;
import org.ruru.ffta2editor.utility.ByteStringConverter;
import org.ruru.ffta2editor.utility.UnsignedShortStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

import org.ruru.ffta2editor.utility.AutoCompleteComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class EquipmentController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");
    
    public static class ItemCell<T extends ItemData>  extends ListCell<T> {
        Label label = new Label();

        public ItemCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                label.setText(String.format("%X: %s", item.id, item.name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    @FXML ListView<EquipmentData> equipmentList;

    @FXML TextField equipmentName;
    @FXML TextArea equipmentDescription;

    @FXML AutoCompleteComboBox<AbilityElement> element;
    @FXML AutoCompleteComboBox<EquipmentType> equipmentType;
    @FXML AutoCompleteComboBox<EquipmentLocation> equipmentLocation;
    @FXML ComboBox<StringWithId> bonusEffect;
    @FXML AutoCompleteComboBox<AbilityData> ability1;
    @FXML AutoCompleteComboBox<AbilityData> ability2;
    @FXML AutoCompleteComboBox<AbilityData> ability3;
    @FXML AutoCompleteComboBox<JobGroup> jobGroup1;
    @FXML AutoCompleteComboBox<JobGroup> jobGroup2;
    @FXML AutoCompleteComboBox<JobGroup> jobGroup3;
    
    @FXML ComboBox<UnitSprite> sprite;
    @FXML ComboBox<Byte> palette;

    @FXML TextField notName;
    @FXML TextField buy;
    @FXML TextField sell;
    @FXML TextField attackEffect;
    
    @FXML TextField hitEffect;
    @FXML TextField hitSound;

    @FXML TextField attack;
    @FXML TextField defense;
    @FXML TextField magick;
    @FXML TextField resistance;
    @FXML TextField speed;
    @FXML TextField evasion;
    @FXML TextField move;
    @FXML TextField jump;
    @FXML TextField range;

    @FXML CheckBox isTwoHanded;
    @FXML CheckBox isDualWieldable;
    @FXML CheckBox isMeleeOneHanded;
    @FXML CheckBox isMeleeTwoHanded;
    @FXML CheckBox isSellable;
    @FXML CheckBox isLimitedStock;
    @FXML CheckBox startsInShop;
    @FXML CheckBox isBow;
    @FXML CheckBox isBladed;
    @FXML CheckBox isPiercing;
    @FXML CheckBox isBlunt;
    @FXML CheckBox isRanged;
    @FXML CheckBox isInstrument_Book;
    @FXML CheckBox isFemaleOnly;
    @FXML CheckBox propertyBit14;
    @FXML CheckBox propertyBit15;
    @FXML CheckBox propertyBit16;
    @FXML CheckBox propertyBit17;
    @FXML CheckBox propertyBit18;
    @FXML CheckBox propertyBit19;
    @FXML CheckBox propertyBit20;
    @FXML CheckBox propertyBit21;
    @FXML CheckBox propertyBit22;
    @FXML CheckBox propertyBit23;
    @FXML CheckBox propertyBit24;
    @FXML CheckBox propertyBit25;
    @FXML CheckBox propertyBit26;
    @FXML CheckBox propertyBit27;
    @FXML CheckBox propertyBit28;
    @FXML CheckBox propertyBit29;
    @FXML CheckBox propertyBit30;
    @FXML CheckBox propertyBit31;

    private ObjectProperty<EquipmentData> equipmentProperty = new SimpleObjectProperty<>();

    // TODO: Expand item palette list
    private ObservableList<Byte> paletteList = FXCollections.observableArrayList((byte)0, (byte)1, (byte)2);

    @FXML
    public void initialize() {
        
        equipmentList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindEquipmentData();
            equipmentProperty.setValue(newValue);
            if (newValue != null) bindEquipmentData();
        });
        ObservableList<AbilityElement> abilityElementEnums = FXCollections.observableArrayList(AbilityElement.values());
        element.setData(abilityElementEnums);
        ObservableList<EquipmentType> equipmentTypeEnums = FXCollections.observableArrayList(EquipmentType.values());
        equipmentType.setData(equipmentTypeEnums);
        ObservableList<EquipmentLocation> equipmentLocationEnums = FXCollections.observableArrayList(EquipmentLocation.values());
        equipmentLocation.setData(equipmentLocationEnums);
        palette.setItems(paletteList);

    
        palette.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int newPalette = newValue != null ? newValue : 0;
            sprite.setButtonCell(new JobSpriteCell(0, newPalette, 2));
            sprite.setCellFactory(x -> new JobSpriteCell(0, newPalette, 2));
        });

        sprite.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selected = palette.getSelectionModel().getSelectedIndex();
            paletteList.clear();
            if (newValue != null) {
                IntStream.range(0, newValue.spritePalettes.palettes.size()).forEach(i -> paletteList.add((byte)i));
            }
            if (selected < paletteList.size()) palette.getSelectionModel().select(selected);
            else palette.getSelectionModel().select(0);
        });

        // Data validators
        notName.textProperty().addListener(new ShortChangeListener(notName));
        buy.textProperty().addListener(new ShortChangeListener(buy));
        sell.textProperty().addListener(new ShortChangeListener(sell));
        attackEffect.textProperty().addListener(new ShortChangeListener(attackEffect));

        hitEffect.textProperty().addListener(new ByteChangeListener(hitEffect));
        hitSound.textProperty().addListener(new ByteChangeListener(hitSound));
        attack.textProperty().addListener(new ByteChangeListener(attack, PatchesController.patchedSignedEquipmentStats));
        defense.textProperty().addListener(new ByteChangeListener(defense, PatchesController.patchedSignedEquipmentStats));
        magick.textProperty().addListener(new ByteChangeListener(magick, PatchesController.patchedSignedEquipmentStats));
        resistance.textProperty().addListener(new ByteChangeListener(resistance, PatchesController.patchedSignedEquipmentStats));
        speed.textProperty().addListener(new ByteChangeListener(speed, PatchesController.patchedSignedEquipmentStats));
        evasion.textProperty().addListener(new ByteChangeListener(evasion, PatchesController.patchedSignedEquipmentStats));
        move.textProperty().addListener(new ByteChangeListener(move, PatchesController.patchedSignedEquipmentStats));
        jump.textProperty().addListener(new ByteChangeListener(jump, PatchesController.patchedSignedEquipmentStats));
        range.textProperty().addListener(new ByteChangeListener(range));
    }

    private void unbindEquipmentData() {
        equipmentName.textProperty().unbindBidirectional(equipmentProperty.getValue().name);
        equipmentDescription.textProperty().unbindBidirectional(equipmentProperty.getValue().description);

        notName.textProperty().unbindBidirectional(equipmentProperty.getValue().notName);
        buy.textProperty().unbindBidirectional(equipmentProperty.getValue().buy);
        sell.textProperty().unbindBidirectional(equipmentProperty.getValue().sell);
        attackEffect.textProperty().unbindBidirectional(equipmentProperty.getValue().attackEffect);

        hitEffect.textProperty().unbindBidirectional(equipmentProperty.getValue().hitEffect);
        hitSound.textProperty().unbindBidirectional(equipmentProperty.getValue().hitSound);
        attack.textProperty().unbindBidirectional(equipmentProperty.getValue().attack);
        defense.textProperty().unbindBidirectional(equipmentProperty.getValue().defense);
        magick.textProperty().unbindBidirectional(equipmentProperty.getValue().magick);
        resistance.textProperty().unbindBidirectional(equipmentProperty.getValue().resistance);
        speed.textProperty().unbindBidirectional(equipmentProperty.getValue().speed);
        evasion.textProperty().unbindBidirectional(equipmentProperty.getValue().evasion);
        move.textProperty().unbindBidirectional(equipmentProperty.getValue().move);
        jump.textProperty().unbindBidirectional(equipmentProperty.getValue().jump);
        range.textProperty().unbindBidirectional(equipmentProperty.getValue().range);

        palette.valueProperty().unbindBidirectional(equipmentProperty.getValue().palette);
        sprite.valueProperty().unbindBidirectional(equipmentProperty.getValue().sprite);

        element.valueProperty().unbindBidirectional(equipmentProperty.getValue().element);
        equipmentType.valueProperty().unbindBidirectional(equipmentProperty.getValue().equipmentType);
        equipmentLocation.valueProperty().unbindBidirectional(equipmentProperty.getValue().equipmentLocation);
        ability1.valueProperty().unbindBidirectional(equipmentProperty.getValue().ability1);
        ability2.valueProperty().unbindBidirectional(equipmentProperty.getValue().ability2);
        ability3.valueProperty().unbindBidirectional(equipmentProperty.getValue().ability3);
        jobGroup1.valueProperty().unbindBidirectional(equipmentProperty.getValue().jobGroup1);
        jobGroup2.valueProperty().unbindBidirectional(equipmentProperty.getValue().jobGroup2);
        jobGroup3.valueProperty().unbindBidirectional(equipmentProperty.getValue().jobGroup3);

        equipmentProperty.getValue().bonusEffect.unbind();
        
        isTwoHanded.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isTwoHanded);
        isDualWieldable.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isDualWieldable);
        isMeleeOneHanded.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isMeleeOneHanded);
        isMeleeTwoHanded.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isMeleeTwoHanded);
        isSellable.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isSellable);
        isLimitedStock.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isLimitedStock);
        startsInShop.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.startsInShop);
        isBow.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isBow);
        isBladed.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isBladed);
        isPiercing.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isPiercing);
        isBlunt.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isBlunt);
        isRanged.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isRanged);
        isInstrument_Book.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isInstrument_Book);
        isFemaleOnly.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.isFemaleOnly);
        propertyBit14.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit14);
        propertyBit15.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit15);
        propertyBit16.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit16);
        propertyBit17.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit17);
        propertyBit18.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit18);
        propertyBit19.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit19);
        propertyBit20.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit20);
        propertyBit21.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit21);
        propertyBit22.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit22);
        propertyBit23.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit23);
        propertyBit24.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit24);
        propertyBit25.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit25);
        propertyBit26.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit26);
        propertyBit27.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit27);
        propertyBit28.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit28);
        propertyBit29.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit29);
        propertyBit30.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit30);
        propertyBit31.selectedProperty().unbindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit31);
    }

    private void bindEquipmentData() {
        equipmentName.textProperty().bindBidirectional(equipmentProperty.getValue().name);
        equipmentDescription.textProperty().bindBidirectional(equipmentProperty.getValue().description);
        
        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(notName.textProperty(), equipmentProperty.getValue().notName, unsignedShortConverter);
        Bindings.bindBidirectional(buy.textProperty(), equipmentProperty.getValue().buy, unsignedShortConverter);
        Bindings.bindBidirectional(sell.textProperty(), equipmentProperty.getValue().sell, unsignedShortConverter);
        Bindings.bindBidirectional(attackEffect.textProperty(), equipmentProperty.getValue().attackEffect, unsignedShortConverter);
        
        StringConverter<Byte> unsignedByteConverter = new ByteStringConverter();
        StringConverter<Byte> equipmentStatByteConverter = new ByteStringConverter(PatchesController.patchedSignedEquipmentStats);
        Bindings.bindBidirectional(hitEffect.textProperty(), equipmentProperty.getValue().hitEffect, unsignedByteConverter);
        Bindings.bindBidirectional(hitSound.textProperty(), equipmentProperty.getValue().hitSound, unsignedByteConverter);
        Bindings.bindBidirectional(attack.textProperty(), equipmentProperty.getValue().attack, equipmentStatByteConverter);
        Bindings.bindBidirectional(defense.textProperty(), equipmentProperty.getValue().defense, equipmentStatByteConverter);
        Bindings.bindBidirectional(magick.textProperty(), equipmentProperty.getValue().magick, equipmentStatByteConverter);
        Bindings.bindBidirectional(resistance.textProperty(), equipmentProperty.getValue().resistance, equipmentStatByteConverter);
        Bindings.bindBidirectional(speed.textProperty(), equipmentProperty.getValue().speed, equipmentStatByteConverter);
        Bindings.bindBidirectional(evasion.textProperty(), equipmentProperty.getValue().evasion, equipmentStatByteConverter);
        Bindings.bindBidirectional(move.textProperty(), equipmentProperty.getValue().move, equipmentStatByteConverter);
        Bindings.bindBidirectional(jump.textProperty(), equipmentProperty.getValue().jump, equipmentStatByteConverter);
        Bindings.bindBidirectional(range.textProperty(), equipmentProperty.getValue().range, unsignedByteConverter);

        
        sprite.valueProperty().bindBidirectional(equipmentProperty.getValue().sprite);
        sprite.setButtonCell(new JobSpriteCell(0, equipmentProperty.getValue().palette.getValue(), 2));
        sprite.setCellFactory(x -> new JobSpriteCell(0, equipmentProperty.getValue().palette.getValue(), 2));
        palette.valueProperty().bindBidirectional(equipmentProperty.getValue().palette);

        element.valueProperty().bindBidirectional(equipmentProperty.getValue().element);
        equipmentType.valueProperty().bindBidirectional(equipmentProperty.getValue().equipmentType);
        equipmentLocation.valueProperty().bindBidirectional(equipmentProperty.getValue().equipmentLocation);
        ability1.valueProperty().bindBidirectional(equipmentProperty.getValue().ability1);
        ability2.valueProperty().bindBidirectional(equipmentProperty.getValue().ability2);
        ability3.valueProperty().bindBidirectional(equipmentProperty.getValue().ability3);
        jobGroup1.valueProperty().bindBidirectional(equipmentProperty.getValue().jobGroup1);
        jobGroup2.valueProperty().bindBidirectional(equipmentProperty.getValue().jobGroup2);
        jobGroup3.valueProperty().bindBidirectional(equipmentProperty.getValue().jobGroup3);

        bonusEffect.getSelectionModel().select(Short.toUnsignedInt(equipmentProperty.getValue().bonusEffect.getValue()));
        equipmentProperty.getValue().bonusEffect.bind(new ObjectBinding<Byte>() {
            {bind(bonusEffect.valueProperty());}
            @Override
            protected Byte computeValue() {
                return (byte)App.bonusEffects.indexOf(bonusEffect.valueProperty().getValue());
            }

        });
        
        isTwoHanded.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isTwoHanded);
        isDualWieldable.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isDualWieldable);
        isMeleeOneHanded.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isMeleeOneHanded);
        isMeleeTwoHanded.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isMeleeTwoHanded);
        isSellable.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isSellable);
        isLimitedStock.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isLimitedStock);
        startsInShop.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.startsInShop);
        isBow.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isBow);
        isBladed.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isBladed);
        isPiercing.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isPiercing);
        isBlunt.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isBlunt);
        isRanged.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isRanged);
        isInstrument_Book.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isInstrument_Book);
        isFemaleOnly.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.isFemaleOnly);
        propertyBit14.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit14);
        propertyBit15.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit15);
        propertyBit16.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit16);
        propertyBit17.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit17);
        propertyBit18.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit18);
        propertyBit19.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit19);
        propertyBit20.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit20);
        propertyBit21.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit21);
        propertyBit22.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit22);
        propertyBit23.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit23);
        propertyBit24.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit24);
        propertyBit25.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit25);
        propertyBit26.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit26);
        propertyBit27.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit27);
        propertyBit28.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit28);
        propertyBit29.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit29);
        propertyBit30.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit30);
        propertyBit31.selectedProperty().bindBidirectional(equipmentProperty.getValue().propertyFlags.propertyBit31);
    }

    public void loadEquipment() throws Exception {
        if (App.archive != null) {

            ByteBuffer equipmentDataBytes = App.sysdata.getFile(16);

            if (equipmentDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Equipment data is null");
            }
            equipmentDataBytes.rewind();

            ObservableList<EquipmentData> equipmentDataList = FXCollections.observableArrayList();

            logger.info("Loading Equipment");
            int numEquipment = Short.toUnsignedInt(App.arm9.getShort(0x000cb3fc))+1;
            for (int i = 0; i < numEquipment; i++) {
                try {
                    EquipmentData equipmentData = new EquipmentData(equipmentDataBytes, i);
                    equipmentDataList.add(equipmentData);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Equipment %d \"%s\"", i, App.itemNames.size() > i ? App.itemNames.get(i).string().getValue() : ""));
                    throw e;
                }
            }
            App.equipmentList = equipmentDataList;
            equipmentList.setItems(equipmentDataList);
            equipmentList.setCellFactory(x -> new ItemCell<>());

            equipmentDataBytes.rewind();

            // Consumables
            ByteBuffer consumableDataBytes = App.sysdata.getFile(17);

            if (consumableDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Consumable data is null");
            }
            consumableDataBytes.rewind();

            ObservableList<ConsumableData> consumableDataList = FXCollections.observableArrayList();

            logger.info("Loading Consumables");
            consumableDataList.add(new ConsumableData("", 0));
            int numConsumables = 0x13;
            for (int i = 0; i < numConsumables; i++) {
                try {
                    ConsumableData consumableData = new ConsumableData(consumableDataBytes, i+numEquipment);
                    consumableDataList.add(consumableData);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Consumable %d \"%s\"", i+numEquipment, App.itemNames.size() > i ? App.itemNames.get(i).string().getValue() : ""));
                    throw e;
                }
            }
            App.consumableList = consumableDataList;

            consumableDataBytes.rewind();

            // Loot
            ByteBuffer lootDataBytes = App.sysdata.getFile(18);

            if (lootDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Loot data is null");
            }
            lootDataBytes.rewind();

            ObservableList<LootData> lootDataList = FXCollections.observableArrayList();

            logger.info("Loading Loot");
            lootDataList.add(new LootData("", 0));
            int numloot = 0xCC;
            for (int i = 0; i < numloot; i++) {
                try {
                    LootData lootData = new LootData(lootDataBytes, i+numEquipment+numConsumables);
                    lootDataList.add(lootData);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Loot %d \"%s\"", i+numEquipment+numConsumables, App.itemNames.size() > i ? App.itemNames.get(i).string().getValue() : ""));
                    throw e;
                }
            }
            App.lootList = lootDataList;

            lootDataBytes.rewind();

            App.itemList = FXCollections.observableArrayList();
            App.itemList.addAll(App.equipmentList);
            App.itemList.addAll(App.consumableList.subList(1, App.consumableList.size()));
            App.itemList.addAll(App.lootList.subList(1, App.lootList.size()));



            
            sprite.setButtonCell(new JobSpriteCell(0, 0, 2));
            sprite.setCellFactory(x -> new JobSpriteCell(0, 0, 2));
            sprite.setItems(App.unitSprites);

            bonusEffect.setItems(App.bonusEffects);
            bonusEffect.setCellFactory(x -> new StringWithIdCell());
            bonusEffect.setButtonCell(new StringWithIdCell());

            ability1.setData(App.abilityList);
            ability1.setCellFactory(x -> new AbilityCell<>());
            ability1.setButtonCell(new AbilityCell<>());

            ability2.setData(App.abilityList);
            ability2.setCellFactory(x -> new AbilityCell<>());
            ability2.setButtonCell(new AbilityCell<>());

            ability3.setData(App.abilityList);
            ability3.setCellFactory(x -> new AbilityCell<>());
            ability3.setButtonCell(new AbilityCell<>());

            jobGroup1.setData(App.jobGroupList);
            jobGroup1.setCellFactory(x -> new JobGroupCell());
            jobGroup1.setButtonCell(new JobGroupCell());

            jobGroup2.setData(App.jobGroupList);
            jobGroup2.setCellFactory(x -> new JobGroupCell());
            jobGroup2.setButtonCell(new JobGroupCell());

            jobGroup3.setData(App.jobGroupList);
            jobGroup3.setCellFactory(x -> new JobGroupCell());
            jobGroup3.setButtonCell(new JobGroupCell());
        }
    }

    public void saveEquipment() {
        if (App.archive != null) {
        List<EquipmentData> equipment = equipmentList.getItems();
        ByteBuffer newEquipmentDatabytes = ByteBuffer.allocate(equipment.size()*0x28).order(ByteOrder.LITTLE_ENDIAN);

        logger.info("Saving Equipment");
        for (int i = 0; i < equipment.size(); i++) {
            try {
                newEquipmentDatabytes.put(equipment.get(i).toBytes());
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Equipment %d \"%s\"", i, equipment.get(i).name.getValue()));
                throw e;
            }
        }
        newEquipmentDatabytes.rewind();
        App.sysdata.setFile(16, newEquipmentDatabytes);
        }
    }

}
