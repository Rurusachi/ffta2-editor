package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.model.Race;
import org.ruru.ffta2editor.model.ability.AbilityAnimation;
import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.ability.AbilityEffect;
import org.ruru.ffta2editor.model.ability.AbilityElement;
import org.ruru.ffta2editor.model.ability.AbilityMenuRoutine;
import org.ruru.ffta2editor.model.ability.ActiveAbilityData;
import org.ruru.ffta2editor.model.ability.RangeAOEType;
import org.ruru.ffta2editor.model.ability.SPAbilityData;
import org.ruru.ffta2editor.model.ability.SpecialRequirement;
import org.ruru.ffta2editor.model.ability.WeaponRequirement;
import org.ruru.ffta2editor.utility.AutoCompleteComboBox;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.ShortChangeListener;
import org.ruru.ffta2editor.utility.ByteStringConverter;
import org.ruru.ffta2editor.utility.UnsignedShortStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class AbilityController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");
    
    public static class AbilityCell<T extends AbilityData> extends ListCell<T> {
        Label label = new Label();


        public AbilityCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(T ability, boolean empty) {
            super.updateItem(ability, empty);
            if (ability != null) {
                label.setText(String.format("%X: %s", ability.id , ability.name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }
    
    public static class AbilityIdCell extends ListCell<Short> {
        Label label = new Label();

        public AbilityIdCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(Short id, boolean empty) {
            super.updateItem(id, empty);
            if (id != null) {
                label.setText(String.format("%X: %s", id, App.abilityNames.get(id).getValue()));
            } else {
                label.setText("None");
            }
            setGraphic(label);
        }
    }
    
    public class AbilitySlotCell extends ListCell<Byte> {
        ComboBox<Race> race;
        Label label = new Label();

        public AbilitySlotCell(ComboBox<Race> race) {
            this.race = race;
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(Byte slotIndex, boolean empty) {
            super.updateItem(slotIndex, empty);
            if (slotIndex != null) {
                int slot = Byte.toUnsignedInt(slotIndex);
                String abilityNames = raceAPSlots.get(race.getSelectionModel().getSelectedItem().value)[slot].stream().map(ability -> ability.name.getValue()).collect(Collectors.joining(", "));
                label.setText(String.format("%X: %s", slot, abilityNames));
            } else {
                label.setText("None");
            }
            setGraphic(label);
        }
    }
    
    @FXML ListView<ActiveAbilityData> activeAbilityList;

    @FXML TextField abilityName;
    @FXML TextArea abilityDescription;


    @FXML AutoCompleteComboBox<AbilityEffect.Targets> targets1;
    @FXML AutoCompleteComboBox<AbilityEffect.Targets> targets2;
    @FXML AutoCompleteComboBox<AbilityEffect.Targets> targets3;
    @FXML AutoCompleteComboBox<AbilityEffect.Targets> targets4;

    @FXML AutoCompleteComboBox<AbilityEffect.Effect> effect1;
    @FXML AutoCompleteComboBox<AbilityEffect.Effect> effect2;
    @FXML AutoCompleteComboBox<AbilityEffect.Effect> effect3;
    @FXML AutoCompleteComboBox<AbilityEffect.Effect> effect4;
    
    @FXML AutoCompleteComboBox<AbilityEffect.Accuracy> accuracy1;
    @FXML AutoCompleteComboBox<AbilityEffect.Accuracy> accuracy2;
    @FXML AutoCompleteComboBox<AbilityEffect.Accuracy> accuracy3;
    @FXML AutoCompleteComboBox<AbilityEffect.Accuracy> accuracy4;
    
    @FXML AutoCompleteComboBox<AbilityEffect.Modifier> modifier1;
    @FXML AutoCompleteComboBox<AbilityEffect.Modifier> modifier2;
    @FXML AutoCompleteComboBox<AbilityEffect.Modifier> modifier3;
    @FXML AutoCompleteComboBox<AbilityEffect.Modifier> modifier4;

    
    @FXML TextField unknownByte0;
    @FXML TextField unknownByte4;
    @FXML TextField unknownByte5;
    @FXML TextField unknownByte30;
    @FXML TextField unknownByte31;

    
    @FXML AutoCompleteComboBox<Race> race1;
    @FXML AutoCompleteComboBox<Race> race2;
    @FXML AutoCompleteComboBox<Race> race3;
    @FXML AutoCompleteComboBox<Race> race4;
    @FXML AutoCompleteComboBox<Race> race5;
    @FXML AutoCompleteComboBox<Race> race6;
    @FXML ComboBox<Byte> apIndex1;
    @FXML ComboBox<Byte> apIndex2;
    @FXML ComboBox<Byte> apIndex3;
    @FXML ComboBox<Byte> apIndex4;
    @FXML ComboBox<Byte> apIndex5;
    @FXML ComboBox<Byte> apIndex6;

    @FXML AutoCompleteComboBox<AbilityData> learnedAbility;


    @FXML TextField power;
    @FXML TextField mpCost;
    @FXML AutoCompleteComboBox<AbilityElement> abilityElement;

    @FXML TextField range;
    @FXML TextField radius;
    @FXML TextField heightDifference;
    @FXML AutoCompleteComboBox<RangeAOEType> rangeAOEType;
    
    @FXML AutoCompleteComboBox<WeaponRequirement> weaponRequirement;
    @FXML AutoCompleteComboBox<SpecialRequirement> specialRequirement;
    @FXML AutoCompleteComboBox<AbilityMenuRoutine> menuRoutine;

    
    @FXML CheckBox masteredByDefault;
    @FXML CheckBox targetSelf;
    @FXML CheckBox invertedOnUndead;
    @FXML CheckBox isMagickal;
    @FXML CheckBox learnableByBlueMagick;
    @FXML CheckBox counterable;
    @FXML CheckBox bypassReactions;
    @FXML CheckBox reflectable;
    
    @FXML CheckBox usableWithMagickFrenzyAndDoublecast;
    @FXML CheckBox ignoreSilence;
    @FXML CheckBox usableByAI;
    @FXML CheckBox activatesBonecrusher_Maybe;
    @FXML CheckBox magickCounterable;
    @FXML CheckBox throwsItem;
    @FXML CheckBox activatesAbsorbMP;
    @FXML CheckBox caughtByStickyFingers;
    
    @FXML CheckBox usableWithMix;
    @FXML CheckBox mirrorItem;
    @FXML CheckBox antiCounter;
    @FXML CheckBox canTargetEmptySquare;
    @FXML CheckBox spawnsObject;
    @FXML CheckBox attackWithSecondWeapon;
    @FXML CheckBox isItem;
    @FXML CheckBox bit_23;
    
    @FXML CheckBox AICheckDamage_Maybe;
    @FXML CheckBox AICheckHealingBuff_Maybe;
    @FXML CheckBox AICheckDebuff_Maybe;
    @FXML CheckBox elpe_UnburdenSoul;
    @FXML CheckBox back_Side_DamageModifier;
    @FXML CheckBox counterable_Maybe;
    @FXML CheckBox bit_30;
    @FXML CheckBox bit_31;

    // Animation dataanimation
    @FXML TextField animation;
    @FXML TextField subtype;
    @FXML TextField start;
    @FXML TextField unitAnimation;
    @FXML TextField second;
    @FXML TextField untilThird;
    @FXML TextField third;
    @FXML TextField untilFourth;
    @FXML TextField fourth;
    @FXML TextField untilEnd;
    @FXML TextField _0x0C;
    @FXML TextField flinch_Glee;
    @FXML TextField _0x10;
    @FXML TextField _0x11;
    @FXML TextField _0x12;
    @FXML TextField _0x13;
    @FXML TextField targetState;
    @FXML TextField _0x16;
    @FXML TextField _0x17;
    

    ByteBuffer currentFile;
    ArrayList<AbilityAnimation> abilityAnimationList;
    private ObjectProperty<ActiveAbilityData> abilityProperty = new SimpleObjectProperty<>();
    private ObjectProperty<AbilityAnimation> abilityAnimationProperty = new SimpleObjectProperty<>();

    private ArrayList<LinkedList<AbilityData>[]> raceAPSlots;

    private boolean isRebinding = false;

    private class ApIndexChangeListener implements ChangeListener<Byte> {
        ComboBox<Race> raceBox;

        public ApIndexChangeListener(ComboBox<Race> race) {
            this.raceBox = race;
        }

        @Override
        public void changed(ObservableValue<? extends Byte> observable, Byte oldValue, Byte newValue) {
            if (isRebinding) return;
            Race race = raceBox.getSelectionModel().getSelectedItem();
            if (oldValue != null && race != Race.NONE) raceAPSlots.get(race.value)[Byte.toUnsignedInt(oldValue)].remove(abilityProperty.getValue());
            if (newValue != null && race != Race.NONE) raceAPSlots.get(race.value)[Byte.toUnsignedInt(newValue)].add(abilityProperty.getValue());
        }

    }

    private class ApRaceChangeListener implements ChangeListener<Race> {
        ComboBox<Byte> apIndexBox;

        public ApRaceChangeListener(ComboBox<Byte> apIndex) {
            this.apIndexBox = apIndex;
        }

        @Override
        public void changed(ObservableValue<? extends Race> observable, Race oldValue, Race newValue) {
            if (isRebinding) return;
            int apIndex = Byte.toUnsignedInt(apIndexBox.getSelectionModel().getSelectedItem());
            if (oldValue != null && oldValue != Race.NONE) raceAPSlots.get(oldValue.value)[apIndex].remove(abilityProperty.getValue());
            if (newValue != null && newValue != Race.NONE) raceAPSlots.get(newValue.value)[apIndex].add(abilityProperty.getValue());
            
            apIndex1.setButtonCell(new AbilitySlotCell(race1));
            apIndex1.setCellFactory(x -> new AbilitySlotCell(race1));
            
            apIndex2.setButtonCell(new AbilitySlotCell(race2));
            apIndex2.setCellFactory(x -> new AbilitySlotCell(race2));
            
            apIndex3.setButtonCell(new AbilitySlotCell(race3));
            apIndex3.setCellFactory(x -> new AbilitySlotCell(race3));
            
            apIndex4.setButtonCell(new AbilitySlotCell(race4));
            apIndex4.setCellFactory(x -> new AbilitySlotCell(race4));
            
            apIndex5.setButtonCell(new AbilitySlotCell(race5));
            apIndex5.setCellFactory(x -> new AbilitySlotCell(race5));
            
            apIndex6.setButtonCell(new AbilitySlotCell(race6));
            apIndex6.setCellFactory(x -> new AbilitySlotCell(race6));
        }

    }

    @FXML
    public void initialize() {
        
        activeAbilityList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            isRebinding = true;
            if (oldValue != null) unbindAbilityData();
            abilityProperty.setValue(newValue);
            abilityAnimationProperty.setValue(newValue != null ? abilityAnimationList.get(newValue.id) : null);
            if (newValue != null) bindAbilityData();
            isRebinding = false;
        });

        apIndex1.getSelectionModel().selectedItemProperty().addListener(new ApIndexChangeListener(race1));
        apIndex2.getSelectionModel().selectedItemProperty().addListener(new ApIndexChangeListener(race2));
        apIndex3.getSelectionModel().selectedItemProperty().addListener(new ApIndexChangeListener(race3));
        apIndex4.getSelectionModel().selectedItemProperty().addListener(new ApIndexChangeListener(race4));
        apIndex5.getSelectionModel().selectedItemProperty().addListener(new ApIndexChangeListener(race5));
        apIndex6.getSelectionModel().selectedItemProperty().addListener(new ApIndexChangeListener(race6));

        race1.getSelectionModel().selectedItemProperty().addListener(new ApRaceChangeListener(apIndex1));
        race2.getSelectionModel().selectedItemProperty().addListener(new ApRaceChangeListener(apIndex2));
        race3.getSelectionModel().selectedItemProperty().addListener(new ApRaceChangeListener(apIndex3));
        race4.getSelectionModel().selectedItemProperty().addListener(new ApRaceChangeListener(apIndex4));
        race5.getSelectionModel().selectedItemProperty().addListener(new ApRaceChangeListener(apIndex5));
        race6.getSelectionModel().selectedItemProperty().addListener(new ApRaceChangeListener(apIndex6));

        ObservableList<AbilityEffect.Targets> targetEnums = FXCollections.observableArrayList(AbilityEffect.Targets.values());
        targets1.setData(targetEnums);
        targets2.setData(targetEnums);
        targets3.setData(targetEnums);
        targets4.setData(targetEnums);
        
        ObservableList<AbilityEffect.Effect> effectEnums = FXCollections.observableArrayList(AbilityEffect.Effect.values());
        effect1.setData(effectEnums);
        effect2.setData(effectEnums);
        effect3.setData(effectEnums);
        effect4.setData(effectEnums);
        
        ObservableList<AbilityEffect.Accuracy> accuracyEnums = FXCollections.observableArrayList(AbilityEffect.Accuracy.values());
        accuracy1.setData(accuracyEnums);
        accuracy2.setData(accuracyEnums);
        accuracy3.setData(accuracyEnums);
        accuracy4.setData(accuracyEnums);
        
        ObservableList<AbilityEffect.Modifier> modifierEnums = FXCollections.observableArrayList(AbilityEffect.Modifier.values());
        modifier1.setData(modifierEnums);
        modifier2.setData(modifierEnums);
        modifier3.setData(modifierEnums);
        modifier4.setData(modifierEnums);

        
        ObservableList<AbilityElement> abilityElementEnums = FXCollections.observableArrayList(AbilityElement.values());
        abilityElement.setData(abilityElementEnums);
        ObservableList<RangeAOEType> rangeAOETypeEnums = FXCollections.observableArrayList(RangeAOEType.values());
        rangeAOEType.setData(rangeAOETypeEnums);
        ObservableList<WeaponRequirement> weaponRequirementEnums = FXCollections.observableArrayList(WeaponRequirement.values());
        weaponRequirement.setData(weaponRequirementEnums);
        ObservableList<SpecialRequirement> specialRequirementEnums = FXCollections.observableArrayList(SpecialRequirement.values());
        specialRequirement.setData(specialRequirementEnums);
        ObservableList<AbilityMenuRoutine> menuRoutineEnums = FXCollections.observableArrayList(AbilityMenuRoutine.values());
        menuRoutine.setData(menuRoutineEnums);
        
        // Data validators
        unknownByte0.textProperty().addListener(new ShortChangeListener(unknownByte0));
        unknownByte4.textProperty().addListener(new ByteChangeListener(unknownByte4));
        unknownByte5.textProperty().addListener(new ByteChangeListener(unknownByte5));
        unknownByte30.textProperty().addListener(new ByteChangeListener(unknownByte30));
        unknownByte31.textProperty().addListener(new ByteChangeListener(unknownByte31));

        power.textProperty().addListener(new ShortChangeListener(power));
        mpCost.textProperty().addListener(new ByteChangeListener(mpCost));
        range.textProperty().addListener(new ByteChangeListener(range));
        radius.textProperty().addListener(new ByteChangeListener(radius));
        heightDifference.textProperty().addListener(new ByteChangeListener(heightDifference));

        animation.textProperty().addListener(new ShortChangeListener(animation));
        subtype.textProperty().addListener(new ByteChangeListener(subtype));
        start.textProperty().addListener(new ByteChangeListener(start));
        unitAnimation.textProperty().addListener(new ShortChangeListener(unitAnimation));
        second.textProperty().addListener(new ByteChangeListener(second));
        untilThird.textProperty().addListener(new ByteChangeListener(untilThird));
        third.textProperty().addListener(new ByteChangeListener(third));
        untilFourth.textProperty().addListener(new ByteChangeListener(untilFourth));
        fourth.textProperty().addListener(new ByteChangeListener(fourth));
        untilEnd.textProperty().addListener(new ByteChangeListener(untilEnd));
        _0x0C.textProperty().addListener(new ShortChangeListener(_0x0C));
        flinch_Glee.textProperty().addListener(new ShortChangeListener(flinch_Glee));
        _0x10.textProperty().addListener(new ByteChangeListener(_0x10));
        _0x11.textProperty().addListener(new ByteChangeListener(_0x11));
        _0x12.textProperty().addListener(new ByteChangeListener(_0x12));
        _0x13.textProperty().addListener(new ByteChangeListener(_0x13));
        targetState.textProperty().addListener(new ShortChangeListener(targetState));
        _0x16.textProperty().addListener(new ByteChangeListener(_0x16));
        _0x17.textProperty().addListener(new ByteChangeListener(_0x17));




        ObservableList<Race> raceEnums = FXCollections.observableArrayList(Race.values());
        race1.setData(raceEnums);
        race2.setData(raceEnums);
        race3.setData(raceEnums);
        race4.setData(raceEnums);
        race5.setData(raceEnums);
        race6.setData(raceEnums);

        
        //ObservableList<String> abilityNameList = FXCollections.observableArrayList(AbilityData.abilityNames);
        //ObservableList<String> abilityNameIds = FXCollections.observableArrayList(IntStream.range(0, AbilityId.abilityNames.length).mapToObj(i -> AbilityId.abilityNames[i]).toList());
    }

    private void unbindAbilityData() {
        abilityName.textProperty().unbindBidirectional(abilityProperty.getValue().name);
        abilityDescription.textProperty().unbindBidirectional(abilityProperty.getValue().description);

        targets1.valueProperty().unbindBidirectional(abilityProperty.getValue().effect1.targetsProperty);
        targets2.valueProperty().unbindBidirectional(abilityProperty.getValue().effect2.targetsProperty);
        targets3.valueProperty().unbindBidirectional(abilityProperty.getValue().effect3.targetsProperty);
        targets4.valueProperty().unbindBidirectional(abilityProperty.getValue().effect4.targetsProperty);
        
        effect1.valueProperty().unbindBidirectional(abilityProperty.getValue().effect1.effectProperty);
        effect2.valueProperty().unbindBidirectional(abilityProperty.getValue().effect2.effectProperty);
        effect3.valueProperty().unbindBidirectional(abilityProperty.getValue().effect3.effectProperty);
        effect4.valueProperty().unbindBidirectional(abilityProperty.getValue().effect4.effectProperty);
        
        accuracy1.valueProperty().unbindBidirectional(abilityProperty.getValue().effect1.accuracyProperty);
        accuracy2.valueProperty().unbindBidirectional(abilityProperty.getValue().effect2.accuracyProperty);
        accuracy3.valueProperty().unbindBidirectional(abilityProperty.getValue().effect3.accuracyProperty);
        accuracy4.valueProperty().unbindBidirectional(abilityProperty.getValue().effect4.accuracyProperty);
        
        modifier1.valueProperty().unbindBidirectional(abilityProperty.getValue().effect1.modifierProperty);
        modifier2.valueProperty().unbindBidirectional(abilityProperty.getValue().effect2.modifierProperty);
        modifier3.valueProperty().unbindBidirectional(abilityProperty.getValue().effect3.modifierProperty);
        modifier4.valueProperty().unbindBidirectional(abilityProperty.getValue().effect4.modifierProperty);

        unknownByte0.textProperty().unbindBidirectional(abilityProperty.getValue()._0x0);
        unknownByte4.textProperty().unbindBidirectional(abilityProperty.getValue()._0x4);
        unknownByte5.textProperty().unbindBidirectional(abilityProperty.getValue()._0x5);
        unknownByte30.textProperty().unbindBidirectional(abilityProperty.getValue()._0x30);
        unknownByte31.textProperty().unbindBidirectional(abilityProperty.getValue()._0x31);

        race1.valueProperty().unbindBidirectional(abilityProperty.getValue().race1);
        race2.valueProperty().unbindBidirectional(abilityProperty.getValue().race2);
        race3.valueProperty().unbindBidirectional(abilityProperty.getValue().race3);
        race4.valueProperty().unbindBidirectional(abilityProperty.getValue().race4);
        race5.valueProperty().unbindBidirectional(abilityProperty.getValue().race5);
        race6.valueProperty().unbindBidirectional(abilityProperty.getValue().race6);

        apIndex1.valueProperty().unbindBidirectional(abilityProperty.getValue().apIndex1);
        apIndex2.valueProperty().unbindBidirectional(abilityProperty.getValue().apIndex2);
        apIndex3.valueProperty().unbindBidirectional(abilityProperty.getValue().apIndex3);
        apIndex4.valueProperty().unbindBidirectional(abilityProperty.getValue().apIndex4);
        apIndex5.valueProperty().unbindBidirectional(abilityProperty.getValue().apIndex5);
        apIndex6.valueProperty().unbindBidirectional(abilityProperty.getValue().apIndex6);
        
        learnedAbility.valueProperty().unbindBidirectional(abilityProperty.getValue().learnedAbility);
        //learnedAbility.valueProperty().unbind();
        //abilityProperty.getValue().learnedAbility.unbind();
        
        power.textProperty().unbindBidirectional(abilityProperty.getValue().power);
        mpCost.textProperty().unbindBidirectional(abilityProperty.getValue().mpCost);
        range.textProperty().unbindBidirectional(abilityProperty.getValue().range);
        radius.textProperty().unbindBidirectional(abilityProperty.getValue().radius);
        heightDifference.textProperty().unbindBidirectional(abilityProperty.getValue().heightDifference);

        
        abilityElement.valueProperty().unbindBidirectional(abilityProperty.getValue().abilityElement);
        rangeAOEType.valueProperty().unbindBidirectional(abilityProperty.getValue().rangeAOEType);
        weaponRequirement.valueProperty().unbindBidirectional(abilityProperty.getValue().weaponRequirement);
        specialRequirement.valueProperty().unbindBidirectional(abilityProperty.getValue().specialRequirement);
        menuRoutine.valueProperty().unbindBidirectional(abilityProperty.getValue().menuRoutine);

        masteredByDefault.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.masteredByDefault);
        targetSelf.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.targetSelf);
        invertedOnUndead.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.invertedOnUndead);
        isMagickal.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.isMagickal);
        counterable.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.counterable);
        bypassReactions.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.bypassReactions);
        reflectable.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.reflectable);
        usableWithMagickFrenzyAndDoublecast.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.usableWithMagickFrenzyAndDoublecast);
        ignoreSilence.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.ignoreSilence);
        usableByAI.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.usableByAI);
        activatesBonecrusher_Maybe.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.activatesBonecrusher_Maybe);
        magickCounterable.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.magickCounterable);
        throwsItem.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.throwsItem);
        activatesAbsorbMP.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.activatesAbsorbMP);
        caughtByStickyFingers.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.caughtByStickyFingers);
        usableWithMix.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.usableWithMix);
        mirrorItem.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.mirrorItem);
        antiCounter.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.antiCounter);
        canTargetEmptySquare.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.canTargetEmptySquare);
        spawnsObject.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.spawnsObject);
        attackWithSecondWeapon.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.attackWithSecondWeapon);
        isItem.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.isItem);
        bit_23.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.bit_23);
        AICheckDamage_Maybe.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.AICheckDamage_Maybe);
        AICheckHealingBuff_Maybe.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.AICheckHealingBuff_Maybe);
        AICheckDebuff_Maybe.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.AICheckDebuff_Maybe);
        elpe_UnburdenSoul.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.elpe_UnburdenSoul);
        back_Side_DamageModifier.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.back_Side_DamageModifier);
        counterable_Maybe.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.counterable_Maybe);
        bit_30.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.bit_30);
        bit_31.selectedProperty().unbindBidirectional(abilityProperty.getValue().abilityFlags.bit_31);

        // Animation data
        animation.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().animation);
        subtype.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().subtype);
        start.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().start);
        unitAnimation.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().unitAnimation);
        second.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().second);
        untilThird.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().untilThird);
        third.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().third);
        untilFourth.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().untilFourth);
        fourth.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().fourth);
        untilEnd.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().untilEnd);
        _0x0C.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x0C);
        flinch_Glee.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().flinch_Glee);
        _0x10.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x10);
        _0x11.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x11);
        _0x12.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x12);
        _0x13.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x13);
        targetState.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().targetState);
        _0x16.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x16);
        _0x17.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x17);
    }


    private void bindAbilityData() {
        abilityName.textProperty().bindBidirectional(abilityProperty.getValue().name);
        abilityDescription.textProperty().bindBidirectional(abilityProperty.getValue().description);

        targets1.valueProperty().bindBidirectional(abilityProperty.getValue().effect1.targetsProperty);
        targets2.valueProperty().bindBidirectional(abilityProperty.getValue().effect2.targetsProperty);
        targets3.valueProperty().bindBidirectional(abilityProperty.getValue().effect3.targetsProperty);
        targets4.valueProperty().bindBidirectional(abilityProperty.getValue().effect4.targetsProperty);
        
        effect1.valueProperty().bindBidirectional(abilityProperty.getValue().effect1.effectProperty);
        effect2.valueProperty().bindBidirectional(abilityProperty.getValue().effect2.effectProperty);
        effect3.valueProperty().bindBidirectional(abilityProperty.getValue().effect3.effectProperty);
        effect4.valueProperty().bindBidirectional(abilityProperty.getValue().effect4.effectProperty);
        
        accuracy1.valueProperty().bindBidirectional(abilityProperty.getValue().effect1.accuracyProperty);
        accuracy2.valueProperty().bindBidirectional(abilityProperty.getValue().effect2.accuracyProperty);
        accuracy3.valueProperty().bindBidirectional(abilityProperty.getValue().effect3.accuracyProperty);
        accuracy4.valueProperty().bindBidirectional(abilityProperty.getValue().effect4.accuracyProperty);
        
        modifier1.valueProperty().bindBidirectional(abilityProperty.getValue().effect1.modifierProperty);
        modifier2.valueProperty().bindBidirectional(abilityProperty.getValue().effect2.modifierProperty);
        modifier3.valueProperty().bindBidirectional(abilityProperty.getValue().effect3.modifierProperty);
        modifier4.valueProperty().bindBidirectional(abilityProperty.getValue().effect4.modifierProperty);


        StringConverter<Byte> unsignedByteConverter = new ByteStringConverter();
        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(unknownByte0.textProperty(), abilityProperty.getValue()._0x0, unsignedShortConverter);
        Bindings.bindBidirectional(unknownByte4.textProperty(), abilityProperty.getValue()._0x4, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte5.textProperty(), abilityProperty.getValue()._0x5, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte30.textProperty(), abilityProperty.getValue()._0x30, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte31.textProperty(), abilityProperty.getValue()._0x31, unsignedByteConverter);
        
        
        race1.valueProperty().bindBidirectional(abilityProperty.getValue().race1);
        race2.valueProperty().bindBidirectional(abilityProperty.getValue().race2);
        race3.valueProperty().bindBidirectional(abilityProperty.getValue().race3);
        race4.valueProperty().bindBidirectional(abilityProperty.getValue().race4);
        race5.valueProperty().bindBidirectional(abilityProperty.getValue().race5);
        race6.valueProperty().bindBidirectional(abilityProperty.getValue().race6);
        
        //Bindings.bindBidirectional(apIndex1.textProperty(), abilityProperty.getValue().apIndex1, unsignedByteConverter);
        //Bindings.bindBidirectional(apIndex2.textProperty(), abilityProperty.getValue().apIndex2, unsignedByteConverter);
        //Bindings.bindBidirectional(apIndex3.textProperty(), abilityProperty.getValue().apIndex3, unsignedByteConverter);
        //Bindings.bindBidirectional(apIndex4.textProperty(), abilityProperty.getValue().apIndex4, unsignedByteConverter);
        //Bindings.bindBidirectional(apIndex5.textProperty(), abilityProperty.getValue().apIndex5, unsignedByteConverter);
        //Bindings.bindBidirectional(apIndex6.textProperty(), abilityProperty.getValue().apIndex6, unsignedByteConverter);

        apIndex1.valueProperty().bindBidirectional(abilityProperty.getValue().apIndex1);
        apIndex2.valueProperty().bindBidirectional(abilityProperty.getValue().apIndex2);
        apIndex3.valueProperty().bindBidirectional(abilityProperty.getValue().apIndex3);
        apIndex4.valueProperty().bindBidirectional(abilityProperty.getValue().apIndex4);
        apIndex5.valueProperty().bindBidirectional(abilityProperty.getValue().apIndex5);
        apIndex6.valueProperty().bindBidirectional(abilityProperty.getValue().apIndex6);
        
        learnedAbility.valueProperty().bindBidirectional(abilityProperty.getValue().learnedAbility);
        //learnedAbility.valueProperty().bindBidirectional();
        // learnedAbility.valueProperty().bind(new StringBinding() {
        //     { bind(abilityProperty.getValue().learnedAbility); }
        //     @Override
        //     protected String computeValue() {
        //         return AbilityId.abilityNames[abilityProperty.getValue().learnedAbility.getValue()];
        //     }
            
        // });

        
        // abilityProperty.getValue().learnedAbility.bind(new ObjectBinding<Short>() {
        //     { bind(learnedAbility.selectionModelProperty().get().selectedIndexProperty()); }
        //     @Override
        //     protected Short computeValue() {
        //         return (short)learnedAbility.selectionModelProperty().get().getSelectedIndex();
        //     }
            
        // });

        Bindings.bindBidirectional(power.textProperty(), abilityProperty.getValue().power, unsignedShortConverter);
        Bindings.bindBidirectional(mpCost.textProperty(), abilityProperty.getValue().mpCost, unsignedByteConverter);
        Bindings.bindBidirectional(range.textProperty(), abilityProperty.getValue().range, unsignedByteConverter);
        Bindings.bindBidirectional(radius.textProperty(), abilityProperty.getValue().radius, unsignedByteConverter);
        Bindings.bindBidirectional(heightDifference.textProperty(), abilityProperty.getValue().heightDifference, unsignedByteConverter);

        
        abilityElement.valueProperty().bindBidirectional(abilityProperty.getValue().abilityElement);
        rangeAOEType.valueProperty().bindBidirectional(abilityProperty.getValue().rangeAOEType);
        weaponRequirement.valueProperty().bindBidirectional(abilityProperty.getValue().weaponRequirement);
        specialRequirement.valueProperty().bindBidirectional(abilityProperty.getValue().specialRequirement);
        menuRoutine.valueProperty().bindBidirectional(abilityProperty.getValue().menuRoutine);

        learnableByBlueMagick.selectedProperty().bind(abilityProperty.getValue().abilityFlags.learnableByBlueMagick);

        masteredByDefault.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.masteredByDefault);
        targetSelf.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.targetSelf);
        invertedOnUndead.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.invertedOnUndead);
        isMagickal.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.isMagickal);
        counterable.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.counterable);
        bypassReactions.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.bypassReactions);
        reflectable.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.reflectable);
        usableWithMagickFrenzyAndDoublecast.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.usableWithMagickFrenzyAndDoublecast);
        ignoreSilence.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.ignoreSilence);
        usableByAI.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.usableByAI);
        activatesBonecrusher_Maybe.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.activatesBonecrusher_Maybe);
        magickCounterable.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.magickCounterable);
        throwsItem.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.throwsItem);
        activatesAbsorbMP.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.activatesAbsorbMP);
        caughtByStickyFingers.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.caughtByStickyFingers);
        usableWithMix.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.usableWithMix);
        mirrorItem.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.mirrorItem);
        antiCounter.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.antiCounter);
        canTargetEmptySquare.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.canTargetEmptySquare);
        spawnsObject.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.spawnsObject);
        attackWithSecondWeapon.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.attackWithSecondWeapon);
        isItem.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.isItem);
        bit_23.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.bit_23);
        AICheckDamage_Maybe.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.AICheckDamage_Maybe);
        AICheckHealingBuff_Maybe.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.AICheckHealingBuff_Maybe);
        AICheckDebuff_Maybe.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.AICheckDebuff_Maybe);
        elpe_UnburdenSoul.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.elpe_UnburdenSoul);
        back_Side_DamageModifier.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.back_Side_DamageModifier);
        counterable_Maybe.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.counterable_Maybe);
        bit_30.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.bit_30);
        bit_31.selectedProperty().bindBidirectional(abilityProperty.getValue().abilityFlags.bit_31);

        // Animation data
        Bindings.bindBidirectional(animation.textProperty(),abilityAnimationProperty.getValue().animation, unsignedShortConverter);
        Bindings.bindBidirectional(subtype.textProperty(),abilityAnimationProperty.getValue().subtype, unsignedByteConverter);
        Bindings.bindBidirectional(start.textProperty(),abilityAnimationProperty.getValue().start, unsignedByteConverter);
        Bindings.bindBidirectional(unitAnimation.textProperty(),abilityAnimationProperty.getValue().unitAnimation, unsignedShortConverter);
        Bindings.bindBidirectional(second.textProperty(),abilityAnimationProperty.getValue().second, unsignedByteConverter);
        Bindings.bindBidirectional(untilThird.textProperty(),abilityAnimationProperty.getValue().untilThird, unsignedByteConverter);
        Bindings.bindBidirectional(third.textProperty(),abilityAnimationProperty.getValue().third, unsignedByteConverter);
        Bindings.bindBidirectional(untilFourth.textProperty(),abilityAnimationProperty.getValue().untilFourth, unsignedByteConverter);
        Bindings.bindBidirectional(fourth.textProperty(),abilityAnimationProperty.getValue().fourth, unsignedByteConverter);
        Bindings.bindBidirectional(untilEnd.textProperty(),abilityAnimationProperty.getValue().untilEnd, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0C.textProperty(),abilityAnimationProperty.getValue()._0x0C, unsignedShortConverter);
        Bindings.bindBidirectional(flinch_Glee.textProperty(),abilityAnimationProperty.getValue().flinch_Glee, unsignedShortConverter);
        Bindings.bindBidirectional(_0x10.textProperty(),abilityAnimationProperty.getValue()._0x10, unsignedByteConverter);
        Bindings.bindBidirectional(_0x11.textProperty(),abilityAnimationProperty.getValue()._0x11, unsignedByteConverter);
        Bindings.bindBidirectional(_0x12.textProperty(),abilityAnimationProperty.getValue()._0x12, unsignedByteConverter);
        Bindings.bindBidirectional(_0x13.textProperty(),abilityAnimationProperty.getValue()._0x13, unsignedByteConverter);
        Bindings.bindBidirectional(targetState.textProperty(),abilityAnimationProperty.getValue().targetState, unsignedShortConverter);
        Bindings.bindBidirectional(_0x16.textProperty(),abilityAnimationProperty.getValue()._0x16, unsignedByteConverter);
        Bindings.bindBidirectional(_0x17.textProperty(),abilityAnimationProperty.getValue()._0x17, unsignedByteConverter);
    }

    @FXML
    public void addAbility() {
        //if (activeAbilityList.getItems() != null) {
        //    int newIndex = activeAbilityList.getItems().size();
        //    App.abilityNames.add(newIndex, new SimpleStringProperty("-"));
        //    App.abilityDescriptions.add(newIndex, new SimpleStringProperty("\\end\\"));
        //    ActiveAbilityData newAbility = new ActiveAbilityData("", newIndex);
        //    activeAbilityList.getItems().add(newAbility);
        //    App.abilityList.add(newIndex, newAbility);
        //    abilityAnimationList.add(new AbilityAnimation());
        //    activeAbilityList.getSelectionModel().selectLast();
        //    for (AbilityData ability : App.reactionAbilityList) {
        //        ability.id++;
        //    }
        //    for (AbilityData ability : App.passiveAbilityList) {
        //        ability.id++;
        //    }
        //}
    }

    @FXML
    public void removeAbility() {
        //if (activeAbilityList.getItems().size() > 0) {
        //    int removedIndex = activeAbilityList.getItems().size()-1;
        //    App.abilityNames.remove(removedIndex);
        //    App.abilityDescriptions.remove(removedIndex);
        //    App.abilityList.remove(removedIndex);
        //    activeAbilityList.getItems().removeLast();
        //    abilityAnimationList.removeLast();
        //    for (AbilityData ability : App.reactionAbilityList) {
        //        ability.id--;
        //    }
        //    for (AbilityData ability : App.passiveAbilityList) {
        //        ability.id--;
        //    }
        //}
    }

    private ObjectProperty<ActiveAbilityData> copyAbilityData = new SimpleObjectProperty<>();

    public void loadAbilities() throws Exception {
        if (App.archive != null) {
            // Active Abilities
            activeAbilityList.getSelectionModel().clearSelection();
            ByteBuffer activeAbilityDataBytes = App.sysdata.getFile(4);

            if (activeAbilityDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Active Abilities are null");
            }
            activeAbilityDataBytes.rewind();

            ObservableList<ActiveAbilityData> activeAbilityDataList = FXCollections.observableArrayList();

            logger.info("Loading Active Abilities");
            for (int i = 0; i < 0x336; i++) {
                //AbilityData abilityData = new AbilityData(abilityDataBytes, AbilityId.abilityNames[i], i);
                try {
                    ActiveAbilityData activeAbilityData = new ActiveAbilityData(activeAbilityDataBytes, i);
                    activeAbilityDataList.add(activeAbilityData);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Active Ability %d \"%s\"", i, App.abilityNames.size() > i ? App.abilityNames.get(i).getValue() : ""));
                    throw e;
                }
            }
            activeAbilityList.setCellFactory(x -> {
                var cell = new AbilityCell<ActiveAbilityData>();

                ContextMenu menu = new ContextMenu();

                MenuItem copyItem = new MenuItem("Copy");
                copyItem.setOnAction(event -> copyAbilityData.set(cell.getItem()));

                
                MenuItem pasteItem = new MenuItem();
                pasteItem.textProperty().bind(copyAbilityData.asString("Paste from \"%s\""));
                pasteItem.setOnAction(event -> {
                    if (copyAbilityData != null) {
                        cell.getItem().copyFrom(copyAbilityData.get());
                        abilityAnimationList.get(cell.getItem().id).copyFrom(abilityAnimationList.get(copyAbilityData.get().id));
                    }
                });
                menu.getItems().addAll(copyItem, pasteItem);

                cell.emptyProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(menu);
                    }
                });

                return cell;
            });
            activeAbilityList.setItems(activeAbilityDataList);
            App.activeAbilityList = activeAbilityDataList;
            
            activeAbilityDataBytes.rewind();

                // Support Abilities
            ByteBuffer reactionAbilityDataBytes = App.sysdata.getFile(6);

            if (reactionAbilityDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Reaction Abilities are null");
            }
            reactionAbilityDataBytes.rewind();

            ObservableList<SPAbilityData> reactionAbilityDataList = FXCollections.observableArrayList();

            reactionAbilityDataList.add(new SPAbilityData("", 0));
            logger.info("Loading Reaction Abilities");
            for (int i = 0; i < 0x1D; i++) {
                try {
                    SPAbilityData reactionAbilityData = new SPAbilityData(reactionAbilityDataBytes, i + 0x336);
                    reactionAbilityDataList.add(reactionAbilityData);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Reaction Ability %d \"%s\"", i + 0x336, App.abilityNames.size() > i ? App.abilityNames.get(i).getValue() : ""));
                    throw e;
                }
            }
            //supportAbilityList.setCellFactory(x -> new AbilityCell());
            //supportAbilityList.setData(reactionAbilityDataList);
            App.reactionAbilityList = reactionAbilityDataList;
            
            reactionAbilityDataBytes.rewind();

        
            // Support Abilities
            ByteBuffer passiveAbilityDataBytes = App.sysdata.getFile(7);

            if (passiveAbilityDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Passive Abilities are null");
            }
            passiveAbilityDataBytes.rewind();

            ObservableList<SPAbilityData> passiveAbilityDataList = FXCollections.observableArrayList();

            passiveAbilityDataList.add(new SPAbilityData("", 0));
            logger.info("Loading Passive Abilities");
            for (int i = 0; i < 0x28; i++) {
                try {
                    SPAbilityData passiveAbilityData = new SPAbilityData(passiveAbilityDataBytes, i + 0x353);
                    passiveAbilityDataList.add(passiveAbilityData);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Passive Ability %d \"%s\"", i + 0x353, App.abilityNames.size() > i ? App.abilityNames.get(i).getValue() : ""));
                    throw e;
                }
            }
            //supportAbilityList.setCellFactory(x -> new AbilityCell());
            //supportAbilityList.setData(supportAbilityDataList);
            App.passiveAbilityList = passiveAbilityDataList;
            
            passiveAbilityDataBytes.rewind();

            App.abilityList = FXCollections.observableArrayList();
            App.abilityList.addAll(App.activeAbilityList);
            App.abilityList.addAll(App.reactionAbilityList.subList(1, App.reactionAbilityList.size()));
            App.abilityList.addAll(App.passiveAbilityList.subList(1, App.passiveAbilityList.size()));

            for (ActiveAbilityData ability : activeAbilityDataList) {
                ability.learnedAbility.set(App.abilityList.get(Short.toUnsignedInt(ability.learnedAbilityId)));
            }

            // Ability Animations
            ByteBuffer abilityAnimationBytes = App.sysdata.getFile(5);
            
            if (abilityAnimationBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Ability Animations are null");
            }
            abilityAnimationBytes.rewind();
            abilityAnimationList = new ArrayList<>();
            logger.info("Loading Ability Animations");
            for (int i = 0; i < 0x336; i++) {
                try {
                    AbilityAnimation abilityAnimation = new AbilityAnimation(abilityAnimationBytes);
                    abilityAnimationList.add(abilityAnimation);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Ability Animation %d", i));
                    throw e;
                }
            }
            abilityAnimationBytes.rewind();

            
            //ObservableList<Short> abilityNameIds = FXCollections.observableArrayList(IntStream.range(0, App.abilityNames.size()).mapToObj(i -> (short)i).toList());
            //learnedAbility.setData(abilityNameIds);
            //learnedAbility.setButtonCell(new AbilityIdCell());
            //learnedAbility.setCellFactory(x -> new AbilityIdCell());
            learnedAbility.setData(App.abilityList);
            learnedAbility.setButtonCell(new AbilityCell<>());
            learnedAbility.setCellFactory(x -> new AbilityCell<>());

            raceAPSlots = new ArrayList<>();
            logger.info("Building race AP slot lists");
            for (Race r : Race.values()) {
                raceAPSlots.add(new LinkedList[200]);
                for (int i = 0; i < 200; i++) {
                    raceAPSlots.getLast()[i] = new LinkedList<>();
                }
            }
            App.passiveAbilityList.forEach(ability -> {
                for (LinkedList<AbilityData>[] slots : raceAPSlots.stream().skip(1).toList()) {
                    int slot = Byte.toUnsignedInt(ability.apIndex.getValue());
                    if (slot >= 0 && slot < 200) {
                        slots[ability.apIndex.getValue()].add(ability);
                    }
                }
            });
            App.reactionAbilityList.forEach(ability -> {
                for (LinkedList<AbilityData>[] slots : raceAPSlots.stream().skip(1).toList()) {
                    int slot = Byte.toUnsignedInt(ability.apIndex.getValue());
                    if (slot >= 0 && slot < 200) {
                        slots[ability.apIndex.getValue()].add(ability);
                    }
                }
            });
            App.activeAbilityList.forEach(ability -> {
                int slot;
                if (ability.race1.getValue().value != 0){
                    slot = Byte.toUnsignedInt(ability.apIndex1.getValue());
                    raceAPSlots.get(ability.race1.getValue().value)[slot].add(ability);
                }

                if (ability.race2.getValue().value != 0){
                slot = Byte.toUnsignedInt(ability.apIndex2.getValue());
                raceAPSlots.get(ability.race2.getValue().value)[slot].add(ability);
                }

                if (ability.race3.getValue().value != 0){
                slot = Byte.toUnsignedInt(ability.apIndex3.getValue());
                raceAPSlots.get(ability.race3.getValue().value)[slot].add(ability);
                }

                if (ability.race4.getValue().value != 0){
                slot = Byte.toUnsignedInt(ability.apIndex4.getValue());
                raceAPSlots.get(ability.race4.getValue().value)[slot].add(ability);
                }

                if (ability.race5.getValue().value != 0){
                slot = Byte.toUnsignedInt(ability.apIndex5.getValue());
                raceAPSlots.get(ability.race5.getValue().value)[slot].add(ability);
                }

                if (ability.race6.getValue().value != 0){
                slot = Byte.toUnsignedInt(ability.apIndex6.getValue());
                raceAPSlots.get(ability.race6.getValue().value)[slot].add(ability);
                }
            });
            
            ObservableList<Byte> apSlotItems = FXCollections.observableArrayList(IntStream.range(0, 200).mapToObj(i -> (byte)i).toList());
            apIndex1.setItems(apSlotItems);
            apIndex1.setButtonCell(new AbilitySlotCell(race1));
            apIndex1.setCellFactory(x -> new AbilitySlotCell(race1));
            
            apIndex2.setItems(apSlotItems);
            apIndex2.setButtonCell(new AbilitySlotCell(race2));
            apIndex2.setCellFactory(x -> new AbilitySlotCell(race2));
            
            apIndex3.setItems(apSlotItems);
            apIndex3.setButtonCell(new AbilitySlotCell(race3));
            apIndex3.setCellFactory(x -> new AbilitySlotCell(race3));
            
            apIndex4.setItems(apSlotItems);
            apIndex4.setButtonCell(new AbilitySlotCell(race4));
            apIndex4.setCellFactory(x -> new AbilitySlotCell(race4));
            
            apIndex5.setItems(apSlotItems);
            apIndex5.setButtonCell(new AbilitySlotCell(race5));
            apIndex5.setCellFactory(x -> new AbilitySlotCell(race5));
            
            apIndex6.setItems(apSlotItems);
            apIndex6.setButtonCell(new AbilitySlotCell(race6));
            apIndex6.setCellFactory(x -> new AbilitySlotCell(race6));
        }
    }

    public void saveAbilities() {
        List<ActiveAbilityData> abilities = activeAbilityList.getItems();
        ByteBuffer newAbilityDatabytes = ByteBuffer.allocate(abilities.size()*0x34).order(ByteOrder.LITTLE_ENDIAN);

        logger.info("Saving Active Abilities");
        for (int i = 0; i < abilities.size(); i++) {
            try {
                newAbilityDatabytes.put(abilities.get(i).toBytes());
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Active Ability %d \"%s\"", i, abilities.get(i).name.getValue()));
                throw e;
            }
        }
        newAbilityDatabytes.rewind();
        App.sysdata.setFile(4, newAbilityDatabytes);

        ByteBuffer newAbilityAnimationbytes = ByteBuffer.allocate(abilityAnimationList.size()*0x18).order(ByteOrder.LITTLE_ENDIAN);

        logger.info("Saving Ability Animations");
        for (int i = 0; i < abilityAnimationList.size(); i++) {
            try {
                newAbilityAnimationbytes.put(abilityAnimationList.get(i).toBytes());
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Ability Animation %d \"%s\"", i, abilities.get(i).name.getValue()));
                throw e;
            }
        }
        newAbilityAnimationbytes.rewind();
        App.sysdata.setFile(5, newAbilityAnimationbytes);


        // App.reactionAbilityList and App.passiveAbilityList have a copy of the 0th ability as a null value
        //int numActive = App.activeAbilityList.size();
        //int numReaction = App.reactionAbilityList.size() - 1;
        //int numPassive = App.passiveAbilityList.size() - 1;


        // TODO: Rewrite FUN_02080f38
        /* 
        // 0x020cb0a8, 0x020bbde4, 0x020c0208, 0x020b63bc, 0x020b68ac, 0x020b6f48, 0x020b84a0, 0x0210f8e0, 0x020b8640 = activeAbilities.size()-1 (short)
        App.arm9.putShort(0x000cb0a8, (short)(numActive - 1));
        App.arm9.putShort(0x000bbde4, (short)(numActive - 1));
        App.arm9.putShort(0x000c0208, (short)(numActive - 1));
        App.arm9.putShort(0x000b63bc, (short)(numActive - 1));
        App.arm9.putShort(0x000b68ac, (short)(numActive - 1));
        App.arm9.putShort(0x000b6f48, (short)(numActive - 1));
        App.arm9.putShort(0x000b84a0, (short)(numActive - 1));
        App.arm9.putShort(0x0010f8e0, (short)(numActive - 1));
        App.arm9.putShort(0x000b8640, (short)(numActive - 1));

        // 0x020bb108, 0x020bbddc, 0x020cb0f4, 0x020b6a34, 0x020cb558, 0x020b84a4, 0x0209f7c0, 0x0210f8e4 = activeAbilities.size() (short)
        App.arm9.putShort(0x000bb108, (short)(numActive));
        App.arm9.putShort(0x000bbddc, (short)(numActive));
        App.arm9.putShort(0x000cb0f4, (short)(numActive));
        App.arm9.putShort(0x000b6a34, (short)(numActive));
        App.arm9.putShort(0x000cb558, (short)(numActive));
        App.arm9.putShort(0x000b84a4, (short)(numActive));
        App.arm9.putShort(0x0009f7c0, (short)(numActive));
        App.arm9.putShort(0x0010f8e4, (short)(numActive));

        // 0x020b7464 = 0xe3a04fcd (mov r4, 334)
        App.arm9.putInt(0x000b7464, 0xe3a04fcd);
        
        // 0x020b746c = 0xe28400XX (add r0, r4, XX) (XX = activeAbilities.size()-0x334)
        App.arm9.putInt(0x000b746c, 0xe2840000 + (numActive-0x334));

        // 0x020bbc50 = 0xe3e00000 (mvn r0, 0)
        App.arm9.putInt(0x000bbc50, 0xe3e00000);

        // 0x0207c5d4 = activeAbilities.size() - 0x300 (byte)
        App.arm9.put(0x0007c5d4, (byte)(numActive-0x300));

        // 0x020bb10c, 0x020bbde0, 0x020cb144, 0x020b6b48, 0x020b84a8, 0x0209f7c4, 0x0210f8e8 = activeAbilities.size() + reactionAblities.size() (short)
        App.arm9.putShort(0x000bb10c, (short)(numActive + numReaction));
        App.arm9.putShort(0x000bbde0, (short)(numActive + numReaction));
        App.arm9.putShort(0x000cb144, (short)(numActive + numReaction));
        App.arm9.putShort(0x000b6b48, (short)(numActive + numReaction));
        App.arm9.putShort(0x000b84a8, (short)(numActive + numReaction));
        App.arm9.putShort(0x0009f7c4, (short)(numActive + numReaction));
        App.arm9.putShort(0x0010f8e8, (short)(numActive + numReaction));

        // 0x020f0028 = activeAbilities.size() + reactionAblities.size() - 1 (short)
        App.arm9.putShort(0x000f0028, (short)(numActive + numReaction - 1));

        // 0x020cb0f8 = -activeAblities.size() (int)
        App.arm9.putInt(0x000cb0f8, -numActive);

        // 0x020cb148 = -(activeAblities.size() + reactionAblities.size()) (int)
        App.arm9.putInt(0x000cb148, -(numActive + numReaction));

        // 0x020baebc, 0x020bbb58, 0x020cb0d8, 0x020b697c, 0x020cb538, 0x020b83c0, 0x0209f72c, 0x0210f8ac, 0x020efe60 = reactionAbilities.size()-1 (byte)
        App.arm9.put(0x000baebc, (byte)(numReaction - 1));
        App.arm9.put(0x000bbb58, (byte)(numReaction - 1));
        App.arm9.put(0x000cb0d8, (byte)(numReaction - 1));
        App.arm9.put(0x000b697c, (byte)(numReaction - 1));
        App.arm9.put(0x000cb538, (byte)(numReaction - 1));
        App.arm9.put(0x000b83c0, (byte)(numReaction - 1));
        App.arm9.put(0x0009f72c, (byte)(numReaction - 1));
        App.arm9.put(0x0010f8ac, (byte)(numReaction - 1));
        App.arm9.put(0x000efe60, (byte)(numReaction - 1));

        // 0x020baee4, 0x020bbbcc, 0x020cb128, 0x020b6a90, 0x020b8420, 0x0209f74c, 0x0210f8c8 = passiveAbilities.size()-1 (byte)
        App.arm9.put(0x000baee4, (byte)(numPassive - 1));
        App.arm9.put(0x000bbbcc, (byte)(numPassive - 1));
        App.arm9.put(0x000cb128, (byte)(numPassive - 1));
        App.arm9.put(0x000b6a90, (byte)(numPassive - 1));
        App.arm9.put(0x000b8420, (byte)(numPassive - 1));
        App.arm9.put(0x0009f74c, (byte)(numPassive - 1));
        App.arm9.put(0x0010f8c8, (byte)(numPassive - 1));

        // 0x020efe70 = passiveAbilities.size() (byte)
        App.arm9.put(0x000efe70, (byte)numPassive);

        // 0x020cb18c = abilityAnimationList.size()-1 (should be same as activeAbilities.size()-1)
        App.arm9.put(0x000cb18c, (byte)(abilityAnimationList.size() - 1));
        */




        //Pair<ByteBuffer, ByteBuffer> idxPak = App.sysdata.repack();
        //App.archive.setFile("system/rom/sysdata_rom.idx", idxPak.getKey());
        //App.archive.setFile("system/rom/sysdata.pak", idxPak.getValue());
        
    }
}
