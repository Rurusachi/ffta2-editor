package org.ruru.ffta2editor.model.quest;

public enum FFTA2Month {
    NONE(0),
    BLACKFROST(1),
    SKYFROST(2),
    GREENFIRE(3),
    BLOODFIRE(4),
    ROSEFIRE(5),
    COPPERSUN(6),
    GOLDSUN(7),
    SILVERSUN(8),
    ASHLEAF(9),
    MISTLEAF(10),
    EMBERLEAF(11),
    PLUMFROST(12);

    public final byte value;

    FFTA2Month(int value) {
        this.value = (byte)value;
    }
        
    private static FFTA2Month[] values = FFTA2Month.values();
    public static FFTA2Month fromInteger(int value) {
        for (FFTA2Month e : values) {
            if (e.value == value) {
                return e;
            }
        }
        return FFTA2Month.NONE;
    }

    public static FFTA2Month fromString(String string) {
        return valueOf(string);
    }

}
