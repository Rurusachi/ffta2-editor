package org.ruru.ffta2editor.model.job;

public enum JobMoveType {
    NONE(0x00),
    GROUNDED(0x01),
    FLYING(0x02),
    TELEPORT(0x03);

    public final byte value;

    JobMoveType(int value) {
        this.value = (byte)value;
    }
        
    private static JobMoveType[] values = JobMoveType.values();
    public static JobMoveType fromInteger(int value) {
        for (JobMoveType e : values) {
            if (e.value == value) {
                return e;
            }
        }
        return JobMoveType.NONE;
    }

    public static JobMoveType fromString(String string) {
        return valueOf(string);
    }
}
