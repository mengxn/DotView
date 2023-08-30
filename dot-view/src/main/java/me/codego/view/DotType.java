package me.codego.view;

/**
 * @author mengxn
 * @date 2022/2/11
 */
public enum DotType {
    /**
     * 普通，不限制
     */
    NORMAL(0),
    /**
     * 省略号
     */
    ELLIPSIS(1),
    /**
     * 99+
     */
    PLUS(2);

    private int code;

    DotType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static DotType valueOf(int code) {
        for (DotType type : DotType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
