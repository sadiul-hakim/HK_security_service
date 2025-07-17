package xyz.sadiulhakim.util;

import xyz.sadiulhakim.enumeration.UserLevel;
import xyz.sadiulhakim.user.User;

public class SecurityUtility {
    private SecurityUtility() {
    }

    public static boolean isAdmin(User user) {
        if (user == null) {
            return false;
        }
        return user.getLevel().equals(UserLevel.ADMIN);
    }

    public static boolean isSuperUser(User user) {
        if (user == null) {
            return false;
        }
        return user.getLevel().equals(UserLevel.SUPER);
    }
}
