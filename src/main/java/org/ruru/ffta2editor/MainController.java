package org.ruru.ffta2editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

import org.ruru.ffta2editor.utility.Archive;
import org.ruru.ffta2editor.utility.IdxAndPak;
import org.ruru.ffta2editor.utility.LZSS;

import javafx.fxml.FXML;
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

    @FXML AnchorPane equipmentTab;
    @FXML EquipmentController equipmentTabController;
    

    File romFile;
    

    @FXML
    private void openFileSelector() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Directory");
        File file = chooser.showOpenDialog(abilityTab.getScene().getWindow());
        if (file == null) return;
        romFile = file;
        Path dataPath = file.toPath().resolveSibling("data");
        //ProcessBuilder ndsTool = new ProcessBuilder("G:\\ndstool.exe", "-x", file.toPath().toString(), "-9", dataPath.resolve("arm9.bin").toString(), "-d", dataPath.toString());
        ProcessBuilder ndsTool = new ProcessBuilder("ndstool.exe", "-x", file.toPath().toString(),
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
        try {
            Files.createDirectories(dataPath);
            ndsTool.start().waitFor();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }




         
        //DirectoryChooser chooser = new DirectoryChooser();
        //chooser.setTitle("Open Directory");
        //File file = chooser.showDialog(abilityTab.getScene().getWindow());
        //if (file == null) return;
        
        //File file = Path.of("C:\\Users\\Ruru\\Documents\\GitHub\\ffta2-editor").toFile();
        try {
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
        } catch (Exception e) {
            System.err.println(e);
            return;
        }


        ByteBuffer sysdataIdx = App.archive.getFile("system/rom/sysdata_rom.idx");
        ByteBuffer sysdataPak = App.archive.getFile("system/rom/sysdata.pak");

        App.sysdata = new IdxAndPak(sysdataIdx, sysdataPak);
        
        ByteBuffer unitSstIdx = App.archive.getFile("char/rom/rom_idx/UnitSst.rom_idx");
        ByteBuffer unitSstPak = App.archive.getFile("char/rom/pak/UnitSst.pak");

        App.unitSsts = new IdxAndPak(unitSstIdx, unitSstPak);


        ByteBuffer unitCgsIdx = App.archive.getFile("char/rom/rom_idx/UnitCg.rom_idx");
        ByteBuffer unitCgsPak = App.archive.getFile("char/rom/pak/UnitCg.pak");
        App.unitCgs = new IdxAndPak(unitCgsIdx, unitCgsPak);
        
        // 0 = US language
        ByteBuffer jdMessageIdx = App.archive.getFile(String.format("system/rom/JD_message_rom_%d.idx", 0));
        ByteBuffer jdMessagePak = App.archive.getFile(String.format("system/rom/JD_message_%d.pak", 0));
        App.jdMessage = new IdxAndPak(jdMessageIdx, jdMessagePak);

        ByteBuffer entrydataIdx = App.archive.getFile("system/rom/entrydata_rom.idx");
        ByteBuffer entrydataPak = App.archive.getFile("system/rom/entrydata.pak");
        App.entrydata = new IdxAndPak(entrydataIdx, entrydataPak);

        try {
            var animTable = App.archive.getFile("char/NaUnitAnimTable.bin");
            App.naUnitAnimTable = LZSS.decode(animTable.position(4)).decodedData;
        } catch (Exception e) {
            System.err.println(e);
        }

        
        

        textTabController.loadMessages();
        abilityTabController.loadAbilities();
        spritesTabController.loadSprites();
        jobTabController.loadJobs();
        characterTabController.loadCharacters();
        jobRequirementTabController.loadJobRequirements();
        jobGroupTabController.loadJobGroups();
        equipmentTabController.loadEquipment();
        formationTabController.loadFormations();
        questTabController.loadQuests();
        bazaarTabController.loadBazaar();
        // var animTable = App.archive.getFile("char/NaUnitAnimTable.bin");
        // try {
        //     LZSSDecodeResult decoded = LZSS.decode(animTable.position(4));
        //     ByteBuffer encoded = LZSS.encode(ByteBuffer.wrap(decoded.decodedData.array()));
        //     FileOutputStream lzssTestOutput = new FileOutputStream(new File("G:\\animEncoded"));
        //     lzssTestOutput.write( Arrays.copyOfRange(encoded.array(), 0, encoded.limit()) );
        //     lzssTestOutput.close();
        //     LZSSDecodeResult redecoded = LZSS.decode(encoded.rewind());
        //     lzssTestOutput = new FileOutputStream(new File("G:\\animReDecoded"));
        //     lzssTestOutput.write(redecoded.decodedData.array());
        //     lzssTestOutput.close();
        // } catch (Exception e) {
        //     System.err.println(e);
        // }

        // ByteBuffer unitCgsIdx = App.archive.getFile("char/rom/rom_idx/UnitCg.rom_idx");
        // ByteBuffer unitCgsPak = App.archive.getFile("char/rom/pak/UnitCg.pak");
        // IdxAndPak unitCgs = new IdxAndPak(unitCgsIdx, unitCgsPak);
        
        // ByteBuffer unitSstsIdx = App.archive.getFile("char/rom/rom_idx/UnitSst.rom_idx");
        // ByteBuffer unitSstsPak = App.archive.getFile("char/rom/pak/UnitSst.pak");
        // IdxAndPak unitSsts = new IdxAndPak(unitSstsIdx, unitSstsPak);

        // for (int i = 0; i < unitSsts.numFiles(); i++) {
        //     try {
        //         System.out.println(i);
        //         ByteBuffer soldierCg = unitCgs.getFile(i);
        //         ByteBuffer soldierSst = unitSsts.getFile(i);
        //         UnitSst testSst = new UnitSst(soldierSst);
        //         //SpritePalettes testPalette = testSst.getPalettes();
        //         ByteBuffer testPalette = testSst.getPalettes();
        //         SpriteData testData = testSst.getSpriteMap();

        //         UnitSprite testSprite = new UnitSprite(testData, testPalette, soldierCg);
        //         for (int pal = 0; pal < testSprite.spritePalettes.palettes.size(); pal++) {
        //             for (int pose = 0; pose < testData.spriteMaps.size(); pose++) {
        //                 BufferedImage fullImage = testSprite.getSprite(pose, pal);
        //                 Path filePath = Path.of(String.format("G:\\sprites\\unit%03d\\pal%d\\%d.png", i, pal, pose));
        //                 Files.createDirectories(filePath.getParent());
        //                 ImageIO.write(fullImage, "png", filePath.toFile());
        //             }
        //         }
        //     } catch (Exception e) {
        //         System.err.println(e);
        //     }
            
        // }
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
        //DirectoryChooser chooser = new DirectoryChooser();
        //chooser.setTitle("Open Directory");
        //File savePath = chooser.showDialog(abilityTab.getScene().getWindow());
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save as");
        //chooser.setInitialDirectory(romFile.getParent());
        chooser.setInitialFileName(romFile.getName());
        File savePath = chooser.showSaveDialog(abilityTab.getScene().getWindow());
        if (savePath == null) {
            setDim(false);
            return;
        }
        save(savePath);
    }

    private void save(File savePath) {
        try {
            textTabController.saveMessages();
            abilityTabController.saveAbilities();
            jobTabController.saveJobs();
            spritesTabController.saveSprites();
            characterTabController.saveCharacters();
            jobRequirementTabController.saveJobRequirements();
            jobGroupTabController.saveJobGroups();
            equipmentTabController.saveEquipment();
            formationTabController.saveFormations();
            questTabController.saveQuests();
            bazaarTabController.saveBazaar();
            //patchesTabController.applyPatches(); // TODO: Redo patch application

            // Repack sub-archives
            Pair<ByteBuffer, ByteBuffer> idxPak = App.sysdata.repack();
            App.archive.setFile("system/rom/sysdata_rom.idx", idxPak.getKey());
            App.archive.setFile("system/rom/sysdata.pak", idxPak.getValue());

            Pair<ByteBuffer, ByteBuffer> sstIdxPak = App.unitSsts.repack();
            App.archive.setFile("char/rom/rom_idx/UnitSst.rom_idx", sstIdxPak.getKey());
            App.archive.setFile("char/rom/pak/UnitSst.pak", sstIdxPak.getValue());
            
            Pair<ByteBuffer, ByteBuffer> cgIdxPak = App.unitCgs.repack();
            App.archive.setFile("char/rom/rom_idx/UnitCg.rom_idx", cgIdxPak.getKey());
            App.archive.setFile("char/rom/pak/UnitCg.pak", cgIdxPak.getValue());
            
            Pair<ByteBuffer, ByteBuffer> jdMessageIdxPak = App.jdMessage.repack();
            App.archive.setFile(String.format("system/rom/JD_message_rom_%d.idx", 0), jdMessageIdxPak.getKey());
            App.archive.setFile(String.format("system/rom/JD_message_%d.pak", 0), jdMessageIdxPak.getValue());

            Pair<ByteBuffer, ByteBuffer> entrydataIdxPak = App.entrydata.repack();
            App.archive.setFile("system/rom/entrydata_rom.idx", entrydataIdxPak.getKey());
            App.archive.setFile("system/rom/entrydata.pak", entrydataIdxPak.getValue());

            ByteBuffer encodedTable = LZSS.encode(App.naUnitAnimTable.rewind());
            ByteBuffer newTable = ByteBuffer.allocate(encodedTable.capacity()+4);
            newTable.putInt(encodedTable.getShort(1));
            newTable.put(encodedTable);
            App.archive.setFile("char/NaUnitAnimTable.bin", newTable);

            // Repack archive
            ByteBuffer newIdx = ByteBuffer.allocate(256*1024*1024).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer newBin = ByteBuffer.allocate(256*1024*1024).order(ByteOrder.LITTLE_ENDIAN);
            App.archive.repack(newIdx, newBin);
            System.out.println("Archive successfully repacked");



            //FileOutputStream newRom = new FileOutputStream(savePath);
            //Files.copy(romFile.toPath(), newRom);
            //newRom.close();

            Path dataPath = romFile.toPath().resolveSibling("data");
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

            //ProcessBuilder ndsTool = new ProcessBuilder("G:\\ndstool.exe", "-c", savePath.toPath().toString(), "-9", dataPath.resolve("arm9.bin").toString(), "-d", dataPath.toString());
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
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            setDim(false);
        }

    }

}
