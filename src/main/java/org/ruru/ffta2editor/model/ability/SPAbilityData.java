package org.ruru.ffta2editor.model.ability;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class SPAbilityData extends AbilityData {

    public SimpleObjectProperty<Byte> maxAP = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> apIndex = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Short> padding = new SimpleObjectProperty<>();

    public SPAbilityData(ByteBuffer bytes, int id) {
        if (id < App.abilityNames.size()) {
            this.name = App.abilityNames.get(id);
        } else {
            this.name = new SimpleStringProperty("");
        }
        if (id < App.abilityDescriptions.size()) {
            this.description = App.abilityDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:00\\\\end\\");
        }
        this.id = id;

        maxAP.set(bytes.get());
        apIndex.set(bytes.get());
        padding.set(bytes.getShort());
    }

    public SPAbilityData(String name, int id) {
        if (id < App.abilityNames.size()) {
            this.name = App.abilityNames.get(id);
        } else {
            this.name = new SimpleStringProperty(name);
            App.abilityNames.add(this.name);
        }
        if (id < App.abilityDescriptions.size()) {
            this.description = App.abilityDescriptions.get(id);
        } else {
            this.description = new SimpleStringProperty("\\var2:00\\\\end\\");
            App.abilityDescriptions.add(this.description);
        }
        this.id = id;

        maxAP.set((byte)0);
        apIndex.set((byte)0);
        padding.set((short)0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x04).order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(maxAP.getValue());
        buffer.put(apIndex.getValue());
        buffer.putShort(padding.getValue());

        return buffer.array();
    }
}
