package com.inlab.racodemoapi.Constants;


import java.util.Random;

public class OAuthParamsTemplate {
    public static final String clientID = "YOUR_CLIENT_ID";
    public static final String clientSecret = "YOUR_CLIENT_SECRET";
    public static final String redirectUri = "apifib://raco";
    public static String responseType = "code";
    public static String state;

    public static String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 16) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        state = saltStr;
        return saltStr;
    }
}
