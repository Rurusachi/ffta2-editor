package org.ruru.ffta2editor.model.job;

public enum JobMovablePlaces {
    NONE(0x00),
    CANNOT_ENTER_WATER(0x01),
    CAN_STAND_IN_WATER(0x02),
    CAN_STAND_ON_WATER(0x03),
    CAN_FLOAT_OVER_WATER(0x04),
    DUMMY(0x05);

    public final byte value;

    JobMovablePlaces(int value) {
        this.value = (byte)value;
    }
        
    private static JobMovablePlaces[] values = JobMovablePlaces.values();

    public static JobMovablePlaces fromInteger(byte value) {
        return fromInteger(Byte.toUnsignedInt(value));
    }

    public static JobMovablePlaces fromInteger(short value) {
        return fromInteger(Short.toUnsignedInt(value));
        
    }

    public static JobMovablePlaces fromInteger(int value) {
        for (JobMovablePlaces e : values) {
            if (e.value == (byte)value) {
                return e;
            }
        }
        System.err.println(String.format("Unknown JobMovablePlaces: %02X", value));
        return JobMovablePlaces.CANNOT_ENTER_WATER;
    }

    public static JobMovablePlaces fromString(String string) {
        return valueOf(string);
    }
}
