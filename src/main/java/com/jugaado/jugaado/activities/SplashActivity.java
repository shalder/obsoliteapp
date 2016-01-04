package com.jugaado.jugaado.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.auth.LoginActivity;
import com.jugaado.jugaado.activities.base.BaseActivity;
import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.manager.AccountManager;

import org.json.JSONObject;


public class SplashActivity extends BaseActivity {
    public static final String TAG = "Splash Activity";
    private LinearLayout loadingContainer;
    private LinearLayout messageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadingContainer = (LinearLayout) findViewById(R.id.splash_loading_container);
        messageContainer = (LinearLayout) findViewById(R.id.splash_display_message_container);

        findViewById(R.id.splash_retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                } else {
                    Log.d(TAG, "Displaying the login activity");
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
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
}
