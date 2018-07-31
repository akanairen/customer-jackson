package com.pb.constants;

/**
 *
 */
public enum StatusDictionary {

    /**
     * 失效
     */
    INVALID(-1, "失效"),
    /**
     * 正常
     */
    NORMAL(0, "正常");

    StatusDictionary(int code, String name) {
        this.code = code;
        this.name = name;
    }


    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String of(Integer code) {
        if (code == null) {
            return null;
        }

        StatusDictionary[] values = StatusDictionary.values();
        for (StatusDictionary value : values) {
            if (value.getCode() == code) {
                return value.getName();
            }
        }
        return null;
    }
}
