package edu.carleton.syncronizedtodolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

public class ListActivity extends AppCompatActivity {
    private List list;
    private static MainActivity ma  = MainActivity.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Gson gson = new Gson();

        Intent i = getIntent();
        String listJson = i.getStringExtra("LIST");
        Log.i("LIST", listJson);
        list = gson.fromJson(listJson, List.class);
        getSupportActionBar().setTitle(list.getName());
    }
}
