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
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ruru.ffta2editor.model.character.CharacterData;
import org.ruru.ffta2editor.model.item.EquipmentData;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.utility.Archive;
import org.ruru.ffta2editor.utility.IdxAndPak;
import org.ruru.ffta2editor.utility.LZSS;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    

    File romFile;
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");

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
        ByteBuffer sysdataIdx = App.archive.getFile("system/rom/sysdata_rom.idx");
        ByteBuffer sysdataPak = App.archive.getFile("system/rom/sysdata.pak");

        App.sysdata = new IdxAndPak(sysdataIdx, sysdataPak);
        
        logger.info("Parsing UnitSst");
        ByteBuffer unitSstIdx = App.archive.getFile("char/rom/rom_idx/UnitSst.rom_idx");
        ByteBuffer unitSstPak = App.archive.getFile("char/rom/pak/UnitSst.pak");

        App.unitSsts = new IdxAndPak(unitSstIdx, unitSstPak);

        logger.info("Parsing UnitCg");
        ByteBuffer unitCgsIdx = App.archive.getFile("char/rom/rom_idx/UnitCg.rom_idx");
        ByteBuffer unitCgsPak = App.archive.getFile("char/rom/pak/UnitCg.pak");
        App.unitCgs = new IdxAndPak(unitCgsIdx, unitCgsPak);
        
        logger.info("Parsing JD_message");
        // 0 = US language
        ByteBuffer jdMessageIdx = App.archive.getFile(String.format("system/rom/JD_message_rom_%d.idx", 0));
        ByteBuffer jdMessagePak = App.archive.getFile(String.format("system/rom/JD_message_%d.pak", 0));
        App.jdMessage = new IdxAndPak(jdMessageIdx, jdMessagePak);

        logger.info("Parsing JH_questtext");
        ByteBuffer jhQuestIdx = App.archive.getFile(String.format("system/rom/JH_questtext_rom_%d.idx", 0));
        ByteBuffer jhQuestPak = App.archive.getFile(String.format("system/rom/JH_questtext_%d.pak", 0));
        App.jhQuest = new IdxAndPak(jhQuestIdx, jhQuestPak);
        
        logger.info("Parsing JH_uwasatext");
        ByteBuffer jhRumorIdx = App.archive.getFile(String.format("system/rom/JH_uwasatext_rom_%d.idx", 0));
        ByteBuffer jhRumorPak = App.archive.getFile(String.format("system/rom/JH_uwasatext_%d.pak", 0));
        App.jhRumor = new IdxAndPak(jhRumorIdx, jhRumorPak);
        
        logger.info("Parsing JH_freepapermes");
        ByteBuffer jhNoticeIdx = App.archive.getFile(String.format("system/rom/JH_freepapermes_rom_%d.idx", 0));
        ByteBuffer jhNoticePak = App.archive.getFile(String.format("system/rom/JH_freepapermes_%d.pak", 0));
        App.jhNotice = new IdxAndPak(jhNoticeIdx, jhNoticePak);
        
        logger.info("Parsing ev_msg");
        ByteBuffer evMsgIdx = App.archive.getFile(String.format("event/rom/ev_msg%d_rom.idx", 0));
        ByteBuffer evMsgPak = App.archive.getFile(String.format("event/rom/ev_msg%d.pak", 0));
        App.evMsg = new IdxAndPak(evMsgIdx, evMsgPak);

        logger.info("Parsing entrydata");
        ByteBuffer entrydataIdx = App.archive.getFile("system/rom/entrydata_rom.idx");
        ByteBuffer entrydataPak = App.archive.getFile("system/rom/entrydata.pak");
        App.entrydata = new IdxAndPak(entrydataIdx, entrydataPak);
        
        logger.info("Parsing atl");
        ByteBuffer atlIdx = App.archive.getFile("menu/atl_rom/atl_rom.idx");
        ByteBuffer atlPak = App.archive.getFile("menu/atl_rom/atl.pak");
        App.atl = new IdxAndPak(atlIdx, atlPak);
        
        logger.info("Parsing face");
        ByteBuffer faceIdx = App.archive.getFile("menu/face_rom/face_rom.idx");
        ByteBuffer facePak = App.archive.getFile("menu/face_rom/face.pak");
        App.face = new IdxAndPak(faceIdx, facePak);


        logger.info("Decoding NaUnitAnimTable");
        var animTable = App.archive.getFile("char/NaUnitAnimTable.bin");
        App.naUnitAnimTable = LZSS.decode(animTable.position(4)).decodedData;


        

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
        logger.info("Loading Patches");
        patchesTabController.loadPatches();
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

        Alert saveAlert = new Alert(AlertType.NONE);
        saveAlert.setTitle("Saving");
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

        // Repack sub-archives
        logger.info("Repacking sysdata");
        Pair<ByteBuffer, ByteBuffer> sysdataIdxPak = App.sysdata.repack();
        App.archive.setFile("system/rom/sysdata_rom.idx", sysdataIdxPak.getKey());
        App.archive.setFile("system/rom/sysdata.pak", sysdataIdxPak.getValue());

        logger.info("Repacking UnitSst");
        Pair<ByteBuffer, ByteBuffer> sstIdxPak = App.unitSsts.repack();
        App.archive.setFile("char/rom/rom_idx/UnitSst.rom_idx", sstIdxPak.getKey());
        App.archive.setFile("char/rom/pak/UnitSst.pak", sstIdxPak.getValue());
        
        logger.info("Repacking UnitCg");
        Pair<ByteBuffer, ByteBuffer> cgIdxPak = App.unitCgs.repack();
        App.archive.setFile("char/rom/rom_idx/UnitCg.rom_idx", cgIdxPak.getKey());
        App.archive.setFile("char/rom/pak/UnitCg.pak", cgIdxPak.getValue());
        
        logger.info("Repacking JD_message");
        Pair<ByteBuffer, ByteBuffer> jdMessageIdxPak = App.jdMessage.repack();
        App.archive.setFile(String.format("system/rom/JD_message_rom_%d.idx", 0), jdMessageIdxPak.getKey());
        App.archive.setFile(String.format("system/rom/JD_message_%d.pak", 0), jdMessageIdxPak.getValue());
        
        logger.info("Repacking JH_questtext");
        Pair<ByteBuffer, ByteBuffer> jhQuestIdxPak = App.jhQuest.repack();
        App.archive.setFile(String.format("system/rom/JH_questtext_rom_%d.idx", 0), jhQuestIdxPak.getKey());
        App.archive.setFile(String.format("system/rom/JH_questtext_%d.pak", 0), jhQuestIdxPak.getValue());
        
        logger.info("Repacking JH_uwasatext");
        Pair<ByteBuffer, ByteBuffer> jhRumorIdxPak = App.jhRumor.repack();
        App.archive.setFile(String.format("system/rom/JH_uwasatext_rom_%d.idx", 0), jhRumorIdxPak.getKey());
        App.archive.setFile(String.format("system/rom/JH_uwasatext_%d.pak", 0), jhRumorIdxPak.getValue());
        
        logger.info("Repacking JH_freepapermes");
        Pair<ByteBuffer, ByteBuffer> jhNoticeIdxPak = App.jhNotice.repack();
        App.archive.setFile(String.format("system/rom/JH_freepapermes_rom_%d.idx", 0), jhNoticeIdxPak.getKey());
        App.archive.setFile(String.format("system/rom/JH_freepapermes_%d.pak", 0), jhNoticeIdxPak.getValue());
        
        logger.info("Repacking ev_msg");
        Pair<ByteBuffer, ByteBuffer> evMsgIdxPak = App.evMsg.repack();
        App.archive.setFile(String.format("event/rom/ev_msg%d_rom.idx", 0), evMsgIdxPak.getKey());
        App.archive.setFile(String.format("event/rom/ev_msg%d.pak", 0), evMsgIdxPak.getValue());

        logger.info("Repacking entrydata");
        Pair<ByteBuffer, ByteBuffer> entrydataIdxPak = App.entrydata.repack();
        App.archive.setFile("system/rom/entrydata_rom.idx", entrydataIdxPak.getKey());
        App.archive.setFile("system/rom/entrydata.pak", entrydataIdxPak.getValue());

        logger.info("Repacking atl");
        Pair<ByteBuffer, ByteBuffer> atlIdxPak = App.atl.repack();
        App.archive.setFile("menu/atl_rom/atl_rom.idx", atlIdxPak.getKey());
        App.archive.setFile("menu/atl_rom/atl.pak", atlIdxPak.getValue());

        logger.info("Repacking face");
        Pair<ByteBuffer, ByteBuffer> faceIdxPak = App.face.repack();
        App.archive.setFile("menu/face_rom/face_rom.idx", faceIdxPak.getKey());
        App.archive.setFile("menu/face_rom/face.pak", faceIdxPak.getValue());

        
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



        //FileOutputStream newRom = new FileOutputStream(savePath);
        //Files.copy(romFile.toPath(), newRom);
        //newRom.close();

        //Path dataPath = romFile.toPath().resolveSibling("data");
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
