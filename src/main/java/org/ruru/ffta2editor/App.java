package org.ruru.ffta2editor;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.IntStream;

import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.ability.ActiveAbilityData;
import org.ruru.ffta2editor.model.ability.SPAbilityData;
import org.ruru.ffta2editor.model.character.CharacterData;
import org.ruru.ffta2editor.model.equipment.EquipmentData;
import org.ruru.ffta2editor.model.job.AbilitySet;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.model.job.JobGroup;
import org.ruru.ffta2editor.utility.Archive;
import org.ruru.ffta2editor.utility.FFTA2Charset;
import org.ruru.ffta2editor.utility.IdxAndPak;
import org.ruru.ffta2editor.utility.UnitSprite;


// build exe: jpackage --type app-image --module org.ruru.ffta2editor/org.ruru.ffta2editor.App  -n ffta2-editor --runtime-image .\target\image\
/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Archive archive;
    public static IdxAndPak sysdata;
    public static IdxAndPak unitSsts;
    public static IdxAndPak unitCgs;
    public static IdxAndPak jdMessage;
    public static ByteBuffer arm9;
    public static ByteBuffer overlay11;


    public static ObservableList<UnitSprite> unitSprites;
    public static ObservableList<JobData> jobDataList;
    public static ObservableList<ActiveAbilityData> activeAbilityList;
    public static ObservableList<SPAbilityData> supportAbilityList;
    public static ObservableList<SPAbilityData> reactionAbilityList;
    public static ObservableList<AbilityData> abilityList;
    public static ObservableList<AbilitySet> abilitySetList;
    public static ObservableList<CharacterData> characterList;
    public static ObservableList<JobGroup> jobGroupList;
    public static ObservableList<EquipmentData> equipmentList;

    public static ObservableList<StringProperty> characterNames;
    public static ObservableList<StringProperty> jobNames;
    public static ObservableList<StringProperty> abilitySetNames;
    public static ObservableList<StringProperty> abilityNames;
    public static ObservableList<StringProperty> jobDescriptions;
    public static ObservableList<StringProperty> abilitySetDescriptions;
    public static ObservableList<StringProperty> abilityDescriptions;
    public static ObservableList<StringProperty> itemNames;
    public static ObservableList<StringProperty> itemDescriptions;
    public static ObservableList<StringProperty> bonusEffects;



    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main"), 1280, 720);
        stage.setScene(scene);
        stage.setTitle("FFTA2 Editor");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        //String s = FFTA2Charset.decode(ByteBuffer.wrap(new byte[] {0x09, 0x20, 0x27, 0x27, 0x2A, -63, 0x00, 0x18, 0x2A, 0x2D, 0x27, 0x1F, 0x73}).order(ByteOrder.LITTLE_ENDIAN));
        //try {
        //    String s = "Hello\nWorld!<~><end>";
        //    System.out.println(s);
        //    byte[] bytes = FFTA2Charset.encode(s);
        //    StringBuilder sb = new StringBuilder();
        //    for (byte b : bytes) {
        //        sb.append(String.format("%02X ", b));
        //    }
        //    System.out.println(sb.toString());
        //    System.out.println(FFTA2Charset.decode(ByteBuffer.wrap(bytes)));
        //    System.out.println(IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).filter(b -> b == (byte)0xC0 || b == (byte)0xC2).mapToInt(b -> (int)b).count());
        //} catch (Exception e) {
        //    System.err.println(e);
        //}
        launch();
    }

}
