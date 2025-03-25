package org.ruru.ffta2editor.model.ability;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javafx.beans.property.SimpleObjectProperty;

public class AbilityAnimation {

    
    public SimpleObjectProperty<Short> animation = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> subtype = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> start = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> first = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> untilSecond = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> second = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> untilThird = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> third = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> untilFourth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> fourth = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> untilEnd = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x0C = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x0D = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> flinch_Glee = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x0F = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x10 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x11 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x12 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x13 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x14 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x15 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x16 = new SimpleObjectProperty<>();
    public SimpleObjectProperty<Byte> _0x17 = new SimpleObjectProperty<>();

    public AbilityAnimation(ByteBuffer bytes) {
        animation.set(bytes.getShort());
        subtype.set(bytes.get());
        start.set(bytes.get());
        first.set(bytes.get());
        untilSecond.set(bytes.get());
        second.set(bytes.get());
        untilThird.set(bytes.get());
        third.set(bytes.get());
        untilFourth.set(bytes.get());
        fourth.set(bytes.get());
        untilEnd.set(bytes.get());
        _0x0C.set(bytes.get());
        _0x0D.set(bytes.get());
        flinch_Glee.set(bytes.get());
        _0x0F.set(bytes.get());
        _0x10.set(bytes.get());
        _0x11.set(bytes.get());
        _0x12.set(bytes.get());
        _0x13.set(bytes.get());
        _0x14.set(bytes.get());
        _0x15.set(bytes.get());
        _0x16.set(bytes.get());
        _0x17.set(bytes.get());
    }

    public AbilityAnimation() {
        animation.set((short)0);
        subtype.set((byte)0);
        start.set((byte)0);
        first.set((byte)0);
        untilSecond.set((byte)0);
        second.set((byte)0);
        untilThird.set((byte)0);
        third.set((byte)0);
        untilFourth.set((byte)0);
        fourth.set((byte)0);
        untilEnd.set((byte)0);
        _0x0C.set((byte)0);
        _0x0D.set((byte)0);
        flinch_Glee.set((byte)0);
        _0x0F.set((byte)0);
        _0x10.set((byte)0);
        _0x11.set((byte)0);
        _0x12.set((byte)0);
        _0x13.set((byte)0);
        _0x14.set((byte)0);
        _0x15.set((byte)0);
        _0x16.set((byte)0);
        _0x17.set((byte)0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(0x18).order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort(animation.getValue().shortValue());
        buffer.put(subtype.getValue().byteValue());
        buffer.put(start.getValue().byteValue());
        buffer.put(first.getValue().byteValue());
        buffer.put(untilSecond.getValue().byteValue());
        buffer.put(second.getValue().byteValue());
        buffer.put(untilThird.getValue().byteValue());
        buffer.put(third.getValue().byteValue());
        buffer.put(untilFourth.getValue().byteValue());
        buffer.put(fourth.getValue().byteValue());
        buffer.put(untilEnd.getValue().byteValue());
        buffer.put(_0x0C.getValue().byteValue());
        buffer.put(_0x0D.getValue().byteValue());
        buffer.put(flinch_Glee.getValue().byteValue());
        buffer.put(_0x0F.getValue().byteValue());
        buffer.put(_0x10.getValue().byteValue());
        buffer.put(_0x11.getValue().byteValue());
        buffer.put(_0x12.getValue().byteValue());
        buffer.put(_0x13.getValue().byteValue());
        buffer.put(_0x14.getValue().byteValue());
        buffer.put(_0x15.getValue().byteValue());
        buffer.put(_0x16.getValue().byteValue());
        buffer.put(_0x17.getValue().byteValue());

        return buffer.array();
    }
}
