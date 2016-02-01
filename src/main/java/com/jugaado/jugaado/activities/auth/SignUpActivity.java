package com.jugaado.jugaado.activities.auth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.CategoryActivity;
import com.jugaado.jugaado.activities.MessagingActivity;
import com.jugaado.jugaado.activities.base.BaseActivity;
import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.manager.AccountManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class SignUpActivity extends BaseActivity {

    public static final String TAG = "Sign Up Activity";

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;


    private Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loadOtherCustomActionBar();
        nameEditText = (EditText) findViewById(R.id.signup_name_edittext);
        usernameEditText = (EditText) findViewById(R.id.signup_username_edit_text);
        emailEditText = (EditText) findViewById(R.id.signup_email_edittext);
        passwordEditText = (EditText) findViewById(R.id.signup_password_edittext);
        signUpButton = (Button) findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(SIGNUP_CLICK_LISTENER);
    }
    private boolean signUpValidation(){
        String EMAIL_REGEX ="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        String USERNAME_REGEX ="[a-zA-Z0-9 ]*";
        ArrayList<String> errors = new ArrayList<>();
        if ("".equals(nameEditText.getText().toString())){
            errors.add("Please provide your name");
        }

        if ("".equals(usernameEditText.getText().toString())){
            errors.add("Please provide your username");
        }

        if(!usernameEditText.getText().toString().matches(USERNAME_REGEX)){
            errors.add("Please provide username with no special character");
        }

        if (!emailEditText.getText().toString().matches(EMAIL_REGEX)){
            errors.add("Please provide your valid email");
        }

        if ("".equals(passwordEditText.getText().toString())){
            errors.add("Please select a password for authenticity");
        }
        if (errors.size() > 0){
            showError(errors);
            return false;
        }

        return true;
    }
    private View.OnClickListener SIGNUP_CLICK_LISTENER = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean status = signUpValidation();
            if (status) {
                showLoading();
                AccountManager.getSharedInstance().signup(SignUpActivity.this,
                        nameEditText.getText().toString(),
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        emailEditText.getText().toString(), new EventCallback() {
                            @Override
                            public void onSuccess(JSONObject js) {
                                hideLoading();
                                Intent intent = new Intent(SignUpActivity.this, CategoryActivity.class);
                                startActivity(intent);
                                SignUpActivity.this.finish();
                            }

                            @Override
                            public void onFailure(String message) {
                                hideLoading(message);
                            }

                            @Override
                            public void onError(JSONObject js, String message) {
                                hideLoading(message);
                            }
                        });
            }
        }
    };
}
