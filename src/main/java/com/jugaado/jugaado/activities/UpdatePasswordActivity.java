package com.jugaado.jugaado.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jugaado.jugaado.R;

/**
 * Created by websofttechs on 11/30/2015.
 */
public class UpdatePasswordActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpasswordwithpasskey);
        Button update_button=(Button)findViewById(R.id.button2);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //org.jivesoftware.openfire.commands.admin.user.ChangeUserPassword changeUserPassword=new org.jivesoftware.openfire.commands.admin.user.ChangeUserPassword();

            }
        });
    }

}
