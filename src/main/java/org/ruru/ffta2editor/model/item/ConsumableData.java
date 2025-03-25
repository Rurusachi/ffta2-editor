package org.ruru.ffta2editor.model.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.ability.ActiveAbilityData;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ConsumableData extends ItemData {
    public ObjectProperty<Short> buy = new SimpleObjectProperty<>();
    public ObjectProperty<Short> sell = new SimpleObjectProperty<>();
    public ObjectProperty<ActiveAbilityData> ability = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x6 = new SimpleObjectProperty<>();
    public ObjectProperty<Byte> _0x7 = new SimpleObjectProperty<>();

    public ConsumableData(ByteBuffer bytes, int id) {
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
        ability.set(App.activeAbilityList.get(Short.toUnsignedInt(bytes.getShort())));
        _0x6.set(bytes.get());
        _0x7.set(bytes.get());
    }

    public ConsumableData(String name, int id) {
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
        ability.set(App.activeAbilityList.get(0));
        _0x6.set((byte)0);
        _0x7.set((byte)0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x8).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(buy.getValue());
        buffer.putShort(sell.getValue());
        buffer.putShort((short)ability.getValue().id);
        buffer.put(_0x6.getValue());
        buffer.put(_0x7.getValue());

        return buffer.array();
    }
}
