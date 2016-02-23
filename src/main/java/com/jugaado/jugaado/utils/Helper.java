package com.jugaado.jugaado.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ravivooda on 9/6/15.
 */
public class Helper {
    public String HOST = "";
    public int PORT;

    public String HOST_USER_NAME = "";
    public String HOST_PWD = "";

    public String FORGET_PWD_URL="";

    public static String CONFIGURL = "http://45.79.94.220/chat/config.php";
    public static String SERVERURL = "http://45.79.94.220/chat/server.php";
    public static String SECRETSTRING = "My App Jugaado";

    public Helper(HelperConfig helperConfig) {
        this.HOST = helperConfig.getHOST();
        this.PORT = Integer.parseInt(helperConfig.getPORT());
        this.HOST_USER_NAME = helperConfig.getHOST_USER_NAME();
        this.HOST_PWD = helperConfig.getHOST_PWD();
        this.FORGET_PWD_URL = helperConfig.getFORGET_PWD_URL();
    }

    public static void showError(String error, Context context) {
        if (error == null || error.trim().equals("")){
            error = "An unknown error occurred. Please try again later";
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(error);
        alert.setPositiveButton("Okay",null);
        alert.show();
    }

    public static void openURL(Activity activity, String URL) {
        if (!URL.startsWith("http://") && !URL.startsWith("https://")) {
            URL = "http://" + URL;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        activity.startActivity(browserIntent);
    }
}
