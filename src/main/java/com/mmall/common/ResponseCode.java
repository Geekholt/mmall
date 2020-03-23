package com.mmall.common;

/**
 * @author：wuhaoteng
 * @date：2020/3/20 1:33 下午
 * @desc：
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "用户未登录，无法获取当前用户信息"),
    NOT_MANAGER(20,"当前用户不是管理员,没有权限"),
    ILLEGAL_ARGUMENT(30, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
