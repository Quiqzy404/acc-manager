package dev.quiqzy.accsmanager;

public final class SessionTokenAuth {

    public static boolean isValidFormat(String token) {
        return token != null
                && token.startsWith("eyJ")
                && token.length() > 100;
    }

}
