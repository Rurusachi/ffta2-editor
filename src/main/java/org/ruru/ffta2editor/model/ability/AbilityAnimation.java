package org.ruru.ffta2editor.model.ability;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.ruru.ffta2editor.model.ability.ActiveAbilityData.AbilityFlags;

import javafx.beans.property.SimpleObjectProperty;

public class AbilityAnimation {

    
    public SimpleObjectProperty<Short> animation = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> subtype = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> start = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Short> unitAnimation = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> second = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> untilThird = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> third = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> untilFourth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> fourth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> untilEnd = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Short> _0x0C = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Short> flinch_Glee = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x10 = new SimpleObjectProperty<>(); // Unused?
    public SimpleObjectProperty<Byte> _0x11 = new SimpleObjectProperty<>(); // Unused?
    public SimpleObjectProperty<Byte> _0x12 = new SimpleObjectProperty<>(); // Unused?
    public SimpleObjectProperty<Byte> _0x13 = new SimpleObjectProperty<>(); // Unused?
    public SimpleObjectProperty<Short> targetState = new SimpleObjectProperty<>(); // Enemy state?
    public SimpleObjectProperty<Byte> _0x16 = new SimpleObjectProperty<>(); // 4-bit bitfield
    public SimpleObjectProperty<Byte> _0x17 = new SimpleObjectProperty<>(); // Unused?

    public AbilityAnimation(ByteBuffer bytes) {
        animation.set(bytes.getShort());
        subtype.set(bytes.get());
        start.set(bytes.get());
        unitAnimation.set(bytes.getShort());
        second.set(bytes.get());
        untilThird.set(bytes.get());
        third.set(bytes.get());
        untilFourth.set(bytes.get());
        fourth.set(bytes.get());
        untilEnd.set(bytes.get());
        _0x0C.set(bytes.getShort());
        flinch_Glee.set(bytes.getShort());
        _0x10.set(bytes.get());
        _0x11.set(bytes.get());
        _0x12.set(bytes.get());
        _0x13.set(bytes.get());
        targetState.set(bytes.getShort());
        _0x16.set(bytes.get());
        _0x17.set(bytes.get());
    }

    public AbilityAnimation() {
        animation.set((short)0);
        subtype.set((byte)0);
        start.set((byte)0);
        unitAnimation.set((short)0);
        second.set((byte)0);
        untilThird.set((byte)0);
        third.set((byte)0);
        untilFourth.set((byte)0);
        fourth.set((byte)0);
        untilEnd.set((byte)0);
        _0x0C.set((short)0);
        flinch_Glee.set((short)0);
        _0x10.set((byte)0);
        _0x11.set((byte)0);
        _0x12.set((byte)0);
        _0x13.set((byte)0);
        targetState.set((short)0);
        _0x16.set((byte)0);
        _0x17.set((byte)0);
    }

    public void copyFrom(AbilityAnimation source) {
        animation.set(source.animation.get());
        subtype.set(source.subtype.get());
        start.set(source.start.get());
        unitAnimation.set(source.unitAnimation.get());
        second.set(source.second.get());
        untilThird.set(source.untilThird.get());
        third.set(source.third.get());
        untilFourth.set(source.untilFourth.get());
        fourth.set(source.fourth.get());
        untilEnd.set(source.untilEnd.get());
        _0x0C.set(source._0x0C.get());
        flinch_Glee.set(source.flinch_Glee.get());
        _0x10.set(source._0x10.get());
        _0x11.set(source._0x11.get());
        _0x12.set(source._0x12.get());
        _0x13.set(source._0x13.get());
        targetState.set(source.targetState.get());
        _0x16.set(source._0x16.get());
        _0x17.set(source._0x17.get());
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x18).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(animation.getValue());
        buffer.put(subtype.getValue());
        buffer.put(start.getValue());
        buffer.putShort(unitAnimation.getValue());
        buffer.put(second.getValue());
        buffer.put(untilThird.getValue());
        buffer.put(third.getValue());
        buffer.put(untilFourth.getValue());
        buffer.put(fourth.getValue());
        buffer.put(untilEnd.getValue());
        buffer.putShort(_0x0C.getValue());
        buffer.putShort(flinch_Glee.getValue());
        buffer.put(_0x10.getValue());
        buffer.put(_0x11.getValue());
        buffer.put(_0x12.getValue());
        buffer.put(_0x13.getValue());
        buffer.putShort(targetState.getValue());
        buffer.put(_0x16.getValue());
        buffer.put(_0x17.getValue());

        return buffer.array();
    }
}
