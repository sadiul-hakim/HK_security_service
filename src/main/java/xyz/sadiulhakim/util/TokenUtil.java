package xyz.sadiulhakim.util;

public class TokenUtil {
    private TokenUtil() {
    }

    public static boolean isJwtToken(String token) {
        return token.startsWith("Bearer");
    }
}
