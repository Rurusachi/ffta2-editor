package org.ruru.ffta2editor.model.job;

public enum JobGender {
    MALE(0x00),
    FEMALE(0x01),
    UNKNOWN(0x02);

    public final byte value;

    JobGender(int value) {
        this.value = (byte)value;
    }
        
    private static JobGender[] values = JobGender.values();

    public static JobGender fromInteger(byte value) {
        return fromInteger(Byte.toUnsignedInt(value));
    }

    public static JobGender fromInteger(short value) {
        return fromInteger(Short.toUnsignedInt(value));
        
    }

    public static JobGender fromInteger(int value) {
        for (JobGender e : values) {
            if (e.value == (byte)value) {
                return e;
            }
        }
        System.err.println(String.format("Unknown JobGender: %02X", value));
        return JobGender.MALE;
    }

    public static JobGender fromString(String string) {
        return valueOf(string);
    }

}
