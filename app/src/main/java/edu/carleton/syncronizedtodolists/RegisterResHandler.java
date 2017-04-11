package edu.carleton.syncronizedtodolists;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import communication.Event;
import communication.EventHandler;
import communication.Fields;

public class RegisterResHandler extends AppCompatActivity implements EventHandler{
    private MainActivity ma;
    private Gson gson;
    private View mProgressView;

    public RegisterResHandler(){
        ma = MainActivity.getInstance();
        gson = new Gson();
    }


    @Override
    public void handleEvent(Event event) {
        boolean res = (boolean) event.get(Fields.VALUE);
        //login successful
        if(res){
            String userStr = event.get(Fields.USER).toString();
            User user = gson.fromJson(userStr, User.class);
            ma.setUser(user);
            ma.renderUserInfo();
        }
        //login unsuccessful
        else{
            runOnUiThread(new Runnable() {
                public void run() {
                    mProgressView = LoginActivity.getInstance().findViewById(R.id.login_progress);
                    mProgressView.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.getInstance(),R.string.failed_register_toast,Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}