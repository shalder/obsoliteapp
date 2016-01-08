package com.jugaado.jugaado.models;

import android.util.Log;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.json.JSONObject;

/**
 * Created by ravivooda on 9/5/15.
 */
public class User {
    private static final String TAG = "User";
    private int user_id;
    private String user_name;
    private String email;
    private String full_name;

    private Address address;

    public int getUser_id() {
        return user_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public String getEmail() {
        return email;
    }
    public String getFull_name() {
        return full_name;
    }
    public Address getAddress() {
        return address;
    }
    public void save(String full_name){
        this.full_name = full_name;
    }

    @Override
    public String toString() {
        return "User {" +
                "\n" + user_id + "," +
                "\n" + user_name + "," +
                "\n" + email + "," +
                "\n" + full_name + "," +
                "\n" + address.toString() + "" +
                "\n" + "}";
    }
    public String formatName(String name_fragment){
        if (name_fragment == null) {
            name_fragment = "";
        } else {
            name_fragment = name_fragment.trim();
        }
        return name_fragment;
    }

    public User(VCard vCard) {
        Log.d(TAG, "VCard " + vCard.toString());

        String firstName = vCard.getFirstName();
        firstName = formatName(firstName) + " ";

        String middleName = vCard.getMiddleName();
        middleName = formatName(middleName) + " ";

        String lastName = vCard.getLastName();
        lastName = formatName(lastName);

        this.full_name = firstName + middleName + lastName;
        this.email = vCard.getEmailHome();
        this.user_name = "";
        this.address = new Address(vCard);
    }

    public void saveToVCard(VCard vCard) {
        if (vCard == null)
            return;
        vCard.setFirstName(this.full_name);
        vCard.setEmailHome(this.email);
        this.address.saveToVCard(vCard);
    }
}
