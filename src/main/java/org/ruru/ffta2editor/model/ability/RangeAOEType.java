package org.ruru.ffta2editor.model.ability;

public enum RangeAOEType {
    NONE(0x00),
    INFINITE_LINE(0x01),
    LINE(0x02),
    SURROUNDING_CROSS(0x04),
    ALL_UNITS(0x05),
    CONE(0x06),
    WEAPON(0x07),
    SELF(0x08),
    DEMON_WALL_LINE(0x09),
    NEUKHIA_POD_LINE(0x0A),
    NEUKHIA_STONE_CROSS(0x0B),
    NEUKHIA_WISPS(0x0C),
    YOWIE_ADJACENT(0x0D),
    RAFFLESIA_ADJACENT(0x0E),
    RAFFLESIA_CLOUD(0x0F),
    HUGE_FRONT(0x10),
    WIDE_LINE(0x11),
    NEUKHIA_BLUE_STONES(0x12),
    SUMMON(0x13);

    
    public final byte value;

    RangeAOEType(int value) {
        this.value = (byte)value;
    }
        
    private static RangeAOEType[] values = RangeAOEType.values();
    public static RangeAOEType fromInteger(int value) {
        for (RangeAOEType e : values) {
            if (e.value == value) {
                return e;
            }
        }
        return RangeAOEType.NONE;
    }

    public static RangeAOEType fromString(String string) {
        return valueOf(string);
    }

}
