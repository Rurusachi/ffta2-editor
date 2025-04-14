package org.ruru.ffta2editor.model.ability;

public enum SpecialRequirement {
    NONE(0x00),
    WATER_TERRAIN(0x01),
    NATURAL_TERRAIN(0x02),
    ARTIFICIAL_TERRAIN(0x03),
    GRASS_TERRAIN(0x04),
    DIRT_TERRAIN(0x05),
    SUNNY_WEATHER(0x06),
    RAINY_WEATHER(0x07),
    SNOWY_WEATHER(0x08),
    MISTY_WEATHER(0x09),
    RIDING_CHOCOBO(0x0A),
    IS_PRIMED(0x0B),
    NEUKHIA_CHARGE(0x0C),
    TARGET_HUME_MAYBE(0x0D),
    TARGET_BANGAA_MAYBE(0x0E),
    TARGET_NU_MOU_MAYBE(0x0F),
    TARGET_VIERA_MAYBE(0x10),
    TARGET_MOOGLE_MAYBE(0x11),
    TARGET_SEEQ_MAYBE(0x12),
    TARGET_GRIA_MAYBE(0x13),
    TARGET__0x08_MAYBE(0x14),
    TARGET__0x09_MAYBE(0x15),
    TARGET_BAKNAMY(0x16),
    TARGET_SPRITE(0x17),
    TARGET_LAMIA(0x18),
    TARGET_WOLF(0x19),
    TARGET_DREAMHARE(0x1A),
    TARGET_WEREWOLF(0x1B),
    TARGET_ANTLION(0x1C),
    TARGET_SHELLING(0x1D),
    TARGET_MALBORO(0x1E),
    TARGET_DEADLY_NIGHTSHADE(0x1F),
    TARGET_COCKATRICE(0x20),
    TARGET_CHOCOBO_MAYBE(0x21),
    TARGET_FLAN(0x22),
    TARGET_BOMB(0x23),
    TARGET_ZOMBIE(0x24),
    TARGET_GHOST(0x25),
    TARGET_DEATHSCYTHE(0x26),
    TARGET_FLOATING_EYE(0x27),
    TARGET_AHRIMAN(0x28),
    TARGET_TONBERRY_MAYBE(0x29),
    TARGET_HEADLESS(0x2A),
    TARGET_BEHEMOTH(0x2B),
    TARGET_MAGICK_POT_MAYBE(0x2C),
    TARGET_DRAKE(0x2D),
    TARGET_MIMIC_MAYBE(0x2E),
    TARGET__0x23_MAYBE(0x2F),
    TARGET_YOWIE_MAYBE(0x30),
    TARGET_RAFFLESIA_MAYBE(0x31),
    TARGET_DEMON_WALL_MAYBE(0x32),
    TARGET_NEUKHIA_MAYBE(0x33),
    TARGET_UPSILON_MAYBE(0x34);
    
    public final byte value;

    SpecialRequirement(int value) {
        this.value = (byte)value;
    }
        
    private static SpecialRequirement[] values = SpecialRequirement.values();
    public static SpecialRequirement fromInteger(int value) {
        for (SpecialRequirement e : values) {
            if (e.value == (byte)value) {
                return e;
            }
        }
        System.err.println(String.format("Unknown SpecialRequirement: %02X", value));
        return SpecialRequirement.NONE;
    }

    public static SpecialRequirement fromString(String string) {
        return valueOf(string);
    }
}
