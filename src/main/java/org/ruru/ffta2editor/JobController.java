package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.AbilityController.AbilityCell;
import org.ruru.ffta2editor.SpritesController.FaceCell;
import org.ruru.ffta2editor.SpritesController.TopSpriteCell;
import org.ruru.ffta2editor.model.Race;
import org.ruru.ffta2editor.model.ability.ActiveAbilityData;
import org.ruru.ffta2editor.model.job.AbilitySet;
import org.ruru.ffta2editor.model.job.AbilitySetAbility;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.model.job.JobElementalResistance;
import org.ruru.ffta2editor.model.job.JobGender;
import org.ruru.ffta2editor.model.job.JobMoveType;
import org.ruru.ffta2editor.model.topSprite.TopSprite;
import org.ruru.ffta2editor.model.unitFace.UnitFace;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.UnitSprite;
import org.ruru.ffta2editor.utility.UnsignedByteStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class JobController {
    
    public static class JobCell extends ListCell<JobData> {
        Label label = new Label();

        public JobCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(JobData job, boolean empty) {
            super.updateItem(job, empty);
            if (job != null) {
                label.setText(String.format("%X: %s", job.id , job.name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }
    
    public static class JobIdCell extends ListCell<Short> {
        Label label = new Label();

        public JobIdCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(Short id, boolean empty) {
            super.updateItem(id, empty);
            if (id != null) {
                label.setText(String.format("%X: %s", id, App.jobNames.get(id).getValue()));
            } else {
                label.setText("None");
            }
            setGraphic(label);
        }
    }
    
    public static class AbilitySetCell extends ListCell<AbilitySet> {
        Label label = new Label();

        public AbilitySetCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(AbilitySet abilitySet, boolean empty) {
            super.updateItem(abilitySet, empty);
            if (abilitySet != null) {
                //label.setText(id + ": " + AbilityId.abilityNames[id]);
                label.setText(String.format("%X: %s", abilitySet.id , abilitySet.name.getValue()));
            } else {
                label.setText("None");
            }
            setGraphic(label);
        }
    }
    public static class AbilitySetAbilityCell extends ListCell<AbilitySetAbility> {
        Label label = new Label();

        public AbilitySetAbilityCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(AbilitySetAbility abilitySet, boolean empty) {
            super.updateItem(abilitySet, empty);
            if (abilitySet != null && abilitySet.ability.getValue() != null) {
                //label.setText(id + ": " + AbilityId.abilityNames[id]);
                label.setText(String.format("%X: %s", abilitySet.ability.getValue().id , abilitySet.ability.getValue().name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }
    public static class JobSpriteCell extends ListCell<UnitSprite> {
        //Label label = new Label();
        ImageView image = new ImageView();
        int spriteIndex;
        int paletteIndex;
        int scale;


        public JobSpriteCell(int spriteIndex, int paletteIndex, int scale) {
            this.spriteIndex = spriteIndex;
            this.paletteIndex = paletteIndex;
            this.scale = scale;
        }

        @Override protected void updateItem(UnitSprite unitSprite, boolean empty) {
            super.updateItem(unitSprite, empty);
            if (unitSprite != null) {
                //label.setText(String.format("%X: %s", ));
                image.setImage(SwingFXUtils.toFXImage(unitSprite.getSprite(spriteIndex, paletteIndex, scale), null));
            } else {
                image.setImage(null);
            }
            setGraphic(image);
        }
    }

    @FXML ListView<JobData> jobList;

    @FXML TextField jobName;
    @FXML TextArea jobDescription;

    @FXML CheckBox equipKnife;
    @FXML CheckBox equipSword;
    @FXML CheckBox equipBlade;
    @FXML CheckBox equipSaber;
    @FXML CheckBox equipKnightsword;
    @FXML CheckBox equipRapier;
    @FXML CheckBox equipGreatsword;
    @FXML CheckBox equipBroadsword;
    @FXML CheckBox equipKatana;
    @FXML CheckBox equipSpear;
    @FXML CheckBox equipRod;
    @FXML CheckBox equipStaff;
    @FXML CheckBox equipPole;
    @FXML CheckBox equipKnuckles;
    @FXML CheckBox equipBow;
    @FXML CheckBox equipGreatbow;
    @FXML CheckBox equipGun;
    @FXML CheckBox equipInstrument;
    @FXML CheckBox equipHandCannon;
    @FXML CheckBox equipGrenade;
    @FXML CheckBox equipAxe;
    @FXML CheckBox equipHammer;
    @FXML CheckBox equipMace;
    @FXML CheckBox equipCard;
    @FXML CheckBox equipBook;
    @FXML CheckBox equipShield;
    @FXML CheckBox equipHelm;
    @FXML CheckBox equipHairAccessory;
    @FXML CheckBox equipHat;
    @FXML CheckBox equipHeavyArmor;
    @FXML CheckBox equipLightArmor;
    @FXML CheckBox equipRobe;
    @FXML CheckBox equipBoots;
    @FXML CheckBox equipGloves;
    @FXML CheckBox equipAccessory;
    @FXML CheckBox equipBit0;
    @FXML CheckBox equipBit36;
    @FXML CheckBox equipBit37;
    @FXML CheckBox equipBit38;
    @FXML CheckBox equipBit39;

    @FXML CheckBox propertyBit0;
    @FXML CheckBox propertyBit1;
    @FXML CheckBox canChangeJobs;
    @FXML CheckBox isUndead;
    @FXML CheckBox propertyBit4;
    @FXML CheckBox propertyBit5;
    @FXML CheckBox canAlwaysUseItems;
    @FXML CheckBox propertyBit7;

    @FXML TextField hpBase;
    @FXML TextField mpBase;
    @FXML TextField speedBase;
    @FXML TextField attackBase;
    @FXML TextField defenseBase;
    @FXML TextField magickBase;
    @FXML TextField resistanceBase;
    @FXML TextField hpGrowth;
    @FXML TextField mpGrowth;
    @FXML TextField speedGrowth;
    @FXML TextField attackGrowth;
    @FXML TextField defenseGrowth;
    @FXML TextField magickGrowth;
    @FXML TextField resistanceGrowth;

    @FXML TextField movablePlaces;
    @FXML TextField move;
    @FXML TextField jump;
    @FXML TextField evasion;
    @FXML TextField resilience;
    @FXML TextField unarmedBonus;
    @FXML TextField _0x2a;
    @FXML TextField _0x2b;
    @FXML TextField _0x2c;
    @FXML ComboBox<JobElementalResistance> fireResistance;
    @FXML ComboBox<JobElementalResistance> airResistance;
    @FXML ComboBox<JobElementalResistance> earthResistance;
    @FXML ComboBox<JobElementalResistance> waterResistance;
    @FXML ComboBox<JobElementalResistance> iceResistance;
    @FXML ComboBox<JobElementalResistance> electricityResistance;
    @FXML ComboBox<JobElementalResistance> holyResistance;
    @FXML ComboBox<JobElementalResistance> darkResistance;

    @FXML ComboBox<JobGender> gender;
    @FXML ComboBox<Race> race;
    @FXML ComboBox<JobMoveType> moveType;

    @FXML TextField raceSomethingMaybe;
    @FXML TextField _0x32;
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
    @FXML TextField _0x41;

    @FXML ComboBox<UnitFace> unitPortrait;
    @FXML ComboBox<UnitFace> enemyPortrait;
    @FXML ComboBox<TopSprite> unitTopSprite;
    @FXML ComboBox<TopSprite> enemyTopSprite;

    @FXML ComboBox<UnitSprite> unitSprite;
    @FXML ComboBox<UnitSprite> unitAlternateSprite;

    @FXML ComboBox<UnitSprite> enemySprite;
    @FXML ComboBox<UnitSprite> enemyAlternateSprite;
    
    @FXML ComboBox<Byte> unitPalette;
    @FXML ComboBox<Byte> enemyPalette;


    // Ability set
    @FXML ComboBox<AbilitySet> abilitySetList;
    @FXML ListView<AbilitySetAbility> abilitySetAbilityList;
    
    @FXML TextField abilitySetName;
    @FXML TextArea abilitySetDescription;

    @FXML ComboBox<ActiveAbilityData> abilitySetAbilityData;
    @FXML TextField maxAP;
    @FXML TextField abilityAnimation;
    @FXML TextField weaponAnimation;
    @FXML TextField ability_0x4;
    @FXML TextField ability_0x5;
    @FXML TextField ability_0x6;
    @FXML TextField ability_0x7;
    @FXML TextField ability_0x8;
    @FXML TextField ability_0xa;
    @FXML TextField ability_0xb;

    @FXML CheckBox raceBit_0;
    @FXML CheckBox raceHume;
    @FXML CheckBox raceBangaa;
    @FXML CheckBox raceNuMou;
    @FXML CheckBox raceViera;
    @FXML CheckBox raceMoogle;
    @FXML CheckBox raceSeeq;
    @FXML CheckBox raceGria;
    @FXML CheckBox raceBit_8;
    @FXML CheckBox raceBit_9;
    @FXML CheckBox raceBaknamy;
    @FXML CheckBox raceSprite;
    @FXML CheckBox raceLamia;
    @FXML CheckBox raceWolf;
    @FXML CheckBox raceDreamhare;
    @FXML CheckBox raceWerewolf;
    @FXML CheckBox raceAntlion;
    @FXML CheckBox raceShelling;
    @FXML CheckBox raceMalboro;
    @FXML CheckBox raceTomato;
    @FXML CheckBox raceCockatrice;
    @FXML CheckBox raceChocobo;
    @FXML CheckBox raceFlan;
    @FXML CheckBox raceBomb;
    @FXML CheckBox raceZombie;
    @FXML CheckBox raceGhost;
    @FXML CheckBox raceDeathscythe;
    @FXML CheckBox raceFloatingEye;
    @FXML CheckBox raceAhriman;
    @FXML CheckBox raceTonberry;
    @FXML CheckBox raceHeadless;
    @FXML CheckBox raceBehemoth;
    @FXML CheckBox raceMagickPot;
    @FXML CheckBox raceDrake;
    @FXML CheckBox raceMimic;
    @FXML CheckBox raceBit_35;
    @FXML CheckBox raceYowie;
    @FXML CheckBox raceRafflesia;
    @FXML CheckBox raceDemonWall;
    @FXML CheckBox raceNeukhia;
    @FXML CheckBox raceUpsilon;
    @FXML CheckBox raceBit_41;
    @FXML CheckBox raceBit_42;
    @FXML CheckBox raceBit_43;
    @FXML CheckBox raceBit_44;
    @FXML CheckBox raceBit_45;
    @FXML CheckBox raceBit_46;
    @FXML CheckBox raceBit_47;


    private ObjectProperty<JobData> jobProperty = new SimpleObjectProperty<>();
    private ObjectProperty<AbilitySet> abilitySetProperty = new SimpleObjectProperty<>();
    private ObjectProperty<AbilitySetAbility> abilitySetAbilityProperty = new SimpleObjectProperty<>();

    private ObservableList<Byte> unitPaletteList = FXCollections.observableArrayList();
    private ObservableList<Byte> enemyPaletteList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        
        jobList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindJobData();
            jobProperty.setValue(newValue);
            if (newValue != null) bindJobData();
        });
        abilitySetList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindAbilitySetData();
            abilitySetProperty.setValue(newValue);
            if (newValue != null) bindAbilitySetData();
        });
        abilitySetAbilityList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindAbilitySetAbilityData();
            abilitySetAbilityProperty.setValue(newValue);
            if (newValue != null) bindAbilitySetAbilityData();
        });
        abilitySetAbilityData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            abilitySetAbilityList.refresh();
        });
        ObservableList<Race> raceEnums = FXCollections.observableArrayList(Race.values());
        race.setItems(raceEnums);
        ObservableList<JobMoveType> moveTypeEnums = FXCollections.observableArrayList(JobMoveType.values());
        moveType.setItems(moveTypeEnums);
        ObservableList<JobElementalResistance> elementalResistanceEnums = FXCollections.observableArrayList(JobElementalResistance.values());
        fireResistance.setItems(elementalResistanceEnums);
        airResistance.setItems(elementalResistanceEnums);
        earthResistance.setItems(elementalResistanceEnums);
        waterResistance.setItems(elementalResistanceEnums);
        iceResistance.setItems(elementalResistanceEnums);
        electricityResistance.setItems(elementalResistanceEnums);
        holyResistance.setItems(elementalResistanceEnums);
        darkResistance.setItems(elementalResistanceEnums);
        ObservableList<JobGender> genderEnums = FXCollections.observableArrayList(JobGender.values());
        gender.setItems(genderEnums);

        unitPalette.setItems(unitPaletteList);
        enemyPalette.setItems(enemyPaletteList);

        unitPalette.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int newPalette = newValue != null ? newValue : 0;
            unitSprite.setButtonCell(new JobSpriteCell(0, newPalette, 2));
            unitSprite.setCellFactory(x -> new JobSpriteCell(0, newPalette, 2));
            unitAlternateSprite.setButtonCell(new JobSpriteCell(0, newPalette, 2));
            unitAlternateSprite.setCellFactory(x -> new JobSpriteCell(0, newPalette, 2));
        });
        enemyPalette.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int newPalette = newValue != null ? newValue : 0;
            enemySprite.setButtonCell(new JobSpriteCell(0, newPalette, 2));
            enemySprite.setCellFactory(x -> new JobSpriteCell(0, newPalette, 2));
            enemyAlternateSprite.setButtonCell(new JobSpriteCell(0, newPalette, 2));
            enemyAlternateSprite.setCellFactory(x -> new JobSpriteCell(0, newPalette, 2));
        });

        unitSprite.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selected = unitPalette.getSelectionModel().getSelectedIndex();
            unitPaletteList.clear();
            if (newValue != null) {
                IntStream.range(0, newValue.spritePalettes.palettes.size()).forEach(i -> unitPaletteList.add((byte)i));
            }
            if (selected < unitPaletteList.size()) unitPalette.getSelectionModel().select(selected);
            else unitPalette.getSelectionModel().select(0);
        });
        enemySprite.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selected = enemyPalette.getSelectionModel().getSelectedIndex();
            enemyPaletteList.clear();
            if (newValue != null) {
                IntStream.range(0, newValue.spritePalettes.palettes.size()).forEach(i -> enemyPaletteList.add((byte)i));
            }
            if (selected < enemyPaletteList.size()) enemyPalette.getSelectionModel().select(selected);
            else enemyPalette.getSelectionModel().select(0);
        });
        
        // Data validators
        hpBase.textProperty().addListener(new ByteChangeListener(hpBase));
        mpBase.textProperty().addListener(new ByteChangeListener(mpBase));
        speedBase.textProperty().addListener(new ByteChangeListener(speedBase));
        attackBase.textProperty().addListener(new ByteChangeListener(attackBase));
        defenseBase.textProperty().addListener(new ByteChangeListener(defenseBase));
        magickBase.textProperty().addListener(new ByteChangeListener(magickBase));
        resistanceBase.textProperty().addListener(new ByteChangeListener(resistanceBase));
        hpGrowth.textProperty().addListener(new ByteChangeListener(hpGrowth));
        mpGrowth.textProperty().addListener(new ByteChangeListener(mpGrowth));
        speedGrowth.textProperty().addListener(new ByteChangeListener(speedGrowth));
        attackGrowth.textProperty().addListener(new ByteChangeListener(attackGrowth));
        defenseGrowth.textProperty().addListener(new ByteChangeListener(defenseGrowth));
        magickGrowth.textProperty().addListener(new ByteChangeListener(magickGrowth));
        resistanceGrowth.textProperty().addListener(new ByteChangeListener(resistanceGrowth));
        movablePlaces.textProperty().addListener(new ByteChangeListener(movablePlaces));
        move.textProperty().addListener(new ByteChangeListener(move));
        jump.textProperty().addListener(new ByteChangeListener(jump));
        evasion.textProperty().addListener(new ByteChangeListener(evasion));
        resilience.textProperty().addListener(new ByteChangeListener(resilience));
        unarmedBonus.textProperty().addListener(new ByteChangeListener(unarmedBonus));
        _0x2a.textProperty().addListener(new ByteChangeListener(_0x2a));
        _0x2b.textProperty().addListener(new ByteChangeListener(_0x2b));
        _0x2c.textProperty().addListener(new ByteChangeListener(_0x2c));
        raceSomethingMaybe.textProperty().addListener(new ByteChangeListener(raceSomethingMaybe));
        _0x32.textProperty().addListener(new ByteChangeListener(_0x32));
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
        _0x41.textProperty().addListener(new ByteChangeListener(_0x41));
        
        //unitPortrait.textProperty().addListener(new ShortChangeListener(unitPortrait));
        //enemyPortrait.textProperty().addListener(new ShortChangeListener(enemyPortrait));

        maxAP.textProperty().addListener(new ByteChangeListener(maxAP));
        abilityAnimation.textProperty().addListener(new ByteChangeListener(abilityAnimation));
        weaponAnimation.textProperty().addListener(new ByteChangeListener(weaponAnimation));
        ability_0x4.textProperty().addListener(new ByteChangeListener(ability_0x4));
        ability_0x5.textProperty().addListener(new ByteChangeListener(ability_0x5));
        ability_0x6.textProperty().addListener(new ByteChangeListener(ability_0x6));
        ability_0x7.textProperty().addListener(new ByteChangeListener(ability_0x7));
        ability_0x8.textProperty().addListener(new ByteChangeListener(ability_0x8));
        ability_0xa.textProperty().addListener(new ByteChangeListener(ability_0xa));
        ability_0xb.textProperty().addListener(new ByteChangeListener(ability_0xb));
    }

    private void unbindJobData() {
        equipBit0.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBit0);
        equipKnife.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipKnife);
        equipSword.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipSword);
        equipBlade.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBlade);
        equipSaber.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipSaber);
        equipKnightsword.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipKnightsword);
        equipRapier.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipRapier);
        equipGreatsword.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipGreatsword);
        equipBroadsword.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBroadsword);
        equipKatana.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipKatana);
        equipSpear.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipSpear);
        equipRod.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipRod);
        equipStaff.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipStaff);
        equipPole.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipPole);
        equipKnuckles.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipKnuckles);
        equipBow.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBow);
        equipGreatbow.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipGreatbow);
        equipGun.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipGun);
        equipInstrument.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipInstrument);
        equipHandCannon.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipHandCannon);
        equipGrenade.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipGrenade);
        equipAxe.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipAxe);
        equipHammer.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipHammer);
        equipMace.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipMace);
        equipCard.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipCard);
        equipBook.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBook);
        equipShield.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipShield);
        equipHelm.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipHelm);
        equipHairAccessory.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipHairAccessory);
        equipHat.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipHat);
        equipHeavyArmor.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipHeavyArmor);
        equipLightArmor.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipLightArmor);
        equipRobe.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipRobe);
        equipBoots.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBoots);
        equipGloves.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipGloves);
        equipAccessory.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipAccessory);
        equipBit36.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBit36);
        equipBit37.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBit37);
        equipBit38.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBit38);
        equipBit39.selectedProperty().unbindBidirectional(jobProperty.getValue().equipFlags.equipBit39);

        propertyBit0.selectedProperty().unbindBidirectional(jobProperty.getValue().propertyFlags.propertyBit0);
        propertyBit1.selectedProperty().unbindBidirectional(jobProperty.getValue().propertyFlags.propertyBit1);
        canChangeJobs.selectedProperty().unbindBidirectional(jobProperty.getValue().propertyFlags.canChangeJobs);
        isUndead.selectedProperty().unbindBidirectional(jobProperty.getValue().propertyFlags.isUndead);
        propertyBit4.selectedProperty().unbindBidirectional(jobProperty.getValue().propertyFlags.propertyBit4);
        propertyBit5.selectedProperty().unbindBidirectional(jobProperty.getValue().propertyFlags.propertyBit5);
        canAlwaysUseItems.selectedProperty().unbindBidirectional(jobProperty.getValue().propertyFlags.canAlwaysUseItems);
        propertyBit7.selectedProperty().unbindBidirectional(jobProperty.getValue().propertyFlags.propertyBit7);

        fireResistance.valueProperty().unbindBidirectional(jobProperty.getValue().fireResistance);
        airResistance.valueProperty().unbindBidirectional(jobProperty.getValue().airResistance);
        earthResistance.valueProperty().unbindBidirectional(jobProperty.getValue().earthResistance);
        waterResistance.valueProperty().unbindBidirectional(jobProperty.getValue().waterResistance);
        iceResistance.valueProperty().unbindBidirectional(jobProperty.getValue().iceResistance);
        electricityResistance.valueProperty().unbindBidirectional(jobProperty.getValue().electricityResistance);
        holyResistance.valueProperty().unbindBidirectional(jobProperty.getValue().holyResistance);
        darkResistance.valueProperty().unbindBidirectional(jobProperty.getValue().darkResistance);

        gender.valueProperty().unbindBidirectional(jobProperty.getValue().gender);
        race.valueProperty().unbindBidirectional(jobProperty.getValue().race);
        moveType.valueProperty().unbindBidirectional(jobProperty.getValue().moveType);

        hpBase.textProperty().unbindBidirectional(jobProperty.getValue().hpBase);
        mpBase.textProperty().unbindBidirectional(jobProperty.getValue().mpBase);
        speedBase.textProperty().unbindBidirectional(jobProperty.getValue().speedBase);
        attackBase.textProperty().unbindBidirectional(jobProperty.getValue().attackBase);
        defenseBase.textProperty().unbindBidirectional(jobProperty.getValue().defenseBase);
        magickBase.textProperty().unbindBidirectional(jobProperty.getValue().magickBase);
        resistanceBase.textProperty().unbindBidirectional(jobProperty.getValue().resistanceBase);
        hpGrowth.textProperty().unbindBidirectional(jobProperty.getValue().hpGrowth);
        mpGrowth.textProperty().unbindBidirectional(jobProperty.getValue().mpGrowth);
        speedGrowth.textProperty().unbindBidirectional(jobProperty.getValue().speedGrowth);
        attackGrowth.textProperty().unbindBidirectional(jobProperty.getValue().attackGrowth);
        defenseGrowth.textProperty().unbindBidirectional(jobProperty.getValue().defenseGrowth);
        magickGrowth.textProperty().unbindBidirectional(jobProperty.getValue().magickGrowth);
        resistanceGrowth.textProperty().unbindBidirectional(jobProperty.getValue().resistanceGrowth);
        movablePlaces.textProperty().unbindBidirectional(jobProperty.getValue().movablePlaces);
        move.textProperty().unbindBidirectional(jobProperty.getValue().move);
        jump.textProperty().unbindBidirectional(jobProperty.getValue().jump);
        evasion.textProperty().unbindBidirectional(jobProperty.getValue().evasion);
        resilience.textProperty().unbindBidirectional(jobProperty.getValue().resilience);
        unarmedBonus.textProperty().unbindBidirectional(jobProperty.getValue().unarmedBonus);
        _0x2a.textProperty().unbindBidirectional(jobProperty.getValue()._0x2a);
        _0x2b.textProperty().unbindBidirectional(jobProperty.getValue()._0x2b);
        _0x2c.textProperty().unbindBidirectional(jobProperty.getValue()._0x2c);
        raceSomethingMaybe.textProperty().unbindBidirectional(jobProperty.getValue().raceSomethingMaybe);
        _0x32.textProperty().unbindBidirectional(jobProperty.getValue()._0x32);
        _0x34.textProperty().unbindBidirectional(jobProperty.getValue()._0x34);
        _0x35.textProperty().unbindBidirectional(jobProperty.getValue()._0x35);
        _0x36.textProperty().unbindBidirectional(jobProperty.getValue()._0x36);
        _0x37.textProperty().unbindBidirectional(jobProperty.getValue()._0x37);
        _0x38.textProperty().unbindBidirectional(jobProperty.getValue()._0x38);
        _0x39.textProperty().unbindBidirectional(jobProperty.getValue()._0x39);
        _0x3a.textProperty().unbindBidirectional(jobProperty.getValue()._0x3a);
        _0x3b.textProperty().unbindBidirectional(jobProperty.getValue()._0x3b);
        _0x3c.textProperty().unbindBidirectional(jobProperty.getValue()._0x3c);
        _0x3d.textProperty().unbindBidirectional(jobProperty.getValue()._0x3d);
        _0x3e.textProperty().unbindBidirectional(jobProperty.getValue()._0x3e);
        _0x41.textProperty().unbindBidirectional(jobProperty.getValue()._0x41);
        
        unitPortrait.valueProperty().unbindBidirectional(jobProperty.getValue().unitPortrait);
        enemyPortrait.valueProperty().unbindBidirectional(jobProperty.getValue().enemyPortrait);
        unitTopSprite.valueProperty().unbindBidirectional(jobProperty.getValue().unitTopSprite);
        enemyTopSprite.valueProperty().unbindBidirectional(jobProperty.getValue().enemyTopSprite);
        
        unitSprite.valueProperty().unbindBidirectional(jobProperty.getValue().unitSprite);
        unitAlternateSprite.valueProperty().unbindBidirectional(jobProperty.getValue().unitAlternateSprite);
        enemySprite.valueProperty().unbindBidirectional(jobProperty.getValue().enemySprite);
        enemyAlternateSprite.valueProperty().unbindBidirectional(jobProperty.getValue().enemyAlternateSprite);
        unitPalette.valueProperty().unbindBidirectional(jobProperty.getValue().unitPalette);
        enemyPalette.valueProperty().unbindBidirectional(jobProperty.getValue().enemyPalette);

        abilitySetList.valueProperty().unbindBidirectional(jobProperty.getValue().abilitySet);

        jobName.textProperty().unbindBidirectional(jobProperty.getValue().name);
        jobDescription.textProperty().unbindBidirectional(jobProperty.getValue().description);
    }

    private void bindJobData() {
        equipBit0.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBit0);
        equipKnife.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipKnife);
        equipSword.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipSword);
        equipBlade.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBlade);
        equipSaber.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipSaber);
        equipKnightsword.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipKnightsword);
        equipRapier.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipRapier);
        equipGreatsword.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipGreatsword);
        equipBroadsword.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBroadsword);
        equipKatana.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipKatana);
        equipSpear.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipSpear);
        equipRod.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipRod);
        equipStaff.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipStaff);
        equipPole.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipPole);
        equipKnuckles.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipKnuckles);
        equipBow.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBow);
        equipGreatbow.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipGreatbow);
        equipGun.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipGun);
        equipInstrument.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipInstrument);
        equipHandCannon.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipHandCannon);
        equipGrenade.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipGrenade);
        equipAxe.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipAxe);
        equipHammer.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipHammer);
        equipMace.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipMace);
        equipCard.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipCard);
        equipBook.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBook);
        equipShield.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipShield);
        equipHelm.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipHelm);
        equipHairAccessory.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipHairAccessory);
        equipHat.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipHat);
        equipHeavyArmor.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipHeavyArmor);
        equipLightArmor.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipLightArmor);
        equipRobe.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipRobe);
        equipBoots.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBoots);
        equipGloves.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipGloves);
        equipAccessory.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipAccessory);
        equipBit36.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBit36);
        equipBit37.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBit37);
        equipBit38.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBit38);
        equipBit39.selectedProperty().bindBidirectional(jobProperty.getValue().equipFlags.equipBit39);

        propertyBit0.selectedProperty().bindBidirectional(jobProperty.getValue().propertyFlags.propertyBit0);
        propertyBit1.selectedProperty().bindBidirectional(jobProperty.getValue().propertyFlags.propertyBit1);
        canChangeJobs.selectedProperty().bindBidirectional(jobProperty.getValue().propertyFlags.canChangeJobs);
        isUndead.selectedProperty().bindBidirectional(jobProperty.getValue().propertyFlags.isUndead);
        propertyBit4.selectedProperty().bindBidirectional(jobProperty.getValue().propertyFlags.propertyBit4);
        propertyBit5.selectedProperty().bindBidirectional(jobProperty.getValue().propertyFlags.propertyBit5);
        canAlwaysUseItems.selectedProperty().bindBidirectional(jobProperty.getValue().propertyFlags.canAlwaysUseItems);
        propertyBit7.selectedProperty().bindBidirectional(jobProperty.getValue().propertyFlags.propertyBit7);

        fireResistance.valueProperty().bindBidirectional(jobProperty.getValue().fireResistance);
        airResistance.valueProperty().bindBidirectional(jobProperty.getValue().airResistance);
        earthResistance.valueProperty().bindBidirectional(jobProperty.getValue().earthResistance);
        waterResistance.valueProperty().bindBidirectional(jobProperty.getValue().waterResistance);
        iceResistance.valueProperty().bindBidirectional(jobProperty.getValue().iceResistance);
        electricityResistance.valueProperty().bindBidirectional(jobProperty.getValue().electricityResistance);
        holyResistance.valueProperty().bindBidirectional(jobProperty.getValue().holyResistance);
        darkResistance.valueProperty().bindBidirectional(jobProperty.getValue().darkResistance);

        gender.valueProperty().bindBidirectional(jobProperty.getValue().gender);
        race.valueProperty().bindBidirectional(jobProperty.getValue().race);
        moveType.valueProperty().bindBidirectional(jobProperty.getValue().moveType);

        StringConverter<Byte> unsignedByteConverter = new UnsignedByteStringConverter();
        Bindings.bindBidirectional(hpBase.textProperty(), jobProperty.getValue().hpBase, unsignedByteConverter);
        Bindings.bindBidirectional(mpBase.textProperty(), jobProperty.getValue().mpBase, unsignedByteConverter);
        Bindings.bindBidirectional(speedBase.textProperty(), jobProperty.getValue().speedBase, unsignedByteConverter);
        Bindings.bindBidirectional(attackBase.textProperty(), jobProperty.getValue().attackBase, unsignedByteConverter);
        Bindings.bindBidirectional(defenseBase.textProperty(), jobProperty.getValue().defenseBase, unsignedByteConverter);
        Bindings.bindBidirectional(magickBase.textProperty(), jobProperty.getValue().magickBase, unsignedByteConverter);
        Bindings.bindBidirectional(resistanceBase.textProperty(), jobProperty.getValue().resistanceBase, unsignedByteConverter);
        Bindings.bindBidirectional(hpGrowth.textProperty(), jobProperty.getValue().hpGrowth, unsignedByteConverter);
        Bindings.bindBidirectional(mpGrowth.textProperty(), jobProperty.getValue().mpGrowth, unsignedByteConverter);
        Bindings.bindBidirectional(speedGrowth.textProperty(), jobProperty.getValue().speedGrowth, unsignedByteConverter);
        Bindings.bindBidirectional(attackGrowth.textProperty(), jobProperty.getValue().attackGrowth, unsignedByteConverter);
        Bindings.bindBidirectional(defenseGrowth.textProperty(), jobProperty.getValue().defenseGrowth, unsignedByteConverter);
        Bindings.bindBidirectional(magickGrowth.textProperty(), jobProperty.getValue().magickGrowth, unsignedByteConverter);
        Bindings.bindBidirectional(resistanceGrowth.textProperty(), jobProperty.getValue().resistanceGrowth, unsignedByteConverter);
        Bindings.bindBidirectional(movablePlaces.textProperty(), jobProperty.getValue().movablePlaces, unsignedByteConverter);
        Bindings.bindBidirectional(move.textProperty(), jobProperty.getValue().move, unsignedByteConverter);
        Bindings.bindBidirectional(jump.textProperty(), jobProperty.getValue().jump, unsignedByteConverter);
        Bindings.bindBidirectional(evasion.textProperty(), jobProperty.getValue().evasion, unsignedByteConverter);
        Bindings.bindBidirectional(resilience.textProperty(), jobProperty.getValue().resilience, unsignedByteConverter);
        Bindings.bindBidirectional(unarmedBonus.textProperty(), jobProperty.getValue().unarmedBonus, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2a.textProperty(), jobProperty.getValue()._0x2a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2b.textProperty(), jobProperty.getValue()._0x2b, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2c.textProperty(), jobProperty.getValue()._0x2c, unsignedByteConverter);
        Bindings.bindBidirectional(raceSomethingMaybe.textProperty(), jobProperty.getValue().raceSomethingMaybe, unsignedByteConverter);
        Bindings.bindBidirectional(_0x32.textProperty(), jobProperty.getValue()._0x32, unsignedByteConverter);
        Bindings.bindBidirectional(_0x34.textProperty(), jobProperty.getValue()._0x34, unsignedByteConverter);
        Bindings.bindBidirectional(_0x35.textProperty(), jobProperty.getValue()._0x35, unsignedByteConverter);
        Bindings.bindBidirectional(_0x36.textProperty(), jobProperty.getValue()._0x36, unsignedByteConverter);
        Bindings.bindBidirectional(_0x37.textProperty(), jobProperty.getValue()._0x37, unsignedByteConverter);
        Bindings.bindBidirectional(_0x38.textProperty(), jobProperty.getValue()._0x38, unsignedByteConverter);
        Bindings.bindBidirectional(_0x39.textProperty(), jobProperty.getValue()._0x39, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3a.textProperty(), jobProperty.getValue()._0x3a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3b.textProperty(), jobProperty.getValue()._0x3b, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3c.textProperty(), jobProperty.getValue()._0x3c, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3d.textProperty(), jobProperty.getValue()._0x3d, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3e.textProperty(), jobProperty.getValue()._0x3e, unsignedByteConverter);
        Bindings.bindBidirectional(_0x41.textProperty(), jobProperty.getValue()._0x41, unsignedByteConverter);

        //StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        //Bindings.bindBidirectional(unitPortrait.textProperty(), jobProperty.getValue().unitPortrait, unsignedShortConverter);
        //Bindings.bindBidirectional(enemyPortrait.textProperty(), jobProperty.getValue().enemyPortrait, unsignedShortConverter);
        


        unitSprite.valueProperty().bindBidirectional(jobProperty.getValue().unitSprite);
        unitAlternateSprite.valueProperty().bindBidirectional(jobProperty.getValue().unitAlternateSprite);
        enemySprite.valueProperty().bindBidirectional(jobProperty.getValue().enemySprite);
        enemyAlternateSprite.valueProperty().bindBidirectional(jobProperty.getValue().enemyAlternateSprite);
        unitPalette.valueProperty().bindBidirectional(jobProperty.getValue().unitPalette);
        enemyPalette.valueProperty().bindBidirectional(jobProperty.getValue().enemyPalette);
        
        unitSprite.setButtonCell(new JobSpriteCell(0, jobProperty.getValue().unitPalette.getValue(), 2));
        unitSprite.setCellFactory(x -> new JobSpriteCell(0, jobProperty.getValue().unitPalette.getValue(), 2));
        unitAlternateSprite.setButtonCell(new JobSpriteCell(0, jobProperty.getValue().unitPalette.getValue(), 2));
        unitAlternateSprite.setCellFactory(x -> new JobSpriteCell(0, jobProperty.getValue().unitPalette.getValue(), 2));

        enemySprite.setButtonCell(new JobSpriteCell(0, jobProperty.getValue().enemyPalette.getValue(), 2));
        enemySprite.setCellFactory(x -> new JobSpriteCell(0, jobProperty.getValue().enemyPalette.getValue(), 2));
        enemyAlternateSprite.setButtonCell(new JobSpriteCell(0, jobProperty.getValue().enemyPalette.getValue(), 2));
        enemyAlternateSprite.setCellFactory(x -> new JobSpriteCell(0, jobProperty.getValue().enemyPalette.getValue(), 2));
        
        unitPortrait.valueProperty().bindBidirectional(jobProperty.getValue().unitPortrait);
        enemyPortrait.valueProperty().bindBidirectional(jobProperty.getValue().enemyPortrait);
        
        unitTopSprite.valueProperty().bindBidirectional(jobProperty.getValue().unitTopSprite);
        enemyTopSprite.valueProperty().bindBidirectional(jobProperty.getValue().enemyTopSprite);
        
        abilitySetList.valueProperty().bindBidirectional(jobProperty.getValue().abilitySet);
        
        jobName.textProperty().bindBidirectional(jobProperty.getValue().name);
        jobDescription.textProperty().bindBidirectional(jobProperty.getValue().description);
    }

    public void unbindAbilitySetData() {
        abilitySetName.textProperty().unbindBidirectional(abilitySetProperty.getValue().name);
        abilitySetDescription.textProperty().unbindBidirectional(abilitySetProperty.getValue().description);

        raceBit_0.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_0);
        raceHume.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceHume);
        raceBangaa.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBangaa);
        raceNuMou.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceNuMou);
        raceViera.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceViera);
        raceMoogle.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceMoogle);
        raceSeeq.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceSeeq);
        raceGria.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceGria);
        raceBit_8.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_8);
        raceBit_9.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_9);
        raceBaknamy.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBaknamy);
        raceSprite.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceSprite);
        raceLamia.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceLamia);
        raceWolf.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceWolf);
        raceDreamhare.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceDreamhare);
        raceWerewolf.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceWerewolf);
        raceAntlion.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceAntlion);
        raceShelling.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceShelling);
        raceMalboro.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceMalboro);
        raceTomato.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceTomato);
        raceCockatrice.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceCockatrice);
        raceChocobo.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceChocobo);
        raceFlan.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceFlan);
        raceBomb.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBomb);
        raceZombie.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceZombie);
        raceGhost.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceGhost);
        raceDeathscythe.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceDeathscythe);
        raceFloatingEye.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceFloatingEye);
        raceAhriman.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceAhriman);
        raceTonberry.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceTonberry);
        raceHeadless.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceHeadless);
        raceBehemoth.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBehemoth);
        raceMagickPot.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceMagickPot);
        raceDrake.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceDrake);
        raceMimic.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceMimic);
        raceBit_35.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_35);
        raceYowie.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceYowie);
        raceRafflesia.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceRafflesia);
        raceDemonWall.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceDemonWall);
        raceNeukhia.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceNeukhia);
        raceUpsilon.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceUpsilon);
        raceBit_41.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_41);
        raceBit_42.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_42);
        raceBit_43.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_43);
        raceBit_44.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_44);
        raceBit_45.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_45);
        raceBit_46.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_46);
        raceBit_47.selectedProperty().unbindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_47);

        abilitySetAbilityList.setItems(null);
    }

    public void bindAbilitySetData() {
        abilitySetName.textProperty().bindBidirectional(abilitySetProperty.getValue().name);
        abilitySetDescription.textProperty().bindBidirectional(abilitySetProperty.getValue().description);

        raceBit_0.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_0);
        raceHume.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceHume);
        raceBangaa.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBangaa);
        raceNuMou.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceNuMou);
        raceViera.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceViera);
        raceMoogle.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceMoogle);
        raceSeeq.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceSeeq);
        raceGria.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceGria);
        raceBit_8.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_8);
        raceBit_9.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_9);
        raceBaknamy.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBaknamy);
        raceSprite.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceSprite);
        raceLamia.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceLamia);
        raceWolf.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceWolf);
        raceDreamhare.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceDreamhare);
        raceWerewolf.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceWerewolf);
        raceAntlion.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceAntlion);
        raceShelling.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceShelling);
        raceMalboro.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceMalboro);
        raceTomato.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceTomato);
        raceCockatrice.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceCockatrice);
        raceChocobo.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceChocobo);
        raceFlan.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceFlan);
        raceBomb.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBomb);
        raceZombie.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceZombie);
        raceGhost.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceGhost);
        raceDeathscythe.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceDeathscythe);
        raceFloatingEye.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceFloatingEye);
        raceAhriman.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceAhriman);
        raceTonberry.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceTonberry);
        raceHeadless.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceHeadless);
        raceBehemoth.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBehemoth);
        raceMagickPot.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceMagickPot);
        raceDrake.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceDrake);
        raceMimic.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceMimic);
        raceBit_35.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_35);
        raceYowie.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceYowie);
        raceRafflesia.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceRafflesia);
        raceDemonWall.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceDemonWall);
        raceNeukhia.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceNeukhia);
        raceUpsilon.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceUpsilon);
        raceBit_41.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_41);
        raceBit_42.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_42);
        raceBit_43.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_43);
        raceBit_44.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_44);
        raceBit_45.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_45);
        raceBit_46.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_46);
        raceBit_47.selectedProperty().bindBidirectional(abilitySetProperty.getValue().raceFlags.raceBit_47);

        abilitySetAbilityList.setItems(abilitySetProperty.getValue().abilities);
    }

    public void unbindAbilitySetAbilityData() {
        maxAP.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().maxAP);
        abilityAnimation.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().abilityAnimation);
        weaponAnimation.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().weaponAnimation);
        ability_0x4.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().ability_0x4);
        ability_0x5.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().ability_0x5);
        ability_0x6.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().ability_0x6);
        ability_0x7.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().ability_0x7);
        ability_0x8.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().ability_0x8);
        ability_0xa.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().ability_0xa);
        ability_0xb.textProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().ability_0xb);
        
        abilitySetAbilityData.valueProperty().unbindBidirectional(abilitySetAbilityProperty.getValue().ability);
    }

    public void bindAbilitySetAbilityData() {
        StringConverter<Byte> unsignedByteConverter = new UnsignedByteStringConverter();
        Bindings.bindBidirectional(maxAP.textProperty(), abilitySetAbilityProperty.getValue().maxAP, unsignedByteConverter);
        Bindings.bindBidirectional(abilityAnimation.textProperty(), abilitySetAbilityProperty.getValue().abilityAnimation, unsignedByteConverter);
        Bindings.bindBidirectional(weaponAnimation.textProperty(), abilitySetAbilityProperty.getValue().weaponAnimation, unsignedByteConverter);
        Bindings.bindBidirectional(ability_0x4.textProperty(), abilitySetAbilityProperty.getValue().ability_0x4, unsignedByteConverter);
        Bindings.bindBidirectional(ability_0x5.textProperty(), abilitySetAbilityProperty.getValue().ability_0x5, unsignedByteConverter);
        Bindings.bindBidirectional(ability_0x6.textProperty(), abilitySetAbilityProperty.getValue().ability_0x6, unsignedByteConverter);
        Bindings.bindBidirectional(ability_0x7.textProperty(), abilitySetAbilityProperty.getValue().ability_0x7, unsignedByteConverter);
        Bindings.bindBidirectional(ability_0x8.textProperty(), abilitySetAbilityProperty.getValue().ability_0x8, unsignedByteConverter);
        Bindings.bindBidirectional(ability_0xa.textProperty(), abilitySetAbilityProperty.getValue().ability_0xa, unsignedByteConverter);
        Bindings.bindBidirectional(ability_0xb.textProperty(), abilitySetAbilityProperty.getValue().ability_0xb, unsignedByteConverter);

        abilitySetAbilityData.valueProperty().bindBidirectional(abilitySetAbilityProperty.getValue().ability);
    }

    @FXML
    public void addAbilitySet() {
        if (abilitySetList.getItems() != null) {
            int newIndex = abilitySetList.getItems().size();
            abilitySetList.getItems().add(new AbilitySet("", newIndex));
            abilitySetList.getSelectionModel().selectLast();;
        }
    }
    @FXML
    public void removeAbilitySet() {
        //if (abilitySetList.getSelectionModel().getSelectedItem() != null && abilitySetList.getSelectionModel().getSelectedIndex() > 0) {
        //    abilitySetList.getItems().remove(abilitySetList.getSelectionModel().getSelectedIndex());
        //}
        if (abilitySetList.getItems().size() > 0) {
            abilitySetList.getItems().removeLast();
        }
    }

    @FXML
    public void addAbilitySetAbility() {
        if (abilitySetList.getSelectionModel().getSelectedItem() != null && abilitySetList.getSelectionModel().getSelectedIndex() > 0) {
            abilitySetAbilityList.getItems().add(new AbilitySetAbility());
            abilitySetAbilityList.getSelectionModel().selectLast();;
        }
    }

    @FXML
    public void removeAbilitySetAbility() {
        if (abilitySetAbilityList.getSelectionModel().getSelectedItem() != null && abilitySetList.getSelectionModel().getSelectedIndex() > 0) {
            abilitySetAbilityList.getItems().remove(abilitySetAbilityList.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    public void addJob() {
        if (jobList.getItems() != null) {
            int newIndex = jobList.getItems().size();
            jobList.getItems().add(new JobData("", newIndex));
            jobList.getSelectionModel().selectLast();
        }
    }

    @FXML
    public void removeJob() {
        //if (jobList.getSelectionModel().getSelectedItem() != null && jobList.getSelectionModel().getSelectedIndex() > 0) {
        //    jobList.getItems().remove(jobList.getSelectionModel().getSelectedIndex());
        //}
        if (jobList.getItems().size() > 0) {
            jobList.getItems().removeLast();
        }
    }


    public void loadJobs() {
        if (App.archive != null) {

            ByteBuffer abilitySetBytes = App.sysdata.getFile(2);
            ByteBuffer abilitySetAbilityBytes = App.sysdata.getFile(3);

            if (abilitySetBytes == null || abilitySetAbilityBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            abilitySetBytes.rewind();
            abilitySetAbilityBytes.rewind();

            ObservableList<AbilitySet> abilitySetDataList = FXCollections.observableArrayList();


            //int numAbilitysets = abilitySetBytes.remaining() / 0xc;
            int numAbilitysets = Byte.toUnsignedInt(App.arm9.get(0x000cb054))+1;
            for (int i = 0; i < numAbilitysets; i++) {
                AbilitySet abilitySet = new AbilitySet(abilitySetBytes, abilitySetAbilityBytes, i);
                abilitySetDataList.add(abilitySet);
            }
            //abilitySetList.setCellFactory(x -> new JobCell());
            App.abilitySetList = abilitySetDataList;
            abilitySetList.setItems(abilitySetDataList);
            abilitySetList.setButtonCell(new AbilitySetCell());
            abilitySetList.setCellFactory(x -> new AbilitySetCell());

            abilitySetAbilityData.setItems(App.activeAbilityList);
            abilitySetAbilityData.setButtonCell(new AbilityCell<>());
            abilitySetAbilityData.setCellFactory(x -> new AbilityCell<>());

            abilitySetAbilityList.setCellFactory(x -> new AbilitySetAbilityCell());
            
            abilitySetBytes.rewind();
            abilitySetAbilityBytes.rewind();

        

            ByteBuffer jobDataBytes = App.sysdata.getFile(1);

            if (jobDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            jobDataBytes.rewind();

            ObservableList<JobData> jobDataList = FXCollections.observableArrayList();

            //int numJobs = jobDataBytes.remaining() / 0x48;
            int numJobs = Byte.toUnsignedInt(App.arm9.get(0x000cafdc))+1;
            for (int i = 0; i < numJobs; i++) {
                //JobData abilityData = new JobData(jobDataBytes, JobId.jobNames[i], i);
                JobData abilityData = new JobData(jobDataBytes, i);
                jobDataList.add(abilityData);
            }
            jobList.setCellFactory(x -> new JobCell());
            jobList.setItems(jobDataList);
            App.jobDataList = jobDataList;
            
            jobDataBytes.rewind();
            
            unitSprite.setButtonCell(new JobSpriteCell(0, 0, 2));
            unitSprite.setCellFactory(x -> new JobSpriteCell(0, 0, 2));
            unitSprite.setItems(App.unitSprites);

            unitAlternateSprite.setButtonCell(new JobSpriteCell(0, 0, 2));
            unitAlternateSprite.setCellFactory(x -> new JobSpriteCell(0, 0, 2));
            unitAlternateSprite.setItems(App.unitSprites);
            
            enemySprite.setButtonCell(new JobSpriteCell(0, 0, 2));
            enemySprite.setCellFactory(x -> new JobSpriteCell(0, 0, 2));
            enemySprite.setItems(App.unitSprites);
            
            enemyAlternateSprite.setButtonCell(new JobSpriteCell(0, 0, 2));
            enemyAlternateSprite.setCellFactory(x -> new JobSpriteCell(0, 0, 2));
            enemyAlternateSprite.setItems(App.unitSprites);

            unitTopSprite.setButtonCell(new TopSpriteCell(0, 2));
            unitTopSprite.setCellFactory(x -> new TopSpriteCell(0, 2));
            unitTopSprite.setItems(App.topSprites);
            
            enemyTopSprite.setButtonCell(new TopSpriteCell(0, 2));
            enemyTopSprite.setCellFactory(x -> new TopSpriteCell(0, 2));
            enemyTopSprite.setItems(App.topSprites);
            
            unitPortrait.setButtonCell(new FaceCell());
            unitPortrait.setCellFactory(x -> new FaceCell());
            unitPortrait.setItems(App.unitFaces);
            
            enemyPortrait.setButtonCell(new FaceCell());
            enemyPortrait.setCellFactory(x -> new FaceCell());
            enemyPortrait.setItems(App.unitFaces);
        }
    }

    public void saveJobs() {
        
        List<JobData> jobs = jobList.getItems();
        ByteBuffer newJobDatabytes = ByteBuffer.allocate(jobs.size()*0x48).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < jobs.size(); i++) {
            newJobDatabytes.put(jobs.get(i).toBytes());
        }
        newJobDatabytes.rewind();
        App.sysdata.setFile(1, newJobDatabytes);
        // Patch arm9 code with new Job length
        App.arm9.put(0x000cafdc, (byte)(jobs.size()-1));
        App.arm9.put(0x000cafe0, (byte)(jobs.size()-1));

        
        ByteBuffer abilitySetAbilityBytes = App.sysdata.getFile(3);
        abilitySetAbilityBytes.rewind();
        
        int firstAbilityIndex = 0x0;
        List<AbilitySet> abilitySets = abilitySetList.getItems();
        ByteBuffer newAbilitySetbytes = ByteBuffer.allocate(abilitySets.size()*0xc).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer newAbilitySetAbilitybytes = ByteBuffer.allocate(abilitySets.stream().mapToInt(x -> x.abilities.size()).sum() * 0xc).order(ByteOrder.LITTLE_ENDIAN);
        //newAbilitySetAbilitybytes.put(abilitySetAbilityBytes.slice(0, 0xc*1)); // Copy the empty ability
        for (AbilitySet abilitySet : abilitySets) {
            assert newAbilitySetAbilitybytes.position() / 0xc == firstAbilityIndex;
            //System.out.println(String.format("%s: %04x", abilitySet.name, firstAbilityIndex));
            Pair<byte[], byte[]> abilitySetBytes = abilitySet.tobytes(firstAbilityIndex);
            newAbilitySetbytes.put(abilitySetBytes.getKey());
            newAbilitySetAbilitybytes.put(abilitySetBytes.getValue());
            firstAbilityIndex += abilitySet.abilities.size();
        }
        //System.out.println(newAbilitySetAbilitybytes.capacity() / 0xc);
        assert (newAbilitySetAbilitybytes.capacity() / 0xc) - 1 == firstAbilityIndex - 1;

        App.sysdata.setFile(2, newAbilitySetbytes.rewind());
        // Patch code with new Ability Set length
        App.arm9.put(0x000cb054, (byte)(abilitySets.size()-1));
        App.arm9.put(0x000cb058, (byte)(abilitySets.size()-1));
        App.arm9.put(0x000b81dc, (byte)abilitySets.size());
        App.overlay11.put(0x4898, (byte)abilitySets.size());
        App.overlay11.put(0x83f8, (byte)abilitySets.size());


        App.sysdata.setFile(3, newAbilitySetAbilitybytes.rewind());
        // Patch code with new Ability Set Abilities length
        App.arm9.putInt(0x000cb1d0, firstAbilityIndex - 1);
        
        
    }


}
