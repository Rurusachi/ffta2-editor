package org.ruru.ffta2editor.model.auction;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.item.ItemData;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuctionPrizeTable {
    public int id;

    public static class AuctionPrizeItem {
        public ObjectProperty<ItemData> item = new SimpleObjectProperty<>();
        public ObjectProperty<Short> flagRequirement = new SimpleObjectProperty<>();

        public AuctionPrizeItem(short itemId, short flag) {
            this.item.set(App.itemList.get(Short.toUnsignedInt(itemId)));
            this.flagRequirement.set(flag);
        }

        public byte[] toBytes() {
            ByteBuffer buffer = ByteBuffer.allocate(0x4).order(ByteOrder.LITTLE_ENDIAN);

            buffer.putShort((short)item.getValue().id);
            buffer.putShort(flagRequirement.getValue());

            return buffer.array();
        }
    }

    public ListProperty<AuctionPrizeItem> prizes = new SimpleListProperty<>();

    public AuctionPrizeTable(ByteBuffer bytes, int id) {
        this.id = id;

        ObservableList<AuctionPrizeItem> prizeList = FXCollections.observableArrayList();
        for (int i = 0; i < 8; i++) {
            AuctionPrizeItem item = new AuctionPrizeItem(bytes.getShort(), bytes.getShort());
            prizeList.add(item);
        }
        prizes.set(prizeList);
    }

    public AuctionPrizeTable(int id) {
        this.id = id;

        ObservableList<AuctionPrizeItem> prizeList = FXCollections.observableArrayList();
        for (int i = 0; i < 8; i++) {
            AuctionPrizeItem item = new AuctionPrizeItem((short)0, (short)0);
            prizeList.add(item);
        }
        prizes.set(prizeList);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x20).order(ByteOrder.LITTLE_ENDIAN);
        
        for (AuctionPrizeItem prize : prizes) {
            buffer.put(prize.toBytes());
        }

        return buffer.array();
    }
}
