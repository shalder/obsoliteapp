package com.jugaado.jugaado.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.auth.LoginActivity;
import com.jugaado.jugaado.activities.base.BaseActivity;
import com.jugaado.jugaado.manager.DataBaseManager;
import com.jugaado.jugaado.utils.Helper;
import com.jugaado.jugaado.utils.HelperConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by websofttechs on 11/18/2015.
 */
public class ForgetPasswordActivity extends BaseActivity {
    EditText username_edittext;
    Button sendpasskey_button,popup_btn;
    String forgetPasswordURL;
    final String TAG = "Forget Password";
    String username,responseBody;
    TextView popup_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*float configAppVersion = getIntent().getExtras().getFloat("configAppVersion");
        float configReloadAppVersion = getIntent().getExtras().getFloat("configReloadAppVersion");
        String playStoreURL = getIntent().getExtras().getString("playStoreURL");

        if (configAppVersion!=configReloadAppVersion) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreURL));
            startActivity(browserIntent);

            Toast.makeText(ForgetPasswordActivity.this, "Please Upgrade to the latest app.", Toast.LENGTH_LONG).show();

            android.os.Process.killProcess(android.os.Process.myPid());
        }*/

        setContentView(R.layout.activity_forgetpassword);
        loadOtherCustomActionBar();

        username_edittext = (EditText) findViewById(R.id.editText);
        username = username_edittext.getText().toString();
        //System.out.println(username);


        sendpasskey_button = (Button) findViewById(R.id.button);
        sendpasskey_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Your code goes here
                            username_edittext = (EditText) findViewById(R.id.editText);
                            username = username_edittext.getText().toString();

                            DataBaseManager dataBaseManager = new DataBaseManager(ForgetPasswordActivity.this);

                            HelperConfig helperConfig = null;

                            try {
                                helperConfig = dataBaseManager.getConfig();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            forgetPasswordURL = helperConfig.getFORGET_PWD_URL() + username;
                            Log.d(TAG, forgetPasswordURL);
                            //URL url = new URL(forgetPasswordURL);
                            HttpClient httpclient;

                            HttpGet httppost = new HttpGet(forgetPasswordURL);


                            HttpParams params = new BasicHttpParams();

                            HttpProtocolParams.setContentCharset(params, "UTF-8");

                            httpclient = new DefaultHttpClient(params);


                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            responseBody = httpclient.execute(httppost, responseHandler);

                            Log.d(TAG, "responseBody : " + responseBody);

                            ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    showError("We have emailed you the Password Reset Details to  your registered Email ID. \n \n PS - If you do not see the email in your inbox, please check your spam folder. Email questions to admin@jugaado.in");

                                }
                            });
                            Handler handler = new Handler(Looper.getMainLooper());
                            final Runnable run = new Runnable() {
                                public void run() {
                                    //do your stuff here after DELAY m.sec
                                    Intent intent = new Intent(ForgetPasswordActivity.this,SplashActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            };
                            handler.postDelayed(run, 5000);
                            //Toast.makeText(getApplicationContext(),responseBody,Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
            }
        });


    }
    /*private void backToLoginPage(View v){
        Log.d(TAG,"inside onclick method");
        Intent intent = new Intent(ForgetPasswordActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }*/


}



