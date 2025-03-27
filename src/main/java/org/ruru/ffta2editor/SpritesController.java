package org.ruru.ffta2editor;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.ruru.ffta2editor.model.unitSst.SpriteData;
import org.ruru.ffta2editor.model.unitSst.UnitSst;
import org.ruru.ffta2editor.utility.UnitSprite;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class SpritesController {
    public static class SpriteCell extends ListCell<UnitSprite> {
        //Label label = new Label();
        ImageView image = new ImageView();
        int spriteIndex;
        int paletteIndex;
        int scale;


        public SpriteCell(int spriteIndex, int paletteIndex, int scale) {
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

    private record SpriteRecord(ImageView scaledImage, int unitIndex, int spriteIndex, int paletteIndex) {};

    private ObjectProperty<UnitSprite> unitProperty = new SimpleObjectProperty<>();
    //private ArrayList<UnitSprite> unitSprites;

    @FXML ListView<UnitSprite> unitList;
    //@FXML ListView<ImageView> spriteList;
    @FXML VBox spriteVBox;

    @FXML
    public void initialize() {
        
        unitList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //if (oldValue != null) unbindAbilityData();
            unitProperty.setValue(newValue);
            if (newValue == null) return;
            //abilityAnimationProperty.setValue(abilityAnimationList.get(newValue.id));
            //bindAbilityData();
            //ObservableList<ImageView> unitImageViews = FXCollections.observableArrayList();
            //spriteVBox.getChildren().clear();
            //ArrayList<FlowPane> spriteFlowList = new ArrayList<>();
            refresh();
            
        });
    }

    public void refresh() {
        ArrayList<VBox> spriteVBoxList = new ArrayList<>();
            for (int p = 0; p < unitProperty.getValue().spritePalettes.palettes.size(); p++) {
                ArrayList<SpriteRecord> unitSpriteRecords = new ArrayList<>();
                for (int s = 0; s < unitProperty.getValue().spriteData.spriteMaps.size(); s++) {
                    BufferedImage image = unitProperty.getValue().getSprite(s, p, 2);
                    //unitImageViews.add(new ImageView(SwingFXUtils.toFXImage(image, null)));
                    //Image temp = image.getScaledInstance(image.getWidth()*4, image.getHeight()*4, Image.SCALE_SMOOTH);
                    unitSpriteRecords.add(new SpriteRecord(new ImageView(SwingFXUtils.toFXImage(image, null)), unitProperty.getValue().unitIndex, s, p));
                }
                FlowPane spriteFlow = new FlowPane();
                spriteFlow.getChildren().setAll(unitSpriteRecords.stream().map(spriteRecord -> {
                    Button exportButton = new Button("Export");
                    exportButton.setOnAction(event -> {
                        FileChooser chooser = new FileChooser();
                        chooser.setTitle("Export Sprite");
                        chooser.getExtensionFilters().add(new ExtensionFilter("Image", "*.png"));
                        chooser.setInitialFileName(String.format("unit%03d_%d_%d",  spriteRecord.unitIndex(), spriteRecord.spriteIndex(), spriteRecord.paletteIndex()));
                        File savePath = chooser.showSaveDialog(spriteFlow.getScene().getWindow());
                        if (savePath == null) {
                            return;
                        }
                        BufferedImage image = unitProperty.getValue().getSprite(spriteRecord.spriteIndex(), spriteRecord.paletteIndex());
                        try {
                            ImageIO.write(image, "png", savePath);
                        } catch (Exception err) {
                            System.err.println(err);
                        }
                    });
                    Button importButton = new Button("Import");
                    importButton.setOnAction(event -> {
                        FileChooser chooser = new FileChooser();
                        chooser.setTitle("Import Sprite");
                        chooser.getExtensionFilters().add(new ExtensionFilter("Image", "*.png"));
                        chooser.setInitialFileName(String.format("unit%03d_%d_%d",  spriteRecord.unitIndex(), spriteRecord.spriteIndex(), spriteRecord.paletteIndex()));
                        File loadPath = chooser.showOpenDialog(spriteFlow.getScene().getWindow());
                        if (loadPath == null) {
                            return;
                        }
                        try {
                            BufferedImage loadedImage = ImageIO.read(loadPath);
                            if (!(loadedImage.getColorModel() instanceof IndexColorModel)) throw new Exception("Wrong color model");
                            unitProperty.getValue().setSprite(spriteRecord.spriteIndex(), loadedImage);
                            refresh();
                            unitList.refresh();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    });
                    VBox spriteBox = new VBox(spriteRecord.scaledImage(), exportButton, importButton);
                    spriteBox.alignmentProperty().setValue(Pos.CENTER);
                    return spriteBox;
                }).toList());
                //spriteVBox.getChildren().add(spriteFlow);
                Button importButton = new Button("Replace palette");
                final int paletteIndex = p;
                importButton.setOnAction(event -> {
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle("Import Palette from sprite");
                    chooser.getExtensionFilters().add(new ExtensionFilter("Image", "*.png"));
                    chooser.setInitialFileName("sprite");
                    File loadPath = chooser.showOpenDialog(spriteFlow.getScene().getWindow());
                    if (loadPath == null) {
                        return;
                    }
                    try {
                        BufferedImage loadedImage = ImageIO.read(loadPath);
                        if (!(loadedImage.getColorModel() instanceof IndexColorModel)) throw new Exception("Wrong color model");
                        unitProperty.getValue().setPalette(paletteIndex, loadedImage);
                        refresh();
                        unitList.refresh();
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                });
                Button addButton = new Button("Add palette");
                addButton.setOnAction(event -> {
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle("Import Palette from sprite");
                    chooser.getExtensionFilters().add(new ExtensionFilter("Image", "*.png"));
                    chooser.setInitialFileName("sprite");
                    File loadPath = chooser.showOpenDialog(spriteFlow.getScene().getWindow());
                    if (loadPath == null) {
                        return;
                    }
                    try {
                        BufferedImage loadedImage = ImageIO.read(loadPath);
                        if (!(loadedImage.getColorModel() instanceof IndexColorModel)) throw new Exception("Wrong color model");
                        unitProperty.getValue().addPalette(loadedImage);
                        refresh();
                        unitList.refresh();
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                });
                Label label = new Label(String.format("Palette %d", p));
                label.setStyle("-fx-font-size: 24");
                HBox paletteBox = new HBox(label, importButton, addButton);
                paletteBox.alignmentProperty().setValue(Pos.CENTER);
                paletteBox.setSpacing(10);
                VBox spriteBox = new VBox(paletteBox, spriteFlow);
                spriteBox.alignmentProperty().setValue(Pos.TOP_CENTER);
                //paletteBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                //spriteFlow.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                spriteVBoxList.add(spriteBox);
            }
            //spriteList.setItems(unitImageViews);
            spriteVBox.getChildren().setAll(spriteVBoxList);
    }

    //@FXML
    //public void addUnit() {
    //    if (unitList.getItems() != null) {
    //        int newIndex = unitList.getItems().size();
    //        unitList.getItems().add(new new UnitSprite(newIndex, null, null, null));
    //        unitList.getSelectionModel().selectLast();
    //    }
    //}

    @FXML
    public void copyUnit() {
        if (unitList.getItems() != null && unitList.getSelectionModel().getSelectedItem() != null) {
            int oldIndex = unitList.getSelectionModel().getSelectedIndex();
            UnitSprite unitSprite = unitList.getSelectionModel().getSelectedItem();
            ByteBuffer unitCgBytes = App.unitCgs.getFile(oldIndex).rewind();

            var savedSprite = unitSprite.saveSprites();
            UnitSst unitSst = App.unitSstList.get(oldIndex);

            UnitSst newUnitSst = new UnitSst(unitSst.toByteBuffer());
            unitSst.setCompressedValue(0x00FF, savedSprite.getKey());
            unitSst.setCompressedValue(0x00F0, unitSprite.savePalettes());
            App.unitSstList.add(newUnitSst);
            
            int numEntries = Short.toUnsignedInt(App.naUnitAnimTable.getShort(0)) + 1;
            int entryLength = Short.toUnsignedInt(App.naUnitAnimTable.getShort(2)) + 1;

            byte[] animBytes = new byte[entryLength];
            App.naUnitAnimTable.get(4 + oldIndex*entryLength, animBytes);

            ByteBuffer newTable = ByteBuffer.allocate(App.naUnitAnimTable.limit() + entryLength).order(ByteOrder.LITTLE_ENDIAN);

            newTable.put(App.naUnitAnimTable.rewind());
            newTable.put(animBytes);
            newTable.putShort(0, (short)numEntries);

            App.naUnitAnimTable = newTable;

            int newIndex = unitList.getItems().size();
            UnitSprite newUnitsprite = new UnitSprite(newIndex, newUnitSst.getSpriteMap(), newUnitSst.getPalettes(), unitCgBytes);

            unitList.getItems().add(newUnitsprite);
            unitList.getSelectionModel().selectLast();
        }
    }

    @FXML
    public void removeUnit() {
        if (unitList.getItems().size() > 0) {
            unitList.getItems().removeLast();
        }
    }

    public void loadSprites() {
        ObservableList<UnitSst> unitSstDataList = FXCollections.observableArrayList();
        ObservableList<UnitSprite> unitSprites = FXCollections.observableArrayList();
        // last file is empty??
        for (int i = 0; i < App.unitSsts.numFiles(); i++) {
            ByteBuffer unitCgBytes = App.unitCgs.getFile(i);
            ByteBuffer unitSstBytes = App.unitSsts.getFile(i);
            if (unitCgBytes == null) {
                System.err.println(String.format("Cg %d is null", i));
                continue;
            }
            if (unitSstBytes == null) {
                System.err.println(String.format("Sst %d is null", i));
                continue;
            }
            UnitSst unitSst = new UnitSst(unitSstBytes);
            unitSstDataList.add(unitSst);
            ByteBuffer spritePalette = unitSst.getPalettes();
            SpriteData spriteData = unitSst.getSpriteMap();
            UnitSprite unitSprite = new UnitSprite(i, spriteData, spritePalette, unitCgBytes);
            unitSprites.add(unitSprite);

            unitCgBytes.rewind();
            unitSstBytes.rewind();
        }
        App.unitSstList = unitSstDataList;
        unitList.setCellFactory(x -> new SpriteCell(0, 0, 2));
        unitList.setItems(unitSprites);
        App.unitSprites = unitSprites;

    }

    public void saveSprites() {
        App.unitSsts.setNumFiles(unitList.getItems().size());
        App.unitCgs.setNumFiles(unitList.getItems().size());
        for (int i = 0; i < unitList.getItems().size(); i++) {
            if (!unitList.getItems().get(i).hasChanged && !App.unitSstList.get(i).hasChanged) continue;
            //ByteBuffer unitSstBytes = App.unitSsts.getFile(i);
            //UnitSst unitSst = new UnitSst(unitSstBytes);
            UnitSst unitSst = App.unitSstList.get(i);
            var savedSprite = unitList.getItems().get(i).saveSprites();
            //SstHeaderNode node = unitSst.find(0x00FF);
            //ByteBuffer newCompressedValue = ByteBuffer.allocate(savedSprite.getKey().capacity()+4);
            //newCompressedValue.putShort(savedSprite.getKey().getShort(1));
            //for (int j = 2; j < 4; j++) {
            //    newCompressedValue.put(node.compressedValue[j]);
            //}
            //newCompressedValue.put(savedSprite.getKey());
            //unitSst.find(0x00FF).compressedValue = newCompressedValue.array();
            unitSst.setCompressedValue(0x00FF, savedSprite.getKey());
            unitSst.setCompressedValue(0x00F0, unitList.getItems().get(i).savePalettes());
            App.unitSsts.setFile(i, unitSst.toByteBuffer());
            App.unitCgs.setFile(i, savedSprite.getValue());
        }
        // Pair<ByteBuffer, ByteBuffer> sstIdxPak = App.unitSsts.repack();
        // App.archive.setFile("char/rom/rom_idx/UnitSst.rom_idx", sstIdxPak.getKey());
        // App.archive.setFile("char/rom/pak/UnitSst.pak", sstIdxPak.getValue());
        
        // Pair<ByteBuffer, ByteBuffer> cgIdxPak = App.unitCgs.repack();
        // App.archive.setFile("char/rom/rom_idx/UnitCg.rom_idx", cgIdxPak.getKey());
        // App.archive.setFile("char/rom/pak/UnitCg.pak", cgIdxPak.getValue());

    }
}