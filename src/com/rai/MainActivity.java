package com.rai;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import com.rai.context.ContextManager;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContextManager contextManager = ContextManager.instance();
        contextManager.setContext(getApplicationContext());

        String user = contextManager.getString("user");
        if(!user.equals("")){
            Log.i(TAG,"User found");

        }
        else{
            Log.i(TAG,"No user");
            Intent webActivity = new Intent(getApplicationContext(),WebActivity.class);
            startActivity(webActivity);
        }

    }

    
}
