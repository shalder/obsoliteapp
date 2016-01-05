package com.jugaado.jugaado.activities.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.base.BaseActivity;

/**
 * Created by websofttechs on 1/2/2016.
 */
public class AboutUsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        //showBackInActionBar();
    }
}
