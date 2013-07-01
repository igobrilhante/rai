package com.rai;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.app.Activity;
import android.graphics.Color;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolygonOptionsCreator;

import android.view.Menu;
import com.rai.context.ContextApp;
import com.rai.context.ContextManager;
import com.rai.services.TwitterService;
import com.rai.services.WeatherService;

public class MapActivity extends Activity implements View.OnClickListener, View.OnHoverListener {

    private final String TAG = "MapActivity";
	private GoogleMap googleMaps;

    private View    up;
    private View    down;
    private View    box1;
    private View    box2;
    private View    box3;
    private View    box4;
    private View    searchTweetsView;

    private int screenWidth  = 0;
    private int screenHeight = 0;

    private ContextManager contextManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

        Log.d(TAG,"Context "+getApplicationContext());

        this.contextManager = ContextManager.instance();
        this.contextManager.setContext(this.getApplicationContext());

        this.box1 = findViewById(R.id.box1);
        this.box1.setOnClickListener(this);

        this.box2 = findViewById(R.id.box2);
        this.box3 = findViewById(R.id.box3);
        this.box4 = findViewById(R.id.box4);
        this.searchTweetsView = findViewById(R.id.searchTweet);

        this.up = findViewById(R.id.up);
        this.up.setVisibility(View.GONE);

        this.down = findViewById(R.id.down);
        this.down.setVisibility(View.VISIBLE);

        this.up.setOnClickListener(this);
        this.up.setOnHoverListener(this);
        this.down.setOnClickListener(this);
        this.down.setOnHoverListener(this);
        this.searchTweetsView.setOnClickListener(this);
		
		this.googleMaps = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

        updateWeather();;

        hideMapControllers();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}


    @Override
    public void onClick(View view) {
        screenConf();

        if(view == up){
            showMenu();
            hideMapControllers();
            return;
        }
        if(view == down){
            hideMenu();
            showMapControllers();
            return;
        }
        if(view == box1){
            updateWeather();
            return;
        }
        if(view == searchTweetsView){
            searchTweets();
            return;
        }
    }

    private void searchTweets(){
        String latitude     =   contextManager.getValue("latitude");
        String longitude    =   contextManager.getValue("longitude");
        String radius       =   "10km";

        TwitterService ts = new TwitterService(this);
        ts.execute(latitude,longitude,radius);

    }

    private void updateWeather(){
        new WeatherService(this).execute(contextManager.getValue("latitude"),contextManager.getValue("longitude"));
    }

    private void hideMenu(){
        Log.i(TAG,"hideMenu");

        this.box1.setVisibility(View.GONE);
        this.box2.setVisibility(View.GONE);
        this.box3.setVisibility(View.GONE);
        this.box4.setVisibility(View.GONE);
        this.searchTweetsView.setVisibility(View.GONE);

        this.up.setVisibility(View.VISIBLE);
        this.down.setVisibility(View.GONE);


    }

    private void showMenu(){
        Log.i(TAG,"showMenu");

        this.box1.setVisibility(View.VISIBLE);
        this.box2.setVisibility(View.VISIBLE);
        this.box3.setVisibility(View.VISIBLE);
        this.box4.setVisibility(View.VISIBLE);
        this.searchTweetsView.setVisibility(View.VISIBLE);

        this.up.setVisibility(View.GONE);
        this.down.setVisibility(View.VISIBLE);

    }

    private void screenConf(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.screenWidth   = dm.widthPixels;
        this.screenHeight  = dm.heightPixels;

        Log.i("TAG",String.format("Width: %s\nHeight: %s",screenWidth,screenHeight));

    }

    private void resizeBoxes(View v1, View v2, View v3, View v4, View v5){

    }

    private void showMapControllers(){
        googleMaps.getUiSettings().setAllGesturesEnabled(true);
        googleMaps.setMyLocationEnabled(true);
        googleMaps.getUiSettings().setZoomControlsEnabled(true);
        googleMaps.getUiSettings().setZoomGesturesEnabled(true);
    }

    private void hideMapControllers(){
        googleMaps.getUiSettings().setAllGesturesEnabled(false);
        googleMaps.setMyLocationEnabled(false);
        googleMaps.getUiSettings().setZoomControlsEnabled(false);
        googleMaps.getUiSettings().setZoomGesturesEnabled(false);
    }

    @Override
    public boolean onHover(View view, MotionEvent motionEvent) {
        Log.i(TAG,"onHover "+motionEvent.getAction());
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                showMenu();
                hideMapControllers();
                return true;
            case MotionEvent.ACTION_DOWN:
                hideMenu();
                showMapControllers();
                return true;
        }
        return true;
    }
}
