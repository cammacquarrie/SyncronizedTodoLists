package edu.carleton.syncronizedtodolists;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
        final int listID = (int) event.get(Fields.LIST_ID);
        final String sender = (String) event.get(Fields.SENDER);
        //login successful
        String userStr = event.get(Fields.USER).toString();
        runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(ma.getApplicationContext())
                        .setTitle("New Invite")
                        .setMessage("Invite to join List from" + sender)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                answerInvite(listID, true);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                answerInvite(listID, false);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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