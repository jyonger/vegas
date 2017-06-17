package org.yong.mall.dict;

/**
 * Created by Yong on 2017/6/8.
 */
public class UserDict {

    public static final String CURRENT_USER = "current_user";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role {
        int CUSTOMER = 1;
        int ADMIN = 0;
    }
}
