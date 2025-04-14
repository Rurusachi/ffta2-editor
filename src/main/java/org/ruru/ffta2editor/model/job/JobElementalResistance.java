package org.ruru.ffta2editor.model.job;

public enum JobElementalResistance {
    WEAK(0x00),
    NEUTRAL(0x01),
    HALF(0x02),
    NULL(0x03),
    ABSORB(0x04);

    public final byte value;

    JobElementalResistance(int value) {
        this.value = (byte)value;
    }
        
    private static JobElementalResistance[] values = JobElementalResistance.values();
    public static JobElementalResistance fromInteger(int value) {
        for (JobElementalResistance e : values) {
            if (e.value == (byte)value) {
                return e;
            }
        }
        System.err.println(String.format("Unknown JobElementalResistance: %02X", value));
        return JobElementalResistance.NEUTRAL;
    }

    public static JobElementalResistance fromString(String string) {
        return valueOf(string);
    }

}
