package com.jugaado.jugaado.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.base.BaseActivity;
import com.jugaado.jugaado.adapters.MessagingAdapter;
import com.jugaado.jugaado.chatMessage.ChatMessage;
import com.jugaado.jugaado.chatMessage.ChatMessageAdapter;
import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.manager.AccountManager;
import com.jugaado.jugaado.models.Message;
import com.jugaado.jugaado.models.MessageThread;
import com.jugaado.jugaado.notifications.RefreshListener;
import com.jugaado.jugaado.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessagingActivity extends BaseActivity implements RefreshListener {
    public static final String TAG = "Messaging Activity";

    private MessageThread messageThread;

    private MessagingAdapter messagingAdapter;
    private ListView messagesListView;
    private EditText messageInputEditText;
    private Button sendButton;
    private LinearLayout messageEmptyContainer;
    private TextView chat_description;
    String chatcategory;
    ConnectivityManager connectivityManager;
    NetworkInfo activeNetworkInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messaging);

        //  Custom Action Bar
        loadCustomActionBar();

        ImageButton buttonChat=(ImageButton)findViewById(R.id.chat_button);
        buttonChat.setEnabled(false);

        ImageView cat_chat_separator = (ImageView)findViewById(R.id.cat_chat_separator);


        Drawable catChatSeparatorDrawable = ContextCompat.getDrawable(this, R.drawable.cat_chat_separator_transparent);
        cat_chat_separator.setImageDrawable(catChatSeparatorDrawable);


        RelativeLayout chat_icon_layout = (RelativeLayout)findViewById(R.id.chat_icon_layout);
        chat_icon_layout.setBackgroundResource(R.color.gainsboro);


        chatcategory = getIntent().getStringExtra("chat_CATEGORY");
        chatcategory = (chatcategory==null?"test":chatcategory);

        Log.d(TAG,chatcategory);
        chat_description=(TextView)findViewById(R.id.chat_description);
        //chat_description.setText("Start a conversation now! What would you like to know about "+ chatcategory +"?");



        messagesListView = (ListView) findViewById(R.id.messages_list_view);
        messageInputEditText = (EditText) findViewById(R.id.message_edit_text);
        sendButton = (Button) findViewById(R.id.message_send_button);
        messageEmptyContainer = (LinearLayout) findViewById(R.id.messages_empty_message_container);

        messagingAdapter = new MessagingAdapter(this, R.layout.message_list_item, null);
        messagesListView.setAdapter(messagingAdapter);




        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageInputEditText.getText().toString().equals("")) {
                    sendMessage(messageInputEditText.getText().toString());
                    messageInputEditText.setText("");
                    chat_description.setText("");
                }
            }
        });

        messageInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendButton.setEnabled(!messageInputEditText.getText().toString().equals(""));
            }
        });
        sendButton.setEnabled(false);
        if(!chatcategory.equals("test")) {
            loadMessages();
            sendMessage(chatcategory,true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AccountManager.getSharedInstance().removeRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AccountManager.getSharedInstance().addRefreshListener(this);
        loadMessages();
    }
    public boolean isInternetAvailable(Context context) {
        //ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
        try{
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if(activeNetworkInfo.isConnected())
            {
                return true;
            }
        }
        catch(Exception e){
            Log.d(TAG, "CheckConnectivity Exception: " + e.getMessage());
        }

        return false;

    }

    public void sendMessage(String text) {
        sendMessage(text,false);
    }
    private void sendMessage(String text, boolean isCategoryMsg) {
        if(isInternetAvailable(this.getApplicationContext())) {
            Log.d(TAG, "Internet true");
            text = text.trim();
            final Message message;

            if (isCategoryMsg)
                message = new Message(text,true);
            else
                message = new Message(text);
            messagingAdapter.notifyDataSetChanged();
            messageThread.sendMessage(this, message, new EventCallback() {
                @Override
                public void onSuccess(JSONObject js) {
                    Log.d(TAG, "Successfully sent message :" + message.getMessage());
                }

                @Override
                public void onFailure(String message) {
                    Helper.showError(message, MessagingActivity.this);
                }

                @Override
                public void onError(JSONObject js, String message) {
                    Log.d(TAG, "Got error: " + message);
                    Helper.showError(message, MessagingActivity.this);
                }
            });
            messagesListView.setSelection(this.messageThread.getCount());
        }
        else{
            Log.d(TAG, "Internet false");
            MessagingActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    showError("Unable to connect to Jugaado. Please check your internet connection");
                }
            });

            AccountManager.getSharedInstance().logout(MessagingActivity.this);

        }
    }

    @Override
    public void updateMessageThread(int position, int thread_id) {
        loadMessages();
    }

    public void loadMessages(){
        MessageThread[] userThreads = AccountManager.getSharedInstance().getUserThreads();
        if (userThreads != null && userThreads.length > 0){
            this.messageThread = userThreads[0];
        } else {
            this.messageThread = AccountManager.getSharedInstance().getNewUserThread("");
        }

        if (this.messageThread.getMessages().size() == 0) {
            messageEmptyContainer.setVisibility(View.VISIBLE);
        } else {
            messageEmptyContainer.setVisibility(View.GONE);
        }

        //Message msg = new Message("This is test");

        messagingAdapter.setMessageArrayList(this.messageThread.getMessages());
        messagingAdapter.notifyDataSetChanged();
        messagesListView.setSelection(this.messageThread.getCount());
    }
    public void onClickChat_category(View v){
        Intent intent = new Intent(MessagingActivity.this, CategoryActivity.class);
        startActivity(intent);
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
