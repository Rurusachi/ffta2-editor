package org.ruru.ffta2editor.model.ability;

public enum AbilityElement {
    NONE(0x00),
    FIRE(0x01),
    WIND(0x02),
    EARTH(0x03),
    WATER(0x04),
    ICE(0x05),
    THUNDER(0x06),
    HOLY(0x07),
    DARK(0x08);
    
    public final byte value;

    AbilityElement(int value) {
        this.value = (byte)value;
    }
        
    private static AbilityElement[] values = AbilityElement.values();
    public static AbilityElement fromInteger(int value) {
        for (AbilityElement e : values) {
            if (e.value == value) {
                return e;
            }
        }
        return AbilityElement.NONE;
    }

    public static AbilityElement fromString(String string) {
        return valueOf(string);
    }
}
