package com.jugaado.jugaado.models;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.json.JSONObject;

/**
 * Created by ravivooda on 10/11/15.
 */
public class Address {

    private String street_address = "";
    private String street_address_2 = "";
    private String city = "";
    private String pincode = "";

    public static final String STREET_KEY = "STREET";
    public static final String STREET_1_KEY = "LOCALITY";
    public static final String CITY_KEY = "REGION";
    public static final String PINCODE_KEY = "PCODE";

    public Address(VCard vCard) {
        this.street_address = vCard.getAddressFieldHome(STREET_KEY);
        this.street_address_2 = vCard.getAddressFieldHome(STREET_1_KEY);
        this.city = vCard.getAddressFieldHome(CITY_KEY);
        this.pincode = vCard.getAddressFieldHome(PINCODE_KEY);
    }

    public String getStreet_address() {
        return street_address;
    }
    public String getStreet_address_2() {
        return street_address_2;
    }
    public String getCity() {
        return city;
    }
    public String getPincode() {
        return pincode;
    }

    public void save(String street_address, String street_address_2, String city, String pincode){
        this.street_address = street_address;
        this.street_address_2 = street_address_2;
        this.city = city;
        this.pincode = pincode;
    }

    public boolean isEmpty(){
        return !(street_address != null && city != null && pincode != null
                && !street_address.equals("") && !city.equals("") && !pincode.equals(""));
    }

    @Override
    public String toString() {
        return "Address {" +
                "\n" + street_address + "," +
                "\n" + street_address_2 + "," +
                "\n" + city + "," +
                "\n" + pincode + "" +
                "\n}";
    }

    public void saveToVCard(VCard vCard) {
        if (vCard == null)
            return;
        vCard.setAddressFieldHome(STREET_KEY, this.street_address);
        vCard.setAddressFieldHome(STREET_1_KEY , this.street_address_2);
        vCard.setAddressFieldHome(CITY_KEY, this.city);
        vCard.setAddressFieldHome(PINCODE_KEY, this.pincode);
    }
}
