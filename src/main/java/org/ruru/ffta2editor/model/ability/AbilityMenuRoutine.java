package org.ruru.ffta2editor.model.ability;

public enum AbilityMenuRoutine {
    NONE(0x0),
    REVIVE(0x1),
    MAGICK_FRENZY(0x2),
    NATURAL_SELECTION(0x3),
    MIX_MAYBE(0x4),
    _0x5(0x5),
    THROW_ITEM(0x6),
    GIL_TOSS(0x7),
    MIRROR_ITEM(0x8),
    DOUBLECAST(0x9),
    CONTROL_MONSTER(0xA);
    
    public final byte value;

    AbilityMenuRoutine(int value) {
        this.value = (byte)value;
    }
        
    private static AbilityMenuRoutine[] values = AbilityMenuRoutine.values();

    public static AbilityMenuRoutine fromInteger(byte value) {
        return fromInteger(Byte.toUnsignedInt(value));
    }

    public static AbilityMenuRoutine fromInteger(short value) {
        return fromInteger(Short.toUnsignedInt(value));
        
    }

    public static AbilityMenuRoutine fromInteger(int value) {
        for (AbilityMenuRoutine e : values) {
            if (e.value == (byte)value) {
                return e;
            }
        }
        System.err.println(String.format("Unknown AbilityMenuRoutine: %02X", value));
        return AbilityMenuRoutine.NONE;
    }

    public static AbilityMenuRoutine fromString(String string) {
        return valueOf(string);
    }

}
