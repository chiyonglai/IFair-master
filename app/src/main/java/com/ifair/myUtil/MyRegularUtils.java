package com.ifair.myUtil;

import android.text.TextUtils;

import com.ideabus.ideabuslibrary.util.RegularUtils;

import java.util.regex.Pattern;

/**
 * 正則判斷工具
 */

public class MyRegularUtils{


    //驗證信箱
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    //驗證只能有英文和數字，範圍6~12
    private static final String REGEX_PASSWORD = "^(?!.*[^a-zA-Z0-9])(?=.*\\d)(?=.*[a-zA-Z]).{8,20}$";

    //TODO: 驗證手機10碼
    //驗證手機
    private static final  String REGEX_TEL="^[0-9]{10}$";

    public static boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }

    public static boolean isEmail(String email) {
        return isMatch(REGEX_EMAIL, email);
    }

    public static boolean isPassword(String password) {
        return isMatch(REGEX_PASSWORD, password);
    }

    public static boolean isTel(String tel) {
        return isMatch(REGEX_TEL, tel);
    }


}
