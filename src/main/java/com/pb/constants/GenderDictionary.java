package com.pb.constants;

/**
 * @author PengBin
 */
public enum GenderDictionary {
    /**
     * 女
     */
    WOMAN(0, "女性"),
    /**
     * 男
     */
    MAN(1, "男性"),
    /**
     * 中
     */
    MIDDLE(2, "中性");

    GenderDictionary(int code, String name) {
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

        GenderDictionary[] values = GenderDictionary.values();
        for (GenderDictionary value : values) {
            if (value.getCode() == code) {
                return value.getName();
            }
        }
        return "";
    }
}
