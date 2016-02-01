package com.jugaado.jugaado.activities.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.CategoryActivity;
import com.jugaado.jugaado.activities.MessagingActivity;
import com.jugaado.jugaado.activities.base.BaseActivity;
import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.manager.AccountManager;
import com.jugaado.jugaado.models.User;

import org.json.JSONObject;

/**
 * Created by ravivooda on 10/11/15.
 */
public class ProfileActivity extends BaseActivity {
    public static final String TAG = "Profile Activity";


    /**
     * Static Detail Views
     */
    private TextView full_name_text_view;
    private TextView street_text_view;
    private TextView street_2_text_view;
    private TextView city_text_view;
    private TextView pincode_text_view;

    private TextView[] static_text_views;

    /**
     * Editing Detail Views
     */
    private EditText full_name_edit_view;
    private EditText street_edit_view;
    private EditText street_2_edit_view;
    private EditText city_edit_view;
    private EditText pincode_edit_view;

    private EditText[] editing_edit_views;


    /**
     * Other References
     */
    private Button saveButton;
    private LinearLayout addressContainer;
    private TextView noAddressLabel;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loadOtherCustomActionBar();
        final ImageView editImageView = (ImageView)findViewById(R.id.action_button_edit);
        editImageView.setVisibility(View.VISIBLE);
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditing(true);
                editImageView.setVisibility(View.INVISIBLE);
            }
        });

        // Loading Static TextViews
        static_text_views = new TextView[5];
        static_text_views[0] = full_name_text_view = (TextView) findViewById(R.id.profile_name_text_view);
        static_text_views[1] = street_text_view = (TextView) findViewById(R.id.profile_street_text_view);
        static_text_views[2] = street_2_text_view = (TextView) findViewById(R.id.profile_street_2_text_view);
        static_text_views[3] = city_text_view = (TextView) findViewById(R.id.profile_city_text_view);
        static_text_views[4] = pincode_text_view = (TextView) findViewById(R.id.profile_pincode_text_view);

        // Loading Editable EditViews
        editing_edit_views = new EditText[5];
        editing_edit_views[0] = full_name_edit_view = (EditText) findViewById(R.id.profile_name_edit_view);
        editing_edit_views[1] = street_edit_view = (EditText) findViewById(R.id.profile_street_edit_view);
        editing_edit_views[2] = street_2_edit_view = (EditText) findViewById(R.id.profile_street_2_edit_view);
        editing_edit_views[3] = city_edit_view = (EditText) findViewById(R.id.profile_city_edit_view);
        editing_edit_views[4] = pincode_edit_view = (EditText) findViewById(R.id.profile_pincode_edit_view);

        saveButton = (Button) findViewById(R.id.profile_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
                editImageView.setVisibility(View.VISIBLE);
            }
        });

        addressContainer = (LinearLayout) findViewById(R.id.profile_address_container);
        noAddressLabel = (TextView) findViewById(R.id.profile_no_address_text_view);

        loadDetails(mainUser);
        showBackInActionBar();
    }

    private void setEditing(boolean isEditing) {
        this.isEditing = isEditing;
        for (EditText editText: editing_edit_views) {
            editText.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        }
        for (TextView textView : static_text_views) {
            textView.setVisibility(isEditing? View.GONE : View.VISIBLE);
        }

        invalidateOptionsMenu();
        saveButton.setVisibility(isEditing ? View.VISIBLE: View.GONE);

        if (isEditing) {
            addressContainer.setVisibility(View.VISIBLE);
            noAddressLabel.setVisibility(View.GONE);
        }
    }

    private void loadDetails(User user) {
        Log.d(TAG, "Main User is " + mainUser);
        setEditing(false);
        full_name_text_view.setText(user.getFull_name());
        full_name_edit_view.setText(user.getFull_name());

        street_text_view.setText(user.getAddress().getStreet_address());
        street_edit_view.setText(user.getAddress().getStreet_address());

        street_2_text_view.setText(user.getAddress().getStreet_address_2());
        street_2_edit_view.setText(user.getAddress().getStreet_address_2());

        city_text_view.setText(user.getAddress().getCity());
        city_edit_view.setText(user.getAddress().getCity());

        pincode_text_view.setText(user.getAddress().getPincode());
        pincode_edit_view.setText(user.getAddress().getPincode());

        if (user.getAddress().getStreet_address_2() == null || user.getAddress().getStreet_address_2().equals("")) {
            street_2_text_view.setVisibility(View.GONE);
        }

        addressContainer.setVisibility(user.getAddress().isEmpty() ? View.GONE : View.VISIBLE);
        noAddressLabel.setVisibility(user.getAddress().isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void saveDetails() {
        showLoading();
        final String name = full_name_edit_view.getText().toString();
        final String street_address = street_edit_view.getText().toString();
        final String street_address_2 = street_2_edit_view.getText().toString();
        final String city = city_edit_view.getText().toString();
        final String pincode = pincode_edit_view.getText().toString();
        accountManager.saveProfileDetails(this, name, street_address, street_address_2, city, pincode, new EventCallback() {
            @Override
            public void onSuccess(JSONObject js) {
                hideLoading();

                // Saving Name
                mainUser.save(name);
                mainUser.getAddress().save(street_address, street_address_2, city, pincode);

                loadDetails(mainUser);
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
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.action_edit).setVisible(!isEditing);
        return retValue;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            setEditing(true);
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void onClickChat_category(View v){
        Intent intent = new Intent(ProfileActivity.this, CategoryActivity.class);
        startActivity(intent);
    }
    public void onClickChat_direct(View v){
        Intent intent = new Intent(ProfileActivity.this, MessagingActivity.class);
        startActivity(intent);
    }
}
