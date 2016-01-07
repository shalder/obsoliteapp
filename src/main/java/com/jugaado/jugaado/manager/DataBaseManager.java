package com.jugaado.jugaado.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import com.jugaado.jugaado.activities.auth.LoginActivity;
import com.jugaado.jugaado.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ravivooda on 10/13/15.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private final static String TAG = "Data Base Manager";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "messagesPDB";

    // Contacts table name
    private static final String TABLE_MESSAGES = "messagePTable";

    // Contacts Table Columns names
    private static final String MESSAGE_ID = "id";
    private static final String MESSAGE_MESSAGE = "message";
    private static final String MESSAGE_WAY = "message_way";
    private static final String MESSAGE_DATENTIME= "date_n_time";
    private static final String USER_ID= "user_id";

    String userid;

    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + MESSAGE_ID + " INTEGER PRIMARY KEY," + MESSAGE_MESSAGE + " TEXT,"
                + MESSAGE_WAY + " TEXT," + MESSAGE_DATENTIME +" TEXT,"+ USER_ID + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Add msg - " + message.getMessage());
        ContentValues values = new ContentValues();
        values.put(MESSAGE_MESSAGE, message.getMessage()); // Contact Name
        values.put(MESSAGE_WAY, message.getMessage_way() == Message.message_way.MESSAGE_WAY_IN ? "1" : "0"); // Contact Phone Number
        //SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss");
        //final String currentDateandTime = sdf.format(new Date());
        values.put(MESSAGE_DATENTIME,message.getDATEnTIME());
        values.put(USER_ID,message.getUser_id());
        Log.d(TAG, "add message->" + message.getMessage() + "");
        Log.d(TAG, "add message-time->" + message.getDATEnTIME());
        // Inserting Row
        db.insert(TABLE_MESSAGES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Message getMessage(int id) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGES, new String[] { MESSAGE_ID, MESSAGE_MESSAGE, MESSAGE_WAY ,MESSAGE_DATENTIME}, MESSAGE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null ,null);
        if (cursor != null)
            cursor.moveToFirst();

        /*Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));*/
        // return contact
        JSONObject message = new JSONObject();
        if (cursor != null) {

            Log.d("TAG", "Get msg - " + cursor.getString(1));

            message.put("message_id", cursor.getString(0));
            message.put("message", cursor.getString(1));
            message.put("message_way",cursor.getString(2));
            message.put("date_n_time",cursor.getString(3));
            cursor.close();
        }
        Log.d(TAG, "getMessage->" + message + "");
        return new Message(message);
    }

    // Getting All Contacts
    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> contactList = new ArrayList<>();
        //LoginActivity longi=new LoginActivity();

        //final String user_id= "prato";//longi.getUsername();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES + " WHERE user_id = '"+userid+"'";
        Log.d("Print query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                JSONObject message = new JSONObject();
                try {

                    Log.d(TAG, "getALLmesaages-msg->" + cursor.getString(1)+"");
                    message.put("message_id", cursor.getString(0));
                    message.put("message", cursor.getString(1));
                    message.put("message_way",cursor.getString(2));
                    message.put("date_n_time", cursor.getString(3));
                    Log.d(TAG, "getALLmesaages-time->" + cursor.getString(3) + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Adding contact to list
                contactList.add(new Message(message));
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return contactList;
    }

    // Getting contacts Count
    public int getMessagesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void clearMessages() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);

        // Create tables again
        onCreate(db);
    }
    public void setUserId(String userid){
        this.userid=userid;
    }
}
