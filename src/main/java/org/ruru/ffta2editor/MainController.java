package org.ruru.ffta2editor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.ruru.ffta2editor.model.stringTable.MessageId;
import org.ruru.ffta2editor.model.stringTable.StringSingle;
import org.ruru.ffta2editor.model.stringTable.StringTable;
import org.ruru.ffta2editor.model.topSprite.TopSprite;
import org.ruru.ffta2editor.model.unitFace.UnitFace;
import org.ruru.ffta2editor.utility.Archive;
import org.ruru.ffta2editor.utility.IdxAndPak;
import org.ruru.ffta2editor.utility.LZSS;
import org.ruru.ffta2editor.utility.UnitSprite;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
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

    @FXML AnchorPane consumableTab;
    @FXML ConsumableController consumableTabController;

    @FXML AnchorPane lootTab;
    @FXML LootController lootTabController;

    @FXML AnchorPane itemTableTab;
    @FXML ItemTableController itemTableTabController;

    @FXML AnchorPane lawBonusTab;
    @FXML LawBonusController lawBonusTabController;
    
    @FXML MenuItem saveMenuItem;

    Path dataPath;
    File romFile;
    ObjectProperty<File> lastSavePath = new SimpleObjectProperty<>();
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

    private class ExportImageFlags {
        BitSet flags;

        public ExportImageFlags setUnitSprites(boolean value) {
            flags.set(0, value);
            return this;
        }
        public ExportImageFlags setUnitSprites() {
            return setUnitSprites(true);
        }
        public boolean getUnitSprites() {
            return flags.get(0);
        }

        public ExportImageFlags setTopSprites(boolean value) {
            flags.set(1, value);
            return this;
        }
        public ExportImageFlags setTopSprites() {
            return setTopSprites(true);
        }
        public boolean getTopSprites() {
            return flags.get(1);
        }

        public ExportImageFlags setFaces(boolean value) {
            flags.set(2, value);
            return this;
        }
        public ExportImageFlags setFaces() {
            return setFaces(true);
        }
        public boolean getFaces() {
            return flags.get(2);
        }

        ExportImageFlags() {
            flags = new BitSet(3);
        }


    } 

    @FXML MenuItem exportAllImagesMenuItem;
    @FXML MenuItem exportUnitSpritesMenuItem;
    @FXML MenuItem exportTopSpritesMenuItem;
    @FXML MenuItem exportFacesMenuItem;

    @FXML
    public void initialize() {
        saveMenuItem.disableProperty().bind(lastSavePath.isNull());
        exportAllImagesMenuItem.setOnAction(e -> exportSpritesSelector(new ExportImageFlags().setUnitSprites().setTopSprites().setFaces()));
        exportUnitSpritesMenuItem.setOnAction(e -> exportSpritesSelector(new ExportImageFlags().setUnitSprites()));
        exportTopSpritesMenuItem.setOnAction(e -> exportSpritesSelector(new ExportImageFlags().setTopSprites()));
        exportFacesMenuItem.setOnAction(e -> exportSpritesSelector(new ExportImageFlags().setFaces()));
    }

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

        IdxAndPak sysdata = new IdxAndPak("sysdata", App.archive.getFile(IdxPaks.sysdata.idx()), App.archive.getFile(IdxPaks.sysdata.pak()));

        IdxAndPak unitSsts = new IdxAndPak("unitSsts", App.archive.getFile(IdxPaks.unitSsts.idx()), App.archive.getFile(IdxPaks.unitSsts.pak()));

        IdxAndPak unitCgs = new IdxAndPak("unitCgs", App.archive.getFile(IdxPaks.unitCgs.idx()), App.archive.getFile(IdxPaks.unitCgs.pak()));
        
        IdxAndPak jdMessage = new IdxAndPak("jdMessage", App.archive.getFile(IdxPaks.jdMessage.idx()), App.archive.getFile(IdxPaks.jdMessage.pak()));

        IdxAndPak jhQuest = new IdxAndPak("jhQuest", App.archive.getFile(IdxPaks.jhQuest.idx()), App.archive.getFile(IdxPaks.jhQuest.pak()));
        
        IdxAndPak jhRumor = new IdxAndPak("jhRumor", App.archive.getFile(IdxPaks.jhRumor.idx()), App.archive.getFile(IdxPaks.jhRumor.pak()));
        
        IdxAndPak jhNotice = new IdxAndPak("jhNotice", App.archive.getFile(IdxPaks.jhNotice.idx()), App.archive.getFile(IdxPaks.jhNotice.pak()));
        
        IdxAndPak evMsg = new IdxAndPak("evMsg", App.archive.getFile(IdxPaks.evMsg.idx()), App.archive.getFile(IdxPaks.evMsg.pak()));

        IdxAndPak entrydata = new IdxAndPak("entrydata", App.archive.getFile(IdxPaks.entrydata.idx()), App.archive.getFile(IdxPaks.entrydata.pak()));
        
        IdxAndPak atl = new IdxAndPak("atl", App.archive.getFile(IdxPaks.atl.idx()), App.archive.getFile(IdxPaks.atl.pak()));
        
        IdxAndPak face = new IdxAndPak("face", App.archive.getFile(IdxPaks.face.idx()), App.archive.getFile(IdxPaks.face.pak()));


        Pair<ByteBuffer, ByteBuffer> repackedBytes = App.sysdata.repack();
        IdxAndPak repacked = new IdxAndPak("sysdata", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(sysdata, App.sysdata, "sysdata");

        repackedBytes = App.unitSsts.repack();
        repacked = new IdxAndPak("unitSsts", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(unitSsts, App.unitSsts, "unitSsts");
        
        repackedBytes = App.unitCgs.repack();
        repacked = new IdxAndPak("unitCgs", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(unitCgs, App.unitCgs, "unitCgs");
        
        repackedBytes = App.jdMessage.repack();
        repacked = new IdxAndPak("jdMessage", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(jdMessage, App.jdMessage, "jdMessage");
        
        repackedBytes = App.jhQuest.repack();
        repacked = new IdxAndPak("jhQuest", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(jhQuest, App.jhQuest, "jhQuest");
        
        repackedBytes = App.jhRumor.repack();
        repacked = new IdxAndPak("jhRumor", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(jhRumor, App.jhRumor, "jhRumor");
        
        repackedBytes = App.jhNotice.repack();
        repacked = new IdxAndPak("jhNotice", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(jhNotice, App.jhNotice, "jhNotice");
        
        repackedBytes = App.evMsg.repack();
        repacked = new IdxAndPak("evMsg", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(evMsg, App.evMsg, "evMsg");
        
        repackedBytes = App.entrydata.repack();
        repacked = new IdxAndPak("entrydata", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(entrydata, App.entrydata, "entrydata");
        
        repackedBytes = App.atl.repack();
        repacked = new IdxAndPak("atl", repackedBytes.getKey(),repackedBytes.getValue());
        compareIdxPaks(atl, App.atl, "atl");
        
        repackedBytes = App.face.repack();
        repacked = new IdxAndPak("face", repackedBytes.getKey(),repackedBytes.getValue());
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

    private ByteBuffer loadAsBuffer(Path p) throws IOException {
        return ByteBuffer.wrap(Files.readAllBytes(p)).order(ByteOrder.LITTLE_ENDIAN);
    }

    @FXML
    private void openFileSelector() {

        setDim(true);
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open ROM");
        chooser.setInitialDirectory(App.getLastRomPath());
        File loadPath = chooser.showOpenDialog(abilityTab.getScene().getWindow());
        if (loadPath == null) {
            setDim(false);
            return;
        }
        App.saveLastRomPath(loadPath);
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
        } finally {
            setDim(false);
        }
        if (!App.loadWarningList.isEmpty()) {
            Alert loadAlert = new Alert(AlertType.ERROR);
            loadAlert.setTitle("Loading");
            loadAlert.setHeaderText(String.format("Loading succeeded with %d warnings", App.loadWarningList.size()));
            String warningMessages = App.loadWarningList.stream().limit(10).collect(Collectors.joining("\n"));
            if (App.loadWarningList.size() > 10) {
                warningMessages = warningMessages.concat(String.format("\n+ %d addtional warnings", App.loadWarningList.size()-10));
            }
            App.loadWarningList.clear();
            loadAlert.setContentText(warningMessages);
            loadAlert.show();
        }

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
        App.loadWarningList.clear();
        logger.info("Unpacking rom with ndstool");
        romFile = loadPath;

        dataPath = Files.createTempDirectory(Path.of("."), loadPath.getName());
        Path dataPathCapture = dataPath;
        Runtime.getRuntime().addShutdownHook((new Thread() {
            @Override
            public void run() {
                try (var stream = Files.walk(dataPathCapture)) {
                    stream.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
                    
                } catch (Exception e) {
                    System.err.println(String.format("Failed to delete temp folder %s", dataPathCapture));
                }
            }
        }));
        //Path dataPath = Path.of("data");
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
        App.arm9 = loadAsBuffer(dataPath.resolve("arm9.bin"));
        App.overlay11 = loadAsBuffer(dataPath.resolve("overlay\\overlay_0011.bin"));
        App.overlay8 = loadAsBuffer(dataPath.resolve("overlay\\overlay_0008.bin"));

        


        
        logger.info("Parsing sysdata");
        App.sysdata = new IdxAndPak("sysdata", App.archive.getFile(IdxPaks.sysdata.idx()), App.archive.getFile(IdxPaks.sysdata.pak()));
        
        logger.info("Parsing UnitSst");
        App.unitSsts = new IdxAndPak("unitSsts", App.archive.getFile(IdxPaks.unitSsts.idx()), App.archive.getFile(IdxPaks.unitSsts.pak()));

        logger.info("Parsing UnitCg");
        App.unitCgs = new IdxAndPak("unitCgs", App.archive.getFile(IdxPaks.unitCgs.idx()), App.archive.getFile(IdxPaks.unitCgs.pak()));
        
        logger.info("Parsing JD_message");
        App.jdMessage = new IdxAndPak("jdMessage", App.archive.getFile(IdxPaks.jdMessage.idx()), App.archive.getFile(IdxPaks.jdMessage.pak()));

        logger.info("Parsing JH_questtext");
        App.jhQuest = new IdxAndPak("jhQuest", App.archive.getFile(IdxPaks.jhQuest.idx()), App.archive.getFile(IdxPaks.jhQuest.pak()));
        
        logger.info("Parsing JH_uwasatext");
        App.jhRumor = new IdxAndPak("jhRumor", App.archive.getFile(IdxPaks.jhRumor.idx()), App.archive.getFile(IdxPaks.jhRumor.pak()));
        
        logger.info("Parsing JH_freepapermes");
        App.jhNotice = new IdxAndPak("jhNotice", App.archive.getFile(IdxPaks.jhNotice.idx()), App.archive.getFile(IdxPaks.jhNotice.pak()));
        
        logger.info("Parsing ev_msg");
        App.evMsg = new IdxAndPak("evMsg", App.archive.getFile(IdxPaks.evMsg.idx()), App.archive.getFile(IdxPaks.evMsg.pak()));

        logger.info("Parsing entrydata");
        App.entrydata = new IdxAndPak("entrydata", App.archive.getFile(IdxPaks.entrydata.idx()), App.archive.getFile(IdxPaks.entrydata.pak()));
        
        logger.info("Parsing atl");
        App.atl = new IdxAndPak("atl", App.archive.getFile(IdxPaks.atl.idx()), App.archive.getFile(IdxPaks.atl.pak()));
        
        logger.info("Parsing face");
        App.face = new IdxAndPak("face", App.archive.getFile(IdxPaks.face.idx()), App.archive.getFile(IdxPaks.face.pak()));


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
        logger.info("Loading Consumables");
        consumableTabController.loadConsumables();
        logger.info("Loading Loot");
        lootTabController.loadLoot();
        logger.info("Loading Item Tables");
        itemTableTabController.loadItemTables();
        logger.info("Loading Law Bonus");
        lawBonusTabController.loadLawBonus();
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
        {
            saveTo(lastSavePath.get());
        } else {
            setDim(false);
        }
    }

    @FXML
    private void saveFileSelector() {
        if (romFile == null) return;
        setDim(true);
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save as");
        chooser.setInitialFileName(romFile.getName());
        chooser.setInitialDirectory(App.getLastRomPath());
        File savePath = chooser.showSaveDialog(abilityTab.getScene().getWindow());
        if (savePath == null) {
            setDim(false);
            return;
        }
        App.saveLastRomPath(savePath);
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
        logger.info("Saving Consumables");
        consumableTabController.saveConsumables();
        logger.info("Saving Loot");
        lootTabController.saveLoot();
        logger.info("Saving Item Tables");
        itemTableTabController.saveItemTables();
        logger.info("Saving Law Bonus");
        lawBonusTabController.saveLawBonus();
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
        int padding = 0;
        if (encodedTable.capacity() % 4 != 0) {
            logger.info("Padding NaUnitAnimTable");
            padding = 4 - (encodedTable.capacity() % 4);
        }
        ByteBuffer newTable = ByteBuffer.allocate(encodedTable.capacity() + 4 + padding);
        newTable.putInt(encodedTable.getShort(1));
        newTable.put(encodedTable);
        App.archive.setFile("char/NaUnitAnimTable.bin", newTable);

        // Repack archive
        logger.info("Repacking archive");
        var archivePair = App.archive.repack();
        ByteBuffer newIdx = archivePair.getKey();
        ByteBuffer newBin = archivePair.getValue();
        System.out.println("Archive successfully repacked");

        Path pcIdx = dataPath.resolve("data\\master\\pc.idx");
        Path pcBin = dataPath.resolve("data\\master\\pc.bin");
        Path arm9 = dataPath.resolve("arm9.bin");
        Path overlay11 = dataPath.resolve("overlay\\overlay_0011.bin");

        Files.write(pcIdx, newIdx.array());
        Files.write(pcBin, newBin.array());
        Files.write(arm9, App.arm9.array());
        Files.write(overlay11, App.overlay11.array());

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

    
    @FXML
    private void resetTextFileSelector() {
        if (App.archive == null) return;
        setDim(true);

        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setContentText("This fix is for ROMs saved with editors older than v1.3.2 which had a text encoding bug that replaced \"*\" with \"ー\".\nThis will load text from an original ROM to reset affected text.");
        confirmAlert.setHeaderText("Fix pre-v1.3.2 text encoding bug");

        var result = confirmAlert.showAndWait();

        if (!result.isPresent() || result.get() != ButtonType.OK) {
            setDim(false);
            return;
        }


        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Original ROM");
        chooser.setInitialDirectory(App.getLastRomPath());
        File loadPath = chooser.showOpenDialog(abilityTab.getScene().getWindow());
        if (loadPath == null) {
            setDim(false);
            return;
        }
        try {
            resetText(loadPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Failed to reset text"), e);
            System.err.println(e);
            Alert loadAlert = new Alert(AlertType.ERROR);
            loadAlert.setTitle("Resetting text");
            loadAlert.setHeaderText("Reset failed");
            loadAlert.setContentText(e.toString());
            loadAlert.show();
        } finally {
            setDim(false);
        }
    }


    public void resetText(File loadPath) throws Exception {
        logger.info("Unpacking rom with ndstool");
        romFile = loadPath;
        
        Path vanillaDataPath = Files.createTempDirectory(Path.of("."), loadPath.getName());
        Runtime.getRuntime().addShutdownHook((new Thread() {
            @Override
            public void run() {
                try (var stream = Files.walk(vanillaDataPath)) {
                    stream.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
                    
                } catch (Exception e) {
                    System.err.println(String.format("Failed to delete temp folder %s", dataPath));
                }
            }
        }));
        ProcessBuilder ndsTool = new ProcessBuilder("ndstool.exe", "-x", loadPath.toPath().toString(),
                                               "-9", vanillaDataPath.resolve("arm9.bin").toString(),
                                               "-7", vanillaDataPath.resolve("arm7.bin").toString(),
                                               "-y9", vanillaDataPath.resolve("y9.bin").toString(),
                                               "-y7", vanillaDataPath.resolve("y7.bin").toString(),
                                               "-d", vanillaDataPath.resolve("data").toString(),
                                               "-y", vanillaDataPath.resolve("overlay").toString(),
                                               "-t", vanillaDataPath.resolve("banner.bin").toString(),
                                               "-h", vanillaDataPath.resolve("header.bin").toString());
        ndsTool.redirectOutput(Redirect.INHERIT);
        ndsTool.redirectError(Redirect.INHERIT);
        Files.createDirectories(vanillaDataPath);
        ndsTool.start().waitFor();

        logger.info("Parsing archive");
        File pcIdx = vanillaDataPath.resolve("data\\master\\pc.idx").toFile();
        File pcBin = vanillaDataPath.resolve("data\\master\\pc.bin").toFile();
        
        Archive vanillaArchive = new Archive(pcIdx, pcBin);

        logger.info("Parsing JD_message");
        IdxAndPak jdMessage = new IdxAndPak("jdMessage", vanillaArchive.getFile(IdxPaks.jdMessage.idx()), vanillaArchive.getFile(IdxPaks.jdMessage.pak()));

        logger.info("Parsing JH_questtext");
        IdxAndPak jhQuest = new IdxAndPak("jhQuest", vanillaArchive.getFile(IdxPaks.jhQuest.idx()), vanillaArchive.getFile(IdxPaks.jhQuest.pak()));
        
        logger.info("Parsing JH_uwasatext");
        IdxAndPak jhRumor = new IdxAndPak("jhRumor", vanillaArchive.getFile(IdxPaks.jhRumor.idx()), vanillaArchive.getFile(IdxPaks.jhRumor.pak()));
        
        logger.info("Parsing JH_freepapermes");
        IdxAndPak jhNotice = new IdxAndPak("jhNotice", vanillaArchive.getFile(IdxPaks.jhNotice.idx()), vanillaArchive.getFile(IdxPaks.jhNotice.pak()));
        
        logger.info("Parsing ev_msg");
        IdxAndPak evMsg = new IdxAndPak("evMsg", vanillaArchive.getFile(IdxPaks.evMsg.idx()), vanillaArchive.getFile(IdxPaks.evMsg.pak()));

        int numAffected = 0;
        for (int i = 0; i < jdMessage.numFiles(); i++) {
            ByteBuffer stringTableBytes = jdMessage.getFile(i);
            if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                continue;
            }

            StringTable vanillaStringTable = new StringTable(stringTableBytes, new SimpleStringProperty(MessageId.messageNames[i]), i);

            int curr = i;
            var stringTable = textTabController.messageList.getItems().filtered(x -> x.id == curr).getFirst();
            var filtered = vanillaStringTable.strings.filtered(x -> x.string().getValue().contains("*"));
            numAffected += filtered.size();
            filtered.forEach(x -> stringTable.strings.get(x.id()).string().set(x.string().get()));
        }

        // The only instance of ` in the game apparently
        numAffected++;
        textTabController.messageList.getItems().get(0x15).strings.get(0x15).string().setValue("`");

        for (int i = 0; i < evMsg.numFiles(); i++) {
            ByteBuffer stringTableBytes = evMsg.getFile(i);
            if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                continue;
            }

            StringTable vanillaStringTable = new StringTable(stringTableBytes, new SimpleStringProperty("Unknown"), i);

            int curr = i;
            var stringTable = textTabController.eventMsgList.getItems().filtered(x -> x.id == curr).getFirst();
            var filtered = vanillaStringTable.strings.filtered(x -> x.string().getValue().contains("*"));
            numAffected += filtered.size();
            filtered.forEach(x -> stringTable.strings.get(x.id()).string().set(x.string().get()));
        }

        for (int i = 0; i < jhQuest.numFiles(); i++) {
            ByteBuffer stringTableBytes = jhQuest.getFile(i);
            if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                continue;
            }

            StringSingle vanillaStringTable = new StringSingle(stringTableBytes, App.questNames.get(i).string(), i);

            int curr = i;
            var stringTable = textTabController.questList.getItems().filtered(x -> x.id == curr).getFirst();
            if (vanillaStringTable.text.getValue().contains("*")) {
                numAffected++;
                stringTable.text.set(vanillaStringTable.text.get());
            }
        }

        for (int i = 0; i < jhRumor.numFiles(); i++) {
            ByteBuffer stringTableBytes = jhRumor.getFile(i);
            if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                continue;
            }

            StringSingle vanillaStringTable = new StringSingle(stringTableBytes, App.rumorNames.get(i).string(), i);

            int curr = i;
            var stringTable = textTabController.rumorList.getItems().filtered(x -> x.id == curr).getFirst();
            if (vanillaStringTable.text.getValue().contains("*")) {
                numAffected++;
                stringTable.text.set(vanillaStringTable.text.get());
            }
        }

        for (int i = 0; i < jhNotice.numFiles(); i++) {
            ByteBuffer stringTableBytes = jhNotice.getFile(i);
            if (stringTableBytes == null || stringTableBytes.rewind().remaining() == 0) {
                continue;
            }

            StringSingle vanillaStringTable = new StringSingle(stringTableBytes, App.noticeNames.get(i).string(), i);

            int curr = i;
            var stringTable = textTabController.noticeList.getItems().filtered(x -> x.id == curr).getFirst();
            if (vanillaStringTable.text.getValue().contains("*")) {
                numAffected++;
                stringTable.text.set(vanillaStringTable.text.get());
            }
        }
        System.out.println(String.format("Reset %d strings", numAffected));
        
    }

    private void exportSpritesSelector(ExportImageFlags exportFlags) {
        if (romFile == null) return;
        setDim(true);
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Export to");
        chooser.setInitialDirectory(App.getLastFile());
        File savePath = chooser.showDialog(abilityTab.getScene().getWindow());
        if (savePath == null) {
            setDim(false);
            return;
        }
        App.saveLastFile(savePath);


        exportSprites(savePath.toPath(), exportFlags);
    }

    private void exportSprites(Path savePath, ExportImageFlags exportFlags) {

        Alert saveAlert = new Alert(AlertType.NONE);
        saveAlert.setTitle(String.format("Exporting to %s", savePath));
        saveAlert.setHeaderText("Exporting. Please wait.");
        saveAlert.show();
        CompletableFuture.runAsync(() -> {
            try {
                if (exportFlags.getUnitSprites()) exportUnitSprites(savePath.resolve("Unit Sprites"));
                if (exportFlags.getTopSprites()) exportTopSprites(savePath.resolve("Top Sprites"));
                if (exportFlags.getFaces()) exportFaceSprites(savePath.resolve("Faces"));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    logger.log(Level.SEVERE, String.format("Failed to export"), e);
                    setDim(false);
                    saveAlert.setAlertType(AlertType.ERROR);
                    saveAlert.setHeaderText("Failed to export");
                    saveAlert.setContentText(e.getMessage());
                    saveAlert.getDialogPane().getScene().getWindow().sizeToScene();
                });
                return;
            }
            Platform.runLater(() -> {
                setDim(false);
                saveAlert.setAlertType(AlertType.INFORMATION);
                saveAlert.setHeaderText("Exported");
            });
        });
    }

    private void exportUnitSprites(Path savePath) throws IOException {
        for (UnitSprite unitSprite : spritesTabController.unitList.getItems()) {
            Path unitPath = savePath.resolve(Integer.toString(unitSprite.unitIndex));
            for (int p = 0; p < unitSprite.spritePalettes.palettes.size(); p++) {
                Path palettePath = unitPath.resolve(Integer.toString(p));
                Files.createDirectories(palettePath);
                for (int s = 0; s < unitSprite.spriteData.spriteMaps.size(); s++) {
                    Path imagePath = palettePath.resolve(String.format("%d.png", s));
                    BufferedImage image = unitSprite.getSprite(s, p);
                    ImageIO.write(image, "png", imagePath.toFile());
                }
            }
        }
    }

    private void exportTopSprites(Path savePath) throws IOException {
        for (TopSprite topSprite : spritesTabController.topSpriteList.getItems()) {
            if (topSprite.id == 0) continue;
            Path unitPath = savePath.resolve(Integer.toString(topSprite.id));
            Files.createDirectories(unitPath);
            for (int s = 0; s < topSprite.sprites.size(); s++) {
                Path imagePath = unitPath.resolve(String.format("%d.png", s));
                BufferedImage image = topSprite.getSprite(s);
                ImageIO.write(image, "png", imagePath.toFile());
            }
        }
    }
    public void exportFaceSprites(Path savePath) throws IOException {
        Files.createDirectories(savePath);
        for (UnitFace face : spritesTabController.faceList.getItems()) {
            if (face.id == 0) continue;
            Path imagePath = savePath.resolve(String.format("%d.png", face.id));
            BufferedImage image = face.getImage();
                ImageIO.write(image, "png", imagePath.toFile());
        }
    }
}
