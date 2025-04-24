package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ruru.ffta2editor.AbilityController.AbilityCell;
import org.ruru.ffta2editor.CharacterController.CharacterCell;
import org.ruru.ffta2editor.EquipmentController.ItemCell;
import org.ruru.ffta2editor.JobController.AbilitySetCell;
import org.ruru.ffta2editor.JobController.JobCell;
import org.ruru.ffta2editor.TextController.StringPropertyCell;
import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.ability.SPAbilityData;
import org.ruru.ffta2editor.model.character.CharacterData;
import org.ruru.ffta2editor.model.formation.FormationData;
import org.ruru.ffta2editor.model.formation.FormationUnit;
import org.ruru.ffta2editor.model.item.EquipmentData;
import org.ruru.ffta2editor.model.job.AbilitySet;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.ByteStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.ruru.ffta2editor.utility.AutoCompleteComboBox;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class FormationController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");
    
    public static class FormationCell extends ListCell<FormationData> {
        Label label = new Label();

        public FormationCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(FormationData formation, boolean empty) {
            super.updateItem(formation, empty);
            if (formation != null) {
                label.setText(String.format("%d", formation.id));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }
    
    public static class FormationUnitCell extends ListCell<FormationUnit> {
        Label label = new Label();

        public FormationUnitCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(FormationUnit unit, boolean empty) {
            super.updateItem(unit, empty);
            if (unit != null) {
                String unitName;
                String jobName;
                if (unit.character.getValue().id == 0) {
                    unitName = unit.nameString.getValue();
                    jobName = unit.job.getValue().name.getValue();
                } else {
                    if (unit.job.getValue().id == 0) {
                        jobName = unit.character.getValue().defaultJob.getValue().name.getValue();
                    } else {
                        jobName = unit.job.getValue().name.getValue();
                    }
                    if (unit.name.getValue() == 0) {
                        unitName = unit.character.getValue().nameString.getValue();
                    } else {
                        unitName = unit.nameString.getValue();
                    }
                }
                label.setText(String.format("%s : %s", unitName, jobName));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    @FXML ListView<FormationData> formationList;
    @FXML ListView<FormationUnit> formationUnitList;

    @FXML ComboBox<StringProperty> law;
    @FXML TextField partyLimit;
    @FXML TextField _0x02;
    @FXML TextField _0x04;
    @FXML TextField _0x05;
    @FXML TextField _0x06;
    @FXML TextField _0x07;
    @FXML TextField _0x08;
    @FXML TextField _0x09;
    @FXML TextField _0x0a;
    @FXML TextField _0x0b;
    @FXML TextField _0x0c;
    @FXML TextField _0x0d;
    @FXML TextField _0x0e;
    @FXML TextField _0x0f;
    @FXML TextField _0x10;
    @FXML TextField _0x11;
    @FXML TextField _0x12;
    @FXML TextField _0x13;
    @FXML TextField _0x14;
    @FXML TextField _0x15;
    @FXML TextField _0x16;
    @FXML TextField _0x17;
    @FXML TextField _0x18;
    @FXML TextField _0x19;
    @FXML TextField _0x1a;
    @FXML TextField _0x1b;
    @FXML TextField _0x1c;
    @FXML TextField _0x1d;
    @FXML TextField _0x1e;
    @FXML TextField _0x1f;
    @FXML TextField _0x20;
    @FXML TextField _0x21;
    @FXML TextField _0x22;
    @FXML TextField _0x23;
    @FXML TextField _0x24;
    @FXML TextField _0x25;
    @FXML TextField _0x26;
    @FXML TextField _0x27;
    @FXML TextField _0x28;
    @FXML TextField _0x29;
    @FXML TextField _0x2a;
    @FXML TextField _0x2b;
    @FXML TextField _0x2c;
    @FXML TextField _0x2d;
    @FXML TextField _0x2e;
    @FXML TextField _0x2f;
    @FXML TextField _0x30;
    @FXML TextField _0x31;
    @FXML TextField _0x32;
    @FXML TextField _0x33;
    @FXML TextField _0x34;
    @FXML TextField _0x35;
    @FXML TextField _0x36;
    @FXML TextField _0x37;
    @FXML TextField _0x38;
    @FXML TextField _0x39;
    @FXML TextField _0x3a;
    @FXML TextField _0x3b;
    @FXML TextField _0x3c;
    @FXML TextField _0x3d;
    @FXML TextField _0x3e;
    @FXML TextField _0x3f;
    @FXML TextField _0x40;
    @FXML TextField _0x41;
    @FXML TextField _0x42;
    @FXML TextField _0x43;

    // Unit
    @FXML AutoCompleteComboBox<CharacterData> character;
    @FXML AutoCompleteComboBox<JobData> job;
    @FXML ComboBox<StringProperty> name;
    @FXML TextField minLevel;
    @FXML TextField maxLevel;
    @FXML TextField unit_0x06;
    @FXML TextField unit_0x07;
    @FXML TextField unit_0x08;
    @FXML TextField unit_0x09;
    @FXML TextField xPosition;
    @FXML TextField yPosition;
    @FXML TextField unit_0x0c;
    @FXML TextField unit_0x0d;


    @FXML AutoCompleteComboBox<AbilityData> primaryAbility1;
    @FXML AutoCompleteComboBox<AbilityData> primaryAbility2;
    @FXML AutoCompleteComboBox<AbilityData> primaryAbility3;
    @FXML AutoCompleteComboBox<AbilityData> primaryAbility4;
    @FXML AutoCompleteComboBox<AbilityData> primaryAbility5;
    @FXML AutoCompleteComboBox<AbilityData> primaryAbility6;

    @FXML AutoCompleteComboBox<AbilitySet> secondaryAbilitySet;

    @FXML AutoCompleteComboBox<AbilityData> secondaryAbility1;
    @FXML AutoCompleteComboBox<AbilityData> secondaryAbility2;
    @FXML AutoCompleteComboBox<AbilityData> secondaryAbility3;
    @FXML AutoCompleteComboBox<AbilityData> secondaryAbility4;

    @FXML AutoCompleteComboBox<SPAbilityData> passiveAbility;
    @FXML AutoCompleteComboBox<SPAbilityData> reactionAbility;

    @FXML AutoCompleteComboBox<EquipmentData> equipment1;
    @FXML AutoCompleteComboBox<EquipmentData> equipment2;
    @FXML AutoCompleteComboBox<EquipmentData> equipment3;
    @FXML AutoCompleteComboBox<EquipmentData> equipment4;
    @FXML AutoCompleteComboBox<EquipmentData> equipment5;

    @FXML TextField unit_0x32;
    @FXML TextField unit_0x33;
    @FXML TextField lootLevel1;
    @FXML TextField lootLevel2;
    @FXML TextField lootLevel3;
    @FXML TextField lootLevel4;
    @FXML TextField lootConsumable;
    @FXML TextField lootGil;
    @FXML TextField faction;
    @FXML TextField unit_0x3b;


    
    private ObjectProperty<FormationData> formationProperty = new SimpleObjectProperty<>();
    private ObjectProperty<FormationUnit> unitProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        formationList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindFormationData();
            formationProperty.setValue(newValue);
            if (newValue != null) bindFormationData();
            formationUnitList.refresh();
        });
        formationUnitList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindFormationUnitData();
            unitProperty.setValue(newValue);
            if (newValue != null) bindFormationUnitData();
        });
        job.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<AbilityData> abilityList = FXCollections.observableArrayList(App.abilityList.getFirst());
                if (newValue.id != 0) {
                    abilityList.addAll(newValue.abilitySet.getValue().abilities.getValue().stream().map(x -> x.ability.getValue()).toList());
                } else if (unitProperty.getValue().character.getValue().id != 0){
                    abilityList.addAll(unitProperty.getValue().character.getValue().defaultJob.getValue().abilitySet.getValue().abilities.getValue().stream().map(x -> x.ability.getValue()).toList());
                }

                primaryAbility1.setData(abilityList);
                primaryAbility2.setData(abilityList);
                primaryAbility3.setData(abilityList);
                primaryAbility4.setData(abilityList);
                primaryAbility5.setData(abilityList);
                primaryAbility6.setData(abilityList);
            }
        });
        secondaryAbilitySet.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<AbilityData> abilityList = FXCollections.observableArrayList(App.abilityList.getFirst());
                abilityList.addAll(newValue.abilities.getValue().stream().map(x -> x.ability.getValue()).toList());
                secondaryAbility1.setData(abilityList);
                secondaryAbility2.setData(abilityList);
                secondaryAbility3.setData(abilityList);
                secondaryAbility4.setData(abilityList);
            }
        });


        
        // Data validators
        partyLimit.textProperty().addListener(new ByteChangeListener(partyLimit));
        _0x02.textProperty().addListener(new ByteChangeListener(_0x02));
        _0x04.textProperty().addListener(new ByteChangeListener(_0x04));
        _0x05.textProperty().addListener(new ByteChangeListener(_0x05));
        _0x06.textProperty().addListener(new ByteChangeListener(_0x06));
        _0x07.textProperty().addListener(new ByteChangeListener(_0x07));
        _0x08.textProperty().addListener(new ByteChangeListener(_0x08));
        _0x09.textProperty().addListener(new ByteChangeListener(_0x09));
        _0x0a.textProperty().addListener(new ByteChangeListener(_0x0a));
        _0x0b.textProperty().addListener(new ByteChangeListener(_0x0b));
        _0x0c.textProperty().addListener(new ByteChangeListener(_0x0c));
        _0x0d.textProperty().addListener(new ByteChangeListener(_0x0d));
        _0x0e.textProperty().addListener(new ByteChangeListener(_0x0e));
        _0x0f.textProperty().addListener(new ByteChangeListener(_0x0f));
        _0x10.textProperty().addListener(new ByteChangeListener(_0x10));
        _0x11.textProperty().addListener(new ByteChangeListener(_0x11));
        _0x12.textProperty().addListener(new ByteChangeListener(_0x12));
        _0x13.textProperty().addListener(new ByteChangeListener(_0x13));
        _0x14.textProperty().addListener(new ByteChangeListener(_0x14));
        _0x15.textProperty().addListener(new ByteChangeListener(_0x15));
        _0x16.textProperty().addListener(new ByteChangeListener(_0x16));
        _0x17.textProperty().addListener(new ByteChangeListener(_0x17));
        _0x18.textProperty().addListener(new ByteChangeListener(_0x18));
        _0x19.textProperty().addListener(new ByteChangeListener(_0x19));
        _0x1a.textProperty().addListener(new ByteChangeListener(_0x1a));
        _0x1b.textProperty().addListener(new ByteChangeListener(_0x1b));
        _0x1c.textProperty().addListener(new ByteChangeListener(_0x1c));
        _0x1d.textProperty().addListener(new ByteChangeListener(_0x1d));
        _0x1e.textProperty().addListener(new ByteChangeListener(_0x1e));
        _0x1f.textProperty().addListener(new ByteChangeListener(_0x1f));
        _0x20.textProperty().addListener(new ByteChangeListener(_0x20));
        _0x21.textProperty().addListener(new ByteChangeListener(_0x21));
        _0x22.textProperty().addListener(new ByteChangeListener(_0x22));
        _0x23.textProperty().addListener(new ByteChangeListener(_0x23));
        _0x24.textProperty().addListener(new ByteChangeListener(_0x24));
        _0x25.textProperty().addListener(new ByteChangeListener(_0x25));
        _0x26.textProperty().addListener(new ByteChangeListener(_0x26));
        _0x27.textProperty().addListener(new ByteChangeListener(_0x27));
        _0x28.textProperty().addListener(new ByteChangeListener(_0x28));
        _0x29.textProperty().addListener(new ByteChangeListener(_0x29));
        _0x2a.textProperty().addListener(new ByteChangeListener(_0x2a));
        _0x2b.textProperty().addListener(new ByteChangeListener(_0x2b));
        _0x2c.textProperty().addListener(new ByteChangeListener(_0x2c));
        _0x2d.textProperty().addListener(new ByteChangeListener(_0x2d));
        _0x2e.textProperty().addListener(new ByteChangeListener(_0x2e));
        _0x2f.textProperty().addListener(new ByteChangeListener(_0x2f));
        _0x30.textProperty().addListener(new ByteChangeListener(_0x30));
        _0x31.textProperty().addListener(new ByteChangeListener(_0x31));
        _0x32.textProperty().addListener(new ByteChangeListener(_0x32));
        _0x33.textProperty().addListener(new ByteChangeListener(_0x33));
        _0x34.textProperty().addListener(new ByteChangeListener(_0x34));
        _0x35.textProperty().addListener(new ByteChangeListener(_0x35));
        _0x36.textProperty().addListener(new ByteChangeListener(_0x36));
        _0x37.textProperty().addListener(new ByteChangeListener(_0x37));
        _0x38.textProperty().addListener(new ByteChangeListener(_0x38));
        _0x39.textProperty().addListener(new ByteChangeListener(_0x39));
        _0x3a.textProperty().addListener(new ByteChangeListener(_0x3a));
        _0x3b.textProperty().addListener(new ByteChangeListener(_0x3b));
        _0x3c.textProperty().addListener(new ByteChangeListener(_0x3c));
        _0x3d.textProperty().addListener(new ByteChangeListener(_0x3d));
        _0x3e.textProperty().addListener(new ByteChangeListener(_0x3e));
        _0x3f.textProperty().addListener(new ByteChangeListener(_0x3f));
        _0x40.textProperty().addListener(new ByteChangeListener(_0x40));
        _0x41.textProperty().addListener(new ByteChangeListener(_0x41));
        _0x42.textProperty().addListener(new ByteChangeListener(_0x42));
        _0x43.textProperty().addListener(new ByteChangeListener(_0x43));

        minLevel.textProperty().addListener(new ByteChangeListener(minLevel));
        maxLevel.textProperty().addListener(new ByteChangeListener(maxLevel));
        unit_0x06.textProperty().addListener(new ByteChangeListener(unit_0x06));
        unit_0x07.textProperty().addListener(new ByteChangeListener(unit_0x07));
        unit_0x08.textProperty().addListener(new ByteChangeListener(unit_0x08));
        unit_0x09.textProperty().addListener(new ByteChangeListener(unit_0x09));
        xPosition.textProperty().addListener(new ByteChangeListener(xPosition));
        yPosition.textProperty().addListener(new ByteChangeListener(yPosition));
        unit_0x0c.textProperty().addListener(new ByteChangeListener(unit_0x0c));
        unit_0x0d.textProperty().addListener(new ByteChangeListener(unit_0x0d));
        unit_0x32.textProperty().addListener(new ByteChangeListener(unit_0x32));
        unit_0x33.textProperty().addListener(new ByteChangeListener(unit_0x33));
        lootLevel1.textProperty().addListener(new ByteChangeListener(lootLevel1));
        lootLevel2.textProperty().addListener(new ByteChangeListener(lootLevel2));
        lootLevel3.textProperty().addListener(new ByteChangeListener(lootLevel3));
        lootLevel4.textProperty().addListener(new ByteChangeListener(lootLevel4));
        lootConsumable.textProperty().addListener(new ByteChangeListener(lootConsumable));
        lootGil.textProperty().addListener(new ByteChangeListener(lootGil));
        faction.textProperty().addListener(new ByteChangeListener(faction));
        unit_0x3b.textProperty().addListener(new ByteChangeListener(unit_0x3b));
    }

    public void unbindFormationData() {
        formationProperty.getValue().header.law.unbind();

        partyLimit.textProperty().unbindBidirectional(formationProperty.getValue().header.partyLimit);
        _0x02.textProperty().unbindBidirectional(formationProperty.getValue().header._0x02);
        _0x04.textProperty().unbindBidirectional(formationProperty.getValue().header._0x04);
        _0x05.textProperty().unbindBidirectional(formationProperty.getValue().header._0x05);
        _0x06.textProperty().unbindBidirectional(formationProperty.getValue().header._0x06);
        _0x07.textProperty().unbindBidirectional(formationProperty.getValue().header._0x07);
        _0x08.textProperty().unbindBidirectional(formationProperty.getValue().header._0x08);
        _0x09.textProperty().unbindBidirectional(formationProperty.getValue().header._0x09);
        _0x0a.textProperty().unbindBidirectional(formationProperty.getValue().header._0x0a);
        _0x0b.textProperty().unbindBidirectional(formationProperty.getValue().header._0x0b);
        _0x0c.textProperty().unbindBidirectional(formationProperty.getValue().header._0x0c);
        _0x0d.textProperty().unbindBidirectional(formationProperty.getValue().header._0x0d);
        _0x0e.textProperty().unbindBidirectional(formationProperty.getValue().header._0x0e);
        _0x0f.textProperty().unbindBidirectional(formationProperty.getValue().header._0x0f);
        _0x10.textProperty().unbindBidirectional(formationProperty.getValue().header._0x10);
        _0x11.textProperty().unbindBidirectional(formationProperty.getValue().header._0x11);
        _0x12.textProperty().unbindBidirectional(formationProperty.getValue().header._0x12);
        _0x13.textProperty().unbindBidirectional(formationProperty.getValue().header._0x13);
        _0x14.textProperty().unbindBidirectional(formationProperty.getValue().header._0x14);
        _0x15.textProperty().unbindBidirectional(formationProperty.getValue().header._0x15);
        _0x16.textProperty().unbindBidirectional(formationProperty.getValue().header._0x16);
        _0x17.textProperty().unbindBidirectional(formationProperty.getValue().header._0x17);
        _0x18.textProperty().unbindBidirectional(formationProperty.getValue().header._0x18);
        _0x19.textProperty().unbindBidirectional(formationProperty.getValue().header._0x19);
        _0x1a.textProperty().unbindBidirectional(formationProperty.getValue().header._0x1a);
        _0x1b.textProperty().unbindBidirectional(formationProperty.getValue().header._0x1b);
        _0x1c.textProperty().unbindBidirectional(formationProperty.getValue().header._0x1c);
        _0x1d.textProperty().unbindBidirectional(formationProperty.getValue().header._0x1d);
        _0x1e.textProperty().unbindBidirectional(formationProperty.getValue().header._0x1e);
        _0x1f.textProperty().unbindBidirectional(formationProperty.getValue().header._0x1f);
        _0x20.textProperty().unbindBidirectional(formationProperty.getValue().header._0x20);
        _0x21.textProperty().unbindBidirectional(formationProperty.getValue().header._0x21);
        _0x22.textProperty().unbindBidirectional(formationProperty.getValue().header._0x22);
        _0x23.textProperty().unbindBidirectional(formationProperty.getValue().header._0x23);
        _0x24.textProperty().unbindBidirectional(formationProperty.getValue().header._0x24);
        _0x25.textProperty().unbindBidirectional(formationProperty.getValue().header._0x25);
        _0x26.textProperty().unbindBidirectional(formationProperty.getValue().header._0x26);
        _0x27.textProperty().unbindBidirectional(formationProperty.getValue().header._0x27);
        _0x28.textProperty().unbindBidirectional(formationProperty.getValue().header._0x28);
        _0x29.textProperty().unbindBidirectional(formationProperty.getValue().header._0x29);
        _0x2a.textProperty().unbindBidirectional(formationProperty.getValue().header._0x2a);
        _0x2b.textProperty().unbindBidirectional(formationProperty.getValue().header._0x2b);
        _0x2c.textProperty().unbindBidirectional(formationProperty.getValue().header._0x2c);
        _0x2d.textProperty().unbindBidirectional(formationProperty.getValue().header._0x2d);
        _0x2e.textProperty().unbindBidirectional(formationProperty.getValue().header._0x2e);
        _0x2f.textProperty().unbindBidirectional(formationProperty.getValue().header._0x2f);
        _0x30.textProperty().unbindBidirectional(formationProperty.getValue().header._0x30);
        _0x31.textProperty().unbindBidirectional(formationProperty.getValue().header._0x31);
        _0x32.textProperty().unbindBidirectional(formationProperty.getValue().header._0x32);
        _0x33.textProperty().unbindBidirectional(formationProperty.getValue().header._0x33);
        _0x34.textProperty().unbindBidirectional(formationProperty.getValue().header._0x34);
        _0x35.textProperty().unbindBidirectional(formationProperty.getValue().header._0x35);
        _0x36.textProperty().unbindBidirectional(formationProperty.getValue().header._0x36);
        _0x37.textProperty().unbindBidirectional(formationProperty.getValue().header._0x37);
        _0x38.textProperty().unbindBidirectional(formationProperty.getValue().header._0x38);
        _0x39.textProperty().unbindBidirectional(formationProperty.getValue().header._0x39);
        _0x3a.textProperty().unbindBidirectional(formationProperty.getValue().header._0x3a);
        _0x3b.textProperty().unbindBidirectional(formationProperty.getValue().header._0x3b);
        _0x3c.textProperty().unbindBidirectional(formationProperty.getValue().header._0x3c);
        _0x3d.textProperty().unbindBidirectional(formationProperty.getValue().header._0x3d);
        _0x3e.textProperty().unbindBidirectional(formationProperty.getValue().header._0x3e);
        _0x3f.textProperty().unbindBidirectional(formationProperty.getValue().header._0x3f);
        _0x40.textProperty().unbindBidirectional(formationProperty.getValue().header._0x40);
        _0x41.textProperty().unbindBidirectional(formationProperty.getValue().header._0x41);
        _0x42.textProperty().unbindBidirectional(formationProperty.getValue().header._0x42);
        _0x43.textProperty().unbindBidirectional(formationProperty.getValue().header._0x43);
        
        formationUnitList.setItems(null);
    }

    public void bindFormationData() {
        law.getSelectionModel().select(Short.toUnsignedInt(formationProperty.getValue().header.law.getValue()));
        formationProperty.getValue().header.law.bind(new ObjectBinding<Byte>() {
            {bind(law.valueProperty());}
            @Override
            protected Byte computeValue() {
                return (byte)App.lawNames.indexOf(law.valueProperty().getValue());
            }

        });

        StringConverter<Byte> unsignedByteConverter = new ByteStringConverter();
        Bindings.bindBidirectional(partyLimit.textProperty(), formationProperty.getValue().header.partyLimit, unsignedByteConverter);
        Bindings.bindBidirectional(_0x02.textProperty(), formationProperty.getValue().header._0x02, unsignedByteConverter);
        Bindings.bindBidirectional(_0x04.textProperty(), formationProperty.getValue().header._0x04, unsignedByteConverter);
        Bindings.bindBidirectional(_0x05.textProperty(), formationProperty.getValue().header._0x05, unsignedByteConverter);
        Bindings.bindBidirectional(_0x06.textProperty(), formationProperty.getValue().header._0x06, unsignedByteConverter);
        Bindings.bindBidirectional(_0x07.textProperty(), formationProperty.getValue().header._0x07, unsignedByteConverter);
        Bindings.bindBidirectional(_0x08.textProperty(), formationProperty.getValue().header._0x08, unsignedByteConverter);
        Bindings.bindBidirectional(_0x09.textProperty(), formationProperty.getValue().header._0x09, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0a.textProperty(), formationProperty.getValue().header._0x0a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0b.textProperty(), formationProperty.getValue().header._0x0b, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0c.textProperty(), formationProperty.getValue().header._0x0c, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0d.textProperty(), formationProperty.getValue().header._0x0d, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0e.textProperty(), formationProperty.getValue().header._0x0e, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0f.textProperty(), formationProperty.getValue().header._0x0f, unsignedByteConverter);
        Bindings.bindBidirectional(_0x10.textProperty(), formationProperty.getValue().header._0x10, unsignedByteConverter);
        Bindings.bindBidirectional(_0x11.textProperty(), formationProperty.getValue().header._0x11, unsignedByteConverter);
        Bindings.bindBidirectional(_0x12.textProperty(), formationProperty.getValue().header._0x12, unsignedByteConverter);
        Bindings.bindBidirectional(_0x13.textProperty(), formationProperty.getValue().header._0x13, unsignedByteConverter);
        Bindings.bindBidirectional(_0x14.textProperty(), formationProperty.getValue().header._0x14, unsignedByteConverter);
        Bindings.bindBidirectional(_0x15.textProperty(), formationProperty.getValue().header._0x15, unsignedByteConverter);
        Bindings.bindBidirectional(_0x16.textProperty(), formationProperty.getValue().header._0x16, unsignedByteConverter);
        Bindings.bindBidirectional(_0x17.textProperty(), formationProperty.getValue().header._0x17, unsignedByteConverter);
        Bindings.bindBidirectional(_0x18.textProperty(), formationProperty.getValue().header._0x18, unsignedByteConverter);
        Bindings.bindBidirectional(_0x19.textProperty(), formationProperty.getValue().header._0x19, unsignedByteConverter);
        Bindings.bindBidirectional(_0x1a.textProperty(), formationProperty.getValue().header._0x1a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x1b.textProperty(), formationProperty.getValue().header._0x1b, unsignedByteConverter);
        Bindings.bindBidirectional(_0x1c.textProperty(), formationProperty.getValue().header._0x1c, unsignedByteConverter);
        Bindings.bindBidirectional(_0x1d.textProperty(), formationProperty.getValue().header._0x1d, unsignedByteConverter);
        Bindings.bindBidirectional(_0x1e.textProperty(), formationProperty.getValue().header._0x1e, unsignedByteConverter);
        Bindings.bindBidirectional(_0x1f.textProperty(), formationProperty.getValue().header._0x1f, unsignedByteConverter);
        Bindings.bindBidirectional(_0x20.textProperty(), formationProperty.getValue().header._0x20, unsignedByteConverter);
        Bindings.bindBidirectional(_0x21.textProperty(), formationProperty.getValue().header._0x21, unsignedByteConverter);
        Bindings.bindBidirectional(_0x22.textProperty(), formationProperty.getValue().header._0x22, unsignedByteConverter);
        Bindings.bindBidirectional(_0x23.textProperty(), formationProperty.getValue().header._0x23, unsignedByteConverter);
        Bindings.bindBidirectional(_0x24.textProperty(), formationProperty.getValue().header._0x24, unsignedByteConverter);
        Bindings.bindBidirectional(_0x25.textProperty(), formationProperty.getValue().header._0x25, unsignedByteConverter);
        Bindings.bindBidirectional(_0x26.textProperty(), formationProperty.getValue().header._0x26, unsignedByteConverter);
        Bindings.bindBidirectional(_0x27.textProperty(), formationProperty.getValue().header._0x27, unsignedByteConverter);
        Bindings.bindBidirectional(_0x28.textProperty(), formationProperty.getValue().header._0x28, unsignedByteConverter);
        Bindings.bindBidirectional(_0x29.textProperty(), formationProperty.getValue().header._0x29, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2a.textProperty(), formationProperty.getValue().header._0x2a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2b.textProperty(), formationProperty.getValue().header._0x2b, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2c.textProperty(), formationProperty.getValue().header._0x2c, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2d.textProperty(), formationProperty.getValue().header._0x2d, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2e.textProperty(), formationProperty.getValue().header._0x2e, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2f.textProperty(), formationProperty.getValue().header._0x2f, unsignedByteConverter);
        Bindings.bindBidirectional(_0x30.textProperty(), formationProperty.getValue().header._0x30, unsignedByteConverter);
        Bindings.bindBidirectional(_0x31.textProperty(), formationProperty.getValue().header._0x31, unsignedByteConverter);
        Bindings.bindBidirectional(_0x32.textProperty(), formationProperty.getValue().header._0x32, unsignedByteConverter);
        Bindings.bindBidirectional(_0x33.textProperty(), formationProperty.getValue().header._0x33, unsignedByteConverter);
        Bindings.bindBidirectional(_0x34.textProperty(), formationProperty.getValue().header._0x34, unsignedByteConverter);
        Bindings.bindBidirectional(_0x35.textProperty(), formationProperty.getValue().header._0x35, unsignedByteConverter);
        Bindings.bindBidirectional(_0x36.textProperty(), formationProperty.getValue().header._0x36, unsignedByteConverter);
        Bindings.bindBidirectional(_0x37.textProperty(), formationProperty.getValue().header._0x37, unsignedByteConverter);
        Bindings.bindBidirectional(_0x38.textProperty(), formationProperty.getValue().header._0x38, unsignedByteConverter);
        Bindings.bindBidirectional(_0x39.textProperty(), formationProperty.getValue().header._0x39, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3a.textProperty(), formationProperty.getValue().header._0x3a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3b.textProperty(), formationProperty.getValue().header._0x3b, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3c.textProperty(), formationProperty.getValue().header._0x3c, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3d.textProperty(), formationProperty.getValue().header._0x3d, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3e.textProperty(), formationProperty.getValue().header._0x3e, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3f.textProperty(), formationProperty.getValue().header._0x3f, unsignedByteConverter);
        Bindings.bindBidirectional(_0x40.textProperty(), formationProperty.getValue().header._0x40, unsignedByteConverter);
        Bindings.bindBidirectional(_0x41.textProperty(), formationProperty.getValue().header._0x41, unsignedByteConverter);
        Bindings.bindBidirectional(_0x42.textProperty(), formationProperty.getValue().header._0x42, unsignedByteConverter);
        Bindings.bindBidirectional(_0x43.textProperty(), formationProperty.getValue().header._0x43, unsignedByteConverter);

        formationUnitList.setItems(formationProperty.getValue().units);
    }

    public void unbindFormationUnitData() {
        character.valueProperty().unbindBidirectional(unitProperty.getValue().character);
        job.valueProperty().unbindBidirectional(unitProperty.getValue().job);
        unitProperty.getValue().name.unbind();

        minLevel.textProperty().unbindBidirectional(unitProperty.getValue().minLevel);
        maxLevel.textProperty().unbindBidirectional(unitProperty.getValue().maxLevel);
        unit_0x06.textProperty().unbindBidirectional(unitProperty.getValue()._0x06);
        unit_0x07.textProperty().unbindBidirectional(unitProperty.getValue()._0x07);
        unit_0x08.textProperty().unbindBidirectional(unitProperty.getValue()._0x08);
        unit_0x09.textProperty().unbindBidirectional(unitProperty.getValue()._0x09);
        xPosition.textProperty().unbindBidirectional(unitProperty.getValue().xPosition);
        yPosition.textProperty().unbindBidirectional(unitProperty.getValue().yPosition);
        unit_0x0c.textProperty().unbindBidirectional(unitProperty.getValue()._0x0c);
        unit_0x0d.textProperty().unbindBidirectional(unitProperty.getValue()._0x0d);

        unit_0x32.textProperty().unbindBidirectional(unitProperty.getValue()._0x32);
        unit_0x33.textProperty().unbindBidirectional(unitProperty.getValue()._0x33);
        lootLevel1.textProperty().unbindBidirectional(unitProperty.getValue().lootLevel1);
        lootLevel2.textProperty().unbindBidirectional(unitProperty.getValue().lootLevel2);
        lootLevel3.textProperty().unbindBidirectional(unitProperty.getValue().lootLevel3);
        lootLevel4.textProperty().unbindBidirectional(unitProperty.getValue().lootLevel4);
        lootConsumable.textProperty().unbindBidirectional(unitProperty.getValue().lootConsumable);
        lootGil.textProperty().unbindBidirectional(unitProperty.getValue().lootGil);
        faction.textProperty().unbindBidirectional(unitProperty.getValue().faction);
        unit_0x3b.textProperty().unbindBidirectional(unitProperty.getValue()._0x3b);

        primaryAbility1.valueProperty().unbindBidirectional(unitProperty.getValue().primaryAbility1);
        primaryAbility2.valueProperty().unbindBidirectional(unitProperty.getValue().primaryAbility2);
        primaryAbility3.valueProperty().unbindBidirectional(unitProperty.getValue().primaryAbility3);
        primaryAbility4.valueProperty().unbindBidirectional(unitProperty.getValue().primaryAbility4);
        primaryAbility5.valueProperty().unbindBidirectional(unitProperty.getValue().primaryAbility5);
        primaryAbility6.valueProperty().unbindBidirectional(unitProperty.getValue().primaryAbility6);
        secondaryAbilitySet.valueProperty().unbindBidirectional(unitProperty.getValue().secondaryAbilitySet);
        secondaryAbility1.valueProperty().unbindBidirectional(unitProperty.getValue().secondaryAbility1);
        secondaryAbility2.valueProperty().unbindBidirectional(unitProperty.getValue().secondaryAbility2);
        secondaryAbility3.valueProperty().unbindBidirectional(unitProperty.getValue().secondaryAbility3);
        secondaryAbility4.valueProperty().unbindBidirectional(unitProperty.getValue().secondaryAbility4);
        passiveAbility.valueProperty().unbindBidirectional(unitProperty.getValue().passiveAbility);
        reactionAbility.valueProperty().unbindBidirectional(unitProperty.getValue().reactionAbility);
        equipment1.valueProperty().unbindBidirectional(unitProperty.getValue().equipment1);
        equipment2.valueProperty().unbindBidirectional(unitProperty.getValue().equipment2);
        equipment3.valueProperty().unbindBidirectional(unitProperty.getValue().equipment3);
        equipment4.valueProperty().unbindBidirectional(unitProperty.getValue().equipment4);
        equipment5.valueProperty().unbindBidirectional(unitProperty.getValue().equipment5);
    }

    public void bindFormationUnitData() {
        job.getSelectionModel().clearSelection();
        character.valueProperty().bindBidirectional(unitProperty.getValue().character);
        job.valueProperty().bindBidirectional(unitProperty.getValue().job);

        name.getSelectionModel().select(Short.toUnsignedInt(unitProperty.getValue().name.getValue()));
        unitProperty.getValue().name.bind(new ObjectBinding<Short>() {
            {bind(name.valueProperty());}
            @Override
            protected Short computeValue() {
                return (short)App.characterNames.indexOf(name.valueProperty().getValue());
            }

        });

        StringConverter<Byte> unsignedByteConverter = new ByteStringConverter();
        Bindings.bindBidirectional(minLevel.textProperty(), unitProperty.getValue().minLevel, unsignedByteConverter);
        Bindings.bindBidirectional(maxLevel.textProperty(), unitProperty.getValue().maxLevel, unsignedByteConverter);
        Bindings.bindBidirectional(unit_0x06.textProperty(), unitProperty.getValue()._0x06, unsignedByteConverter);
        Bindings.bindBidirectional(unit_0x07.textProperty(), unitProperty.getValue()._0x07, unsignedByteConverter);
        Bindings.bindBidirectional(unit_0x08.textProperty(), unitProperty.getValue()._0x08, unsignedByteConverter);
        Bindings.bindBidirectional(unit_0x09.textProperty(), unitProperty.getValue()._0x09, unsignedByteConverter);
        Bindings.bindBidirectional(xPosition.textProperty(), unitProperty.getValue().xPosition, unsignedByteConverter);
        Bindings.bindBidirectional(yPosition.textProperty(), unitProperty.getValue().yPosition, unsignedByteConverter);
        Bindings.bindBidirectional(unit_0x0c.textProperty(), unitProperty.getValue()._0x0c, unsignedByteConverter);
        Bindings.bindBidirectional(unit_0x0d.textProperty(), unitProperty.getValue()._0x0d, unsignedByteConverter);

        Bindings.bindBidirectional(unit_0x32.textProperty(), unitProperty.getValue()._0x32, unsignedByteConverter);
        Bindings.bindBidirectional(unit_0x33.textProperty(), unitProperty.getValue()._0x33, unsignedByteConverter);
        Bindings.bindBidirectional(lootLevel1.textProperty(), unitProperty.getValue().lootLevel1, unsignedByteConverter);
        Bindings.bindBidirectional(lootLevel2.textProperty(), unitProperty.getValue().lootLevel2, unsignedByteConverter);
        Bindings.bindBidirectional(lootLevel3.textProperty(), unitProperty.getValue().lootLevel3, unsignedByteConverter);
        Bindings.bindBidirectional(lootLevel4.textProperty(), unitProperty.getValue().lootLevel4, unsignedByteConverter);
        Bindings.bindBidirectional(lootConsumable.textProperty(), unitProperty.getValue().lootConsumable, unsignedByteConverter);
        Bindings.bindBidirectional(lootGil.textProperty(), unitProperty.getValue().lootGil, unsignedByteConverter);
        Bindings.bindBidirectional(faction.textProperty(), unitProperty.getValue().faction, unsignedByteConverter);
        Bindings.bindBidirectional(unit_0x3b.textProperty(), unitProperty.getValue()._0x3b, unsignedByteConverter);

        primaryAbility1.valueProperty().bindBidirectional(unitProperty.getValue().primaryAbility1);
        primaryAbility2.valueProperty().bindBidirectional(unitProperty.getValue().primaryAbility2);
        primaryAbility3.valueProperty().bindBidirectional(unitProperty.getValue().primaryAbility3);
        primaryAbility4.valueProperty().bindBidirectional(unitProperty.getValue().primaryAbility4);
        primaryAbility5.valueProperty().bindBidirectional(unitProperty.getValue().primaryAbility5);
        primaryAbility6.valueProperty().bindBidirectional(unitProperty.getValue().primaryAbility6);
        secondaryAbilitySet.valueProperty().bindBidirectional(unitProperty.getValue().secondaryAbilitySet);
        secondaryAbility1.valueProperty().bindBidirectional(unitProperty.getValue().secondaryAbility1);
        secondaryAbility2.valueProperty().bindBidirectional(unitProperty.getValue().secondaryAbility2);
        secondaryAbility3.valueProperty().bindBidirectional(unitProperty.getValue().secondaryAbility3);
        secondaryAbility4.valueProperty().bindBidirectional(unitProperty.getValue().secondaryAbility4);
        passiveAbility.valueProperty().bindBidirectional(unitProperty.getValue().passiveAbility);
        reactionAbility.valueProperty().bindBidirectional(unitProperty.getValue().reactionAbility);
        equipment1.valueProperty().bindBidirectional(unitProperty.getValue().equipment1);
        equipment2.valueProperty().bindBidirectional(unitProperty.getValue().equipment2);
        equipment3.valueProperty().bindBidirectional(unitProperty.getValue().equipment3);
        equipment4.valueProperty().bindBidirectional(unitProperty.getValue().equipment4);
        equipment5.valueProperty().bindBidirectional(unitProperty.getValue().equipment5);

    }

    @FXML
    public void addFormation() {
        if (formationList.getItems() != null) {
            int newIndex = formationList.getItems().size();
            formationList.getItems().add(new FormationData(newIndex));
            formationList.getSelectionModel().selectLast();;
        }
    }

    @FXML
    public void removeFormation() {
        if (formationList.getItems().size() > 0) {
            formationList.getItems().removeLast();
        }
    }

    @FXML
    public void addUnit() {
        if (formationUnitList.getItems() != null) {
            formationUnitList.getItems().add(new FormationUnit());
            formationUnitList.getSelectionModel().selectLast();
        }
    }

    @FXML
    public void removeUnit() {
        if (formationUnitList.getItems().size() > 0) {
            int selectedIndex = formationUnitList.getSelectionModel().getSelectedIndex();
            formationUnitList.getItems().remove(selectedIndex);
        }
    }

    public void loadFormations() throws Exception {
        if (App.archive != null) {
            int numFormations = App.entrydata.numFiles() / 2;

            ObservableList<FormationData> formationDataList = FXCollections.observableArrayList();

            logger.info("Loading Formations");
            for (int i = 0; i < numFormations; i++) {
                ByteBuffer headerBytes = App.entrydata.getFile(i*2);
                ByteBuffer unitBytes = App.entrydata.getFile(i*2 + 1);
                
                if (headerBytes == null || unitBytes == null) {
                    System.err.println("IdxAndPak null file error");
                    throw new Exception(String.format("Formation %d data is null", i));
                }
                headerBytes.rewind();
                unitBytes.rewind();
                try {
                    formationDataList.add(new FormationData(headerBytes, unitBytes, i));
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Formation %d", i));
                    throw e;
                }

                headerBytes.rewind();
                unitBytes.rewind();
            }
            App.formationList = formationDataList;
            formationList.setItems(formationDataList);
            formationList.setCellFactory(x -> new FormationCell());

            formationUnitList.setCellFactory(x -> new FormationUnitCell());

            character.setData(App.characterList);
            character.setCellFactory(x -> new CharacterCell());
            character.setButtonCell(new CharacterCell());
        
            job.setData(App.jobDataList);
            job.setCellFactory(x -> new JobCell());
            job.setButtonCell(new JobCell());
        
            name.setItems(App.characterNames);
            name.setButtonCell(new StringPropertyCell());
            name.setCellFactory(x -> new StringPropertyCell());
            
            equipment1.setData(App.equipmentList);
            equipment1.setButtonCell(new ItemCell<>());
            equipment1.setCellFactory(x -> new ItemCell<>());
            
            equipment2.setData(App.equipmentList);
            equipment2.setButtonCell(new ItemCell<>());
            equipment2.setCellFactory(x -> new ItemCell<>());
            
            equipment3.setData(App.equipmentList);
            equipment3.setButtonCell(new ItemCell<>());
            equipment3.setCellFactory(x -> new ItemCell<>());
            
            equipment4.setData(App.equipmentList);
            equipment4.setButtonCell(new ItemCell<>());
            equipment4.setCellFactory(x -> new ItemCell<>());
            
            equipment5.setData(App.equipmentList);
            equipment5.setButtonCell(new ItemCell<>());
            equipment5.setCellFactory(x -> new ItemCell<>());
        
            law.setItems(App.lawNames);
            law.setButtonCell(new StringPropertyCell());
            law.setCellFactory(x -> new StringPropertyCell());
        
            secondaryAbilitySet.setData(App.abilitySetList);
            secondaryAbilitySet.setCellFactory(x -> new AbilitySetCell());
            secondaryAbilitySet.setButtonCell(new AbilitySetCell());
            
            passiveAbility.setData(App.passiveAbilityList);
            passiveAbility.setButtonCell(new AbilityCell<>());
            passiveAbility.setCellFactory(x -> new AbilityCell<>());
            
            reactionAbility.setData(App.reactionAbilityList);
            reactionAbility.setButtonCell(new AbilityCell<>());
            reactionAbility.setCellFactory(x -> new AbilityCell<>());
            
            primaryAbility1.setButtonCell(new AbilityCell<>());
            primaryAbility1.setCellFactory(x -> new AbilityCell<>());

            primaryAbility2.setButtonCell(new AbilityCell<>());
            primaryAbility2.setCellFactory(x -> new AbilityCell<>());

            primaryAbility3.setButtonCell(new AbilityCell<>());
            primaryAbility3.setCellFactory(x -> new AbilityCell<>());

            primaryAbility4.setButtonCell(new AbilityCell<>());
            primaryAbility4.setCellFactory(x -> new AbilityCell<>());

            primaryAbility5.setButtonCell(new AbilityCell<>());
            primaryAbility5.setCellFactory(x -> new AbilityCell<>());

            primaryAbility6.setButtonCell(new AbilityCell<>());
            primaryAbility6.setCellFactory(x -> new AbilityCell<>());

            secondaryAbility1.setButtonCell(new AbilityCell<>());
            secondaryAbility1.setCellFactory(x -> new AbilityCell<>());

            secondaryAbility2.setButtonCell(new AbilityCell<>());
            secondaryAbility2.setCellFactory(x -> new AbilityCell<>());

            secondaryAbility3.setButtonCell(new AbilityCell<>());
            secondaryAbility3.setCellFactory(x -> new AbilityCell<>());

            secondaryAbility4.setButtonCell(new AbilityCell<>());
            secondaryAbility4.setCellFactory(x -> new AbilityCell<>());
            
        }
    }

    public void saveFormations() {
        List<FormationData> formations = formationList.getItems();
        //if (App.entrydata.numFiles() != formations.size()*2) {
        //    System.out.println(String.format("Resizing entrydata from %d to %d", App.entrydata.numFiles(), formations.size()*2));
        //    App.entrydata.setNumFiles(formations.size()*2);
        //}
        logger.info("Saving Formations");
        for (int i = 0; i < formationList.getItems().size(); i++) {
            try {
                Pair<ByteBuffer, ByteBuffer> currFormationBytes = formations.get(i).toBytes();
                App.entrydata.setFile(i*2, currFormationBytes.getKey());
                App.entrydata.setFile(i*2 + 1, currFormationBytes.getValue());
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Formation %d", i));
                throw e;
            }
        }
    }
}
