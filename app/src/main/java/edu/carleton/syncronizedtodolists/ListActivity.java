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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import communication.Event;
import communication.Fields;

public class ListActivity extends AppCompatActivity {
    private ListView itemsView;
    private List list;
    private static MainActivity ma  = MainActivity.getInstance();
    public ArrayList<Item> items = new ArrayList<Item>();
    private ArrayAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        itemsView = (ListView) findViewById(R.id.itemListView);

        Gson gson = new Gson();
        final ListActivity instance = this;

        Intent i = getIntent();
        String listJson = i.getStringExtra("LIST");
        Log.i("LIST", listJson);
        list = gson.fromJson(listJson, List.class);
        //System.out.println(list.getItems().toString());
        getSupportActionBar().setTitle(list.getName());

        items.addAll(list.getItems());
        itemsAdapter = new ArrayAdapter<Item>(itemsView.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, items);
        itemsView.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();

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


        itemsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final Item i = (Item) itemsView.getItemAtPosition(position);
                runOnUiThread(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(instance)
                                .setTitle("Claiming " + i.getTitle())
                                .setMessage("Would you like to claim this item?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        claimItem(i.getId());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        claimItem(i.getId());
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }
        });

        Button addUserButton = (Button) findViewById(R.id.add_user_button);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                        builder.setTitle("Invite: ");
                        final EditText input = new EditText(MainActivity.getInstance());
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);
                        // Set up the buttons
                        builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                newInvite(input.getText().toString());
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

                    runOnUiThread(new Runnable() {
                        public void run() {
                            items.add(i);
                            itemsAdapter.notifyDataSetChanged();
                        }
                    });
                    ma.source.putEvent(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(newItem);
        thread.start();

    }

    public void newInvite(final String name){
        Runnable newItem = new Runnable() {
            @Override
            public void run() {
                HashMap<String, Serializable> map = new HashMap<>();
                map.put(Fields.TYPE, Fields.NEW_INVITE);
                map.put(Fields.LIST_ID, list.getId());
                map.put(Fields.SENDER, ma.getUser().getUserName());
                map.put(Fields.RECEIVER, name);
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
    public void claimItem(final int itemID){
        Runnable newItem = new Runnable() {
            @Override
            public void run() {
                HashMap<String, Serializable> map = new HashMap<>();
                map.put(Fields.TYPE, Fields.CLAIM_ITEM);
                map.put(Fields.ITEM_ID, itemID);
                map.put(Fields.SENDER, ma.getUser().getUserName());
                Event event = new Event(ma.source, map);
                try {
                    ma.source.putEvent(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(),R.string.claim_item_toast,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        Thread thread = new Thread(newItem);
        thread.start();
    }
}
