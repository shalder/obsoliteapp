package com.jugaado.jugaado.activities.auth;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.CategoryActivity;
import com.jugaado.jugaado.activities.ForgetPasswordActivity;
import com.jugaado.jugaado.activities.base.BaseActivity;
import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.manager.AccountManager;
import com.jugaado.jugaado.manager.DataBaseManager;
import com.jugaado.jugaado.utils.HelperConfig;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shashankdabriwal on 7/14/15.
 */

public class LoginActivity extends BaseActivity {
    public static final String TAG = "Login Activity";
    private Button loginButton;
    private TextView signUpText;
    private TextView forgetpasswordText;


    private EditText userEditText;
    private EditText passwordEditText;

    //float configAppVersion;
    //float configReloadAppVersion;
    //String playStoreURL;

    private JSONObject objHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        configAppVersion = getIntent().getExtras().getFloat("configAppVersion");
        configReloadAppVersion = getIntent().getExtras().getFloat("configReloadAppVersion");
        playStoreURL = getIntent().getExtras().getString("playStoreURL");*/

        setContentView(R.layout.activity_login);

        signUpText = (TextView) findViewById(R.id.signup_text);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                //intent.putExtra("configAppVersion", configAppVersion);
                //intent.putExtra("configReloadAppVersion", configReloadAppVersion);
                //intent.putExtra("playStoreURL", playStoreURL);
                startActivity(intent);
            }
        });
        forgetpasswordText=(TextView)findViewById(R.id.forgetpassword_text);
        forgetpasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intnt=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                //intnt.putExtra("configAppVersion", configAppVersion);
                //intnt.putExtra("configReloadAppVersion", configReloadAppVersion);
                //intnt.putExtra("playStoreURL", playStoreURL);
                startActivity(intnt);
            }

        });



        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(LOGIN_CLICK_LISTENER);

        userEditText = (EditText) findViewById(R.id.login_user_name);
        passwordEditText = (EditText) findViewById(R.id.login_password);

        hideActionBar();
    }


    private View.OnClickListener LOGIN_CLICK_LISTENER = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String user_name = userEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            ArrayList<String> errors = new ArrayList<>();
            if (user_name.equalsIgnoreCase("")) {
                errors.add("Username is empty");
            }
            if (password.equalsIgnoreCase("")) {
                errors.add("Password is empty");
            }

            if (errors.size() > 0) {
                showError(errors);
                return;
            }

            showLoading();


            /*if (configAppVersion != configReloadAppVersion) {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://45.79.94.220/chat/server.php");
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("user_name", user_name));
                    nameValuePairs.add(new BasicNameValuePair("password", password));
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = client.execute(post);
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "", fulldata = "";
                    while ((line = rd.readLine()) != null) {
                        fulldata += line;
                    }

                    checkConfig(fulldata);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

            AccountManager.getSharedInstance().login(LoginActivity.this, user_name, password, new EventCallback() {
                @Override
                public void onSuccess(JSONObject js) {
                    hideLoading();
                    if (js != null)
                        Log.d(TAG, js.toString());

                    Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }

                @Override
                public void onFailure(String message) {
                    hideLoading(message);
                    Log.d(TAG, "Error: " + message);
                }

                @Override
                public void onError(JSONObject js, String message) {
                    hideLoading(message);
                    Log.d(TAG, "Error2: " + message);
                }
            });

        }
    };

    /*protected void checkConfig(String res) {
        try {
            objHelper = new JSONObject(res);

            if (objHelper.getString("STATUS").equalsIgnoreCase("ERROR")) {
                hideLoading(objHelper.getString("STATUSMSG"));
                Log.d(TAG, "Error: " + objHelper.getString("STATUSMSG"));
            }
            else {
                //Log.d("My App", obj.toString());
                HelperConfig helperConfig = new HelperConfig();

                // read from json and add to this object - helperConfig//

                DataBaseManager dataBaseManager = new DataBaseManager(LoginActivity.this);
                helperConfig.setConfigAppVersion(configReloadAppVersion);


                helperConfig.setHOST(objHelper.getString("HOST"));
                helperConfig.setPORT(objHelper.getString("PORT"));
                helperConfig.setHOST_USER_NAME(objHelper.getString("HOST_USER_NAME"));
                System.out.println("HOST_USER_NAME:"+objHelper.getString("HOST_USER_NAME"));
                helperConfig.setHOST_PWD(objHelper.getString("HOST_PWD"));
                helperConfig.setFORGET_PWD_URL(objHelper.getString("FORGET_PWD_URL"));
                System.out.println("inserting value to database");
                dataBaseManager.addHelperConfig(helperConfig);
                System.out.println("inserted value to HELPER_TABLE");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
