package com.jugaado.jugaado.models;

import android.app.Activity;

import com.jugaado.jugaado.data.EventCallback;
import com.jugaado.jugaado.manager.AccountManager;
import com.jugaado.jugaado.manager.DataBaseManager;

import org.json.JSONObject;

/**
 * Created by ravivooda on 9/5/15.
 */
public class Message {
    /**
     * Helper classes, enums etc.
     */
    public enum MESSAGE_WAY {
        MESSAGE_WAY_IN,
        MESSAGE_WAY_OUT,
        MESSAGE_WAY_WARNING
    }

    private int message_id;
    private String message;
    private User owner;
    private MESSAGE_WAY message_way;
    private String DATE_n_TIME;
    private String user_id;

    public Message(String message){
        this.message = message;
        this.message_way = MESSAGE_WAY.MESSAGE_WAY_OUT;
    }

    public Message(JSONObject object){
        this.message_id = Integer.parseInt(object.optString("message_id", "0"));
        this.message = object.optString("message", "");
        this.message_way = object.optString("message_way", "0").equals("0") ? MESSAGE_WAY.MESSAGE_WAY_OUT :MESSAGE_WAY.MESSAGE_WAY_IN;
        this.DATE_n_TIME=object.optString("date_n_time","");
        this.user_id=object.optString("user_id","");
    }

    public Message(org.jivesoftware.smack.packet.Message message,String datentime,String userid){
        this.message = message.getBody();
        this.message_way = MESSAGE_WAY.MESSAGE_WAY_IN;
        this.DATE_n_TIME=datentime;
        this.user_id=userid;
    }

    public Message addDateTimeNuserid(Message msg,String timedate,String userid){
        this.DATE_n_TIME=timedate;
        this.user_id=userid;
        return msg;
    }

    public void send(final Activity activity,final EventCallback callback){
        AccountManager.getSharedInstance().sendMessage(activity, this, callback);
    }

    public int getMessage_id() {
        return message_id;
    }

    public String getMessage() {
        return message;
    }
    public String getDATEnTIME(){
        return DATE_n_TIME;
    }
    public String getUser_id(){ return user_id; }
    public User getOwner() {
        return owner;
    }

    public MESSAGE_WAY getMessage_way() {
        return message_way;
    }
}
