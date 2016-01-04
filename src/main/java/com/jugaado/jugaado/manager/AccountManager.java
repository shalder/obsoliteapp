package com.jugaado.jugaado.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.jugaado.jugaado.R;
import com.jugaado.jugaado.activities.auth.LoginActivity;
import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.models.MessageThread;
import com.jugaado.jugaado.models.User;
import com.jugaado.jugaado.notifications.RefreshListener;
import com.jugaado.jugaado.utils.Helper;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.packet.Registration;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ravivooda on 9/5/15.
 */
public class AccountManager {
    public static final String TAG = "Account Manager";
    private User appUser;
    private final ArrayList<MessageThread> userThreads;
    private final static ArrayList<RefreshListener> refreshListeners = new ArrayList<>();
    String user_id;

    private static DataBaseManager dataBaseManager;

    XMPPTCPConnection xmpptcpConnection;
    XMPPTCPConnectionConfiguration xmpptcpConnectionConfiguration;

    ChatManager chatManager;
    Chat chat;

    /**
     * Static method to get instance of the AccountManager;
     * @return Returns a sharedInstance
     */
    private static AccountManager sharedInstance;
    public static AccountManager getSharedInstance(){
        synchronized (AccountManager.class) {
            if (sharedInstance == null) {
                sharedInstance = new AccountManager();
            }
        }
        return sharedInstance;
    }

    public void loadXMPP(final Activity activity, final EventCallback callback) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (AccountManager.this) {
                    if (xmpptcpConnection == null || !xmpptcpConnection.isConnected()) {
                        if (xmpptcpConnection != null) {
                            xmpptcpConnection.disconnect();
                        }
                        xmpptcpConnectionConfiguration = XMPPTCPConnectionConfiguration.builder()
                                .setServiceName(Helper.host)
                                .setHost(Helper.host)
                                .setPort(Helper.port)
                                .setUsernameAndPassword(Helper.host_user_name, Helper.host_pwd)
                                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                                .setSendPresence(true)
                                .build();
                        xmpptcpConnection = new XMPPTCPConnection(xmpptcpConnectionConfiguration);
                        if (!xmpptcpConnection.isConnected()) {
                            try {
                                xmpptcpConnection.connect();
                            } catch (SmackException | IOException | XMPPException e) {
                                e.printStackTrace();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(new JSONObject(), "Unable to access Jugaado servers. Please check your connection");
                                    }
                                });
                                return;
                            }
                            Log.d(TAG, "Successfully connected to server");
                        }

                    }
                    tryLogin(activity);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(new JSONObject());
                        }
                    });
                }
            }
        });
        thread.start();
    }

    /**
     * Private method to instantiate the AccountManager;
     */
    private AccountManager(){
        userThreads = new ArrayList<>();
    }

    public MessageThread[] getUserThreads() {
        synchronized (userThreads) {
            return userThreads.toArray(new MessageThread[userThreads.size()]);
        }
    }

    public MessageThread getNewUserThread(String string) {
        synchronized (userThreads){
            // Save the thread to database
            userThreads.add(0,new MessageThread(new JSONObject()));
        }
        return userThreads.get(0);
    }

    public User getAppUser() {
        Log.d(TAG, "Returning Main User " + appUser);
        return appUser;
    }

    public void logout(Activity activity) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                while (!success) {
                    try {
                        Log.d(TAG, "Trying to logout");
                        Presence offlinePresence = new Presence(Presence.Type.unavailable, "offline", 1, Presence.Mode.away);
                        xmpptcpConnection.sendStanza(offlinePresence);
                        xmpptcpConnection.disconnect();
                        xmpptcpConnection = new XMPPTCPConnection(xmpptcpConnectionConfiguration);
                        success = true;
                        //dataBaseManager.clearMessages();
                        userThreads.clear();
                        Log.d(TAG, "Successfully Logged Out");
                    } catch (SmackException.NotConnectedException e) {
                        Log.d(TAG, "Unable to logout");
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.SHARED_PREFERENCES_KEY), Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        appUser = null;

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    private boolean tryLogin(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.SHARED_PREFERENCES_KEY), Context.MODE_PRIVATE);
        String user_name = sharedPreferences.getString(activity.getString(R.string.JUGAADO_USER_USERNAME_KEY), "");
        if (!user_name.equals("")){
            String password = sharedPreferences.getString(activity.getString(R.string.JUGAADO_USER_PASSWORD_KEY), "");
            return login(activity, user_name, password);
        }
        return false;
    }

    private void loadChatManager(final Activity activity){
        chatManager = ChatManager.getInstanceFor(xmpptcpConnection);
        chat = chatManager.createChat("fdsa@openfire", new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, final Message message) {
                Log.d(TAG, "Got Some Message: " + message);
                if (userThreads.size() == 0) {
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MessageThread mainThread = userThreads.get(0);
                        SimpleDateFormat sdfL = new SimpleDateFormat("dd.MM.yyyy   HH:mm:ss");
                        final String currentDateandTimeLeft = sdfL.format(new Date());

                        Log.d(TAG,user_id);
                        com.jugaado.jugaado.models.Message add_message = new com.jugaado.jugaado.models.Message(message,currentDateandTimeLeft,user_id);
                        mainThread.receiveMessage(add_message);
                        dataBaseManager.addMessage(add_message);
                        for (RefreshListener listener : refreshListeners) {
                            listener.shouldUpdateMessageThread(0, 0);
                        }
                    }
                });

            }
        });
    }

    @SuppressLint("CommitPrefEdits")
    private boolean login(Activity activity, String user_name, String password){
        this.user_id=user_name;
        try {
            if (!xmpptcpConnection.isConnected()){
                xmpptcpConnection.connect();
            }
            Log.d(TAG, "Trying to login");
            SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
            xmpptcpConnection.login(user_name, password);
            Log.d(TAG, " Success Login ");

            // Okay successful login => Save the credentials for auto login from next time
            SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.SHARED_PREFERENCES_KEY), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(activity.getString(R.string.JUGAADO_USER_USERNAME_KEY), user_name);
            editor.putString(activity.getString(R.string.JUGAADO_USER_PASSWORD_KEY), password);
            editor.commit();

            dataBaseManager = new DataBaseManager(activity);

            dataBaseManager.setUserId(user_id);

            MessageThread messageThread = new MessageThread(dataBaseManager.getAllMessages());
            //userThreads.set(0, getNewUserThread(user_id)); // pratyay needs to do something here

            userThreads.add(messageThread);



            // Also create AppUser
            loadUser(xmpptcpConnection);
            loadChatManager(activity);
            return true;
        } catch (XMPPException | SmackException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void login(final Activity activity, final String user_name, final String password, final EventCallback callback){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                xmpptcpConnection = new XMPPTCPConnection(xmpptcpConnectionConfiguration);
                if (login(activity, user_name, password)){
                    //user_id = user_name;
                    Log.d("print userid",user_id);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(new JSONObject());
                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(new JSONObject(), "Please check your login credentials and retry again");
                        }
                    });
                }
            }
        });
        thread.start();
    }

    public void signup(final Activity activity, final String full_name, final String user_name, final String password, final String email, final EventCallback callback){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (xmpptcpConnection != null && xmpptcpConnection.isConnected()) {
                        Presence offlinePresence = new Presence(Presence.Type.unavailable, "offline", 1, Presence.Mode.away);
                        xmpptcpConnection.sendStanza(offlinePresence);
                        xmpptcpConnection.disconnect();
                    }

                    xmpptcpConnection = new XMPPTCPConnection(xmpptcpConnectionConfiguration);
                    xmpptcpConnection.connect();

                    SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                    SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
                    xmpptcpConnection.login(Helper.host_user_name, Helper.host_pwd);

                    org.jivesoftware.smackx.iqregister.AccountManager accountManager = org.jivesoftware.smackx.iqregister.AccountManager.getInstance(xmpptcpConnection);

                    accountManager.sensitiveOperationOverInsecureConnection(true);
                    HashMap<String, String> details = new HashMap<>();
                    details.put("email", email);
                    details.put("name", full_name);
                    details.put("registered", "1");
                    Log.d(TAG, "Details used for signup are " + details.toString());
                    Log.d(TAG, "User Name " + user_name);

                        accountManager.createAccount(user_name, password, details);
                        Log.d(TAG, "Signup Successful");

                        if (xmpptcpConnection != null && xmpptcpConnection.isConnected()) {
                            Presence offlinePresence = new Presence(Presence.Type.unavailable, "offline", 1, Presence.Mode.away);
                            xmpptcpConnection.sendStanza(offlinePresence);
                            xmpptcpConnection.disconnect();
                        }

                        xmpptcpConnection = new XMPPTCPConnection(xmpptcpConnectionConfiguration);
                        login(activity,user_name,password);

                        // Save vCard first
                        if (saveProfileDetails(full_name, " ", " ", " ", " ")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(new JSONObject());
                                }
                            });
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError(null, "Unable to save profile at the moment. However, we created the account for you");
                                }
                            });
                        }

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(null);
                            }
                        });






                } catch (XMPPException | SmackException | IOException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(e.getMessage().equals("XMPPError: conflict - cancel"))
                                callback.onError(null, "username is already exist");
                            else if (e.getMessage().contains("XMPPError")) {
                                callback.onError(null, e.getMessage());
                            }
                            else {
                                callback.onError(null, "Experiencing ServerSide Error, Try after some time"); //R.string.serverside_error
                            }
                        }
                    });
                }
            }
        });
        thread.start();
    }

    public void sendMessage(final Activity activity, final com.jugaado.jugaado.models.Message message, final EventCallback eventCallback){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message newMessage = new Message("fdsa@"+Helper.host, message.getMessage());
                    chat.sendMessage(newMessage);

                    SimpleDateFormat sdfR = new SimpleDateFormat("dd.MM.yyyy   HH:mm:ss");
                    final String currentDateandTimeRight = sdfR.format(new Date());

                    Log.d("new msg userid",user_id);
                    message.addDateTimeNuserid(message, currentDateandTimeRight, user_id);
                    dataBaseManager.addMessage(message);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            eventCallback.onSuccess(new JSONObject());
                        }
                    });
                } catch (SmackException.NotConnectedException e) {
                    Log.d(TAG, "Error occurred in sending message to the server");
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            eventCallback.onError(new JSONObject(), activity.getString(R.string.NO_INTERNET_CONNECTION_ERROR));
                        }
                    });
                }
            }
        });
        thread.start();
    }

    /**
     * Add a listener for listening to thread change events
     * @param listener
     * @throws IllegalArgumentException
     */
    public void addRefreshListener(RefreshListener listener) throws IllegalArgumentException {
        synchronized (refreshListeners) {
            if (refreshListeners.contains(listener)) {
                throw new IllegalArgumentException("This listener has been already registered with the Account Manager");
            }
            refreshListeners.add(listener);
        }
    }

    /**
     * Remove listener
     * @param listener
     */
    public void removeRefreshListener(RefreshListener listener) {
        synchronized (refreshListeners) {
            if (refreshListeners.contains(listener)) {
                refreshListeners.remove(listener);
            }
        }
    }

    public boolean isLoggedIn(){
        return appUser != null;
    }

    public void saveProfileDetails(final Activity activity, final String name, final String street, final String street_2, final String city, final String pincode, final EventCallback eventCallback){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (saveProfileDetails(name, street, street_2, city, pincode)) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            eventCallback.onSuccess(new JSONObject());
                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            eventCallback.onError(null, "Unable to save profile at the moment");
                        }
                    });
                }
            }
        });
        thread.start();
    }

    public boolean saveProfileDetails(final String name, final String street, final String street_2, final String city, final String pincode){
        try {
        appUser.save(name);
        appUser.getAddress().save(street, street_2, city, pincode);
        VCard vCard = new VCard();
        vCard.load(xmpptcpConnection);
        appUser.saveToVCard(vCard);
        vCard.save(xmpptcpConnection);
        } catch (XMPPException.XMPPErrorException | SmackException.NotConnectedException | SmackException.NoResponseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * PRIVATE METHODS
     */
    private void loadUser(org.jivesoftware.smackx.iqregister.AccountManager accountManager){
        try {
            VCard vCard = new VCard();
            vCard.load(xmpptcpConnection);

            appUser = new User(vCard);
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            Log.d(TAG, "Error occurred in loading the user details");
            e.printStackTrace();
        }
    }

    private void loadUser(XMPPTCPConnection xmpptcpConnection) {
        org.jivesoftware.smackx.iqregister.AccountManager accountManager = org.jivesoftware.smackx.iqregister.AccountManager.getInstance(xmpptcpConnection);
        loadUser(accountManager);
    }


}
