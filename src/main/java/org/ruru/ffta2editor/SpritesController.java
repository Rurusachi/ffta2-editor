package org.ruru.ffta2editor;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.ruru.ffta2editor.model.topSprite.TopSprite;
import org.ruru.ffta2editor.model.unitFace.UnitFace;
import org.ruru.ffta2editor.model.unitSst.SpriteData;
import org.ruru.ffta2editor.model.unitSst.UnitAnimation;
import org.ruru.ffta2editor.model.unitSst.UnitAnimation.UnitAnimationFrame;
import org.ruru.ffta2editor.model.unitSst.UnitSst;
import org.ruru.ffta2editor.utility.UnitSprite;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class SpritesController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");
    
    public static class SpriteCell extends ListCell<UnitSprite> {
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
                image.setImage(SwingFXUtils.toFXImage(unitSprite.getSprite(spriteIndex, paletteIndex, scale), null));
            } else {
                image.setImage(null);
            }
            setGraphic(image);
        }
    }

    public static class TopSpriteCell extends ListCell<TopSprite> {
        ImageView image = new ImageView();
        int spriteIndex;
        int scale;


        public TopSpriteCell(int spriteIndex, int scale) {
            this.spriteIndex = spriteIndex;
            this.scale = scale;
        }

        @Override protected void updateItem(TopSprite topSprite, boolean empty) {
            super.updateItem(topSprite, empty);
            if (topSprite != null && topSprite.sprites.size() > 0) {
                image.setImage(SwingFXUtils.toFXImage(topSprite.getSprite(spriteIndex, 2), null));
            } else {
                image.setImage(null);
            }
            setGraphic(image);
        }
    }
    
    public static class FaceCell extends ListCell<UnitFace> {
        ImageView image = new ImageView();

        public FaceCell() {
        }

        @Override protected void updateItem(UnitFace face, boolean empty) {
            super.updateItem(face, empty);
            if (face != null) {
                image.setImage(SwingFXUtils.toFXImage(face.getImage(1), null));
            } else {
                image.setImage(null);
            }
            setGraphic(image);
        }
    }
    
    public static class UnitAnimationCell extends ListCell<UnitAnimation> {
        Label label = new Label();


        public UnitAnimationCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(UnitAnimation animation, boolean empty) {
            super.updateItem(animation, empty);
            if (animation != null) {
                label.setText(String.format("%04X (%d)", animation.key, animation.key >>> 8));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    private record SpriteRecord(ImageView scaledImage, int unitIndex, int spriteIndex, int paletteIndex) {};
    private record TopSpriteRecord(ImageView scaledImage, int id, int spriteIndex) {};

    private ObjectProperty<UnitSprite> unitProperty = new SimpleObjectProperty<>();
    private ObjectProperty<TopSprite> topSpriteProperty = new SimpleObjectProperty<>();
    private ObjectProperty<UnitFace> faceProperty = new SimpleObjectProperty<>();
    //private ArrayList<UnitSprite> unitSprites;

    @FXML ListView<UnitSprite> unitList;
    @FXML ListView<UnitAnimation> animationList;
    //@FXML ListView<ImageView> spriteList;
    @FXML VBox spriteVBox;
    
    @FXML ListView<TopSprite> topSpriteList;
    
    @FXML ListView<UnitFace> faceList;

    @FXML Tab animationsTab;
    @FXML ImageView animatedSprite;
    Timeline animationTimeline = new Timeline();

    @FXML
    public void initialize() {
        
        unitList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            unitProperty.setValue(newValue);
            if (newValue == null) return;
            refresh();
            
        });
        
        topSpriteList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            topSpriteProperty.setValue(newValue);
            if (newValue == null) return;
            refreshTopSprite();
        });
        
        faceList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            faceProperty.setValue(newValue);
            if (newValue == null) return;
            refreshFace();
        });

        animationList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //if (oldValue != null) unbindAbilityData();
            if (newValue == null) return;
            playAnimation(newValue);
            
        });

        animationList.setCellFactory(x -> new UnitAnimationCell());
        
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private FileChooser getImageFileDialog(String title, String initialFilename) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Sprite");
        chooser.getExtensionFilters().add(new ExtensionFilter("Image", "*.png"));
        chooser.setInitialFileName(initialFilename);
        chooser.setInitialDirectory(App.getLastFile());

        return chooser;
    }

    private File showSaveImageFileDialog(String title, String initialFilename) {
        File filePath = getImageFileDialog(title, initialFilename).showSaveDialog(unitList.getScene().getWindow());
        if (filePath != null) {
            App.saveLastFile(filePath);
            App.saveConfig();
        }
        return filePath;
    }

    private File showOpenImageFileDialog(String title, String initialFilename) {
        File filePath = getImageFileDialog(title, initialFilename).showOpenDialog(unitList.getScene().getWindow());
        if (filePath != null) {
            App.saveLastFile(filePath);
            App.saveConfig();
        }
        return filePath;
    }

    public void refresh() {
        UnitSst sst = App.unitSstList.get(unitProperty.getValue().unitIndex);
        List<UnitAnimation> animations = sst.asList().stream().filter(node -> node.key != 0xFF && node.key != 0xF0).map(node -> sst.getAnimation(node.key)).filter(x -> x != null).sorted(Comparator.comparingInt(x -> x.key)).toList();
        animationList.setItems(FXCollections.observableArrayList(animations));
        animationsTab.setDisable(false);

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
                Button exportSpriteButton = new Button("Export");
                exportSpriteButton.setOnAction(event -> {

                    File savePath = showSaveImageFileDialog("Export Sprite", String.format("unit%03d_%d_%d",  spriteRecord.unitIndex(), spriteRecord.spriteIndex(), spriteRecord.paletteIndex()));
                    if (savePath == null) {
                        return;
                    }
                    BufferedImage image = unitProperty.getValue().getSprite(spriteRecord.spriteIndex(), spriteRecord.paletteIndex());
                    try {
                        ImageIO.write(image, "png", savePath);
                    } catch (Exception e) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                        System.err.println(e);
                    }
                });
                Button importSpriteButton = new Button("Import");
                importSpriteButton.setOnAction(event -> {
                    File loadPath = showOpenImageFileDialog("Import Sprite", String.format("unit%03d_%d_%d",  spriteRecord.unitIndex(), spriteRecord.spriteIndex(), spriteRecord.paletteIndex()));
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
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                        System.err.println(e);
                    }
                });
                VBox spriteBox = new VBox(spriteRecord.scaledImage(), exportSpriteButton, importSpriteButton);
                spriteBox.alignmentProperty().setValue(Pos.CENTER);
                return spriteBox;
            }).toList());
            //spriteVBox.getChildren().add(spriteFlow);
            Button replacePaletteButton = new Button("Replace palette");
            final int paletteIndex = p;
            replacePaletteButton.setOnAction(event -> {
                File loadPath = showOpenImageFileDialog("Import Palette from sprite", "sprite");
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
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    System.err.println(e);
                }
            });
            Button addPaletteButton = new Button("Add palette");
            addPaletteButton.setOnAction(event -> {
                File loadPath = showOpenImageFileDialog("Import Palette from sprite", "sprite");
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
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    System.err.println(e);
                }
            });
            Label label = new Label(String.format("Palette %d", p));
            label.setStyle("-fx-font-size: 24");
            HBox paletteBox = new HBox(label, replacePaletteButton, addPaletteButton);
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
    
    public void refreshTopSprite() {
        animationList.setItems(null);
        animationsTab.setDisable(true);

        ArrayList<VBox> spriteVBoxList = new ArrayList<>();
        ArrayList<TopSpriteRecord> topSpriteRecords = new ArrayList<>();
        for (int s = 0; s < topSpriteProperty.getValue().sprites.size(); s++) {
            BufferedImage image = topSpriteProperty.getValue().getSprite(s, 4);
            //unitImageViews.add(new ImageView(SwingFXUtils.toFXImage(image, null)));
            //Image temp = image.getScaledInstance(image.getWidth()*4, image.getHeight()*4, Image.SCALE_SMOOTH);
            topSpriteRecords.add(new TopSpriteRecord(new ImageView(SwingFXUtils.toFXImage(image, null)), topSpriteProperty.getValue().id, s));
        }
        FlowPane spriteFlow = new FlowPane();
        spriteFlow.getChildren().setAll(topSpriteRecords.stream().map(topSpriteRecord -> {
            Button exportSpriteButton = new Button("Export");
            exportSpriteButton.setOnAction(event -> {
                File savePath = showSaveImageFileDialog("Export Sprite", String.format("topSprite%03d_%d",  topSpriteRecord.id(), topSpriteRecord.spriteIndex()));
                if (savePath == null) {
                    return;
                }
                BufferedImage image = topSpriteProperty.getValue().getSprite(topSpriteRecord.spriteIndex());
                try {
                    ImageIO.write(image, "png", savePath);
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    System.err.println(e);
                }
            });
            Button importSpriteButton = new Button("Import");
            importSpriteButton.setOnAction(event -> {
                File loadPath = showOpenImageFileDialog("Import Sprite", String.format("unit%03d_%d",  topSpriteRecord.id(), topSpriteRecord.spriteIndex()));
                if (loadPath == null) {
                    return;
                }
                try {
                    BufferedImage loadedImage = ImageIO.read(loadPath);
                    if (!(loadedImage.getColorModel() instanceof IndexColorModel)) throw new Exception("Wrong color model");
                    topSpriteProperty.getValue().setSprite(topSpriteRecord.spriteIndex(), loadedImage);
                    refreshTopSprite();
                    topSpriteList.refresh();
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    System.err.println(e);
                }
            });
            VBox spriteBox = new VBox(topSpriteRecord.scaledImage(), exportSpriteButton, importSpriteButton);
            spriteBox.alignmentProperty().setValue(Pos.CENTER);
            return spriteBox;
        }).toList());
        Button replacePaletteButton = new Button("Replace palette");
        replacePaletteButton.setOnAction(event -> {
            File loadPath = showOpenImageFileDialog("Import Palette from sprite", "sprite");
            if (loadPath == null) {
                return;
            }
            try {
                BufferedImage loadedImage = ImageIO.read(loadPath);
                if (!(loadedImage.getColorModel() instanceof IndexColorModel)) throw new Exception("Wrong color model");
                topSpriteProperty.getValue().setPalette(loadedImage);
                refreshTopSprite();
                topSpriteList.refresh();
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.err.println(e);
            }
        });
        HBox paletteBox = new HBox(replacePaletteButton);
        paletteBox.alignmentProperty().setValue(Pos.CENTER);
        paletteBox.setSpacing(10);
        VBox spriteBox = new VBox(paletteBox, spriteFlow);
        spriteBox.alignmentProperty().setValue(Pos.TOP_CENTER);
        //paletteBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        //spriteFlow.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        spriteVBoxList.add(spriteBox);
        //spriteList.setItems(unitImageViews);
        spriteVBox.getChildren().setAll(spriteVBoxList);
    }
    
    public void refreshFace() {
        animationList.setItems(null);
        animationsTab.setDisable(true);

        UnitFace face = faceProperty.getValue();

        ArrayList<VBox> spriteVBoxList = new ArrayList<>();

        Button exportSpriteButton = new Button("Export Image");
        exportSpriteButton.setOnAction(event -> {
            File savePath = showSaveImageFileDialog("Export Image", String.format("face%03d",  face.id));
            if (savePath == null) {
                return;
            }
            BufferedImage image = face.getImage();
            try {
                ImageIO.write(image, "png", savePath);
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.err.println(e);
            }
        });
        Button exportTextureButton = new Button("Export Texture");
        exportTextureButton.setOnAction(event -> {
            File savePath = showSaveImageFileDialog("Export Texture", String.format("face%03d",  face.id));
            if (savePath == null) {
                return;
            }
            BufferedImage image = face.getTexture();
            try {
                ImageIO.write(image, "png", savePath);
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.err.println(e);
            }
        });
        Button importSpriteButton = new Button("Import");
        importSpriteButton.setOnAction(event -> {
            File loadPath = showOpenImageFileDialog("Import Sprite", String.format("unit%03d",  face.id));
            if (loadPath == null) {
                return;
            }
            try {
                BufferedImage loadedImage = ImageIO.read(loadPath);
                if (!(loadedImage.getColorModel() instanceof IndexColorModel)) throw new Exception("Wrong color model");
                int excessBytes = faceProperty.getValue().setTexture(loadedImage);
                if (excessBytes == 0) {
                    refreshFace();
                    faceList.refresh();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(String.format("Image too large (%d~ bytes)", excessBytes));
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.err.println(e);
            }
        });
        VBox faceBox = new VBox(new ImageView(SwingFXUtils.toFXImage(face.getImage(), null)));
        faceBox.alignmentProperty().setValue(Pos.CENTER);
        VBox textureBox = new VBox(new ImageView(SwingFXUtils.toFXImage(face.getTexture(), null)), exportSpriteButton, exportTextureButton, importSpriteButton);
        textureBox.alignmentProperty().setValue(Pos.CENTER);


        Button replacePaletteButton = new Button("Replace palette");
        replacePaletteButton.setOnAction(event -> {
            File loadPath = showOpenImageFileDialog("Import Palette from sprite", "sprite");
            if (loadPath == null) {
                return;
            }
            try {
                BufferedImage loadedImage = ImageIO.read(loadPath);
                if (!(loadedImage.getColorModel() instanceof IndexColorModel)) throw new Exception("Wrong color model");
                faceProperty.getValue().setPalette(loadedImage);
                refreshFace();
                faceList.refresh();
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.err.println(e);
            }
        });
        HBox paletteBox = new HBox(replacePaletteButton);
        paletteBox.alignmentProperty().setValue(Pos.CENTER);
        paletteBox.setSpacing(10);
        VBox spriteBox = new VBox(paletteBox, faceBox, textureBox);
        spriteBox.alignmentProperty().setValue(Pos.TOP_CENTER);
        spriteBox.setSpacing(20);
        //paletteBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        //spriteFlow.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        spriteVBoxList.add(spriteBox);
        //spriteList.setItems(unitImageViews);
        spriteVBox.getChildren().setAll(spriteVBoxList);
    }

    private void playAnimation(UnitAnimation animation) {
        if (animation == null) return;
        animationTimeline.stop();
        animationTimeline.getKeyFrames().clear();

        Duration frameTime = Duration.ZERO;
        List<WritableImage> images = Stream.of(animation.frames).map(frame -> SwingFXUtils.toFXImage(unitProperty.getValue().getSprite(frame.spriteIndex.getValue(), 0, 4), null)).toList();
        
        double maxHeight = images.stream().mapToDouble(x -> x.getHeight()).max().getAsDouble();
        double maxWidth = images.stream().mapToDouble(x -> x.getWidth()).max().getAsDouble();
        for (UnitAnimationFrame frame : animation.frames) {
            var image = SwingFXUtils.toFXImage(unitProperty.getValue().getSprite(frame.spriteIndex.getValue(), 0, 4), null);
            frameTime = frameTime.add(Duration.seconds(frame.duration.getValue()).divide(60));
            animationTimeline.getKeyFrames().add(new KeyFrame(frameTime, e -> animatedSprite.setImage(image)));
            animationTimeline.getKeyFrames().add(new KeyFrame(frameTime, e -> animatedSprite.setFitHeight(image.getHeight())));
            animationTimeline.getKeyFrames().add(new KeyFrame(frameTime, e -> animatedSprite.setFitWidth(image.getWidth())));
            animationTimeline.getKeyFrames().add(new KeyFrame(frameTime, e -> AnchorPane.setTopAnchor(animatedSprite, maxHeight - image.getHeight())));
            animationTimeline.getKeyFrames().add(new KeyFrame(frameTime, e -> AnchorPane.setLeftAnchor(animatedSprite, maxWidth - image.getWidth())));
            double facing = frame.propertyFlags.isMirrored.getValue() ? -1 : 1;
            animationTimeline.getKeyFrames().add(new KeyFrame(frameTime, e -> animatedSprite.setScaleX(facing)));
        }
        animationTimeline.play();
    }

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
            newUnitsprite.hasChanged = true;

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

    @FXML
    public void copyTopSprite() {
        if (topSpriteList.getItems() != null && topSpriteList.getSelectionModel().getSelectedItem() != null) {
            TopSprite topSprite = topSpriteList.getSelectionModel().getSelectedItem();
        
            int newIndex = topSpriteList.getItems().size();
            TopSprite newTopSprite = new TopSprite(ByteBuffer.wrap(topSprite.toBytes()).order(ByteOrder.LITTLE_ENDIAN), newIndex);
        
            newTopSprite.hasChanged = true;
            topSpriteList.getItems().add(newTopSprite);
            topSpriteList.getSelectionModel().selectLast();
        }
    }

    @FXML
    public void removeTopSprite() {
        if (topSpriteList.getItems().size() > 0) {
            topSpriteList.getItems().removeLast();
        }
    }

    @FXML
    public void copyFace() {
        if (faceList.getItems() != null && faceList.getSelectionModel().getSelectedItem() != null) {
            UnitFace face = faceList.getSelectionModel().getSelectedItem();
        
            int newIndex = faceList.getItems().size();
            UnitFace newTopSprite = new UnitFace(ByteBuffer.wrap(face.toBytes()).order(ByteOrder.LITTLE_ENDIAN), newIndex);
        
            newTopSprite.hasChanged = true;
            faceList.getItems().add(newTopSprite);
            faceList.getSelectionModel().selectLast();
        }
    }

    @FXML
    public void removeFace() {
        if (faceList.getItems().size() > 0) {
            faceList.getItems().removeLast();
        }
    }

    public void loadSprites() {
        ObservableList<UnitSst> unitSstDataList = FXCollections.observableArrayList();
        ObservableList<UnitSprite> unitSprites = FXCollections.observableArrayList();
        // last file is empty??
        logger.info("Loading Unit Sprites");
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
            try {
                UnitSst unitSst = new UnitSst(unitSstBytes);
                unitSstDataList.add(unitSst);
                ByteBuffer spritePalette = unitSst.getPalettes();
                SpriteData spriteData = unitSst.getSpriteMap();
                UnitSprite unitSprite = new UnitSprite(i, spriteData, spritePalette, unitCgBytes);
                unitSprites.add(unitSprite);
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to load Unit Sprite %d", i));
                throw e;
            }

            unitCgBytes.rewind();
            unitSstBytes.rewind();
        }
        App.unitSstList = unitSstDataList;
        unitList.setCellFactory(x -> new SpriteCell(0, 0, 2));
        unitList.setItems(unitSprites);
        App.unitSprites = unitSprites;

        
        logger.info("Loading Top Sprites");
        ObservableList<TopSprite> topSprites = FXCollections.observableArrayList();
        topSprites.addFirst(new TopSprite(0)); // Add blank entry
        // last file is empty??
        for (int i = 0; i < App.atl.numFiles(); i++) {
            ByteBuffer atlBytes = App.atl.getFile(i);
            if (atlBytes == null) {
                System.err.println(String.format("Atl %d is null", i));
                continue;
            }
            try {
                TopSprite topSprite = new TopSprite(atlBytes, i+1);
                topSprites.add(topSprite);
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to load Top Sprite %d", i));
                throw e;
            }

            atlBytes.rewind();
        }
        topSpriteList.setCellFactory(x -> new TopSpriteCell(0,2));
        topSpriteList.setItems(topSprites);
        App.topSprites = topSprites;

        
        logger.info("Loading Faces");
        ObservableList<UnitFace> faces = FXCollections.observableArrayList();
        faces.addFirst(new UnitFace(0)); // Add blank entry
        // last file is empty??
        for (int i = 0; i < App.face.numFiles(); i++) {
            ByteBuffer faceBytes = App.face.getFile(i);
            if (faceBytes == null) {
                System.err.println(String.format("Atl %d is null", i));
                continue;
            }
            try {
                UnitFace face = new UnitFace(faceBytes, i);
                faces.add(face);
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to load Face %d", i));
                throw e;
            }

            faceBytes.rewind();
        }
        faceList.setCellFactory(x -> new FaceCell());
        faceList.setItems(faces);
        App.unitFaces = faces;
        //UnitFace mostPiecedFace = faces.stream().sorted(Comparator.comparingInt(x -> x.pieces.length)).toList().getLast();
        //int maxWidth = faces.stream().flatMap(x -> Stream.of(x.pieces)).mapToInt(x -> x.sourceWidth).max().getAsInt();
        //int maxHeight = faces.stream().flatMap(x -> Stream.of(x.pieces)).mapToInt(x -> x.sourceHeight).max().getAsInt();
        //faces.stream().mapToInt(x -> x.width).distinct().forEach(x -> System.out.println(x));
        //System.out.println(String.format("Most pieces: %d (%d pieces)", mostPiecedFace.id, mostPiecedFace.pieces.length));
        //System.out.println(String.format("Max width: %d", maxWidth));
        //System.out.println(String.format("Max width: %d", maxHeight));
        


        //var keySets = unitSstDataList.stream().limit(61).map(sst -> new HashSet<>(sst.asList().stream().map(node -> node.animationId).toList())).toList();

        //var countedAnimations = new HashMap<Integer, Integer>();
        //var allAnimations = new HashSet<>();
        //var commonAnimations = new HashSet<>();
        //allAnimations.addAll(keySets.get(0));
        //commonAnimations.addAll(keySets.get(0));
        //for (int i = 1; i < keySets.size(); i++) {
        //    commonAnimations.retainAll(keySets.get(i));
        //    allAnimations.addAll(keySets.get(i));
        //    keySets.get(i).stream().forEach(key -> countedAnimations.merge((int)key, 1, Integer::sum));
        //}
        //allAnimations.removeAll(commonAnimations);

        //commonAnimations.stream().sorted().toList().forEach(x -> System.out.println(String.format("Shared: %02X", x)));
        //allAnimations.stream().sorted().toList().forEach(x -> System.out.println(String.format("Not Shared: %02X", x)));

        //countedAnimations.entrySet().stream().filter(e -> e.getValue() > 1).forEach(x -> System.out.println(String.format("Non-unique: %02X * %d", x.getKey(), x.getValue())));
        //countedAnimations.entrySet().stream().filter(e -> e.getValue() == 1).forEach(x -> System.out.println(String.format("Unique: %02X", x.getKey())));


        //var illuaAnimations = new HashSet<>(unitSstDataList.get(69).asList().stream().map(node -> node.animationId).toList());
        //var soldierAnimations = new HashSet<>(unitSstDataList.get(61).asList().stream().map(node -> node.animationId).toList());

        //var missingAnimations = new HashSet<>(commonAnimations);
        //missingAnimations.removeAll(illuaAnimations);

        //missingAnimations.stream().sorted().toList().forEach(x -> System.out.println(String.format("Missing: %02X", x)));
        //illuaAnimations.stream().sorted().toList().forEach(x -> System.out.println(String.format("Illua: %02X", x)));
    }

    public void saveSprites() {
        logger.info("Saving Unit Sprites");
        App.unitSsts.setNumFiles(unitList.getItems().size());
        App.unitCgs.setNumFiles(unitList.getItems().size());
        for (int i = 0; i < unitList.getItems().size(); i++) {
            if (!unitList.getItems().get(i).hasChanged && !App.unitSstList.get(i).hasChanged) continue;
            try {
                UnitSst unitSst = App.unitSstList.get(i);
                var savedSprite = unitList.getItems().get(i).saveSprites();
                
                unitSst.setCompressedValue(0x00FF, savedSprite.getKey());
                unitSst.setCompressedValue(0x00F0, unitList.getItems().get(i).savePalettes());
                App.unitSsts.setFile(i, unitSst.toByteBuffer());
                App.unitCgs.setFile(i, savedSprite.getValue());
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Unit Sprite %d", i));
                throw e;
            }

            //Path testPath = Path.of("G:\\zzz");
            //try {
            //    FileOutputStream testOutput = new FileOutputStream(testPath.resolve(String.format("unit_sst%03d", i)).toFile());
            //    testOutput.write(App.unitSstList.get(i).toBytes());
            //    testOutput.close();
            //} catch (Exception e) {
            //    System.err.println(e);
            //}
        }

        logger.info("Saving Top Sprites");
        App.atl.setNumFiles(topSpriteList.getItems().size()-1);
        for (TopSprite topSprite : topSpriteList.getItems()) {
            if (topSprite.id == 0 || !topSprite.hasChanged) continue;
            try {
                App.atl.setFile(topSprite.id-1, ByteBuffer.wrap(topSprite.toBytes()).order(ByteOrder.LITTLE_ENDIAN));
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Top Sprite %d", topSprite.id));
                throw e;
            }

        }

        logger.info("Saving Faces");
        App.face.setNumFiles(faceList.getItems().size());
        for (UnitFace face : faceList.getItems()) {
            if (face.id == 0 || !face.hasChanged) continue;
            try {
                App.face.setFile(face.id, ByteBuffer.wrap(face.toBytes()).order(ByteOrder.LITTLE_ENDIAN));
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Face %d", face.id));
                throw e;
            }

        }

    }
}