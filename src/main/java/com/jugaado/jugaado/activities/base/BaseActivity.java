package com.jugaado.jugaado.activities.base;

import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.MessagingActivity;
import com.jugaado.jugaado.activities.profile.ProfileActivity;
import com.jugaado.jugaado.activities.settings.SettingsActivity;
import com.jugaado.jugaado.application.JugaadoApplication;
import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.manager.AccountManager;
import com.jugaado.jugaado.models.User;

import java.util.ArrayList;
import java.util.Iterator;

public class BaseActivity extends AppCompatActivity {
    private RelativeLayout progressBar;

    /**
     * Some of the common objects used across activities;
     */
    protected static AccountManager accountManager;
    protected static User mainUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (accountManager == null) {
            accountManager = AccountManager.getSharedInstance();
        }

        mainUser = accountManager.getAppUser();

        progressBar = new RelativeLayout(this);
        ProgressBar actualProgressBar = new ProgressBar(this);
        RelativeLayout.LayoutParams loadingParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        progressBar.addView(actualProgressBar, loadingParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            AccountManager.getSharedInstance().logout(this);
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void loadCustomActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);


        ImageView profileImageView = (ImageView) mCustomView.findViewById(R.id.action_button_profile);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageView settingsImageView = (ImageView) mCustomView.findViewById(R.id.action_button_settings);
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });



        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent =(Toolbar) mCustomView.getParent();



        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * SENDING ANALYTICS EVENTS
         */
        JugaadoApplication application = (JugaadoApplication) getApplication();
        application.getAnalyticsTracker().setScreenName(this.getClass().getSimpleName());
    }

    protected void showBackInActionBar() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeButtonEnabled(true);
    }

    protected void hideActionBar() {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    protected void showActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
    }

    protected void showLoading() {
        addContentView(progressBar, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected void hideLoading() {
        ViewGroup parentGroup = (ViewGroup) progressBar.getParent();
        if (parentGroup != null) {
            parentGroup.removeView(progressBar);
        }
    }

    protected void hideLoading(String errorMessage) {
        hideLoading();
        showError(errorMessage);
    }

    /**
     * ERROR DISPLAYERS
     */
    protected void showError(String message) {
        showError(message, null);
    }

    protected void showError(ArrayList<String> errors) {
        String error_string = "";
        Iterator<String> iterator = errors.iterator();
        while (iterator.hasNext()) {
            error_string = error_string.concat("► " + iterator.next());
            if (iterator.hasNext())
                error_string = error_string.concat("\n");
        }
        AlertDialog.Builder alertErrors = new AlertDialog.Builder(this);
        alertErrors.setMessage(error_string);
        alertErrors.setPositiveButton("Okay", null);
        alertErrors.show();
    }

    protected void showError(String message, AlertDialog.OnClickListener callback) {
        AlertDialog.Builder errorBuilder = new AlertDialog.Builder(this);
        errorBuilder.setMessage(message);
        String positiveMessage = "Okay";
        if (callback != null) {
            positiveMessage = "Retry";
            errorBuilder.setNegativeButton("Okay", null);
        }
        errorBuilder.setPositiveButton(positiveMessage, callback);
        errorBuilder.show();
    }
}
