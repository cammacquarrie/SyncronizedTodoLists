package edu.carleton.syncronizedtodolists;

import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import communication.Fields;
import communication.Event;
import communication.EventHandler;
import communication.Fields;
import com.google.gson.*;

public class LoginResHandler extends AppCompatActivity implements EventHandler{
    private static final MainActivity ma = MainActivity.getInstance();
    private static final LoginActivity la = LoginActivity.getInstance();

    private Gson gson;
    private View mProgressView;
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.getInstance()).edit();

    public LoginResHandler(){
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
            editor.putString(Fields.USERNAME, user.getUserName());
            editor.putString(Fields.DISPLAY, user.getDisplayName());
            //Intent i = new Intent(ma, MainActivity.class);
            //i.putExtra("logedIn", true);
            //ma.startActivity(i);
            ma.renderUserInfo();
        }

        //login unsuccessful
        else{
            runOnUiThread(new Runnable() {
                public void run() {
                    mProgressView = LoginActivity.getInstance().findViewById(R.id.login_progress);
                    mProgressView.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.getInstance(),R.string.failed_login_toast,Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}