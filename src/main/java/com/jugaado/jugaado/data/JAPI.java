package com.jugaado.jugaado.data;

/**
 * Created by shashankdabriwal on 7/14/15.
 */
 
 /**
  *TODO:    1. Rename the file. Include a brief description of what this does
  *         2. Avoid hardcoding. Instead can we get the URL from a config file? 
  *             Or at lease create a private static final class constant
  */
public class JAPI {
    public static String getBaseUrl() {
        return "http://45.79.94.220:8080/";
    }

    public static void signUp(String email, String username, String password, EventCallback callback) {

    }
}
