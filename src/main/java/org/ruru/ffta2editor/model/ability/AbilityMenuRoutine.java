package org.ruru.ffta2editor.model.ability;

public enum AbilityMenuRoutine {
    NONE(0x0),
    REVIVE(0x1),
    MAGICK_FRENZY(0x2),
    NATURAL_SELECTION(0x3),
    MIX_MAYBE(0x4),
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
    public static AbilityMenuRoutine fromInteger(int value) {
        for (AbilityMenuRoutine e : values) {
            if (e.value == value) {
                return e;
            }
        }
        return AbilityMenuRoutine.NONE;
    }

    public static AbilityMenuRoutine fromString(String string) {
        return valueOf(string);
    }

}
