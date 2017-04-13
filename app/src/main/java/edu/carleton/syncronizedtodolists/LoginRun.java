package edu.carleton.syncronizedtodolists;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

import communication.Event;
import communication.Fields;
import communication.JSONEventSource;
import communication.ThreadWithReactor;

/**
 * Created by nicholasrizzo on 2017-04-10.
 */

public class LoginRun implements Runnable{
    private static MainActivity mainActivity;

    public LoginRun(){
        mainActivity = MainActivity.getInstance();
    }


    public void run(){
        Socket socket;
        Gson g = new Gson();
        try  {
            Log.i("AND", "Connecting....");
            socket = new Socket(Config.IP, Config.PORT);
            JSONEventSource source = new JSONEventSource(socket);
            mainActivity.setSource(source);
        } catch (IOException e){
            e.printStackTrace();
        }

        String username = null;
        String password = "";
        String displayname = "";
        if(username == null){
            Intent i = new Intent(mainActivity, LoginActivity.class);
            mainActivity.startActivityForResult(i, 1234);
        }
        else{/*
            Log.i("username", username);
            password = prefs.getString(Fields.PASSWORD, null);
            displayname = prefs.getString(Fields.DISPLAY, null);
            mainActivity.setUser(new User(username, displayname, password));
            login(username, displayname, password);*/
        }
    }

    public static void login(String username, String displayname, String password){
        HashMap<String, Serializable> map = new HashMap<String, Serializable>();
        map.put(Fields.USERNAME, username);
        map.put(Fields.DISPLAY, displayname);
        map.put(Fields.PASSWORD, password);
        Event event = new Event(mainActivity.source, map, Fields.LOGIN);
        try {
            mainActivity.source.putEvent(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void register(String username, String displayname, String password){
        HashMap<String, Serializable> map = new HashMap<String, Serializable>();
        map.put(Fields.USERNAME, username);
        map.put(Fields.DISPLAY, displayname);
        map.put(Fields.PASSWORD, password);
        Event event = new Event(mainActivity.source, map, Fields.REGISTER);
        try {
            mainActivity.source.putEvent(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
