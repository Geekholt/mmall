package com.mmall.common;

/**
 * @author：wuhaoteng
 * @date：2020/3/20 2:17 下午
 * @desc：常量类
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public interface ROLE {
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }
}
