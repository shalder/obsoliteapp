package com.jugaado.jugaado.activities.settings;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.CategoryActivity;
import com.jugaado.jugaado.activities.MessagingActivity;
import com.jugaado.jugaado.activities.base.BaseActivity;
import com.jugaado.jugaado.activities.profile.ProfileActivity;
import com.jugaado.jugaado.manager.AccountManager;
import com.jugaado.jugaado.utils.Helper;

import java.util.List;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        loadOtherCustomActionBar();
        //showBackInActionBar();

        findViewById(R.id.settings_about_text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.activity_aboutus);
                Intent intent=new Intent(SettingsActivity.this,AboutUsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.settings_terms_conditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Helper.openURL(SettingsActivity.this, "http://jugaado.in/tnc.html");
                Intent intent=new Intent(SettingsActivity.this,TermsNConditionActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.settings_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder sureLogout = new AlertDialog.Builder(SettingsActivity.this);
                sureLogout.setMessage("Are you sure you wanna logout?");
                sureLogout.setNegativeButton("Nah!", null);
                sureLogout.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountManager.getSharedInstance().logout(SettingsActivity.this);
                    }
                });
                sureLogout.show();
            }
        });
    }
    public void onClickChat_category(View v){
        Intent intent = new Intent(SettingsActivity.this, CategoryActivity.class);
        startActivity(intent);
    }
    public void onClickChat_direct(View v){
        Intent intent = new Intent(SettingsActivity.this, MessagingActivity.class);
        startActivity(intent);
    }
}
