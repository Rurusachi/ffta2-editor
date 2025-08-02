package org.ruru.ffta2editor.model.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ItemTable {
    public int id;
    
    public ObjectProperty<Short> item1 = new SimpleObjectProperty<>();
    public ObjectProperty<Short> item2 = new SimpleObjectProperty<>();
    public ObjectProperty<Short> item3 = new SimpleObjectProperty<>();
    public ObjectProperty<Short> item4 = new SimpleObjectProperty<>();

    public ItemTable(ByteBuffer bytes, int id) {
        this.id = id;

        item1.set(bytes.getShort());
        item2.set(bytes.getShort());
        item3.set(bytes.getShort());
        item4.set(bytes.getShort());
    }

    public ItemTable(int id) {
        this.id = id;

        item1.set((short)0);
        item2.set((short)0);
        item3.set((short)0);
        item4.set((short)0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x8).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(item1.getValue());
        buffer.putShort(item2.getValue());
        buffer.putShort(item3.getValue());
        buffer.putShort(item4.getValue());

        return buffer.array();
    }

    @Override
    public String toString() {
        
        if (id < 61 || id > 80) {
            return String.format("%X: (%s, %s, %s, %s)", id, App.itemList.get(item1.getValue()).name.getValue(),
                                                                    App.itemList.get(item2.getValue()).name.getValue(),
                                                                    App.itemList.get(item3.getValue()).name.getValue(), 
                                                                    App.itemList.get(item4.getValue()).name.getValue());
        } else {
            return String.format("%X: (%d, %d, %d, %d)", id, item1.getValue(),
                                                                    item2.getValue(),
                                                                    item3.getValue(), 
                                                                    item4.getValue());
        }
    }
}
