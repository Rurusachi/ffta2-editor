package org.ruru.ffta2editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ruru.ffta2editor.utility.Archive;
import org.ruru.ffta2editor.utility.IdxAndPak;
import org.ruru.ffta2editor.utility.LZSS;
import org.ruru.ffta2editor.utility.Archive.ArchiveEntry;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class MainController {

    @FXML AnchorPane abilityTab;
    @FXML AbilityController abilityTabController;

    @FXML AnchorPane patchesTab;
    @FXML PatchesController patchesTabController;

    @FXML AnchorPane spritesTab;
    @FXML SpritesController spritesTabController;

    @FXML AnchorPane jobTab;
    @FXML JobController jobTabController;

    @FXML AnchorPane textTab;
    @FXML TextController textTabController;

    @FXML AnchorPane jobRequirementTab;
    @FXML JobRequirementController jobRequirementTabController;

    @FXML AnchorPane jobGroupTab;
    @FXML JobGroupController jobGroupTabController;

    @FXML AnchorPane characterTab;
    @FXML CharacterController characterTabController;

    @FXML AnchorPane formationTab;
    @FXML FormationController formationTabController;

    @FXML AnchorPane questTab;
    @FXML QuestController questTabController;

    @FXML AnchorPane bazaarTab;
    @FXML BazaarController bazaarTabController;

    @FXML AnchorPane auctionTab;
    @FXML AuctionController auctionTabController;

    @FXML AnchorPane equipmentTab;
    @FXML EquipmentController equipmentTabController;
    
    @FXML MenuItem saveMenuItem;

    File romFile;
    ObjectProperty<File> lastSavePath = new SimpleObjectProperty<>();
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    @FXML
    public void initialize() {
        saveMenuItem.disableProperty().bind(lastSavePath.isNull());
    }

    //public static HashMap<String, Pair<String,String>> idxPakMap = new HashMap<>();
    //static {
    //    idxPakMap.put("sysdata", new Pair<>("system/rom/sysdata_rom.idx", "system/rom/sysdata.pak"));
    //    idxPakMap.put("unitSsts", new Pair<>("char/rom/rom_idx/UnitSst.rom_idx", "char/rom/pak/UnitSst.pak"));
    //    idxPakMap.put("unitCgs", new Pair<>("char/rom/rom_idx/UnitCg.rom_idx", "char/rom/pak/UnitCg.pak"));
    //    idxPakMap.put("jdMessage", new Pair<>(String.format("system/rom/JD_message_rom_%d.idx", 0), String.format("system/rom/JD_message_%d.pak", 0)));
    //    idxPakMap.put("jhQuest", new Pair<>(String.format("system/rom/JH_questtext_rom_%d.idx", 0), String.format("system/rom/JH_questtext_%d.pak", 0)));
    //    idxPakMap.put("jhRumor", new Pair<>(String.format("system/rom/JH_uwasatext_rom_%d.idx", 0), String.format("system/rom/JH_uwasatext_%d.pak", 0)));
    //    idxPakMap.put("jhNotice", new Pair<>(String.format("system/rom/JH_freepapermes_rom_%d.idx", 0), String.format("system/rom/JH_freepapermes_%d.pak", 0)));
    //    idxPakMap.put("evMsg", new Pair<>(String.format("event/rom/ev_msg%d_rom.idx", 0), String.format("event/rom/ev_msg%d.pak", 0)));
    //    idxPakMap.put("entrydata", new Pair<>("system/rom/entrydata_rom.idx", "system/rom/entrydata.pak"));
    //    idxPakMap.put("atl", new Pair<>("menu/atl_rom/atl_rom.idx", "menu/atl_rom/atl.pak"));
    //    idxPakMap.put("face", new Pair<>("menu/face_rom/face_rom.idx", "menu/face_rom/face.pak"));
    //}

    public static class IdxPaks {
        public static record IdxPakPaths(String idx, String pak){};
        public static IdxPakPaths sysdata = new IdxPakPaths("system/rom/sysdata_rom.idx", "system/rom/sysdata.pak");
        public static IdxPakPaths unitSsts = new IdxPakPaths("char/rom/rom_idx/UnitSst.rom_idx", "char/rom/pak/UnitSst.pak");
        public static IdxPakPaths unitCgs = new IdxPakPaths("char/rom/rom_idx/UnitCg.rom_idx", "char/rom/pak/UnitCg.pak");
        public static IdxPakPaths jdMessage = new IdxPakPaths(String.format("system/rom/JD_message_rom_%d.idx", 0), String.format("system/rom/JD_message_%d.pak", 0));
        public static IdxPakPaths jhQuest = new IdxPakPaths(String.format("system/rom/JH_questtext_rom_%d.idx", 0), String.format("system/rom/JH_questtext_%d.pak", 0));
        public static IdxPakPaths jhRumor = new IdxPakPaths(String.format("system/rom/JH_uwasatext_rom_%d.idx", 0), String.format("system/rom/JH_uwasatext_%d.pak", 0));
        public static IdxPakPaths jhNotice = new IdxPakPaths(String.format("system/rom/JH_freepapermes_rom_%d.idx", 0), String.format("system/rom/JH_freepapermes_%d.pak", 0));
        public static IdxPakPaths evMsg = new IdxPakPaths(String.format("event/rom/ev_msg%d_rom.idx", 0), String.format("event/rom/ev_msg%d.pak", 0));
        public static IdxPakPaths entrydata = new IdxPakPaths("system/rom/entrydata_rom.idx", "system/rom/entrydata.pak");
        public static IdxPakPaths atl = new IdxPakPaths("menu/atl_rom/atl_rom.idx", "menu/atl_rom/atl.pak");
        public static IdxPakPaths face = new IdxPakPaths("menu/face_rom/face_rom.idx", "menu/face_rom/face.pak");
    }
    //ArrayList<Pair<String,String>> idxPakPaths = new ArrayList<>() {{
    //    new Pair<>("system/rom/sysdata_rom.idx", "system/rom/sysdata.pak");
    //    new Pair<>("char/rom/rom_idx/UnitSst.rom_idx", "char/rom/pak/UnitSst.pak");
    //    new Pair<>("char/rom/rom_idx/UnitCg.rom_idx", "char/rom/pak/UnitCg.pak");
    //    new Pair<>(String.format("system/rom/JD_message_rom_%d.idx", 0), String.format("system/rom/JD_message_%d.pak", 0));
    //    new Pair<>(String.format("system/rom/JH_questtext_rom_%d.idx", 0), String.format("system/rom/JH_questtext_%d.pak", 0));
    //    new Pair<>(String.format("system/rom/JH_uwasatext_rom_%d.idx", 0), String.format("system/rom/JH_uwasatext_%d.pak", 0));
    //    new Pair<>(String.format("system/rom/JH_freepapermes_rom_%d.idx", 0), String.format("system/rom/JH_freepapermes_%d.pak", 0));
    //    new Pair<>(String.format("event/rom/ev_msg%d_rom.idx", 0), String.format("event/rom/ev_msg%d.pak", 0));
    //    new Pair<>("system/rom/entrydata_rom.idx", "system/rom/entrydata.pak");
    //    new Pair<>("menu/atl_rom/atl_rom.idx", "menu/atl_rom/atl.pak");
    //    new Pair<>("menu/face_rom/face_rom.idx", "menu/face_rom/face.pak");
    //}};

    private void compareIdxPaks(IdxAndPak original, IdxAndPak repacked, String name) throws Exception {

        for (int i = 0; i < original.numFiles() && i < repacked.numFiles(); i++) {
            if (name == "sysdata" && (i == 2 || i == 3)) continue;
            ByteBuffer oldFile = original.getFile(i);
            ByteBuffer newFile = repacked.getFile(i);
            if (oldFile == null || newFile == null) {
                if (oldFile != newFile) throw new Exception(String.format("File %d in %s is null", i, name));
                continue;
            }
            int mismatch = oldFile.rewind().mismatch(newFile.rewind());
            if (mismatch != -1) {
                if (oldFile.rewind().remaining() != newFile.rewind().remaining()) {
                    logger.warning(String.format("File %d in %s not equal size (%d != %d)", i, name, oldFile.remaining(), newFile.remaining()));
                } else {
                    throw new Exception(String.format("File %d in %s has changed at %d", i, name, mismatch));
                }
            }
        }
    }

    private void compareAfterSave() throws Exception {
        //ArrayList<ArchiveEntry> originalFiles = new ArrayList<>();
        //for (ArchiveEntry entry : App.archive.files) {
        //    originalFiles.add(entry.copy());
        //}

        IdxAndPak sysdata = new IdxAndPak(App.archive.getFile(IdxPaks.sysdata.idx()), App.archive.getFile(IdxPaks.sysdata.pak()));

        IdxAndPak unitSsts = new IdxAndPak(App.archive.getFile(IdxPaks.unitSsts.idx()), App.archive.getFile(IdxPaks.unitSsts.pak()));

        IdxAndPak unitCgs = new IdxAndPak(App.archive.getFile(IdxPaks.unitCgs.idx()), App.archive.getFile(IdxPaks.unitCgs.pak()));
        
        IdxAndPak jdMessage = new IdxAndPak(App.archive.getFile(IdxPaks.jdMessage.idx()), App.archive.getFile(IdxPaks.jdMessage.pak()));

        IdxAndPak jhQuest = new IdxAndPak(App.archive.getFile(IdxPaks.jhQuest.idx()), App.archive.getFile(IdxPaks.jhQuest.pak()));
        
        IdxAndPak jhRumor = new IdxAndPak(App.archive.getFile(IdxPaks.jhRumor.idx()), App.archive.getFile(IdxPaks.jhRumor.pak()));
        
        IdxAndPak jhNotice = new IdxAndPak(App.archive.getFile(IdxPaks.jhNotice.idx()), App.archive.getFile(IdxPaks.jhNotice.pak()));
        
        IdxAndPak evMsg = new IdxAndPak(App.archive.getFile(IdxPaks.evMsg.idx()), App.archive.getFile(IdxPaks.evMsg.pak()));

        IdxAndPak entrydata = new IdxAndPak(App.archive.getFile(IdxPaks.entrydata.idx()), App.archive.getFile(IdxPaks.entrydata.pak()));
        
        IdxAndPak atl = new IdxAndPak(App.archive.getFile(IdxPaks.atl.idx()), App.archive.getFile(IdxPaks.atl.pak()));
        
        IdxAndPak face = new IdxAndPak(App.archive.getFile(IdxPaks.face.idx()), App.archive.getFile(IdxPaks.face.pak()));


        Pair<ByteBuffer, ByteBuffer> repackedBytes = App.sysdata.repack();
        IdxAndPak repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(sysdata, App.sysdata, "sysdata");

        repackedBytes = App.unitSsts.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(unitSsts, App.unitSsts, "unitSsts");
        
        repackedBytes = App.unitCgs.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(unitCgs, App.unitCgs, "unitCgs");
        
        repackedBytes = App.jdMessage.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(jdMessage, App.jdMessage, "jdMessage");
        
        repackedBytes = App.jhQuest.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(jhQuest, App.jhQuest, "jhQuest");
        
        repackedBytes = App.jhRumor.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(jhRumor, App.jhRumor, "jhRumor");
        
        repackedBytes = App.jhNotice.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(jhNotice, App.jhNotice, "jhNotice");
        
        repackedBytes = App.evMsg.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(evMsg, App.evMsg, "evMsg");
        
        repackedBytes = App.entrydata.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(entrydata, App.entrydata, "entrydata");
        
        repackedBytes = App.atl.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(atl, App.atl, "atl");
        
        repackedBytes = App.face.repack();
        repacked = new IdxAndPak(repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(face, App.face, "face");

        //App.sysdata.files.get(0).compareTo(null)
        //App.sysdata
        //App.unitSsts
        //App.unitCgs
        //App.jdMessage
        //App.jhQuest
        //App.jhRumor
        //App.jhNotice
        //App.evMsg
        //App.entrydata
        //App.atl
        //App.face

    }

    @FXML
    private void openFileSelector() {

        setDim(true);
        FileChooser chooser = new FileChooser();
        try {
            File lastPath = Path.of(App.config.getProperty("lastPath")).toFile();
            if (!lastPath.exists()) throw new FileNotFoundException("Last path doesn't exist");
            chooser.setInitialDirectory(lastPath);
        } catch (Exception e) {
            App.config.setProperty("lastPath", System.getProperty("user.dir"));
            chooser.setInitialDirectory(Path.of(System.getProperty("user.dir")).toFile());
            System.err.println(e);
        }
        
        chooser.setTitle("Open Directory");
        File loadPath = chooser.showOpenDialog(abilityTab.getScene().getWindow());
        if (loadPath == null) {
            setDim(false);
            return;
        }
        App.config.setProperty("lastPath", loadPath.getParent());
        lastSavePath.set(loadPath);
        try {
            load(loadPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Failed to load ROM"), e);
            System.err.println(e);
            Alert loadAlert = new Alert(AlertType.ERROR);
            loadAlert.setTitle("Loading");
            loadAlert.setHeaderText("Loading failed");
            loadAlert.setContentText(e.toString());
            loadAlert.show();
        }
        setDim(false);

        //if (!load(loadPath)) {
        //    Alert loadAlert = new Alert(AlertType.ERROR);
        //    loadAlert.setTitle("Loading");
        //    loadAlert.setHeaderText("Loading failed");
        //    //saveAlert.setDialogPane(new DialogPane());
        //    loadAlert.show();
        //}

        
        //Alert loadAlert = new Alert(AlertType.INFORMATION);
        //loadAlert.setTitle("Loading");
        //loadAlert.setHeaderText("Loading. Please wait.");
        ////saveAlert.setDialogPane(new DialogPane());
        //loadAlert.show();
        //CompletableFuture.supplyAsync(() -> load(loadPath)).thenAccept(success -> {
        //    if (success) {
        //        Platform.runLater(() -> loadAlert.setContentText("Loaded"));
        //    } else {
        //        Platform.runLater(() -> loadAlert.setContentText("Failed to load"));
        //    }
        //});

        /*
        HashSet<AbilityData> usedAbilities = new HashSet<>();
        for (var formation : App.formationList) {
            for (var unit : formation.units) {
                usedAbilities.add(unit.primaryAbility1.getValue());
                usedAbilities.add(unit.primaryAbility2.getValue());
                usedAbilities.add(unit.primaryAbility3.getValue());
                usedAbilities.add(unit.primaryAbility4.getValue());
                usedAbilities.add(unit.primaryAbility5.getValue());
                usedAbilities.add(unit.primaryAbility6.getValue());
                usedAbilities.add(unit.secondaryAbility1.getValue());
                usedAbilities.add(unit.secondaryAbility2.getValue());
                usedAbilities.add(unit.secondaryAbility3.getValue());
                usedAbilities.add(unit.secondaryAbility4.getValue());
            }
        }
        for (var abilitySet : App.abilitySetList) {
            for (var ability : abilitySet.abilities) {
                usedAbilities.add(ability.ability.getValue());
            }
        }
        for (var item : App.equipmentList) {
            usedAbilities.add(item.ability1.getValue());
            usedAbilities.add(item.ability2.getValue());
            usedAbilities.add(item.ability3.getValue());
        }
        for (var item : App.consumableList) {
            usedAbilities.add(item.ability.getValue());
        }

        HashSet<AbilityData> unusedAbilities = new HashSet<>(App.activeAbilityList);
        unusedAbilities.removeAll(usedAbilities);
        System.out.println("Unused ability ids:");
        unusedAbilities.stream().sorted(Comparator.comparingInt(ability -> ability.id)).forEachOrdered(ability -> System.out.println(String.format("%X: %s", ability.id, ability.name.getValue())));
        */

        //for (AbilityData ability : unusedAbilities) {
        //    System.out.println(String.format("%X: %s", ability.id, ability.name.getValue()));
        //}

        
        /*
        int oldMinPieces = App.faceSprites.stream().skip(1).mapToInt(x -> x.pieces.length).min().getAsInt();
        int oldMaxPieces = App.faceSprites.stream().skip(1).mapToInt(x -> x.pieces.length).max().getAsInt();
        double oldAveragePieces = App.faceSprites.stream().skip(1).mapToInt(x -> x.pieces.length).average().getAsDouble();
        ArrayList<Pair<Integer,Integer>> sizes = new ArrayList<>();
        for(int i = 1; i < App.faceSprites.size(); i++) {
            UnitFace face = App.faceSprites.get(i);
            var oldTexture = face.image;
            int excessBytes = face.setTexture(face.getImage());
            if (excessBytes > 0) {
                System.err.println(String.format("face %d too big (%d bytes too large)", i, excessBytes));
            }
            var newTexture = face.image;
            sizes.add(new Pair<Integer,Integer>(oldTexture.length, newTexture.length));
        }
        int maxDifference = sizes.stream().mapToInt(x -> x.getValue() - x.getKey()).max().getAsInt();
        int minDifference = sizes.stream().mapToInt(x -> x.getValue() - x.getKey()).min().getAsInt();
        double oldAverage = sizes.stream().mapToInt(x -> x.getKey()).average().getAsDouble();
        double newAverage = sizes.stream().mapToInt(x -> x.getValue()).average().getAsDouble();

        int oldMax = sizes.stream().mapToInt(x -> x.getKey()).max().getAsInt();
        int newMax = sizes.stream().mapToInt(x -> x.getValue()).max().getAsInt();

        
        int oldCombined = sizes.stream().mapToInt(x -> x.getKey()).sum();
        int newCombined = sizes.stream().mapToInt(x -> x.getValue()).sum();
        int newMinPieces = App.faceSprites.stream().skip(1).mapToInt(x -> x.pieces.length).min().getAsInt();
        int newMaxPieces = App.faceSprites.stream().skip(1).mapToInt(x -> x.pieces.length).max().getAsInt();
        double newAveragePieces = App.faceSprites.stream().skip(1).mapToInt(x -> x.pieces.length).average().getAsDouble();

        System.out.println(String.format("maxDifference: %d", maxDifference));
        System.out.println(String.format("minDifference: %d", minDifference));
        System.out.println(String.format("oldAverage: %f", oldAverage));
        System.out.println(String.format("newAverage: %f", newAverage));
        System.out.println(String.format("oldMax: %d", oldMax));
        System.out.println(String.format("newMax: %d", newMax));
        System.out.println(String.format("oldCombined: %d", oldCombined));
        System.out.println(String.format("newCombined: %d", newCombined));
        System.out.println(String.format("oldAveragePieces: %f", oldAveragePieces));
        System.out.println(String.format("newAveragePieces: %f", newAveragePieces));
        System.out.println(String.format("oldPieces: min %d max %d", oldMinPieces, oldMaxPieces));
        System.out.println(String.format("newPieces: min %d max %d", newMinPieces, newMaxPieces));

        
        IntStream.range(0, sizes.size()).filter(i -> sizes.get(i).getValue() > 8192).forEach(i -> System.out.println(String.format("Too big: %d (%d bytes)", i+1, sizes.get(i).getValue())));
        */



        //try {
        //    UnitSprite testSprite = new UnitSprite(testData, testPalette, soldierCg);
        //    for (int pal = 0; pal < testSprite.spritePalettes.palettes.size(); pal++) {
        //        for (int pose = 0; pose < testData.spriteMaps.size(); pose++) {
        //            BufferedImage fullImage = testSprite.getSprite(pose, pal);
        //            Path filePath = Path.of(String.format("G:\\sprites\\unit%03d\\pal%d\\%d.png", i, pal, pose));
        //            Files.createDirectories(filePath.getParent());
        //            ImageIO.write(fullImage, "png", filePath.toFile());
        //        }
        //    }
        //} catch (Exception e) {
        //    System.err.println(e);
        //}

        //new Alert(AlertType.INFORMATION, "Loaded").showAndWait();
    }

    private void load(File loadPath) throws Exception {
        logger.info("Unpacking rom with ndstool");
        romFile = loadPath;
        Path dataPath = Path.of("data");
        ProcessBuilder ndsTool = new ProcessBuilder("ndstool.exe", "-x", loadPath.toPath().toString(),
                                               "-9", dataPath.resolve("arm9.bin").toString(),
                                               "-7", dataPath.resolve("arm7.bin").toString(),
                                               "-y9", dataPath.resolve("y9.bin").toString(),
                                               "-y7", dataPath.resolve("y7.bin").toString(),
                                               "-d", dataPath.resolve("data").toString(),
                                               "-y", dataPath.resolve("overlay").toString(),
                                               "-t", dataPath.resolve("banner.bin").toString(),
                                               "-h", dataPath.resolve("header.bin").toString());
        ndsTool.redirectOutput(Redirect.INHERIT);
        ndsTool.redirectError(Redirect.INHERIT);
        Files.createDirectories(dataPath);
        ndsTool.start().waitFor();




         
        //DirectoryChooser chooser = new DirectoryChooser();
        //chooser.setTitle("Open Directory");
        //File file = chooser.showDialog(abilityTab.getScene().getWindow());
        //if (file == null) return;
        
        //File file = Path.of("C:\\Users\\Ruru\\Documents\\GitHub\\ffta2-editor").toFile();
        logger.info("Parsing archive");
        File pcIdx = dataPath.resolve("data\\master\\pc.idx").toFile();
        File pcBin = dataPath.resolve("data\\master\\pc.bin").toFile();
        
        App.archive = new Archive(pcIdx, pcBin);
        FileInputStream arm9 = new FileInputStream(dataPath.resolve("arm9.bin").toFile());
        App.arm9 = ByteBuffer.wrap(arm9.readAllBytes()).order(ByteOrder.LITTLE_ENDIAN);
        arm9.close();
        FileInputStream overlay11 = new FileInputStream(dataPath.resolve("overlay\\overlay_0011.bin").toFile());
        App.overlay11 = ByteBuffer.wrap(overlay11.readAllBytes()).order(ByteOrder.LITTLE_ENDIAN);
        overlay11.close();
        FileInputStream overlay8 = new FileInputStream(dataPath.resolve("overlay\\overlay_0008.bin").toFile());
        App.overlay8 = ByteBuffer.wrap(overlay8.readAllBytes()).order(ByteOrder.LITTLE_ENDIAN);
        overlay8.close();


        
        logger.info("Parsing sysdata");
        App.sysdata = new IdxAndPak(App.archive.getFile(IdxPaks.sysdata.idx()), App.archive.getFile(IdxPaks.sysdata.pak()));
        
        logger.info("Parsing UnitSst");
        App.unitSsts = new IdxAndPak(App.archive.getFile(IdxPaks.unitSsts.idx()), App.archive.getFile(IdxPaks.unitSsts.pak()));

        logger.info("Parsing UnitCg");
        App.unitCgs = new IdxAndPak(App.archive.getFile(IdxPaks.unitCgs.idx()), App.archive.getFile(IdxPaks.unitCgs.pak()));
        
        logger.info("Parsing JD_message");
        App.jdMessage = new IdxAndPak(App.archive.getFile(IdxPaks.jdMessage.idx()), App.archive.getFile(IdxPaks.jdMessage.pak()));

        logger.info("Parsing JH_questtext");
        App.jhQuest = new IdxAndPak(App.archive.getFile(IdxPaks.jhQuest.idx()), App.archive.getFile(IdxPaks.jhQuest.pak()));
        
        logger.info("Parsing JH_uwasatext");
        App.jhRumor = new IdxAndPak(App.archive.getFile(IdxPaks.jhRumor.idx()), App.archive.getFile(IdxPaks.jhRumor.pak()));
        
        logger.info("Parsing JH_freepapermes");
        App.jhNotice = new IdxAndPak(App.archive.getFile(IdxPaks.jhNotice.idx()), App.archive.getFile(IdxPaks.jhNotice.pak()));
        
        logger.info("Parsing ev_msg");
        App.evMsg = new IdxAndPak(App.archive.getFile(IdxPaks.evMsg.idx()), App.archive.getFile(IdxPaks.evMsg.pak()));

        logger.info("Parsing entrydata");
        App.entrydata = new IdxAndPak(App.archive.getFile(IdxPaks.entrydata.idx()), App.archive.getFile(IdxPaks.entrydata.pak()));
        
        logger.info("Parsing atl");
        App.atl = new IdxAndPak(App.archive.getFile(IdxPaks.atl.idx()), App.archive.getFile(IdxPaks.atl.pak()));
        
        logger.info("Parsing face");
        App.face = new IdxAndPak(App.archive.getFile(IdxPaks.face.idx()), App.archive.getFile(IdxPaks.face.pak()));


        logger.info("Decoding NaUnitAnimTable");
        var animTable = App.archive.getFile("char/NaUnitAnimTable.bin");
        App.naUnitAnimTable = LZSS.decode(animTable.position(4)).decodedData;


        

        logger.info("Loading Patches");
        patchesTabController.loadPatches();
        logger.info("Loading Text");
        textTabController.loadMessages();
        logger.info("Loading Abilities");
        abilityTabController.loadAbilities();
        logger.info("Loading Sprites");
        spritesTabController.loadSprites();
        logger.info("Loading Jobs");
        jobTabController.loadJobs();
        logger.info("Loading Characters");
        characterTabController.loadCharacters();
        logger.info("Loading Job Groups");
        jobGroupTabController.loadJobGroups();
        logger.info("Loading Job Requirements");
        jobRequirementTabController.loadJobRequirements();
        logger.info("Loading Equipment");
        equipmentTabController.loadEquipment();
        logger.info("Loading Formations");
        formationTabController.loadFormations();
        logger.info("Loading Quests");
        questTabController.loadQuests();
        logger.info("Loading Bazaar");
        bazaarTabController.loadBazaar();
        logger.info("Loading Auctions");
        auctionTabController.loadAuctions();
    }

    private ColorAdjust dimEffect = new ColorAdjust();

    @FXML
    private void setDim(boolean dim) {
        //opaqueLayer.setMinSize(opaqueLayer.getScene().getWidth(), opaqueLayer.getScene().getHeight());
        //opaqueLayer.setVisible(dim);
        if (dim) {
            dimEffect.setBrightness(-0.5);
        } else {
            dimEffect.setBrightness(0);
        }
        abilityTab.getScene().getRoot().setEffect(dimEffect);
    }

    @FXML void saveToLast() {
        if (romFile == null || lastSavePath.get() == null) return;
        setDim(true);
        Alert saveAlert = new Alert(AlertType.CONFIRMATION);
        saveAlert.setTitle(String.format("Save"));
        saveAlert.setHeaderText(String.format("Save to \"%s\"?", lastSavePath.get().getPath()));
        var result = saveAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
            saveTo(lastSavePath.get());
    }

    @FXML
    private void saveFileSelector() {
        if (romFile == null) return;
        setDim(true);
        FileChooser chooser = new FileChooser();
        try {
            File lastPath = Path.of(App.config.getProperty("lastPath")).toFile();
            if (!lastPath.exists()) throw new FileNotFoundException("Last path doesn't exist");
            chooser.setInitialDirectory(lastPath);
        } catch (Exception e) {
            App.config.setProperty("lastPath", System.getProperty("user.dir"));
            chooser.setInitialDirectory(Path.of(System.getProperty("user.dir")).toFile());
            System.err.println(e);
        }

        chooser.setTitle("Save as");
        chooser.setInitialFileName(romFile.getName());
        File savePath = chooser.showSaveDialog(abilityTab.getScene().getWindow());
        if (savePath == null) {
            setDim(false);
            return;
        }
        App.config.setProperty("lastPath", savePath.getParent());
        lastSavePath.set(savePath);

        saveTo(savePath);
    }

    private void saveTo(File savePath) {
        Alert saveAlert = new Alert(AlertType.NONE);
        saveAlert.setTitle(String.format("Saving to %s", savePath.getPath()));
        saveAlert.setHeaderText("Saving. Please wait.");
        saveAlert.show();
        CompletableFuture.runAsync(() -> {
            try {
                save(savePath);
            } catch (Exception e) {
                Platform.runLater(() -> {
                    logger.log(Level.SEVERE, String.format("Failed to save ROM"), e);
                    setDim(false);
                    saveAlert.setAlertType(AlertType.ERROR);
                    saveAlert.setHeaderText("Failed to save");
                    saveAlert.setContentText(e.getMessage());
                    saveAlert.getDialogPane().getScene().getWindow().sizeToScene();
                });
                return;
            }
            Platform.runLater(() -> {
                setDim(false);
                saveAlert.setAlertType(AlertType.INFORMATION);
                saveAlert.setHeaderText("Saved");
            });
        });
    }

    private void save(File savePath) throws Exception {
        //patchesTabController.applyPatches();
        logger.info("Saving Text");
        textTabController.saveMessages();
        logger.info("Saving Abilities");
        abilityTabController.saveAbilities();
        logger.info("Saving Sprites");
        jobTabController.saveJobs();
        logger.info("Saving Jobs");
        spritesTabController.saveSprites();
        logger.info("Saving Characters");
        characterTabController.saveCharacters();
        logger.info("Saving Job Groups");
        jobGroupTabController.saveJobGroups();
        logger.info("Saving Job Requirements");
        jobRequirementTabController.saveJobRequirements();
        logger.info("Saving Equipment");
        equipmentTabController.saveEquipment();
        logger.info("Saving Formations");
        formationTabController.saveFormations();
        logger.info("Saving Quests");
        questTabController.saveQuests();
        logger.info("Saving Bazaar");
        bazaarTabController.saveBazaar();
        logger.info("Saving Auctions");
        auctionTabController.saveAuctions();

        //compareAfterSave();

        // Repack sub-archives
        logger.info("Repacking sysdata");
        Pair<ByteBuffer, ByteBuffer> sysdataIdxPak = App.sysdata.repack();
        App.archive.setFile(IdxPaks.sysdata.idx(), sysdataIdxPak.getKey());
        App.archive.setFile(IdxPaks.sysdata.pak(), sysdataIdxPak.getValue());

        logger.info("Repacking UnitSst");
        Pair<ByteBuffer, ByteBuffer> sstIdxPak = App.unitSsts.repack();
        App.archive.setFile(IdxPaks.unitSsts.idx(), sstIdxPak.getKey());
        App.archive.setFile(IdxPaks.unitSsts.pak(), sstIdxPak.getValue());
        
        logger.info("Repacking UnitCg");
        Pair<ByteBuffer, ByteBuffer> cgIdxPak = App.unitCgs.repack();
        App.archive.setFile(IdxPaks.unitCgs.idx(), cgIdxPak.getKey());
        App.archive.setFile(IdxPaks.unitCgs.pak(), cgIdxPak.getValue());
        
        logger.info("Repacking JD_message");
        Pair<ByteBuffer, ByteBuffer> jdMessageIdxPak = App.jdMessage.repack();
        App.archive.setFile(IdxPaks.jdMessage.idx(), jdMessageIdxPak.getKey());
        App.archive.setFile(IdxPaks.jdMessage.pak(), jdMessageIdxPak.getValue());
        
        logger.info("Repacking JH_questtext");
        Pair<ByteBuffer, ByteBuffer> jhQuestIdxPak = App.jhQuest.repack();
        App.archive.setFile(IdxPaks.jhQuest.idx(), jhQuestIdxPak.getKey());
        App.archive.setFile(IdxPaks.jhQuest.pak(), jhQuestIdxPak.getValue());
        
        logger.info("Repacking JH_uwasatext");
        Pair<ByteBuffer, ByteBuffer> jhRumorIdxPak = App.jhRumor.repack();
        App.archive.setFile(IdxPaks.jhRumor.idx(), jhRumorIdxPak.getKey());
        App.archive.setFile(IdxPaks.jhRumor.pak(), jhRumorIdxPak.getValue());
        
        logger.info("Repacking JH_freepapermes");
        Pair<ByteBuffer, ByteBuffer> jhNoticeIdxPak = App.jhNotice.repack();
        App.archive.setFile(IdxPaks.jhNotice.idx(), jhNoticeIdxPak.getKey());
        App.archive.setFile(IdxPaks.jhNotice.pak(), jhNoticeIdxPak.getValue());
        
        logger.info("Repacking ev_msg");
        Pair<ByteBuffer, ByteBuffer> evMsgIdxPak = App.evMsg.repack();
        App.archive.setFile(IdxPaks.evMsg.idx(), evMsgIdxPak.getKey());
        App.archive.setFile(IdxPaks.evMsg.pak(), evMsgIdxPak.getValue());

        logger.info("Repacking entrydata");
        Pair<ByteBuffer, ByteBuffer> entrydataIdxPak = App.entrydata.repack();
        App.archive.setFile(IdxPaks.entrydata.idx(), entrydataIdxPak.getKey());
        App.archive.setFile(IdxPaks.entrydata.pak(), entrydataIdxPak.getValue());

        logger.info("Repacking atl");
        Pair<ByteBuffer, ByteBuffer> atlIdxPak = App.atl.repack();
        App.archive.setFile(IdxPaks.atl.idx(), atlIdxPak.getKey());
        App.archive.setFile(IdxPaks.atl.pak(), atlIdxPak.getValue());

        logger.info("Repacking face");
        Pair<ByteBuffer, ByteBuffer> faceIdxPak = App.face.repack();
        App.archive.setFile(IdxPaks.face.idx(), faceIdxPak.getKey());
        App.archive.setFile(IdxPaks.face.pak(), faceIdxPak.getValue());

        
        logger.info("Saving NaUnitAnimTable");
        ByteBuffer encodedTable = LZSS.encode(App.naUnitAnimTable.rewind());
        ByteBuffer newTable = ByteBuffer.allocate(encodedTable.capacity()+4);
        newTable.putInt(encodedTable.getShort(1));
        newTable.put(encodedTable);
        App.archive.setFile("char/NaUnitAnimTable.bin", newTable);

        // Repack archive
        logger.info("Repacking archive");
        ByteBuffer newIdx = ByteBuffer.allocate(256*1024*1024).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer newBin = ByteBuffer.allocate(256*1024*1024).order(ByteOrder.LITTLE_ENDIAN);
        App.archive.repack(newIdx, newBin);
        System.out.println("Archive successfully repacked");

        Path dataPath = Path.of("data");
        File pcIdx = dataPath.resolve("data\\master\\pc.idx").toFile();
        File pcBin = dataPath.resolve("data\\master\\pc.bin").toFile();
        File arm9 = dataPath.resolve("arm9.bin").toFile();
        File overlay11 = dataPath.resolve("overlay\\overlay_0011.bin").toFile();


        FileOutputStream newIdxStream = new FileOutputStream(pcIdx);
        newIdxStream.write(newIdx.array(), 0, newIdx.position());
        newIdxStream.close();
        
        FileOutputStream newBinStream = new FileOutputStream(pcBin);
        newBinStream.write(newBin.array(), 0, newBin.position());
        newBinStream.close();

        FileOutputStream newArm9Stream = new FileOutputStream(arm9);
        newArm9Stream.write(App.arm9.array());
        newArm9Stream.close();

        FileOutputStream newOverlay11Stream = new FileOutputStream(overlay11);
        newOverlay11Stream.write(App.overlay11.array());
        newOverlay11Stream.close();

        logger.info("Repacking rom with ndstool");
        ProcessBuilder ndsTool = new ProcessBuilder("ndstool.exe", "-c", savePath.toPath().toString(),
                                            "-9", dataPath.resolve("arm9.bin").toString(),
                                            "-7", dataPath.resolve("arm7.bin").toString(),
                                            "-y9", dataPath.resolve("y9.bin").toString(),
                                            "-y7", dataPath.resolve("y7.bin").toString(),
                                            "-d", dataPath.resolve("data").toString(),
                                            "-y", dataPath.resolve("overlay").toString(),
                                            "-t", dataPath.resolve("banner.bin").toString(),
                                            "-h", dataPath.resolve("header.bin").toString());
        ndsTool.redirectOutput(Redirect.INHERIT);
        ndsTool.redirectError(Redirect.INHERIT);
        ndsTool.start().waitFor();
    }

}
