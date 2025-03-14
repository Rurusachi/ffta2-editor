package org.ruru.ffta2editor.model.job;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.App;
import org.ruru.ffta2editor.model.ability.ActiveAbilityData;

import javafx.beans.property.SimpleObjectProperty;

public class AbilitySetAbility {
    public int id;

    public SimpleObjectProperty<ActiveAbilityData> ability = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> abilityAnimation = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> weaponAnimation = new SimpleObjectProperty<>();

    public SimpleObjectProperty<Byte> ability_0x4 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> ability_0x5 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> ability_0x6 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> ability_0x7 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> ability_0x8 = new SimpleObjectProperty<>();
    
    public SimpleObjectProperty<Byte> maxAP = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> ability_0xa = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> ability_0xb = new SimpleObjectProperty<>();

    public AbilitySetAbility(ByteBuffer bytes) {
        ability.setValue(App.activeAbilityList.get(Short.toUnsignedInt(bytes.getShort())));
        abilityAnimation.setValue(bytes.get());
        weaponAnimation.setValue(bytes.get());
        ability_0x4.setValue(bytes.get());
        ability_0x5.setValue(bytes.get());
        ability_0x6.setValue(bytes.get());
        ability_0x7.setValue(bytes.get());
        ability_0x8.setValue(bytes.get());
        maxAP.setValue(bytes.get());
        ability_0xa.setValue(bytes.get());
        ability_0xb.setValue(bytes.get());
    }
    public AbilitySetAbility() {
        ability.setValue(App.activeAbilityList.get(0));
        abilityAnimation.setValue((byte)0);
        weaponAnimation.setValue((byte)0);
        ability_0x4.setValue((byte)0);
        ability_0x5.setValue((byte)0);
        ability_0x6.setValue((byte)0);
        ability_0x7.setValue((byte)0);
        ability_0x8.setValue((byte)0);
        maxAP.setValue((byte)0);
        ability_0xa.setValue((byte)0);
        ability_0xb.setValue((byte)0);
    }


    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0xc).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort((short)ability.getValue().id);
        buffer.put(abilityAnimation.getValue());
        buffer.put(weaponAnimation.getValue());
        buffer.put(ability_0x4.getValue());
        buffer.put(ability_0x5.getValue());
        buffer.put(ability_0x6.getValue());
        buffer.put(ability_0x7.getValue());
        buffer.put(ability_0x8.getValue());
        buffer.put(maxAP.getValue());
        buffer.put(ability_0xa.getValue());
        buffer.put(ability_0xb.getValue());

        return buffer.array();
    }
}
