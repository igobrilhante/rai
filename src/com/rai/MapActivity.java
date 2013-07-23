package com.rai;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.app.Activity;
import android.graphics.Color;

import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import android.view.Menu;
import com.rai.context.ContextApp;
import com.rai.context.ContextManager;
import com.rai.services.GeoCoderService;
import com.rai.services.TwitterService;
import com.rai.services.VenueService;
import com.rai.services.WeatherService;

public class MapActivity extends FragmentActivity implements View.OnClickListener {

    private final String TAG = "MapActivity";
	private GoogleMap googleMaps;

    private View    up;
    private View    down;
    private View    box1;
    private View    box2;
    private View    box3;
    private View    box4;
    private View    searchTweetsView;
    private View    contentView;

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

        ((TextView)findViewById(R.id.cityView)).setText("");
        ((TextView)findViewById(R.id.textViewDate)).setText("");
        ((TextView)findViewById(R.id.textViewDegree)).setText("");

        this.box2 = findViewById(R.id.box2);
        this.box3 = findViewById(R.id.box3);
        this.box3.setOnClickListener(this);
        this.box4 = findViewById(R.id.box4);
        this.box4.setOnClickListener(this);
        this.searchTweetsView = findViewById(R.id.searchTweet);

        this.contentView = findViewById(R.id.content);

        this.up = findViewById(R.id.up);
        this.up.setVisibility(View.GONE);

        this.down = findViewById(R.id.down);
        this.down.setVisibility(View.VISIBLE);

        this.up.setOnClickListener(this);
//        this.up.setOnHoverListener(this);
        this.down.setOnClickListener(this);
//        this.down.setOnHoverListener(this);
        this.searchTweetsView.setOnClickListener(this);
		
		this.googleMaps = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng point = new LatLng(Double.parseDouble(this.contextManager.getString("latitude")),
                Double.parseDouble(this.contextManager.getString("longitude")));
        googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(point,14f));

        googleMaps.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                Log.i(TAG,"onInfoWindowClick");

                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                //define o titulo
                builder.setTitle("Direção");
                //define a mensagem
                builder.setMessage("Buscar direção?");
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        try{
                            Log.i(TAG,"Sim");
                            String url = "geo:" + marker.getPosition().latitude + "," + marker.getPosition().longitude;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        }
                        catch (Exception e){
                            Log.e(TAG,"Dialog yes",e);
                        }
                    }
                });
                //define um botão como negativo.
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                //cria o AlertDialog
                AlertDialog alerta = builder.create();
                //Exibe
                alerta.show();

            }
        });

        updateWeather();

        hideMapControllers();

        showMenu();
	}

    @Override
    public void onStop(){
        super.onStop();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

    @Override
    protected void onResume() {
        super.onResume();
        this.contextManager.requestUpdateLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.contextManager.removeUpdateLocation();
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
            exit();
            return;
        }
        if(view == box3){
            searchVenue();
            return;
        }
        if(view == box4){
            searchVenueTag();
            return;
        }
    }

    private void searchVenue(){
        new VenueService(this).execute("near",contextManager.getString("latitude"),contextManager.getString("longitude"));
        hideMenu();
        showMapControllers();

    }

    private void searchVenueTag(){
        Log.i(TAG,"searchVenueTag");

        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);

        builder.setTitle("Buscar Poi");
        builder.setMessage("Digite uma tag (ex. sushi)");


        final EditText input = new EditText(getApplicationContext());
        builder.setView(input);

        final MapActivity mf = this;

        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                try{
                    Log.i(TAG,"Sim");
                    String tag = input.getText().toString();
                    new VenueService(mf).execute("tag",tag);
                    hideMenu();
                    showMapControllers();

                }
                catch (Exception e){
                    Log.e(TAG,"Dialog yes",e);
                }
            }
        });


        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();



    }


    private void searchTweets(){
        String latitude     =   contextManager.getString("latitude");
        String longitude    =   contextManager.getString("longitude");
        String radius       =   "10km";

        TwitterService ts = new TwitterService(this);
        ts.execute(latitude,longitude,radius);

    }

    private void updateWeather(){
        new GeoCoderService(this).execute(contextManager.getString("latitude"),contextManager.getString("longitude"));
        new WeatherService(this).execute(contextManager.getString("latitude"),contextManager.getString("longitude"));
    }

    private void hideMenu(){
        Log.i(TAG,"hideMenu");

        this.contentView.setBackgroundResource(0);


        this.box1.setVisibility(View.GONE);
        this.box2.setVisibility(View.GONE);
        this.box3.setVisibility(View.GONE);
        this.box4.setVisibility(View.GONE);
        this.searchTweetsView.setVisibility(View.GONE);

        this.up.setVisibility(View.VISIBLE);
        this.down.setVisibility(View.GONE);



    }

    private void exit(){
        Log.i(TAG,"exit");

        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);

        builder.setTitle("Sair");
        builder.setMessage("Você deseja sair?");


        final MapActivity mf = this;

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                try{
                    contextManager.clean();
//                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                    startActivity(intent);
                    finish();

                }
                catch (Exception e){
                    Log.e(TAG,"Dialog yes",e);
                }
            }
        });


        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showMenu(){
        Log.i(TAG,"showMenu");

        this.contentView.setBackgroundResource(R.color.common_signin_btn_dark_text_disabled);
        Drawable background = this.contentView.getBackground();
        background.setAlpha(30);



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





//    @Override
//    public boolean onHover(View view, MotionEvent motionEvent) {
//        Log.i(TAG,"onHover "+motionEvent.getAction());
//        switch (motionEvent.getAction()){
//            case MotionEvent.ACTION_UP:
//                showMenu();
//                hideMapControllers();
//                return true;
//            case MotionEvent.ACTION_DOWN:
//                hideMenu();
//                showMapControllers();
//                return true;
//        }
//        return true;
//    }
}
