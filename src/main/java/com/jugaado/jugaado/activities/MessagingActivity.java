package com.jugaado.jugaado.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.jugaado.jugaado.notifications.RefreshListener;
import com.jugaado.jugaado.utils.Helper;

import org.json.JSONObject;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        //  Custom Action Bar
        loadCustomActionBar();

        ImageButton buttonChat=(ImageButton)findViewById(R.id.chat_button);
        buttonChat.setEnabled(false);

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
            sendMessage(chatcategory);
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
    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    private void sendMessage(String text) {
        if(isInternetAvailable()) {
            text = text.trim();
            final Message message = new Message(text);
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
            MessagingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                        showError("Unable to connect to Jugaado. Please check your internet connection");
                }
            });
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

}
