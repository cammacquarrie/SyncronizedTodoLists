package edu.carleton.syncronizedtodolists;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import communication.Fields;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Runnable{
    private static LoginActivity instance;
    private static MainActivity mainActivity =  MainActivity.getInstance();

    // UI references.
    private AutoCompleteTextView uerField;
    private EditText pswrdField;
    private EditText displayName;
    private View mProgressView;
    Button loginButton;
    Boolean register = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        String username = prefs.getString(Fields.USERNAME, null);


        uerField = (AutoCompleteTextView) findViewById(R.id.email);
        displayName = (EditText) findViewById(R.id.displayName);
        Log.i("LOGIN", "YUP");
        loginButton = (Button) findViewById(R.id.email_sign_in_button);

        pswrdField = (EditText) findViewById(R.id.password);
        instance = this;
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register = false;
                Thread thread = new Thread(instance);
                thread.start();
            }
        });


        Button registerButton = (Button) findViewById(R.id.email_register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register = true;
                Thread thread = new Thread(instance);
                thread.start();
            }
        });

    }

    public void run(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mProgressView = findViewById(R.id.login_progress);
                mProgressView.setVisibility(View.VISIBLE);
            }
        });
        if(register){
            LoginRun.register(uerField.getText().toString(), displayName.getText().toString(), pswrdField.getText().toString());
        }
        else {
            LoginRun.login(uerField.getText().toString(), displayName.getText().toString(), pswrdField.getText().toString());
        }
        Intent i = new Intent(instance, MainActivity.class);
        setResult(1234, i);
        finish();

    }

    public static LoginActivity getInstance(){
        return instance;
    }

}

