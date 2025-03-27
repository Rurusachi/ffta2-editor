package org.ruru.ffta2editor.model.unitSst;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UnitAnimation {
    public static class UnitAnimationFrame {
        public short _0x00; // Used to signal damage number and special effects?
        public short duration;
        public short _0x04;
        public byte _0x06;
        public byte spriteIndex;
        public short _0x08;
        public short _0x0A;
        public short _0x0C;
        public short _0x0E;

        public UnitAnimationFrame(ByteBuffer bytes) {
            _0x00 = bytes.getShort();
            duration = bytes.getShort();
            _0x04 = bytes.getShort();
            _0x06 = bytes.get();
            spriteIndex = bytes.get();
            _0x08 = bytes.getShort();
            _0x0A = bytes.getShort();
            _0x0C = bytes.getShort();
            _0x0E = bytes.getShort();
        }

        public byte[] toBytes() {
            ByteBuffer buf = ByteBuffer.allocate(0x10).order(ByteOrder.LITTLE_ENDIAN);
            buf.putShort(_0x00);
            buf.putShort(duration);
            buf.putShort(_0x04);
            buf.put(_0x06);
            buf.put(spriteIndex);
            buf.putShort(_0x08);
            buf.putShort(_0x0A);
            buf.putShort(_0x0C);
            buf.putShort(_0x0E);
            return buf.array();
        }
    }

    public byte[] animHeader;
    public UnitAnimationFrame[] frames;

    public UnitAnimation(ByteBuffer bytes, byte[] animHeader) {
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        this.animHeader = animHeader;
        
        int count = bytes.getInt();
        frames = new UnitAnimationFrame[count];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new UnitAnimationFrame(bytes);
        }
    }

    public byte[] toBytes() {
        ByteBuffer buf = ByteBuffer.allocate(0x10*frames.length + 4).order(ByteOrder.LITTLE_ENDIAN);

        buf.putInt(frames.length);
        for (int i = 0; i < frames.length; i++) {
            buf.put(frames[i].toBytes());
        }
        return buf.array();
    }
}
