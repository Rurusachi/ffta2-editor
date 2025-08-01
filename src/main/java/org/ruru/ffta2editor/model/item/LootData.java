package org.ruru.ffta2editor.model.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.TextController.StringWithId;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class LootData extends ItemData {
    public ObjectProperty<Short> buy = new SimpleObjectProperty<>();
    public ObjectProperty<Short> sell = new SimpleObjectProperty<>();
    public ObjectProperty<LootCategory> category = new SimpleObjectProperty<>();
    public ObjectProperty<LootRank> rank = new SimpleObjectProperty<>();
    public ObjectProperty<Short> _0x6 = new SimpleObjectProperty<>();

    public LootData(ByteBuffer bytes, int id) {
        if (id < App.itemNames.size()) {
            this.name = App.itemNames.get(id).string();
        } else {
            this.name = new SimpleStringProperty("");
        }
        if (id < App.itemDescriptions.size()) {
            this.description = App.itemDescriptions.get(id).string();
        } else {
            this.description = new SimpleStringProperty("\\var2:00\\\\end\\");
        }
        this.id = id;

        buy.set(bytes.getShort());
        sell.set(bytes.getShort());
        category.set(LootCategory.fromInteger(Byte.toUnsignedInt(bytes.get())));
        rank.set(LootRank.fromInteger(Byte.toUnsignedInt(bytes.get())));
        _0x6.set(bytes.getShort());
    }

    public LootData(String name, int id) {
        if (id < App.itemNames.size()) {
            this.name = App.itemNames.get(id).string();
        } else {
            this.name = new SimpleStringProperty(name);
            App.itemNames.add(new StringWithId(id, this.name));
        }
        if (id < App.itemDescriptions.size()) {
            this.description = App.itemDescriptions.get(id).string();
        } else {
            this.description = new SimpleStringProperty("\\var2:00\\\\end\\");
            App.itemDescriptions.add(new StringWithId(id, this.description));
        }
        this.id = id;

        buy.set((short)0);
        sell.set((short)0);
        category.set(LootCategory.NONE);
        rank.set(LootRank.ZERO_STAR);
        _0x6.set((short)0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x8).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(buy.getValue());
        buffer.putShort(sell.getValue());
        buffer.put(category.getValue().value);
        buffer.put(rank.getValue().value);
        buffer.putShort(_0x6.getValue());

        return buffer.array();
    }
}
