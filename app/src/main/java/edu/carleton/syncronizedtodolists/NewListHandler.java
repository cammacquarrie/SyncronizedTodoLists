package edu.carleton.syncronizedtodolists;

import android.content.Intent;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import communication.Event;
import communication.EventHandler;
import communication.Fields;

/**
 * Created by nicholasrizzo on 2017-04-11.
 */

public class NewListHandler implements EventHandler {
    @Override
    public void handleEvent(Event event) {
        int id = ((Double) event.get(Fields.ID)).intValue();
        List list = MainActivity.getInstance().lists.get(MainActivity.getInstance().lists.size()-1);
        list.setId(id);
        System.out.println("list id set");
        Intent i = new Intent(MainActivity.getInstance(), ListActivity.class);
        Gson gson = new Gson();
        String listJson = gson.toJson(list).toString();


        i.putExtra("LIST", listJson);
        MainActivity.getInstance().startActivity(i);
    }
}
