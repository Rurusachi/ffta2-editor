package org.ruru.ffta2editor.model.auction;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuctionInfo {
    public int id;
    public StringProperty regionString = new SimpleStringProperty();
    public StringProperty otherRegion1String = new SimpleStringProperty();
    public StringProperty otherRegion2String = new SimpleStringProperty();

    public ObjectProperty<Byte> region = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> flagRequirement = new SimpleObjectProperty<>();
    public ObjectProperty<Short> storyRequirement = new SimpleObjectProperty<>();
    public ObjectProperty<Short> entryFee = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x06 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x07 = new SimpleObjectProperty<>();

    //public ObjectProperty<Byte> prizeTable1 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> prizeTable2 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> prizeTable3 = new SimpleObjectProperty<>();
    //public ObjectProperty<Byte> prizeTable4 = new SimpleObjectProperty<>();
    public ListProperty<AuctionPrizeTable> prizeTables = new SimpleListProperty<>();

    public ObjectProperty<Byte> _0x0c = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> otherRegion1 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> otherRegion2 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x0f = new SimpleObjectProperty<>();


    public ObjectProperty<AuctionPrizeTable> grandPrizeTable = new SimpleObjectProperty<>();

    public AuctionInfo(ByteBuffer bytes, int id) {
        this.id = id;

        region.set(bytes.get());
        flagRequirement.set(bytes.get());
        storyRequirement.set(bytes.getShort());
        entryFee.set(bytes.getShort());
        _0x06.set(bytes.get());
        _0x07.set(bytes.get());

        ObservableList<AuctionPrizeTable> prizeTableList = FXCollections.observableArrayList();
        for (int i = 0; i < 4; i++) {
            prizeTableList.add(App.auctionPrizeTableList.get(bytes.get()));
        }
        prizeTables.set(prizeTableList);

        _0x0c.set(bytes.get());
        otherRegion1.set(bytes.get());
        otherRegion2.set(bytes.get());
        _0x0f.set(bytes.get());

        grandPrizeTable.set(App.auctionGrandPrizeTableList.get(id));

        regionString.bind(new StringBinding() {
            {bind(region);}
            @Override
            protected String computeValue() {
                int index = Byte.toUnsignedInt(region.getValue());
                if (index < App.regionNames.size()) {
                    return App.regionNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });

        otherRegion1String.bind(new StringBinding() {
            {bind(otherRegion1);}
            @Override
            protected String computeValue() {
                int index = Byte.toUnsignedInt(otherRegion1.getValue());
                if (index < App.regionNames.size()) {
                    return App.regionNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });

        otherRegion2String.bind(new StringBinding() {
            {bind(otherRegion2);}
            @Override
            protected String computeValue() {
                int index = Byte.toUnsignedInt(otherRegion2.getValue());
                if (index < App.regionNames.size()) {
                    return App.regionNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });
    }

    public AuctionInfo(int id) {
        this.id = id;

        region.set((byte)0);
        flagRequirement.set((byte)0);
        storyRequirement.set((short)0);
        entryFee.set((short)0);
        _0x06.set((byte)0);
        _0x07.set((byte)0);

        ObservableList<AuctionPrizeTable> prizeTableList = FXCollections.observableArrayList();
        for (int i = 0; i < 4; i++) {
            prizeTableList.add(App.auctionPrizeTableList.get(0));
        }
        prizeTables.set(prizeTableList);

        _0x0c.set((byte)0);
        otherRegion1.set((byte)0);
        otherRegion2.set((byte)0);
        _0x0f.set((byte)0);

        grandPrizeTable.set(App.auctionGrandPrizeTableList.get(id));

        regionString.bind(new StringBinding() {
            {bind(region);}
            @Override
            protected String computeValue() {
                int index = Byte.toUnsignedInt(region.getValue());
                if (index < App.regionNames.size()) {
                    return App.regionNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });

        otherRegion1String.bind(new StringBinding() {
            {bind(otherRegion1);}
            @Override
            protected String computeValue() {
                int index = Byte.toUnsignedInt(otherRegion1.getValue());
                if (index < App.regionNames.size()) {
                    return App.regionNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });

        otherRegion2String.bind(new StringBinding() {
            {bind(otherRegion2);}
            @Override
            protected String computeValue() {
                int index = Byte.toUnsignedInt(otherRegion2.getValue());
                if (index < App.regionNames.size()) {
                    return App.regionNames.get(index).getValue();
                } else {
                    return "";
                }
            }
        });
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x10).order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(region.getValue());
        buffer.put(flagRequirement.getValue());
        buffer.putShort(storyRequirement.getValue());
        buffer.putShort(entryFee.getValue());
        buffer.put(_0x06.getValue());
        buffer.put(_0x07.getValue());
        
        for (int i = 0; i < 4; i++) {
            if (i < prizeTables.size()) {
                buffer.put((byte)prizeTables.get(i).id);
            } else {
                buffer.put((byte)0);
            }
        }

        buffer.put(_0x0c.getValue());
        buffer.put(otherRegion1.getValue());
        buffer.put(otherRegion2.getValue());
        buffer.put(_0x0f.getValue());

        return buffer.array();
    }
}
