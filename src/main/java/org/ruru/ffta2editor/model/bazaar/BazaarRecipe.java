package org.ruru.ffta2editor.model.bazaar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.item.ItemData;
import org.ruru.ffta2editor.model.item.LootData;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class BazaarRecipe {
    public int id;
    public ItemData item;

    public ObjectProperty<LootData> loot1 = new SimpleObjectProperty<>();
    public ObjectProperty<LootData> loot2 = new SimpleObjectProperty<>();
    public ObjectProperty<LootData> loot3 = new SimpleObjectProperty<>();
    public ObjectProperty<Short> _0x6 = new SimpleObjectProperty<>();

    public BazaarRecipe(ByteBuffer bytes, int id) {
        this.id = id;
        this.item = App.itemList.get(id);

        int abilityId;

        abilityId = Short.toUnsignedInt(bytes.getShort());
        if (abilityId != 0) {
            loot1.set((LootData)App.itemList.get(abilityId));
        } else {
            loot1.set(App.lootList.get(0));
        }

        abilityId = Short.toUnsignedInt(bytes.getShort());
        if (abilityId != 0) {
            loot2.set((LootData)App.itemList.get(abilityId));
        } else {
            loot2.set(App.lootList.get(0));
        }

        abilityId = Short.toUnsignedInt(bytes.getShort());
        if (abilityId != 0) {
            loot3.set((LootData)App.itemList.get(abilityId));
        } else {
            loot3.set(App.lootList.get(0));
        }
        //loot1.set((LootData)App.itemList.get(Short.toUnsignedInt(bytes.getShort())));
        //loot2.set((LootData)App.itemList.get(Short.toUnsignedInt(bytes.getShort())));
        //loot3.set((LootData)App.itemList.get(Short.toUnsignedInt(bytes.getShort())));
        _0x6.set(bytes.getShort());
    }

    public BazaarRecipe(int id) {
        this.id = id;
        this.item = App.itemList.get(id);

        loot1.set(App.lootList.get(0));
        loot2.set(App.lootList.get(0));
        loot3.set(App.lootList.get(0));
        _0x6.set((short)0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x8).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort((short)loot1.getValue().id);
        buffer.putShort((short)loot2.getValue().id);
        buffer.putShort((short)loot3.getValue().id);
        buffer.putShort(_0x6.getValue());

        return buffer.array();
    }
}
