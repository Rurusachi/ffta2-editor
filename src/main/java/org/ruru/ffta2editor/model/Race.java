package org.ruru.ffta2editor.model;

public enum Race {
    NONE(0x00),
    HUME(0x01),
    BANGAA(0x02),
    NU_MOU(0x03),
    VIERA(0x04),
    MOOGLE(0x05),
    SEEQ(0x06),
    GRIA(0x07),
    _0x08(0x08),
    _0x09(0x09),
    BAKNAMY(0x0A),
    SPRITE(0x0B),
    LAMIA(0x0C),
    WOLF(0x0D),
    DREAMHARE(0x0E),
    WEREWOLF(0x0F),
    ANTLION(0x10),
    SHELLING(0x11),
    MALBORO(0x12),
    TOMATO(0x13),
    COCKATRICE(0x14),
    CHOCOBO(0x15),
    FLAN(0x16),
    BOMB(0x17),
    ZOMBIE(0x18),
    GHOST(0x19),
    DEATHSCYTHE(0x1A),
    FLOATING_EYE(0x1B),
    AHRIMAN(0x1C),
    TONBERRY(0x1D),
    HEADLESS(0x1E),
    BEHEMOTH(0x1F),
    MAGICK_POT(0x20),
    DRAKE(0x21),
    MIMIC(0x22),
    _0x23(0x23),
    YOWIE(0x24),
    RAFFLESIA(0x25),
    DEMON_WALL(0x26),
    NEUKHIA(0x27),
    UPSILON(0x28);
    
    public final byte value;

    Race(int value) {
        this.value = (byte)value;
    }
        
    private static Race[] values = Race.values();
    public static Race fromInteger(int value) {
        for (Race e : values) {
            if (e.value == (byte)value) {
                return e;
            }
        }
        System.err.println(String.format("Unknown Race: %02X", value));
        return Race.NONE;
    }

    public static Race fromString(String string) {
        return valueOf(string);
    }
}
