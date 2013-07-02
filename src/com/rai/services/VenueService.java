package com.rai.services;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rai.R;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 01/07/13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class VenueService extends AbstractService<List<JSONObject>> {

    private final FragmentActivity activity;
    private final String TAG = "VenueService";
    private static final String VENUE_URL     = "http://rai-server.herokuapp.com/poi/buscar/";
    private static final String VENUE_TAG_URL = "http://rai-server.herokuapp.com/poi/buscar-por-tag/";

    public VenueService(FragmentActivity activity) {
        this.activity = activity;
    }


    @Override
    protected String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Class<? extends AbstractService> getClassName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onPreExecute(){
        ProgressBar bar = (ProgressBar)this.activity.findViewById(R.id.mapProgressBar);
        bar.setVisibility(View.VISIBLE);

        GoogleMap googleMap = ((SupportMapFragment)this.activity.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.clear();
    }

    @Override
    protected List<JSONObject> doInBackground(String... strings) {

        String method = strings[0];

        String url = "";
        if(method.equalsIgnoreCase("near")){
            url = VENUE_URL+""+strings[1]+"/"+strings[2];
        }
        else{
            if(method.equalsIgnoreCase("tag")){
                url = VENUE_TAG_URL+""+ Uri.encode(strings[1]);
            }
        }


        Log.i(TAG, "URL " + url);
        HttpGet request = new HttpGet(url);

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        JSONObject jsonObject = null;


        try{
            response = httpClient.execute(request);
        }
        catch (Exception e){
            Log.e(TAG,"Internet Problem",e);
        }
        try{
            jsonObject = new JSONObject(IOUtils.toString(response.getEntity().getContent()));
            int count        = jsonObject.getJSONObject("result").getInt("count");

            List<JSONObject> data = new ArrayList<JSONObject>();

            if(count > 0){
                JSONArray venues = jsonObject.getJSONObject("result").getJSONArray("venues");
                for(int i=0; i <count;i++){
                    JSONObject venue = venues.getJSONObject(i);



                    data.add(venue);

                }
            }

            return data;

        }
        catch (Exception e){
            Log.e(TAG,"Json Problem",e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<JSONObject> result) {

        Log.i(TAG, "onPostExecute");
        ProgressBar bar = (ProgressBar)this.activity.findViewById(R.id.mapProgressBar);
        bar.setVisibility(View.GONE);
        if(result !=null){

            Toast.makeText(this.activity.getApplicationContext(),"Foram encontrado(s) "+result.size()+" poi(s)",Toast.LENGTH_SHORT).show();

            GoogleMap googleMap = ((SupportMapFragment)this.activity.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            for(JSONObject venue : result){
                try {

                    String id        = venue.getString("id");
                    String nome      = venue.getString("nome");
                    Double latitude  = venue.getDouble("latitude");
                    Double longitude = venue.getDouble("longitude");
                    Double rating = venue.getDouble("avaliacao");

                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(nome.toUpperCase())
                            .snippet("Avaliação: " + rating));


                } catch (JSONException e) {
                    Log.e(TAG,"Json Problem",e);
                }

            }
            // Update Camera using all the retrieved venues
//            googleMap.animateCamera(CameraUpdateFactory);

        }
        else{
            Toast.makeText(this.activity.getApplicationContext(),"Não foi possível encontrar nenhum poi",Toast.LENGTH_SHORT).show();
        }

    }
}
