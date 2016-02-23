package com.jugaado.jugaado.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.auth.LoginActivity;
import com.jugaado.jugaado.activities.base.BaseActivity;
import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.manager.AccountManager;
import com.jugaado.jugaado.manager.DataBaseManager;
import com.jugaado.jugaado.utils.Helper;
import com.jugaado.jugaado.utils.HelperConfig;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




public class SplashActivity extends BaseActivity {
    public static final String TAG = "Splash Activity";
    private LinearLayout loadingContainer;
    private LinearLayout messageContainer;
    private JSONObject obj,objHelper;
    float appVersion;
    float configAppVersion;
    float configReloadAppVersion;
    String playStoreURL;
    String msgStr=Helper.SECRETSTRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadingContainer = (LinearLayout) findViewById(R.id.splash_loading_container);
        messageContainer = (LinearLayout) findViewById(R.id.splash_display_message_container);

        findViewById(R.id.splash_retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadConfig();
                tryLogin();
            }
        });
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        //obtaining APP Version
        PackageManager manager = this.getApplicationContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appVersion = Float.parseFloat(info.versionName);
        Log.d("appVersion : ", appVersion + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConfig();
        tryLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void tryLogin(){
        AccountManager.getSharedInstance().loadXMPP(SplashActivity.this, new EventCallback() {
            @Override
            public void onSuccess(JSONObject js) {
                Log.d(TAG, "Successfully loaded the XMPP");
                Intent intent;
                if (AccountManager.getSharedInstance().isLoggedIn()) {
                    Log.d(TAG, "And successfully logged in");
                    intent = new Intent(SplashActivity.this, CategoryActivity.class);
                    intent.putExtra("configAppVersion", configAppVersion);
                    intent.putExtra("configReloadAppVersion", configReloadAppVersion);
                    intent.putExtra("playStoreURL", playStoreURL);
                } else {
                    Log.d(TAG, "Displaying the login activity");
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.putExtra("configAppVersion", configAppVersion);
                    intent.putExtra("configReloadAppVersion", configReloadAppVersion);
                    intent.putExtra("playStoreURL", playStoreURL);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                showError();
            }

            @Override
            public void onError(JSONObject js, String message) {
                showError();
            }
        });
    }

    private void showError(){
        messageContainer.setVisibility(View.VISIBLE);
        loadingContainer.setVisibility(View.GONE);
    }

    private void loadConfig() {
        HttpClient client = new DefaultHttpClient();

        try {

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //your codes here

            }

            HttpGet request = new HttpGet(Helper.CONFIGURL);

            HttpResponse response = client.execute(request);

            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            String line = "", fulldata = "";
            while ((line = rd.readLine()) != null) {
                fulldata += line;
            }
            checkConfig(fulldata);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    protected void checkConfig(String res) {
        try {

            obj = new JSONObject(res);

            //Log.d("My App", obj.toString());

            //System.out.println(res);
            Iterator itr = obj.keys();

            float latestVersion = Float.parseFloat(obj.getString("latestVersion"));
            float forceUpgrade = Float.parseFloat(obj.getString("forceUpgrade"));

            Log.d(TAG, "latestVersion:" + latestVersion);
            Log.d(TAG, "forceUpgrade:" + forceUpgrade);

            DataBaseManager dataBaseManager = new DataBaseManager(SplashActivity.this);

            HelperConfig helperConfig = null;

            if(appVersion!=latestVersion) {

                if (appVersion < forceUpgrade) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("playStoreURL")));
                    startActivity(browserIntent);
                    Log.d(TAG, "I am in 1: Please Upgrade to the latest app.");

                    Toast.makeText(SplashActivity.this, "Please Upgrade to the latest app.", Toast.LENGTH_LONG).show();

                    android.os.Process.killProcess(android.os.Process.myPid());
                } else {
                    Log.d(TAG, "I am in 2: A New Version is Available");
                    Toast.makeText(SplashActivity.this, "A New Version is Available", Toast.LENGTH_LONG).show();
                }
            }

            try {
                Log.d(TAG, "trying to get value from database");
                helperConfig = dataBaseManager.getConfig();
                Log.d(TAG, "getting value from database");
                configAppVersion = helperConfig.getConfigAppVersion();
                configReloadAppVersion = Float.parseFloat(obj.getString("configReloadAppVersion"));
                playStoreURL = obj.getString("playStoreURL");

                //String secret2 = getSecretCode();
                //System.out.println ("secret2secret2secret2:::::" + secret2);


                Log.d(TAG, "configReloadAppVersion:" + configReloadAppVersion);
                Log.d(TAG, "configAppVersion:" + configAppVersion);

                if (configReloadAppVersion>configAppVersion) {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(Helper.SERVERURL);
                    try {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        String msgDigest = getMD5(msgStr); //msgStr is "My App Jugaado" which is globally initialized

                        nameValuePairs.add(new BasicNameValuePair("secret", msgDigest));
                        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        HttpResponse response = client.execute(post);
                        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        String line = "", fulldata = "";
                        while ((line = rd.readLine()) != null) {
                            fulldata += line;
                        }

                        helperConfig = validateConfig(fulldata);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



            } catch (Exception ex) {
                ex.printStackTrace();
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("playStoreURL")));
                startActivity(browserIntent);

                Log.d(TAG, "I am in 3: Please Upgrade to the latest app.");

                Toast.makeText(SplashActivity.this,"Please Upgrade to the latest app.",Toast.LENGTH_LONG).show();

                android.os.Process.killProcess(android.os.Process.myPid());*/
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Helper.SERVERURL);
                Log.d(TAG, "I am in 3: Please Upgrade to the latest app.");
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    String msgDigest = getMD5(msgStr); //msgStr is "My App Jugaado" which is globally initialized
                    nameValuePairs.add(new BasicNameValuePair("secret", msgDigest));
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = client.execute(post);
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "", fulldata = "";
                    while ((line = rd.readLine()) != null) {
                        fulldata += line;
                    }

                    helperConfig = validateConfig(fulldata);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            AccountManager.getSharedInstance().createHelper(SplashActivity.this, helperConfig);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }





    protected HelperConfig validateConfig(String res) {
        HelperConfig helperConfig = null;
        try {
            objHelper = new JSONObject(res);

            if (objHelper.getString("STATUS").equalsIgnoreCase("ERROR")) {
                hideLoading(objHelper.getString("STATUSMSG"));
                Log.d(TAG, "Error: " + objHelper.getString("STATUSMSG"));
            } else {
                //Log.d("My App", obj.toString());

                helperConfig = new HelperConfig();
                // read from json and add to this object - helperConfig//

                DataBaseManager dataBaseManager = new DataBaseManager(SplashActivity.this);
                helperConfig.setConfigAppVersion(configReloadAppVersion);


                helperConfig.setHOST(objHelper.getString("HOST"));
                helperConfig.setPORT(objHelper.getString("PORT"));
                helperConfig.setHOST_USER_NAME(objHelper.getString("HOST_USER_NAME"));
                helperConfig.setHOST_PWD(objHelper.getString("HOST_PWD"));
                helperConfig.setFORGET_PWD_URL(objHelper.getString("FORGET_PWD_URL"));
                Log.d(TAG, "inserting value to database");
                dataBaseManager.addHelperConfig(helperConfig);
                Log.d(TAG, "inserted value to HELPER_TABLE");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return helperConfig;
    }
}
