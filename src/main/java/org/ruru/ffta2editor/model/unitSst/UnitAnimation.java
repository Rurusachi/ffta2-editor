package org.ruru.ffta2editor.model.unitSst;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class UnitAnimation {
    public static class UnitAnimationFrame {
        public ObjectProperty<Short> _0x00 = new SimpleObjectProperty<>(); // Used to signal damage number and special effects = new SimpleObjectProperty<>()?
        public ObjectProperty<Short> duration = new SimpleObjectProperty<>();
        public ObjectProperty<Short> _0x04 = new SimpleObjectProperty<>();
        public PropertyFlags propertyFlags;
        public ObjectProperty<Byte> spriteIndex = new SimpleObjectProperty<>();
        public ObjectProperty<Short> _0x08 = new SimpleObjectProperty<>();
        public ObjectProperty<Short> _0x0a = new SimpleObjectProperty<>();
        public ObjectProperty<Short> _0x0c = new SimpleObjectProperty<>();
        public ObjectProperty<Short> _0x0e = new SimpleObjectProperty<>();

        

    public class PropertyFlags {
        private BitSet flags;
        private int length;
        public SimpleBooleanProperty propertyBit0 = new SimpleBooleanProperty();
        public SimpleBooleanProperty isMirrored = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit2 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit3 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit4 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit5 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit6 = new SimpleBooleanProperty();
        public SimpleBooleanProperty propertyBit7 = new SimpleBooleanProperty();

        public PropertyFlags() {
            flags = new BitSet(1*8);
            length = 1;
        }

        public PropertyFlags(byte[] bytes) {
            flags = BitSet.valueOf(bytes);
            length = bytes.length;
            
            // Byte 1
            propertyBit0.setValue(flags.get(0));
            isMirrored.setValue(flags.get(1));
            propertyBit2.setValue(flags.get(2));
            propertyBit3.setValue(flags.get(3));
            propertyBit4.setValue(flags.get(4));
            propertyBit5.setValue(flags.get(5));
            propertyBit6.setValue(flags.get(6));
            propertyBit7.setValue(flags.get(7));
        }

        public byte[] toBytes() {
            flags.set(0, propertyBit0.getValue());
            flags.set(1, isMirrored.getValue());
            flags.set(2, propertyBit2.getValue());
            flags.set(3, propertyBit3.getValue());
            flags.set(4, propertyBit4.getValue());
            flags.set(5, propertyBit5.getValue());
            flags.set(6, propertyBit6.getValue());
            flags.set(7, propertyBit7.getValue());

            ByteBuffer newBytes = ByteBuffer.allocate(length);
            newBytes.put(flags.toByteArray());
            newBytes.put(new byte[newBytes.remaining()]);

            return newBytes.array();
        }
    }

        public UnitAnimationFrame(ByteBuffer bytes) {
            _0x00.set(bytes.getShort());
            duration.set(bytes.getShort());
            _0x04.set(bytes.getShort());
            propertyFlags = new PropertyFlags(new byte[]{ bytes.get() });
            spriteIndex.set(bytes.get());
            _0x08.set(bytes.getShort());
            _0x0a.set(bytes.getShort());
            _0x0c.set(bytes.getShort());
            _0x0e.set(bytes.getShort());
        }

        public byte[] toBytes() {
            ByteBuffer buf = ByteBuffer.allocate(0x10).order(ByteOrder.LITTLE_ENDIAN);
            buf.putShort(_0x00.getValue());
            buf.putShort(duration.getValue());
            buf.putShort(_0x04.getValue());
            buf.put(propertyFlags.toBytes());
            buf.put(spriteIndex.getValue());
            buf.putShort(_0x08.getValue());
            buf.putShort(_0x0a.getValue());
            buf.putShort(_0x0c.getValue());
            buf.putShort(_0x0e.getValue());
            return buf.array();
        }
    }

    public byte[] animHeader;
    public UnitAnimationFrame[] frames;

    public short unknown;

    public int key;

    public UnitAnimation(ByteBuffer bytes, byte[] animHeader, int key) {
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        this.animHeader = animHeader;
        this.key = key;
        
        int count = bytes.getShort();
        this.unknown = bytes.getShort();
        frames = new UnitAnimationFrame[count];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new UnitAnimationFrame(bytes);
        }
    }

    public byte[] toBytes() {
        ByteBuffer buf = ByteBuffer.allocate(0x10*frames.length + 4).order(ByteOrder.LITTLE_ENDIAN);

        buf.putShort((short)frames.length);
        buf.putShort(unknown);
        for (int i = 0; i < frames.length; i++) {
            buf.put(frames[i].toBytes());
        }
        return buf.array();
    }
}
