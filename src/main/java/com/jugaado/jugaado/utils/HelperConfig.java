package com.jugaado.jugaado.utils;

import org.json.JSONObject;

/**
 * Created by websofttechs on 2/11/2016.
 */

public class HelperConfig {

    //private variables
    int id;
    float configAppVersion;
    String HOST;
    String PORT;
    String HOST_USER_NAME;
    String HOST_PWD;
    String FORGET_PWD_URL;

    // Empty constructor
    public HelperConfig(){

    }

    // constructor
    public HelperConfig(float configAppVersion , String HOST, String PORT,String HOST_USER_NAME, String HOST_PWD,String FORGET_PWD_URL){
        this.configAppVersion = configAppVersion;
        this.HOST = HOST;
        this.PORT = PORT;
        this.HOST_USER_NAME = HOST_USER_NAME;
        this.HOST_PWD = HOST_PWD;
        this.FORGET_PWD_URL = FORGET_PWD_URL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getConfigAppVersion() {
        return configAppVersion;
    }

    public void setConfigAppVersion(float configAppVersion) {
        this.configAppVersion = configAppVersion;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public String getHOST_USER_NAME() {
        return HOST_USER_NAME;
    }

    public void setHOST_USER_NAME(String HOST_USER_NAME) {
        this.HOST_USER_NAME = HOST_USER_NAME;
    }

    public String getHOST_PWD() {
        return HOST_PWD;
    }

    public void setHOST_PWD(String HOST_PWD) {
        this.HOST_PWD = HOST_PWD;
    }

    public String getFORGET_PWD_URL() {
        return FORGET_PWD_URL;
    }

    public void setFORGET_PWD_URL(String FORGET_PWD_URL) {
        this.FORGET_PWD_URL = FORGET_PWD_URL;
    }


}