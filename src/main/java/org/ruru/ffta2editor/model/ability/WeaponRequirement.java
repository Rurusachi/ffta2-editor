package org.ruru.ffta2editor.model.ability;

public enum WeaponRequirement {
    NONE(0x00),
    KNIFE(0x01),
    SWORD(0x02),
    BLADE(0x03),
    SABER(0x04),
    KNIGHTSWORD(0x05),
    RAPIER(0x06),
    GREATSWORD(0x07),
    BROADSWORD(0x08),
    KATANA(0x09),
    SPEAR(0x0A),
    ROD(0x0B),
    STAFF(0x0C),
    POLE(0x0D),
    KNUCKLES(0x0E),
    BOW(0x0F),
    GREATBOW(0x10),
    GUN(0x11),
    INSTRUMENT(0x12),
    HAND_CANNON(0x13),
    GRENADE(0x14),
    AXE(0x15),
    HAMMER(0x16),
    MACE(0x17),
    CARD(0x18),
    BOOK(0x19),
    SHIELD(0x1A),
    HELM(0x1B),
    HAIR_ACCESSORY(0x1C),
    HAT(0x1D),
    HEAVY_ARMOR(0x1E),
    LIGHT_ARMOR(0x1F),
    ROBE(0x20),
    BOOTS(0x21),
    GLOVES(0x22),
    ACCESSORY(0x23),
    ANY_WEAPON(0x80),
    BOW_GREATBOW(0x81),
    BLADED(0x82),
    PIERCING(0x83),
    BLUDGEONING(0x84),
    RANGED(0x85),
    INSTRUMENT_BOOK(0x86),
    ESPER_ACCESSORY(0x87);
    
    public final byte value;

    WeaponRequirement(int value) {
        this.value = (byte)value;
    }
        
    private static WeaponRequirement[] values = WeaponRequirement.values();
    public static WeaponRequirement fromInteger(int value) {
        for (WeaponRequirement e : values) {
            if (e.value == value) {
                return e;
            }
        }
        return WeaponRequirement.NONE;
    }

    public static WeaponRequirement fromString(String string) {
        return valueOf(string);
    }
}
