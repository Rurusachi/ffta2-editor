package org.ruru.ffta2editor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.ability.ActiveAbilityData;
import org.ruru.ffta2editor.model.ability.SPAbilityData;
import org.ruru.ffta2editor.model.auction.AuctionPrizeTable;
import org.ruru.ffta2editor.model.bazaar.BazaarRecipe;
import org.ruru.ffta2editor.model.character.CharacterData;
import org.ruru.ffta2editor.model.formation.FormationData;
import org.ruru.ffta2editor.model.item.ConsumableData;
import org.ruru.ffta2editor.model.item.EquipmentData;
import org.ruru.ffta2editor.model.item.ItemData;
import org.ruru.ffta2editor.model.item.LootData;
import org.ruru.ffta2editor.model.job.AbilitySet;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.model.job.JobGroup;
import org.ruru.ffta2editor.model.topSprite.TopSprite;
import org.ruru.ffta2editor.model.unitFace.UnitFace;
import org.ruru.ffta2editor.model.unitSst.UnitSst;
import org.ruru.ffta2editor.utility.Archive;
import org.ruru.ffta2editor.utility.IdxAndPak;
import org.ruru.ffta2editor.utility.UnitSprite;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
    public static IdxAndPak jhQuest;
    public static IdxAndPak jhRumor;
    public static IdxAndPak jhNotice;
    public static IdxAndPak evMsg;
    public static IdxAndPak entrydata;
    public static IdxAndPak atl;
    public static IdxAndPak face;
    public static ByteBuffer arm9;
    public static ByteBuffer overlay8;
    public static ByteBuffer overlay11;
    public static ByteBuffer naUnitAnimTable;


    public static ObservableList<UnitSprite> unitSprites;
    public static ObservableList<TopSprite> topSprites;
    public static ObservableList<UnitFace> unitFaces;
    public static ObservableList<JobData> jobDataList;
    public static ObservableList<ActiveAbilityData> activeAbilityList;
    public static ObservableList<SPAbilityData> passiveAbilityList;
    public static ObservableList<SPAbilityData> reactionAbilityList;
    public static ObservableList<AbilityData> abilityList;
    public static ObservableList<AbilitySet> abilitySetList;
    public static ObservableList<CharacterData> characterList;
    public static ObservableList<JobGroup> jobGroupList;
    public static ObservableList<EquipmentData> equipmentList;
    public static ObservableList<ConsumableData> consumableList;
    public static ObservableList<LootData> lootList;
    public static ObservableList<ItemData> itemList;
    public static ObservableList<FormationData> formationList;
    public static ObservableList<BazaarRecipe> bazaarRecipeList;
    public static ObservableList<UnitSst> unitSstList;
    public static ObservableList<AuctionPrizeTable> auctionPrizeTableList;
    public static ObservableList<AuctionPrizeTable> auctionGrandPrizeTableList;

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
    public static ObservableList<StringProperty> lawNames;
    public static ObservableList<StringProperty> questNames;
    public static ObservableList<StringProperty> questDescriptions;
    public static ObservableList<StringProperty> regionNames;
    public static ObservableList<StringProperty> locationNames;
    public static ObservableList<StringProperty> bazaarSetNames;
    public static ObservableList<StringProperty> bazaarSetDescriptions;
    public static ObservableList<StringProperty> noticeNames;
    public static ObservableList<StringProperty> rumorNames;
    public static Map<Integer, StringProperty> evMsgNames;
    

    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");
    private static final Path logPath = Path.of("logs");
    private static final Path configPath = Path.of("ffta2-editor.properties");
    private static final Properties properties = new Properties();
    public static final Properties config = new Properties();

    @Override
    public void start(Stage stage) throws IOException {
        if (!logPath.toFile().exists()) {
            Files.createDirectory(logPath);
        }
        if (!configPath.toFile().exists()) {
            configPath.toFile().createNewFile();
            config.load(new FileInputStream(configPath.toFile()));
        } else {
            config.load(new FileInputStream(configPath.toFile()));
        }
        FileHandler fh = new FileHandler(String.format("logs/%s.log", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))));
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
        logger.setLevel(Level.ALL);
        Logger globalLogger = Logger.getLogger("");
        Handler[] handlers = globalLogger.getHandlers();
        for (Handler handler : handlers) {
            globalLogger.removeHandler(handler);
        }
        properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
        
        scene = new Scene(loadFXML("main"), 1280, 720);
        stage.setScene(scene);
        stage.setTitle(String.format("FFTA2 Editor %s", properties.getProperty("version")));
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
        launch();
        try {
            config.store(new FileOutputStream(configPath.toFile()), "");
        } catch (Exception e) {
            System.err.println(e);
            logger.log(Level.WARNING, "Failed to save config", e);
        }
    }

}
