package edu.carleton.syncronizedtodolists;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import communication.Event;
import communication.EventHandler;
import communication.Fields;

public class InviteResHandler extends AppCompatActivity implements EventHandler{
    private static final MainActivity ma = MainActivity.getInstance();

    private Gson gson;

    public InviteResHandler(){
        gson = new Gson();
    }


    @Override
    public void handleEvent(Event event) {
        final String listStr = (String) event.get(Fields.LIST).toString();
        final String sender = (String) event.get(Fields.SENDER).toString();
        final List l = gson.fromJson(listStr, List.class);
        //login successful

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ma.getApplicationContext());
                builder.setTitle("New Invite");
                builder.setMessage("Would you like to join " + sender + "'s list?");
                // Set up the buttons
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        answerInvite(l.getId(), true);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        answerInvite(l.getId(), false);
                    }
                });
                builder.show();
            }
        });
    }

    public void answerInvite(final int listID, final Boolean ans){
        Runnable newItem = new Runnable() {
            @Override
            public void run() {
                HashMap<String, Serializable> map = new HashMap<>();
                map.put(Fields.TYPE, Fields.INVITE_RES);
                map.put(Fields.LIST_ID, listID);
                map.put(Fields.USERNAME, ma.getUser().getUserName());
                map.put(Fields.ANSWER, ans);
                Event event = new Event(ma.source, map);
                try {
                    ma.source.putEvent(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(),R.string.invite_sent_toast,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        Thread thread = new Thread(newItem);
        thread.start();

    }

}