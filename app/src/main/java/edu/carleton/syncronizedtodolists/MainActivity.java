package edu.carleton.syncronizedtodolists;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

import communication.Event;
import communication.EventSource;
import communication.Fields;
import communication.JSONEventSource;
import communication.Reactor;
import communication.ThreadWithReactor;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;
    private static User user;
    public static EventSource source;
    private static Reactor reactor;
    private static ThreadWithReactor reactorThread;
    //UI components
    private TextView usernameView;
    private TextView displayView;
    private ListView listsView;
    private ListView itemsView;
    private Button newListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init UI components
        usernameView = (TextView) findViewById(R.id.usernameView);
        displayView = (TextView) findViewById(R.id.displayView);
        listsView = (ListView) findViewById(R.id.listListView);
        itemsView = (ListView) findViewById(R.id.listListView);
        newListBtn = (Button) findViewById(R.id.newListBtn);

        instance = this;
        reactor = new Reactor();

        //register handlers here
        reactor.register(Fields.LOGIN_RES, new LoginResHandler());

        ///////handlers^^

        LoginRun login = new LoginRun();
        Thread thread = new Thread(login);
        thread.start();
        //set listener for button to create a new List
        //go to a new List page in edit mode
        newListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setSource(EventSource s) {
        source = s;
        reactorThread = new ThreadWithReactor(source, reactor);
        reactorThread.start();
    }

    public static MainActivity getInstance(){
        return instance;
    }

    public void setUser(User u){
        user = u;
    }

    public void renderUserInfo(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayView.setText(user.getDisplayName());
                usernameView.setText(user.getUserName());
                ListAdapter listsAdapter = new ArrayAdapter<List>(MainActivity.getInstance(), android.R.layout.simple_expandable_list_item_1, user.getLists());
                ListAdapter itemsAdapter = new ArrayAdapter<Item>(MainActivity.getInstance(), android.R.layout.simple_expandable_list_item_1, user.getItems());
                listsView.setAdapter(listsAdapter);
                itemsView.setAdapter(itemsAdapter);
            }
        });
    }
}
