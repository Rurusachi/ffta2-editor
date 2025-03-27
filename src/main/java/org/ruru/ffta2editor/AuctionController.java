package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import org.ruru.ffta2editor.EquipmentController.ItemCell;
import org.ruru.ffta2editor.TextController.StringPropertyCell;
import org.ruru.ffta2editor.model.auction.AuctionInfo;
import org.ruru.ffta2editor.model.auction.AuctionPrizeTable;
import org.ruru.ffta2editor.model.auction.AuctionPrizeTable.AuctionPrizeItem;
import org.ruru.ffta2editor.model.item.ItemData;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.ShortChangeListener;
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

public class AuctionController {
    
    public static class AuctionInfoCell extends ListCell<AuctionInfo> {
        Label label = new Label();

        public AuctionInfoCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(AuctionInfo auction, boolean empty) {
            super.updateItem(auction, empty);
            if (auction != null) {
                label.setText(String.format("%X: %s", auction.id, auction.regionString.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }
    
    public static class AuctionPrizeTableCell extends ListCell<AuctionPrizeTable> {
        Label label = new Label();

        public AuctionPrizeTableCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(AuctionPrizeTable table, boolean empty) {
            super.updateItem(table, empty);
            if (table != null) {
                label.setText(String.format("Table %X", table.id));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }
    
    public static class AuctionPrizeCell extends ListCell<AuctionPrizeItem> {
        Label label = new Label();

        public AuctionPrizeCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(AuctionPrizeItem prize, boolean empty) {
            super.updateItem(prize, empty);
            if (prize != null) {
                label.setText(String.format("%X: %s", prize.item.getValue().id, prize.item.getValue().name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    @FXML ListView<AuctionInfo> auctionInfoList;
    @FXML ListView<AuctionPrizeTable> auctionPrizeTableList;
    @FXML ListView<AuctionPrizeItem> auctionPrizeTableItemList;
    @FXML ListView<AuctionPrizeItem> auctionGrandPrizeTableItemList;

    // Auction Info
    // ComboBox
    @FXML ComboBox<StringProperty> region;
    @FXML ComboBox<StringProperty> otherRegion1;
    @FXML ComboBox<StringProperty> otherRegion2;

    // Short
    @FXML TextField auctionStoryRequirement;
    @FXML TextField entryFee;

    // Byte
    @FXML TextField auctionFlagRequirement;
    @FXML TextField _0x06;
    @FXML TextField _0x07;
    @FXML TextField _0x0c;
    @FXML TextField _0x0f;


    // Auction Prize
    @FXML ComboBox<ItemData> prize;

    // Short
    @FXML TextField prizeFlagRequirement;


    // Auction Grand Prize
    @FXML ComboBox<ItemData> grandPrize;

    // Short
    @FXML TextField grandPrizeFlagRequirement;


    private ObjectProperty<AuctionInfo> auctionInfoProperty = new SimpleObjectProperty<>();
    private ObjectProperty<AuctionPrizeTable> auctionPrizeTableProperty = new SimpleObjectProperty<>();
    private ObjectProperty<AuctionPrizeItem> auctionPrizeItemProperty = new SimpleObjectProperty<>();
    private ObjectProperty<AuctionPrizeItem> auctionGrandPrizeItemProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        auctionInfoList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindAuctionData();
            auctionInfoProperty.setValue(newValue);
            auctionPrizeTableList.getSelectionModel().clearSelection();
            auctionGrandPrizeTableItemList.getSelectionModel().clearSelection();
            if (newValue != null) bindAuctionData();
        });
        auctionPrizeTableList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindAuctionPrizeTableData();
            auctionPrizeTableProperty.setValue(newValue);
            auctionPrizeTableItemList.getSelectionModel().clearSelection();
            if (newValue != null) bindAuctionPrizeTableData();
        });
        auctionPrizeTableItemList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindAuctionPrizeItemData();
            auctionPrizeItemProperty.setValue(newValue);
            if (newValue != null) bindAuctionPrizeItemData();
        });
        auctionGrandPrizeTableItemList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindAuctionGrandPrizeItemData();
            auctionGrandPrizeItemProperty.setValue(newValue);
            if (newValue != null) bindAuctionGrandPrizeItemData();
        });
        
        // Data validators
        auctionStoryRequirement.textProperty().addListener(new ShortChangeListener(auctionStoryRequirement));
        entryFee.textProperty().addListener(new ShortChangeListener(entryFee));

        auctionFlagRequirement.textProperty().addListener(new ByteChangeListener(auctionFlagRequirement));
        _0x06.textProperty().addListener(new ByteChangeListener(_0x06));
        _0x07.textProperty().addListener(new ByteChangeListener(_0x07));
        _0x0c.textProperty().addListener(new ByteChangeListener(_0x0c));
        _0x0f.textProperty().addListener(new ByteChangeListener(_0x0f));
        
        prizeFlagRequirement.textProperty().addListener(new ShortChangeListener(prizeFlagRequirement));
    }
        
    private void unbindAuctionData() {
        auctionInfoProperty.getValue().region.unbind();
        auctionInfoProperty.getValue().otherRegion1.unbind();
        auctionInfoProperty.getValue().otherRegion2.unbind();
        
        auctionStoryRequirement.textProperty().unbindBidirectional(auctionInfoProperty.getValue().storyRequirement);
        entryFee.textProperty().unbindBidirectional(auctionInfoProperty.getValue().entryFee);

        auctionFlagRequirement.textProperty().unbindBidirectional(auctionInfoProperty.getValue().flagRequirement);
        _0x06.textProperty().unbindBidirectional(auctionInfoProperty.getValue()._0x06);
        _0x07.textProperty().unbindBidirectional(auctionInfoProperty.getValue()._0x07);
        _0x0c.textProperty().unbindBidirectional(auctionInfoProperty.getValue()._0x0c);
        _0x0f.textProperty().unbindBidirectional(auctionInfoProperty.getValue()._0x0f);

        auctionPrizeTableList.setItems(null);
        auctionGrandPrizeTableItemList.setItems(null);
    }

    private void bindAuctionData() {
        
        region.getSelectionModel().select(Byte.toUnsignedInt(auctionInfoProperty.getValue().region.getValue()));
        auctionInfoProperty.getValue().region.bind(new ObjectBinding<Byte>() {
            {bind(region.valueProperty());}
            @Override
            protected Byte computeValue() {
                return (byte)App.regionNames.indexOf(region.valueProperty().getValue());
            }

        });
        
        otherRegion1.getSelectionModel().select(Byte.toUnsignedInt(auctionInfoProperty.getValue().otherRegion1.getValue()));
        auctionInfoProperty.getValue().otherRegion1.bind(new ObjectBinding<Byte>() {
            {bind(otherRegion1.valueProperty());}
            @Override
            protected Byte computeValue() {
                return (byte)App.regionNames.indexOf(otherRegion1.valueProperty().getValue());
            }

        });
        
        otherRegion2.getSelectionModel().select(Byte.toUnsignedInt(auctionInfoProperty.getValue().otherRegion2.getValue()));
        auctionInfoProperty.getValue().otherRegion2.bind(new ObjectBinding<Byte>() {
            {bind(otherRegion2.valueProperty());}
            @Override
            protected Byte computeValue() {
                return (byte)App.regionNames.indexOf(otherRegion2.valueProperty().getValue());
            }

        });


        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(auctionStoryRequirement.textProperty(), auctionInfoProperty.getValue().storyRequirement, unsignedShortConverter);
        Bindings.bindBidirectional(entryFee.textProperty(), auctionInfoProperty.getValue().entryFee, unsignedShortConverter);

        StringConverter<Byte> unsignedByteConverter = new UnsignedByteStringConverter();
        Bindings.bindBidirectional(auctionFlagRequirement.textProperty(), auctionInfoProperty.getValue().flagRequirement, unsignedByteConverter);
        Bindings.bindBidirectional(_0x06.textProperty(), auctionInfoProperty.getValue()._0x06, unsignedByteConverter);
        Bindings.bindBidirectional(_0x07.textProperty(), auctionInfoProperty.getValue()._0x07, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0c.textProperty(), auctionInfoProperty.getValue()._0x0c, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0f.textProperty(), auctionInfoProperty.getValue()._0x0f, unsignedByteConverter);

        auctionPrizeTableList.setItems(auctionInfoProperty.getValue().prizeTables);
        auctionGrandPrizeTableItemList.setItems(auctionInfoProperty.getValue().grandPrizeTable.getValue().prizes);
    }

    private void unbindAuctionPrizeTableData() {

        auctionPrizeTableItemList.setItems(null);
    }

    private void bindAuctionPrizeTableData() {

        auctionPrizeTableItemList.setItems(auctionPrizeTableProperty.getValue().prizes);
    }

    private void unbindAuctionPrizeItemData() {
        prize.valueProperty().unbindBidirectional(auctionPrizeItemProperty.getValue().item);
        prizeFlagRequirement.textProperty().unbindBidirectional(auctionPrizeItemProperty.getValue().flagRequirement);
    }

    private void bindAuctionPrizeItemData() {
        prize.valueProperty().bindBidirectional(auctionPrizeItemProperty.getValue().item);
        
        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(prizeFlagRequirement.textProperty(), auctionPrizeItemProperty.getValue().flagRequirement, unsignedShortConverter);
    }

    private void unbindAuctionGrandPrizeItemData() {
        grandPrize.valueProperty().unbindBidirectional(auctionGrandPrizeItemProperty.getValue().item);
        grandPrizeFlagRequirement.textProperty().unbindBidirectional(auctionGrandPrizeItemProperty.getValue().flagRequirement);
    }

    private void bindAuctionGrandPrizeItemData() {
        grandPrize.valueProperty().bindBidirectional(auctionGrandPrizeItemProperty.getValue().item);
        
        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(grandPrizeFlagRequirement.textProperty(), auctionGrandPrizeItemProperty.getValue().flagRequirement, unsignedShortConverter);
    }

    public void loadAuctions() {
        if (App.archive != null) {

            // Prize Tables
            ByteBuffer auctionPrizeTableBytes = App.sysdata.getFile(33);

            if (auctionPrizeTableBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            auctionPrizeTableBytes.rewind();

            ObservableList<AuctionPrizeTable> auctionPrizeTableDataList = FXCollections.observableArrayList();

            int numAuctionPrizeTables = Byte.toUnsignedInt(App.arm9.get(0x000cb808))+1;
            for (int i = 0; i < numAuctionPrizeTables; i++) {
                AuctionPrizeTable auctionPrizeTableData = new AuctionPrizeTable(auctionPrizeTableBytes, i);
                auctionPrizeTableDataList.add(auctionPrizeTableData);
            }
            App.auctionPrizeTableList = auctionPrizeTableDataList;
            auctionPrizeTableList.setItems(auctionPrizeTableDataList);
            auctionPrizeTableList.setCellFactory(x -> new AuctionPrizeTableCell());

            auctionPrizeTableBytes.rewind();

            // Grand Prize Tables
            ByteBuffer auctionGrandPrizeTableBytes = App.sysdata.getFile(34);

            if (auctionGrandPrizeTableBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            auctionGrandPrizeTableBytes.rewind();

            ObservableList<AuctionPrizeTable> auctionGrandPrizeTableDataList = FXCollections.observableArrayList();

            int numAuctionGrandPrizeTables = Byte.toUnsignedInt(App.arm9.get(0x000cb840))+1;
            for (int i = 0; i < numAuctionGrandPrizeTables; i++) {
                AuctionPrizeTable auctionGrandPrizeTableData = new AuctionPrizeTable(auctionGrandPrizeTableBytes, i);
                auctionGrandPrizeTableDataList.add(auctionGrandPrizeTableData);
            }
            App.auctionGrandPrizeTableList = auctionGrandPrizeTableDataList;

            auctionGrandPrizeTableBytes.rewind();

            // Auction Infos
            ByteBuffer auctionInfoBytes = App.sysdata.getFile(32);

            if (auctionInfoBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            auctionInfoBytes.rewind();

            ObservableList<AuctionInfo> auctionInfoDataList = FXCollections.observableArrayList();

            int numAuctionInfos = Byte.toUnsignedInt(App.arm9.get(0x000cb7d0))+1;
            for (int i = 0; i < numAuctionInfos; i++) {
                AuctionInfo auctionInfoData = new AuctionInfo(auctionInfoBytes, i);
                auctionInfoDataList.add(auctionInfoData);
            }
            auctionInfoList.setItems(auctionInfoDataList);
            auctionInfoList.setCellFactory(x -> new AuctionInfoCell());

            auctionInfoBytes.rewind();

            
            prize.setItems(App.itemList);
            prize.setCellFactory(x -> new ItemCell<>());
            prize.setButtonCell(new ItemCell<>());

            grandPrize.setItems(App.itemList);
            grandPrize.setCellFactory(x -> new ItemCell<>());
            grandPrize.setButtonCell(new ItemCell<>());

            region.setItems(App.regionNames);
            region.setCellFactory(x -> new StringPropertyCell());
            region.setButtonCell(new StringPropertyCell());

            otherRegion1.setItems(App.regionNames);
            otherRegion1.setCellFactory(x -> new StringPropertyCell());
            otherRegion1.setButtonCell(new StringPropertyCell());

            otherRegion2.setItems(App.regionNames);
            otherRegion2.setCellFactory(x -> new StringPropertyCell());
            otherRegion2.setButtonCell(new StringPropertyCell());
            
            auctionPrizeTableItemList.setCellFactory(x -> new AuctionPrizeCell());
            
            auctionGrandPrizeTableItemList.setCellFactory(x -> new AuctionPrizeCell());
        }
    }

    public void saveAuctions() {
        List<AuctionPrizeTable> auctionPrizeTables = App.auctionPrizeTableList;
        ByteBuffer newAuctionPrizeTableDataBytes = ByteBuffer.allocate(auctionPrizeTables.size()*0x20).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < auctionPrizeTables.size(); i++) {
            newAuctionPrizeTableDataBytes.put(auctionPrizeTables.get(i).toBytes());
        }
        newAuctionPrizeTableDataBytes.rewind();
        App.sysdata.setFile(33, newAuctionPrizeTableDataBytes);


        List<AuctionPrizeTable> auctionGrandPrizeTables = App.auctionPrizeTableList;
        ByteBuffer newAuctionGrandPrizeTableDataBytes = ByteBuffer.allocate(auctionGrandPrizeTables.size()*0x20).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < auctionGrandPrizeTables.size(); i++) {
            newAuctionGrandPrizeTableDataBytes.put(auctionGrandPrizeTables.get(i).toBytes());
        }
        newAuctionGrandPrizeTableDataBytes.rewind();
        App.sysdata.setFile(34, newAuctionGrandPrizeTableDataBytes);


        List<AuctionInfo> auctionInfos = auctionInfoList.getItems();
        ByteBuffer newAuctionInfoDataBytes = ByteBuffer.allocate(auctionInfos.size()*0x10).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < auctionInfos.size(); i++) {
            newAuctionInfoDataBytes.put(auctionInfos.get(i).toBytes());
        }
        newAuctionInfoDataBytes.rewind();
        App.sysdata.setFile(32, newAuctionInfoDataBytes);

    }
}
