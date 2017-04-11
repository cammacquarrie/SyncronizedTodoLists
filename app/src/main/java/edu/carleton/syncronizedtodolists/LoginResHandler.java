package edu.carleton.syncronizedtodolists;

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
            User user = gson.fromJson(userStr, User.class);
            ma.setUser(user);
            ma.renderUserInfo();
        }
        //login unsuccessful
        else{
            //send to login activity to try again
        }
    }

}