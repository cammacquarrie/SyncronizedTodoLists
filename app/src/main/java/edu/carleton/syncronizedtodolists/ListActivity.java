package edu.carleton.syncronizedtodolists;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import communication.Event;
import communication.Fields;

public class ListActivity extends AppCompatActivity {
    private ListView itemsView;
    private List list;
    private static MainActivity ma  = MainActivity.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        itemsView = (ListView) findViewById(R.id.itemListView);

        Gson gson = new Gson();

        Intent i = getIntent();
        String listJson = i.getStringExtra("LIST");
        Log.i("LIST", listJson);
        list = gson.fromJson(listJson, List.class);
        getSupportActionBar().setTitle(list.getName());

        itemsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Item i = (Item) itemsView.getItemAtPosition(position);
            }
        });

        Button addItemButton = (Button) findViewById(R.id.add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                        builder.setTitle("Enter new task: ");
                        final EditText input = new EditText(MainActivity.getInstance());
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);
                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                newItem(input.getText().toString());
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                });
            }
        });
    }
    public void newItem(String item){
        final Item i = new Item(item, ma.getUser().getUserName(), list.getId());
        Runnable newItem = new Runnable() {
            @Override
            public void run() {
                Log.i("I", "Running new Item");
                Log.i("I", i.getTitle());
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                Gson gson = new Gson();
                String itemJson = gson.toJson(i).toString();
                HashMap<String, Serializable> map = new HashMap<>();
                map.put(Fields.TYPE, Fields.NEW_ITEM);
                map.put(Fields.ITEM, i);
                Event event = new Event(ma.source, map);
                try {
                    ma.source.putEvent(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent.putExtra("LIST", itemJson);
                startActivity(intent);
            }
        };

        Thread thread = new Thread(newItem);
        thread.start();

    }
}
