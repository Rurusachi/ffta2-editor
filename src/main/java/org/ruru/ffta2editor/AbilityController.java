package org.ruru.ffta2editor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.model.Race;
import org.ruru.ffta2editor.model.ability.AbilityAnimation;
import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.ability.ActiveAbilityData;
import org.ruru.ffta2editor.model.ability.AbilityEffect;
import org.ruru.ffta2editor.model.ability.AbilityElement;
import org.ruru.ffta2editor.model.ability.AbilityId;
import org.ruru.ffta2editor.model.ability.AbilityMenuRoutine;
import org.ruru.ffta2editor.model.ability.RangeAOEType;
import org.ruru.ffta2editor.model.ability.SPAbilityData;
import org.ruru.ffta2editor.model.ability.SpecialRequirement;
import org.ruru.ffta2editor.model.ability.WeaponRequirement;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.IdxAndPak;
import org.ruru.ffta2editor.utility.UnsignedByteStringConverter;
import org.ruru.ffta2editor.utility.UnsignedShortStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class AbilityController {
    
    public static class AbilityCell<T extends AbilityData> extends ListCell<T> {
        Label label = new Label();


        public AbilityCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(T ability, boolean empty) {
            super.updateItem(ability, empty);
            if (ability != null) {
                label.setText(String.format("%X: %s", ability.id , ability.name.getValue()));
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
    
    @FXML ListView<ActiveAbilityData> activeAbilityList;


    @FXML ComboBox<AbilityEffect.Targets> targets1;
    @FXML ComboBox<AbilityEffect.Targets> targets2;
    @FXML ComboBox<AbilityEffect.Targets> targets3;
    @FXML ComboBox<AbilityEffect.Targets> targets4;

    @FXML ComboBox<AbilityEffect.Effect> effect1;
    @FXML ComboBox<AbilityEffect.Effect> effect2;
    @FXML ComboBox<AbilityEffect.Effect> effect3;
    @FXML ComboBox<AbilityEffect.Effect> effect4;
    
    @FXML ComboBox<AbilityEffect.Accuracy> accuracy1;
    @FXML ComboBox<AbilityEffect.Accuracy> accuracy2;
    @FXML ComboBox<AbilityEffect.Accuracy> accuracy3;
    @FXML ComboBox<AbilityEffect.Accuracy> accuracy4;
    
    @FXML ComboBox<AbilityEffect.Modifier> modifier1;
    @FXML ComboBox<AbilityEffect.Modifier> modifier2;
    @FXML ComboBox<AbilityEffect.Modifier> modifier3;
    @FXML ComboBox<AbilityEffect.Modifier> modifier4;

    
    @FXML TextField unknownByte0;
    @FXML TextField unknownByte1;
    @FXML TextField unknownByte4;
    @FXML TextField unknownByte5;
    @FXML TextField unknownByte7;
    @FXML TextField unknownByte9;
    @FXML TextField unknownByte30;
    @FXML TextField unknownByte31;

    
    @FXML ComboBox<Race> race1;
    @FXML ComboBox<Race> race2;
    @FXML ComboBox<Race> race3;
    @FXML ComboBox<Race> race4;
    @FXML ComboBox<Race> race5;
    @FXML ComboBox<Race> race6;
    @FXML TextField apIndex1;
    @FXML TextField apIndex2;
    @FXML TextField apIndex3;
    @FXML TextField apIndex4;
    @FXML TextField apIndex5;
    @FXML TextField apIndex6;

    @FXML ComboBox<Short> learnedAbility;


    @FXML TextField power;
    @FXML TextField mpCost;
    @FXML ComboBox<AbilityElement> abilityElement;

    @FXML TextField range;
    @FXML TextField radius;
    @FXML TextField heightDifference;
    @FXML ComboBox<RangeAOEType> rangeAOEType;
    
    @FXML ComboBox<WeaponRequirement> weaponRequirement;
    @FXML ComboBox<SpecialRequirement> specialRequirement;
    @FXML ComboBox<AbilityMenuRoutine> menuRoutine;

    
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
    @FXML TextField first;
    @FXML TextField untilSecond;
    @FXML TextField second;
    @FXML TextField untilThird;
    @FXML TextField third;
    @FXML TextField untilFourth;
    @FXML TextField fourth;
    @FXML TextField untilEnd;
    @FXML TextField _0x0C;
    @FXML TextField _0x0D;
    @FXML TextField flinch_Glee;
    @FXML TextField _0x0F;
    @FXML TextField _0x10;
    @FXML TextField _0x11;
    @FXML TextField _0x12;
    @FXML TextField _0x13;
    @FXML TextField _0x14;
    @FXML TextField _0x15;
    @FXML TextField _0x16;
    @FXML TextField _0x17;
    

    ByteBuffer currentFile;
    ArrayList<AbilityAnimation> abilityAnimationList;
    private ObjectProperty<ActiveAbilityData> abilityProperty = new SimpleObjectProperty<>();
    private ObjectProperty<AbilityAnimation> abilityAnimationProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        
        activeAbilityList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindAbilityData();
            abilityProperty.setValue(newValue);
            abilityAnimationProperty.setValue(abilityAnimationList.get(newValue.id));
            if (newValue != null) bindAbilityData();
        });
        //abilityProperty.addListener((observable, oldValue, newValue) -> bindAbilityData());
        ObservableList<AbilityEffect.Targets> targetEnums = FXCollections.observableArrayList(AbilityEffect.Targets.values());
        targets1.setItems(targetEnums);
        targets2.setItems(targetEnums);
        targets3.setItems(targetEnums);
        targets4.setItems(targetEnums);
        
        ObservableList<AbilityEffect.Effect> effectEnums = FXCollections.observableArrayList(AbilityEffect.Effect.values());
        effect1.setItems(effectEnums);
        effect2.setItems(effectEnums);
        effect3.setItems(effectEnums);
        effect4.setItems(effectEnums);
        
        ObservableList<AbilityEffect.Accuracy> accuracyEnums = FXCollections.observableArrayList(AbilityEffect.Accuracy.values());
        accuracy1.setItems(accuracyEnums);
        accuracy2.setItems(accuracyEnums);
        accuracy3.setItems(accuracyEnums);
        accuracy4.setItems(accuracyEnums);
        
        ObservableList<AbilityEffect.Modifier> modifierEnums = FXCollections.observableArrayList(AbilityEffect.Modifier.values());
        modifier1.setItems(modifierEnums);
        modifier2.setItems(modifierEnums);
        modifier3.setItems(modifierEnums);
        modifier4.setItems(modifierEnums);

        
        ObservableList<AbilityElement> abilityElementEnums = FXCollections.observableArrayList(AbilityElement.values());
        abilityElement.setItems(abilityElementEnums);
        ObservableList<RangeAOEType> rangeAOETypeEnums = FXCollections.observableArrayList(RangeAOEType.values());
        rangeAOEType.setItems(rangeAOETypeEnums);
        ObservableList<WeaponRequirement> weaponRequirementEnums = FXCollections.observableArrayList(WeaponRequirement.values());
        weaponRequirement.setItems(weaponRequirementEnums);
        ObservableList<SpecialRequirement> specialRequirementEnums = FXCollections.observableArrayList(SpecialRequirement.values());
        specialRequirement.setItems(specialRequirementEnums);
        ObservableList<AbilityMenuRoutine> menuRoutineEnums = FXCollections.observableArrayList(AbilityMenuRoutine.values());
        menuRoutine.setItems(menuRoutineEnums);
        
        // Data validators
        unknownByte0.textProperty().addListener(new ByteChangeListener(unknownByte0));
        unknownByte1.textProperty().addListener(new ByteChangeListener(unknownByte1));
        unknownByte4.textProperty().addListener(new ByteChangeListener(unknownByte4));
        unknownByte5.textProperty().addListener(new ByteChangeListener(unknownByte5));
        unknownByte7.textProperty().addListener(new ByteChangeListener(unknownByte7));
        unknownByte9.textProperty().addListener(new ByteChangeListener(unknownByte9));
        unknownByte30.textProperty().addListener(new ByteChangeListener(unknownByte30));
        unknownByte31.textProperty().addListener(new ByteChangeListener(unknownByte31));


        ObservableList<Race> raceEnums = FXCollections.observableArrayList(Race.values());
        race1.setItems(raceEnums);
        race2.setItems(raceEnums);
        race3.setItems(raceEnums);
        race4.setItems(raceEnums);
        race5.setItems(raceEnums);
        race6.setItems(raceEnums);
        apIndex1.textProperty().addListener(new ByteChangeListener(apIndex1));
        apIndex2.textProperty().addListener(new ByteChangeListener(apIndex2));
        apIndex3.textProperty().addListener(new ByteChangeListener(apIndex3));
        apIndex4.textProperty().addListener(new ByteChangeListener(apIndex4));
        apIndex5.textProperty().addListener(new ByteChangeListener(apIndex5));
        apIndex6.textProperty().addListener(new ByteChangeListener(apIndex6));

        
        //ObservableList<String> abilityNameList = FXCollections.observableArrayList(AbilityData.abilityNames);
        //ObservableList<String> abilityNameIds = FXCollections.observableArrayList(IntStream.range(0, AbilityId.abilityNames.length).mapToObj(i -> AbilityId.abilityNames[i]).toList());
    }

    private void unbindAbilityData() {
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
        unknownByte1.textProperty().unbindBidirectional(abilityProperty.getValue()._0x1);
        unknownByte4.textProperty().unbindBidirectional(abilityProperty.getValue()._0x4);
        unknownByte5.textProperty().unbindBidirectional(abilityProperty.getValue()._0x5);
        unknownByte7.textProperty().unbindBidirectional(abilityProperty.getValue()._0x7);
        unknownByte9.textProperty().unbindBidirectional(abilityProperty.getValue()._0x9);
        unknownByte30.textProperty().unbindBidirectional(abilityProperty.getValue()._0x30);
        unknownByte31.textProperty().unbindBidirectional(abilityProperty.getValue()._0x31);

        race1.valueProperty().unbindBidirectional(abilityProperty.getValue().race1);
        race2.valueProperty().unbindBidirectional(abilityProperty.getValue().race2);
        race3.valueProperty().unbindBidirectional(abilityProperty.getValue().race3);
        race4.valueProperty().unbindBidirectional(abilityProperty.getValue().race4);
        race5.valueProperty().unbindBidirectional(abilityProperty.getValue().race5);
        race6.valueProperty().unbindBidirectional(abilityProperty.getValue().race6);

        apIndex1.textProperty().unbindBidirectional(abilityProperty.getValue().apIndex1);
        apIndex2.textProperty().unbindBidirectional(abilityProperty.getValue().apIndex2);
        apIndex3.textProperty().unbindBidirectional(abilityProperty.getValue().apIndex3);
        apIndex4.textProperty().unbindBidirectional(abilityProperty.getValue().apIndex4);
        apIndex5.textProperty().unbindBidirectional(abilityProperty.getValue().apIndex5);
        apIndex6.textProperty().unbindBidirectional(abilityProperty.getValue().apIndex6);
        
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
        first.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().first);
        untilSecond.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().untilSecond);
        second.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().second);
        untilThird.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().untilThird);
        third.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().third);
        untilFourth.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().untilFourth);
        fourth.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().fourth);
        untilEnd.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().untilEnd);
        _0x0C.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x0C);
        _0x0D.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x0D);
        flinch_Glee.textProperty().unbindBidirectional(abilityAnimationProperty.getValue().flinch_Glee);
        _0x0F.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x0F);
        _0x10.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x10);
        _0x11.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x11);
        _0x12.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x12);
        _0x13.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x13);
        _0x14.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x14);
        _0x15.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x15);
        _0x16.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x16);
        _0x17.textProperty().unbindBidirectional(abilityAnimationProperty.getValue()._0x17);
    }


    private void bindAbilityData() {
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


        StringConverter<Byte> unsignedByteConverter = new UnsignedByteStringConverter();
        Bindings.bindBidirectional(unknownByte0.textProperty(), abilityProperty.getValue()._0x0, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte1.textProperty(), abilityProperty.getValue()._0x1, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte4.textProperty(), abilityProperty.getValue()._0x4, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte5.textProperty(), abilityProperty.getValue()._0x5, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte7.textProperty(), abilityProperty.getValue()._0x7, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte9.textProperty(), abilityProperty.getValue()._0x9, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte30.textProperty(), abilityProperty.getValue()._0x30, unsignedByteConverter);
        Bindings.bindBidirectional(unknownByte31.textProperty(), abilityProperty.getValue()._0x31, unsignedByteConverter);
        
        
        race1.valueProperty().bindBidirectional(abilityProperty.getValue().race1);
        race2.valueProperty().bindBidirectional(abilityProperty.getValue().race2);
        race3.valueProperty().bindBidirectional(abilityProperty.getValue().race3);
        race4.valueProperty().bindBidirectional(abilityProperty.getValue().race4);
        race5.valueProperty().bindBidirectional(abilityProperty.getValue().race5);
        race6.valueProperty().bindBidirectional(abilityProperty.getValue().race6);
        
        Bindings.bindBidirectional(apIndex1.textProperty(), abilityProperty.getValue().apIndex1, unsignedByteConverter);
        Bindings.bindBidirectional(apIndex2.textProperty(), abilityProperty.getValue().apIndex2, unsignedByteConverter);
        Bindings.bindBidirectional(apIndex3.textProperty(), abilityProperty.getValue().apIndex3, unsignedByteConverter);
        Bindings.bindBidirectional(apIndex4.textProperty(), abilityProperty.getValue().apIndex4, unsignedByteConverter);
        Bindings.bindBidirectional(apIndex5.textProperty(), abilityProperty.getValue().apIndex5, unsignedByteConverter);
        Bindings.bindBidirectional(apIndex6.textProperty(), abilityProperty.getValue().apIndex6, unsignedByteConverter);
        
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

        Bindings.bindBidirectional(power.textProperty(), abilityProperty.getValue().power, unsignedByteConverter);
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
        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(animation.textProperty(),abilityAnimationProperty.getValue().animation, unsignedShortConverter);
        Bindings.bindBidirectional(subtype.textProperty(),abilityAnimationProperty.getValue().subtype, unsignedByteConverter);
        Bindings.bindBidirectional(start.textProperty(),abilityAnimationProperty.getValue().start, unsignedByteConverter);
        Bindings.bindBidirectional(first.textProperty(),abilityAnimationProperty.getValue().first, unsignedByteConverter);
        Bindings.bindBidirectional(untilSecond.textProperty(),abilityAnimationProperty.getValue().untilSecond, unsignedByteConverter);
        Bindings.bindBidirectional(second.textProperty(),abilityAnimationProperty.getValue().second, unsignedByteConverter);
        Bindings.bindBidirectional(untilThird.textProperty(),abilityAnimationProperty.getValue().untilThird, unsignedByteConverter);
        Bindings.bindBidirectional(third.textProperty(),abilityAnimationProperty.getValue().third, unsignedByteConverter);
        Bindings.bindBidirectional(untilFourth.textProperty(),abilityAnimationProperty.getValue().untilFourth, unsignedByteConverter);
        Bindings.bindBidirectional(fourth.textProperty(),abilityAnimationProperty.getValue().fourth, unsignedByteConverter);
        Bindings.bindBidirectional(untilEnd.textProperty(),abilityAnimationProperty.getValue().untilEnd, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0C.textProperty(),abilityAnimationProperty.getValue()._0x0C, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0D.textProperty(),abilityAnimationProperty.getValue()._0x0D, unsignedByteConverter);
        Bindings.bindBidirectional(flinch_Glee.textProperty(),abilityAnimationProperty.getValue().flinch_Glee, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0F.textProperty(),abilityAnimationProperty.getValue()._0x0F, unsignedByteConverter);
        Bindings.bindBidirectional(_0x10.textProperty(),abilityAnimationProperty.getValue()._0x10, unsignedByteConverter);
        Bindings.bindBidirectional(_0x11.textProperty(),abilityAnimationProperty.getValue()._0x11, unsignedByteConverter);
        Bindings.bindBidirectional(_0x12.textProperty(),abilityAnimationProperty.getValue()._0x12, unsignedByteConverter);
        Bindings.bindBidirectional(_0x13.textProperty(),abilityAnimationProperty.getValue()._0x13, unsignedByteConverter);
        Bindings.bindBidirectional(_0x14.textProperty(),abilityAnimationProperty.getValue()._0x14, unsignedByteConverter);
        Bindings.bindBidirectional(_0x15.textProperty(),abilityAnimationProperty.getValue()._0x15, unsignedByteConverter);
        Bindings.bindBidirectional(_0x16.textProperty(),abilityAnimationProperty.getValue()._0x16, unsignedByteConverter);
        Bindings.bindBidirectional(_0x17.textProperty(),abilityAnimationProperty.getValue()._0x17, unsignedByteConverter);
    }

    @FXML
    private void loadFile() throws IOException {
        if (App.archive != null) {
            ByteBuffer file = App.archive.getFile("");
            if (file != null) {
                currentFile = file;
                byte[] bytes = new byte[0x20];
                file.get(bytes);
                StringBuffer buf = new StringBuffer();
                for (byte b : bytes) {
                    buf.append(String.format("%02X", b));
                    buf.append(" ");
                }
            }
            
        }
    }

    public void saveAbilities() {
        List<ActiveAbilityData> abilities = activeAbilityList.getItems();
        ByteBuffer newAbilityDatabytes = ByteBuffer.allocate(abilities.size()*0x34).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < abilities.size(); i++) {
            newAbilityDatabytes.put(abilities.get(i).toBytes());
        }
        newAbilityDatabytes.rewind();
        App.sysdata.setFile(4, newAbilityDatabytes);

        ByteBuffer newAbilityAnimationbytes = ByteBuffer.allocate(abilityAnimationList.size()*0x18).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < abilityAnimationList.size(); i++) {
            newAbilityAnimationbytes.put(abilityAnimationList.get(i).toBytes());
        }
        newAbilityAnimationbytes.rewind();
        App.sysdata.setFile(5, newAbilityAnimationbytes);

        //Pair<ByteBuffer, ByteBuffer> idxPak = App.sysdata.repack();
        //App.archive.setFile("system/rom/sysdata_rom.idx", idxPak.getKey());
        //App.archive.setFile("system/rom/sysdata.pak", idxPak.getValue());
        
    }


    public void loadAbilities() {
        if (App.archive != null) {
            // Active Abilities
            ByteBuffer activeAbilityDataBytes = App.sysdata.getFile(4);

            if (activeAbilityDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            activeAbilityDataBytes.rewind();

            ObservableList<ActiveAbilityData> activeAbilityDataList = FXCollections.observableArrayList();

            for (int i = 0; i < 0x336; i++) {
                //AbilityData abilityData = new AbilityData(abilityDataBytes, AbilityId.abilityNames[i], i);
                ActiveAbilityData activeAbilityData = new ActiveAbilityData(activeAbilityDataBytes, i);
                activeAbilityDataList.add(activeAbilityData);
            }
            activeAbilityList.setCellFactory(x -> new AbilityCell<>());
            activeAbilityList.setItems(activeAbilityDataList);
            App.activeAbilityList = activeAbilityDataList;
            
            activeAbilityDataBytes.rewind();

                // Support Abilities
            ByteBuffer reactionAbilityDataBytes = App.sysdata.getFile(6);

            if (reactionAbilityDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            reactionAbilityDataBytes.rewind();

            ObservableList<SPAbilityData> reactionAbilityDataList = FXCollections.observableArrayList();

            for (int i = 0; i < 0x1D; i++) {
                SPAbilityData reactionAbilityData = new SPAbilityData(reactionAbilityDataBytes, i + 0x336);
                reactionAbilityDataList.add(reactionAbilityData);
            }
            //supportAbilityList.setCellFactory(x -> new AbilityCell());
            //supportAbilityList.setItems(reactionAbilityDataList);
            App.reactionAbilityList = reactionAbilityDataList;
            
            reactionAbilityDataBytes.rewind();

        
            // Support Abilities
            ByteBuffer supportAbilityDataBytes = App.sysdata.getFile(7);

            if (supportAbilityDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            supportAbilityDataBytes.rewind();

            ObservableList<SPAbilityData> supportAbilityDataList = FXCollections.observableArrayList();

            for (int i = 0; i < 0x28; i++) {
                SPAbilityData supportAbilityData = new SPAbilityData(supportAbilityDataBytes, i + 0x353);
                supportAbilityDataList.add(supportAbilityData);
            }
            //supportAbilityList.setCellFactory(x -> new AbilityCell());
            //supportAbilityList.setItems(supportAbilityDataList);
            App.supportAbilityList = supportAbilityDataList;
            
            supportAbilityDataBytes.rewind();

            App.abilityList = FXCollections.observableArrayList();
            App.abilityList.addAll(App.activeAbilityList);
            App.abilityList.addAll(App.reactionAbilityList);
            App.abilityList.addAll(App.supportAbilityList);

            // Ability Animations
            ByteBuffer abilityAnimationBytes = App.sysdata.getFile(5);
            
            if (abilityAnimationBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            abilityAnimationBytes.rewind();
            abilityAnimationList = new ArrayList<>();
            for (int i = 0; i < 0x336; i++) {
                AbilityAnimation abilityAnimation = new AbilityAnimation(abilityAnimationBytes);
                abilityAnimationList.add(abilityAnimation);
            }
            abilityAnimationBytes.rewind();

            
            ObservableList<Short> abilityNameIds = FXCollections.observableArrayList(IntStream.range(0, App.abilityNames.size()).mapToObj(i -> (short)i).toList());
            learnedAbility.setItems(abilityNameIds);
            learnedAbility.setButtonCell(new AbilityIdCell());
            learnedAbility.setCellFactory(x -> new AbilityIdCell());

        }
    }
}
