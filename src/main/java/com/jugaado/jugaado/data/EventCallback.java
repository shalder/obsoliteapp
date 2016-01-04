package com.jugaado.jugaado.data;


import org.json.JSONObject;

/**
 * Created by shashankdabriwal on 7/14/15.
 */
 
 /**
  * TODO:   Why abstract class? If there is no default implementation then make it an interface
  */
public abstract class EventCallback {
    public abstract void onSuccess(JSONObject js);
    public abstract void onFailure(String message);
    public abstract void onError(JSONObject js, String message);

}
