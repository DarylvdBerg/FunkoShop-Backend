package com.daryl.util;

import com.daryl.api.User;

/**
 * Util class holding information of which action contains to which privilege
 */
public class PrivilegeUtil {

    // REGULAR USER
    public static final int CHANGE_PASSWORD = 0;
    public static final int UPDATE_USER_INFO = 0;
    public static final int CHECK_USER_PROFILE = 0;
    public static final int SEE_USER_ORDERS = 0;
    public static final int CAN_ORDER = 0;

    // MODERATOR
    public static final int ADD_PRODUCT = 1;
    public static final int UPDATE_PRODUCT = 1;
    public static final int DELETE_PRODUCT = 1;

    // ADMIN
    public static final int GET_ALL_USERS = 2;
    public static final int SEE_ALL_ORDERS = 2;

    public static boolean checkPrivilege(User user, int privilege){
        return user.getPrivilege() >= privilege;
    }
}
