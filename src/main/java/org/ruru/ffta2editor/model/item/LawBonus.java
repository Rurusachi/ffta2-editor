package org.ruru.ffta2editor.model.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class LawBonus {
    public int id;
    
    public ObjectProperty<ItemData> item1 = new SimpleObjectProperty<>();
    public ObjectProperty<ItemData> item2 = new SimpleObjectProperty<>();
    public ObjectProperty<ItemData> item3 = new SimpleObjectProperty<>();
    public ObjectProperty<ItemData> padding = new SimpleObjectProperty<>();
    
    public LawBonus(ByteBuffer bytes, int id) {
        this.id = id;

        item1.set(App.itemList.get(Short.toUnsignedInt(bytes.getShort())));
        item2.set(App.itemList.get(Short.toUnsignedInt(bytes.getShort())));
        item3.set(App.itemList.get(Short.toUnsignedInt(bytes.getShort())));
        padding.set(App.itemList.get(Short.toUnsignedInt(bytes.getShort())));
    }

    public LawBonus(int id) {
        this.id = id;

        item1.set(App.itemList.get(0));
        item2.set(App.itemList.get(0));
        item3.set(App.itemList.get(0));
        padding.set(App.itemList.get(0));
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x8).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort((short)item1.getValue().id);
        buffer.putShort((short)item2.getValue().id);
        buffer.putShort((short)item3.getValue().id);
        buffer.putShort((short)padding.getValue().id);

        return buffer.array();
    }

    @Override
    public String toString() {
        return String.format("%X: (%s, %s, %s)", id, item1.getValue().name.getValue(),
                                                            item2.getValue().name.getValue(),
                                                            item3.getValue().name.getValue());
    }
}
