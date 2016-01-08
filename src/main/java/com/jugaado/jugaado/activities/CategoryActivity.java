package com.jugaado.jugaado.activities;

/**
 * Created by websofttechs on 11/12/2015.
 */

        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.BitmapFactory;
        import android.graphics.PorterDuff;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.widget.Button;
        import android.widget.GridView;
        import android.content.Context;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.jugaado.jugaado.R;
        import com.jugaado.jugaado.activities.base.BaseActivity;
        import com.jugaado.jugaado.activities.profile.ProfileActivity;
        import com.jugaado.jugaado.activities.settings.SettingsActivity;
        import com.jugaado.jugaado.adapters.MessagingAdapter;
        import com.jugaado.jugaado.data.EventCallback;
        import com.jugaado.jugaado.manager.AccountManager;
        import com.jugaado.jugaado.models.Message;
        import com.jugaado.jugaado.models.MessageThread;
        import com.jugaado.jugaado.models.User;
        import com.jugaado.jugaado.utils.Helper;

        import org.jivesoftware.smack.SmackException;
        import org.jivesoftware.smack.chat.Chat;
        import org.json.JSONObject;

        import java.lang.*;

public class CategoryActivity extends BaseActivity {
    String category = "Category_Test";
    public static final String TAG = "Category Activity";

    private MessagingAdapter messagingAdapter;
    private TextView fullName;

    Chat chat;

    MessageThread messageThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        //display custom_action_bar layout
        loadCustomActionBar();

        fullName=(TextView)findViewById(R.id.textView6);
        loadUserName(mainUser);



        ImageButton buttonCategory = (ImageButton) findViewById(R.id.category_button);
        buttonCategory.setEnabled(false);

        RelativeLayout category_icon_layout = (RelativeLayout)findViewById(R.id.cat_icon_layout);
        category_icon_layout.setBackgroundColor(R.color.gainsboro);


        /*image1 = (ImageView) findViewById(R.id.imageView);


        //set the ontouch listener
        image1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        // overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                category = "shopping";
                //sendCategory(category);
                Intent intent = new Intent(CategoryActivity.this, MessagingActivity.class);
                //intent.putExtra("chat_CATEGORY", category);
                startActivity(intent);
                return true;
            }


        });*/

    }
    private void loadUserName(User user) {
        Log.d(TAG, "Main User is " + mainUser);
        fullName.setText("Hi "+user.getFull_name()+", get all the information you want");
    }

    public void onClickShopping(View v) {
        //image1.setImageResource(R.drawable.learning_hobbies_edit);
        category = "Welcome to Jugaado! We understand that there is very little to life that is better than shopping. Let us know what you are looking for";
        //sendCategory(category);
        Intent intent = new Intent(CategoryActivity.this, MessagingActivity.class);
        intent.putExtra("chat_CATEGORY", category);
        startActivity(intent);
    }

    public void onClickOthers(View v) {
        category = "Welcome to Jugaado! How may we help you?";
        //sendCategory(category);
        Intent intent = new Intent(CategoryActivity.this, MessagingActivity.class);
        intent.putExtra("chat_CATEGORY", category);
        startActivity(intent);
    }

    public void onClickFood(View v) {
        category = "Welcome to Jugaado! We understand that there is no sincerer love than the love for food. So what are you craving for?";
        //sendCategory(category);
        Intent intent = new Intent(CategoryActivity.this, MessagingActivity.class);
        intent.putExtra("chat_CATEGORY", category);
        startActivity(intent);
    }

    public void onClickServices(View v) {
        category = "Welcome to Jugaado! From a plumbing problem at home, to a tyre problem on the road, we are there to help you out. Just let us know what your problem is.";
        //sendCategory(category);
        Intent intent = new Intent(CategoryActivity.this, MessagingActivity.class);
        intent.putExtra("chat_CATEGORY", category);
        startActivity(intent);
    }

    public void onClickDelivery(View v) {
        category = "Welcome to Jugaado! We facilitate any kind of transfer of any kind of items, from a delivery to a transport. Just let us know your query.";
        //sendCategory(category);
        Intent intent = new Intent(CategoryActivity.this, MessagingActivity.class);
        intent.putExtra("chat_CATEGORY", category);
        startActivity(intent);
    }

    public void onClickConsultation(View v) {
        category = "Welcome to Jugaado! While we understand that nobody can be wiser than yourself, some times we all need some help. How can we help you today?";
        //sendCategory(category);
        Intent intent = new Intent(CategoryActivity.this, MessagingActivity.class);
        intent.putExtra("chat_CATEGORY", category);
        startActivity(intent);
    }

    public void onClickTravel(View v) {
        category = "Welcome to Jugaado! Not all those who wander are lost. But if you are, we are here to help you out. Tell us about your concern.";
        //sendCategory(category);
        Intent intent = new Intent(CategoryActivity.this, MessagingActivity.class);
        intent.putExtra("chat_CATEGORY", category);
        startActivity(intent);
    }

    public void onClickWeather(View v) {
        category = "Welcome to Jugaado! Are you wondering how the skies look today? Just tell us what weather update would you like?";
        //sendCategory(category);
        Intent intent = new Intent(CategoryActivity.this, MessagingActivity.class);
        intent.putExtra("chat_CATEGORY", category);
        startActivity(intent);
    }



    public void onClickChat_direct(View v) {
    Intent intent=new Intent(CategoryActivity.this,MessagingActivity.class);
    startActivity(intent);

    }


}