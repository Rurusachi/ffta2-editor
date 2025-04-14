package org.ruru.ffta2editor.model.item;

public enum LootCategory {
    NONE(0),
    MAGICITE(1),
    METALS(2),
    SKINS(3),
    BONES(4),
    FLORA(5),
    TIMBER(6),
    PHILTRES(7),
    CLOTH(8);

    public final byte value;

    LootCategory(int value) {
        this.value = (byte)value;
    }
        
    private static LootCategory[] values = LootCategory.values();
    public static LootCategory fromInteger(int value) {
        for (LootCategory e : values) {
            if (e.value == (byte)value) {
                return e;
            }
        }
        System.err.println(String.format("Unknown LootCategory: %02X", value));
        return LootCategory.NONE;
    }

    public static LootCategory fromString(String string) {
        return valueOf(string);
    }
}
