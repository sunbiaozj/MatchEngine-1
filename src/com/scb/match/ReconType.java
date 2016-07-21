package com.scb.match;

/**
 * @author DAO TUAN ANH (DENNIS).
 * @created on 15/7/16.
 */
public enum ReconType {
    LINE_BY_LINE(1),
    X_AS_REFERENCE(2),
    UNKNONW(-1);


    private int code;

    ReconType(int code) {
        this.code = code;
    }

    public static ReconType fromCode(int code) {
        for (ReconType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return UNKNONW;
    }

    public int getCode() {
        return code;
    }
}
