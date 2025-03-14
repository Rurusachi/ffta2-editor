package org.ruru.ffta2editor.model.equipment;

public enum EquipmentLocation {
    NONE(0x00),
    HANDS(0x01),
    HEAD(0x02),
    ARMOR(0x03),
    BOOTS(0x04),
    GLOVES(0x05),
    ACCESSORY(0x06);

    public final byte value;

    EquipmentLocation(int value) {
        this.value = (byte)value;
    }
        
    private static EquipmentLocation[] values = EquipmentLocation.values();
    public static EquipmentLocation fromInteger(int value) {
        for (EquipmentLocation e : values) {
            if (e.value == value) {
                return e;
            }
        }
        return EquipmentLocation.NONE;
    }

    public static EquipmentLocation fromString(String string) {
        return valueOf(string);
    }
}
