package com.jugaado.jugaado.activities.auth;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.CategoryActivity;
import com.jugaado.jugaado.activities.ForgetPasswordActivity;
import com.jugaado.jugaado.activities.base.BaseActivity;
import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.manager.AccountManager;
import com.jugaado.jugaado.manager.DataBaseManager;

import org.json.JSONObject;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        signUpText = (TextView) findViewById(R.id.signup_text);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        forgetpasswordText=(TextView)findViewById(R.id.forgetpassword_text);
        forgetpasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intnt=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
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
            if (password.equalsIgnoreCase("")){
                errors.add("Password is empty");
            }

            if (errors.size() > 0) {
                showError(errors);
                return;
            }

            showLoading();
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


}
