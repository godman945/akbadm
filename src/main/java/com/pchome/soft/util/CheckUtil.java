package com.pchome.soft.util;

public class CheckUtil {
    public static boolean checkEngNum(String str) {
        boolean flag = false;
        if (str != null) {
            int length = str.length();
            if (length > 0) {
                // check valid char
                flag = true;
                for (int i = 0; i < length; i++) {
                    char c = str.charAt(i);
                    if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))) {
                        flag = false;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public static boolean checkEmail(String email) {
        boolean flag = false;
        if (email != null) {
            int length = email.length();
            if (length > 0) {
                // check valid char
                flag = true;
                for (int i = 0; i < length; i++) {
                    char c = email.charAt(i);
                    if(!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c == '-') || (c == '_') || (c == '.') || (c == '@'))) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    // there is no @ || @ is head || @ is end
                    if((email.indexOf("@") == -1) || (email.indexOf("@") == 0) || (email.indexOf("@") == (length-1))) {
                        flag = false;
                    }
                    // duplicate @
                    if((email.indexOf("@") != -1) && (email.substring(email.indexOf("@")+1, length).indexOf("@") != -1)) {
                        flag = false;
                    }
                    // there is no . || . is head || . is end
                    if((email.indexOf(".") == -1) || (email.indexOf(".") == 0) || (email.lastIndexOf(".") == (length-1))) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }
}