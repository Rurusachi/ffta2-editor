package org.ruru.ffta2editor.model.item;

public enum LootRank {
    ZERO_STAR(2),
    ONE_STAR(3),
    TWO_STAR(4),
    THREE_STAR(5);

    public final byte value;

    LootRank(int value) {
        this.value = (byte)value;
    }
        
    private static LootRank[] values = LootRank.values();

    public static LootRank fromInteger(byte value) {
        return fromInteger(Byte.toUnsignedInt(value));
    }

    public static LootRank fromInteger(short value) {
        return fromInteger(Short.toUnsignedInt(value));
        
    }

    public static LootRank fromInteger(int value) {
        for (LootRank e : values) {
            if (e.value == (byte)value) {
                return e;
            }
        }
        System.err.println(String.format("Unknown LootRank: %02X", value));
        return LootRank.ZERO_STAR;
    }

    public static LootRank fromString(String string) {
        return valueOf(string);
    }
}
