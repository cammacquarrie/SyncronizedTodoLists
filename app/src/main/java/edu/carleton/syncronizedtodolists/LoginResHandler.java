package edu.carleton.syncronizedtodolists;

import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import communication.Fields;
import communication.Event;
import communication.EventHandler;
import communication.Fields;
import com.google.gson.*;

public class LoginResHandler implements EventHandler{
    private MainActivity ma;
    private Gson gson;

    public LoginResHandler(){
        ma = MainActivity.getInstance();
        gson = new Gson();
    }


    @Override
    public void handleEvent(Event event) {
        boolean res = (boolean) event.get(Fields.VALUE);
        //login successful
        if(res){
            String userStr = event.get(Fields.USER).toString();
            Log.i("USR JSON", userStr);
            User user = gson.fromJson(userStr, User.class);
            Log.i("USR JSON", user.getDisplayName());
            ma.setUser(user);
            ma.renderUserInfo();
        }
        //login unsuccessful
        else{
            //send to login activity to try again
        }
    }

}