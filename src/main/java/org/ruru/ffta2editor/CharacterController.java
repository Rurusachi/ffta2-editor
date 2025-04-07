package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.JobController.JobCell;
import org.ruru.ffta2editor.JobController.JobSpriteCell;
import org.ruru.ffta2editor.SpritesController.FaceCell;
import org.ruru.ffta2editor.SpritesController.TopSpriteCell;
import org.ruru.ffta2editor.TextController.StringPropertyCell;
import org.ruru.ffta2editor.model.character.CharacterData;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.model.job.JobGender;
import org.ruru.ffta2editor.model.topSprite.TopSprite;
import org.ruru.ffta2editor.model.unitFace.UnitFace;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.ShortChangeListener;
import org.ruru.ffta2editor.utility.UnitSprite;
import org.ruru.ffta2editor.utility.UnsignedByteStringConverter;
import org.ruru.ffta2editor.utility.UnsignedShortStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class CharacterController {
    
    public static class CharacterCell extends ListCell<CharacterData> {
        Label label = new Label();


        public CharacterCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(CharacterData character, boolean empty) {
            super.updateItem(character, empty);
            if (character != null) {
                label.setText(String.format("%X: %s", character.id , character.nameString.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    @FXML ListView<CharacterData> characterList;


    @FXML ComboBox<StringProperty> name;
    @FXML TextField dialogueRole;
    @FXML ComboBox<JobGender> gender;
    @FXML ComboBox<JobData> defaultJob;
    
    // Short
    @FXML ComboBox<StringProperty> jobName;
    @FXML ComboBox<StringProperty> jobDescription;

    // Byte
    @FXML TextField _0x13;
    @FXML TextField _0x1a;
    @FXML TextField _0x1b;

    // Sprite
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



    
    private ObjectProperty<CharacterData> characterProperty = new SimpleObjectProperty<>();
    
    private ObservableList<Byte> unitPaletteList = FXCollections.observableArrayList();
    private ObservableList<Byte> enemyPaletteList = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        characterList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindCharacterData();
            characterProperty.setValue(newValue);
            if (newValue != null) bindCharacterData();
        });
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
        //unitPortrait.textProperty().addListener(new ShortChangeListener(unitPortrait));
        //enemyPortrait.textProperty().addListener(new ShortChangeListener(enemyPortrait));
        //jobName.textProperty().addListener(new ShortChangeListener(jobName));
        //jobDescription.textProperty().addListener(new ShortChangeListener(jobDescription));

        dialogueRole.textProperty().addListener(new ByteChangeListener(dialogueRole));
        _0x13.textProperty().addListener(new ByteChangeListener(_0x13));
        _0x1a.textProperty().addListener(new ByteChangeListener(_0x1a));
        _0x1b.textProperty().addListener(new ByteChangeListener(_0x1b));
    }

    private void unbindCharacterData() {
        unitPortrait.valueProperty().unbindBidirectional(characterProperty.getValue().unitPortrait);
        enemyPortrait.valueProperty().unbindBidirectional(characterProperty.getValue().enemyPortrait);
        //jobName.textProperty().unbindBidirectional(characterProperty.getValue().jobName);
        //jobDescription.textProperty().unbindBidirectional(characterProperty.getValue().jobDescription);

        dialogueRole.textProperty().unbindBidirectional(characterProperty.getValue().dialogueRole);
        _0x13.textProperty().unbindBidirectional(characterProperty.getValue()._0x13);
        _0x1a.textProperty().unbindBidirectional(characterProperty.getValue()._0x1a);
        _0x1b.textProperty().unbindBidirectional(characterProperty.getValue()._0x1b);

        characterProperty.getValue().name.unbind();
        characterProperty.getValue().jobName.unbind();
        characterProperty.getValue().jobDescription.unbind();
        gender.valueProperty().unbindBidirectional(characterProperty.getValue().gender);
        defaultJob.valueProperty().unbindBidirectional(characterProperty.getValue().defaultJob);
        
        unitSprite.valueProperty().unbindBidirectional(characterProperty.getValue().unitSprite);
        unitAlternateSprite.valueProperty().unbindBidirectional(characterProperty.getValue().unitAlternateSprite);
        enemySprite.valueProperty().unbindBidirectional(characterProperty.getValue().enemySprite);
        enemyAlternateSprite.valueProperty().unbindBidirectional(characterProperty.getValue().enemyAlternateSprite);
        unitPalette.valueProperty().unbindBidirectional(characterProperty.getValue().unitPalette);
        enemyPalette.valueProperty().unbindBidirectional(characterProperty.getValue().enemyPalette);
        
        unitTopSprite.valueProperty().unbindBidirectional(characterProperty.getValue().unitTopSprite);
        enemyTopSprite.valueProperty().unbindBidirectional(characterProperty.getValue().enemyTopSprite);
    }

    private void bindCharacterData() {
        //StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        
        unitPortrait.valueProperty().bindBidirectional(characterProperty.getValue().unitPortrait);
        enemyPortrait.valueProperty().bindBidirectional(characterProperty.getValue().enemyPortrait);
        
        StringConverter<Byte> unsignedByteConverter = new UnsignedByteStringConverter();
        Bindings.bindBidirectional(dialogueRole.textProperty(), characterProperty.getValue().dialogueRole, unsignedByteConverter);
        Bindings.bindBidirectional(_0x13.textProperty(), characterProperty.getValue()._0x13, unsignedByteConverter);
        Bindings.bindBidirectional(_0x1a.textProperty(), characterProperty.getValue()._0x1a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x1b.textProperty(), characterProperty.getValue()._0x1b, unsignedByteConverter);

        //name.valueProperty().bindBidirectional(characterProperty.getValue().name);
        name.getSelectionModel().select(Short.toUnsignedInt(characterProperty.getValue().name.getValue()));
        characterProperty.getValue().name.bind(new ObjectBinding<Short>() {
            {bind(name.valueProperty());}
            @Override
            protected Short computeValue() {
                return (short)App.characterNames.indexOf(name.valueProperty().getValue());
            }

        });


        jobName.getSelectionModel().select(characterProperty.getValue().jobName.getValue() == 0 ? 0 : Short.toUnsignedInt(characterProperty.getValue().jobName.getValue()) - 1000);
        characterProperty.getValue().jobName.bind(new ObjectBinding<Short>() {
            {bind(jobName.valueProperty());}
            @Override
            protected Short computeValue() {
                return (short)(App.jobNames.indexOf(jobName.valueProperty().getValue()) == 0 ? 0 : App.jobNames.indexOf(jobName.valueProperty().getValue()) + 1000);
            }

        });
        jobDescription.getSelectionModel().select(characterProperty.getValue().jobDescription.getValue() == 0 ? 0 : Short.toUnsignedInt(characterProperty.getValue().jobDescription.getValue()) - 38000);
        characterProperty.getValue().jobDescription.bind(new ObjectBinding<Short>() {
            {bind(jobDescription.valueProperty());}
            @Override
            protected Short computeValue() {
                return (short)(App.jobDescriptions.indexOf(jobDescription.valueProperty().getValue()) == 0 ? 0 : App.jobDescriptions.indexOf(jobDescription.valueProperty().getValue()) + 38000);
            }

        });

        gender.valueProperty().bindBidirectional(characterProperty.getValue().gender);
        defaultJob.valueProperty().bindBidirectional(characterProperty.getValue().defaultJob);

        unitSprite.valueProperty().bindBidirectional(characterProperty.getValue().unitSprite);
        unitAlternateSprite.valueProperty().bindBidirectional(characterProperty.getValue().unitAlternateSprite);
        enemySprite.valueProperty().bindBidirectional(characterProperty.getValue().enemySprite);
        enemyAlternateSprite.valueProperty().bindBidirectional(characterProperty.getValue().enemyAlternateSprite);
        unitPalette.valueProperty().bindBidirectional(characterProperty.getValue().unitPalette);
        enemyPalette.valueProperty().bindBidirectional(characterProperty.getValue().enemyPalette);
        
        unitTopSprite.valueProperty().bindBidirectional(characterProperty.getValue().unitTopSprite);
        enemyTopSprite.valueProperty().bindBidirectional(characterProperty.getValue().enemyTopSprite);
        
        unitSprite.setButtonCell(new JobSpriteCell(0, characterProperty.getValue().unitPalette.getValue(), 2));
        unitSprite.setCellFactory(x -> new JobSpriteCell(0, characterProperty.getValue().unitPalette.getValue(), 2));
        unitAlternateSprite.setButtonCell(new JobSpriteCell(0, characterProperty.getValue().unitPalette.getValue(), 2));
        unitAlternateSprite.setCellFactory(x -> new JobSpriteCell(0, characterProperty.getValue().unitPalette.getValue(), 2));

        enemySprite.setButtonCell(new JobSpriteCell(0, characterProperty.getValue().enemyPalette.getValue(), 2));
        enemySprite.setCellFactory(x -> new JobSpriteCell(0, characterProperty.getValue().enemyPalette.getValue(), 2));
        enemyAlternateSprite.setButtonCell(new JobSpriteCell(0, characterProperty.getValue().enemyPalette.getValue(), 2));
        enemyAlternateSprite.setCellFactory(x -> new JobSpriteCell(0, characterProperty.getValue().enemyPalette.getValue(), 2));
    }


    @FXML
    public void addCharacter() {
        if (characterList.getItems() != null) {
            int newIndex = characterList.getItems().size();
            characterList.getItems().add(new CharacterData(newIndex));
            characterList.getSelectionModel().selectLast();
        }
    }
 
    @FXML
    public void removeCharacter() {
        //if (characterList.getSelectionModel().getSelectedItem() != null && characterList.getSelectionModel().getSelectedIndex() > 0) {
        //    characterList.getItems().remove(characterList.getSelectionModel().getSelectedIndex());
        //}
        if (characterList.getItems().size() > 0){
            characterList.getItems().removeLast();
        }
    }

    public void loadCharacters() {
        if (App.archive != null) {

            ByteBuffer characterDataBytes = App.sysdata.getFile(0);

            if (characterDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            characterDataBytes.rewind();

            ObservableList<CharacterData> characterDataList = FXCollections.observableArrayList();


            //int numAbilitysets = abilitySetBytes.remaining() / 0xc;
            int numCharacters = Byte.toUnsignedInt(App.arm9.get(0x000cb018))+1;
            for (int i = 0; i < numCharacters; i++) {
                CharacterData character = new CharacterData(characterDataBytes, i);
                characterDataList.add(character);
            }
            App.characterList = characterDataList;
            characterDataBytes.rewind();


            characterList.setItems(characterDataList);
            characterList.setCellFactory(x -> new CharacterCell());
            
            defaultJob.setItems(App.jobDataList);
            defaultJob.setButtonCell(new JobCell());
            defaultJob.setCellFactory(x -> new JobCell());

            name.setItems(App.characterNames);
            name.setButtonCell(new StringPropertyCell());
            name.setCellFactory(x -> new StringPropertyCell());

            jobName.setItems(App.jobNames);
            jobName.setButtonCell(new StringPropertyCell());
            jobName.setCellFactory(x -> new StringPropertyCell());

            jobDescription.setItems(App.jobDescriptions);
            jobDescription.setButtonCell(new StringPropertyCell());
            jobDescription.setCellFactory(x -> new StringPropertyCell());
            
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

    public void saveCharacters() {
        List<CharacterData> characters = characterList.getItems();
        ByteBuffer newCharacterDataBytes = ByteBuffer.allocate(characters.size()*0x1C).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < characters.size(); i++) {
            newCharacterDataBytes.put(characters.get(i).toBytes());
        }
        newCharacterDataBytes.rewind();
        App.sysdata.setFile(0, newCharacterDataBytes);
        // Patch arm9 code with new Characters length
        App.arm9.put(0x000cb018, (byte)(characters.size()-1));
        App.arm9.put(0x000cb01c, (byte)(characters.size()-1));
    }

}
