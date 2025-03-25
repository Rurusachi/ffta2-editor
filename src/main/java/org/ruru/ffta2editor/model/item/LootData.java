package org.ruru.ffta2editor.model.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class LootData extends ItemData {
    public ObjectProperty<Short> buy = new SimpleObjectProperty<>();
    public ObjectProperty<Short> sell = new SimpleObjectProperty<>();
    public ObjectProperty<LootCategory> category = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> rank = new SimpleObjectProperty<>();
    public ObjectProperty<Short> _0x6 = new SimpleObjectProperty<>();

    public LootData(ByteBuffer bytes, int id) {
        if (id < App.itemNames.size()) {
            this.name = App.itemNames.get(id);
        } else {
            this.name = new SimpleStringProperty("");
        }
        if (id < App.itemDescriptions.size()) {
            this.description = App.itemDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
        }
        this.id = id;

        buy.set(bytes.getShort());
        sell.set(bytes.getShort());
        category.set(LootCategory.fromInteger(Byte.toUnsignedInt(bytes.get())));
        rank.set(bytes.get());
        _0x6.set(bytes.getShort());
    }

    public LootData(String name, int id) {
        if (id < App.itemNames.size()) {
            this.name = App.itemNames.get(id);
        } else {
            this.name = new SimpleStringProperty(name);
            App.itemNames.add(this.name);
        }
        if (id < App.itemDescriptions.size()) {
            this.description = App.itemDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:000\\\\end\\");
            App.itemDescriptions.add(this.description);
        }
        this.id = id;

        buy.set((short)0);
        sell.set((short)0);
        category.set(LootCategory.NONE);
        rank.set((byte)0);
        _0x6.set((short)0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x8).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(buy.getValue());
        buffer.putShort(sell.getValue());
        buffer.put(category.getValue().value);
        buffer.put(rank.getValue());
        buffer.putShort(_0x6.getValue());

        return buffer.array();
    }
}
