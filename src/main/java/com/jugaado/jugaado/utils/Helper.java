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
    public static final String host = "52.26.15.67";
    public static final int port = 5222;

    public static final String host_user_name = "asdf";
    public static final String host_pwd = "asdf";

    public static final String forgetpassword_api="http://45.79.94.220/sendverifycode.php?username=";

    public static void showError(String error, Context context) {
        if (error == null || error.equals("")){
            error = "An unknown error occurred. Please try again later";
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(error);
        alert.setPositiveButton("Okay",null);
        alert.show();
    }

    public static void openURL(Activity activity, String URL) {
        if (!URL.startsWith("http://") || !URL.startsWith("https://")) {
            URL = "http://" + URL;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        activity.startActivity(browserIntent);
    }
}
