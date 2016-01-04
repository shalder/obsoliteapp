package com.jugaado.jugaado.models;

import android.app.Activity;

import com.jugaado.jugaado.data.EventCallback;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ravivooda on 9/5/15.
 */
public class MessageThread {
    private int thread_id;
    private String thread_name;
    private final ArrayList<User> participants;
    private final ArrayList<Message> messages;

    private MessageThread(){
        participants = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public MessageThread(JSONObject object){
        this();
        // Need to parse the JSONObject here
    }

    public MessageThread(ArrayList<Message> messages) {
        this();
        this.messages.addAll(messages);
    }

    public Message lastMessage(){
        if (messages != null && messages.size() > 0)
            return messages.get(messages.size() - 1);
        return null;
    }

    public void sendMessage(Activity activity, Message message, final EventCallback callback){
        messages.add(message);
        message.send(activity, callback);
    }

    public void receiveMessage(Message message){
        messages.add(message);

    }

    public int getThread_id() {
        return thread_id;
    }

    public String getThread_name() {
        return thread_name;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public int getCount() {
        if (messages != null)
            return messages.size();
        return 0;
    }

    public void clearMessages(){
        this.messages.clear();
    }
}
