package com.rai;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rai.context.ContextManager;
import com.rai.entity.Recommendation;
import com.rai.entity.Venue;
import com.rai.services.AbstractService;
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
 * Date: 23/07/13
 * Time: 09:45
 * To change this template use File | Settings | File Templates.
 */
public class RecommendationActivity extends FragmentActivity implements View.OnClickListener {

    private ContextManager contextManager;
    private final String TAG = "RecommendationActivity";
    private ArrayList<Recommendation> list;
    private List<MarkerOptions> markers;
    private ListView listView;
    private GoogleMap googleMaps;
    private View    up;
    private View    down;

    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        this.contextManager = ContextManager.instance();
        this.listView = (ListView)findViewById(R.id.list_view);

        String latitude     = this.contextManager.getString("latitude");
        String longitude    = this.contextManager.getString("longitude");

        this.up = findViewById(R.id.upList);
        this.up.setVisibility(View.GONE);

        this.down = findViewById(R.id.downList);
        this.down.setVisibility(View.VISIBLE);

        this.up.setOnClickListener(this);
//        this.up.setOnHoverListener(this);
        this.down.setOnClickListener(this);

        this.listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {

                        Recommendation rec = list.get((int) id);
                        googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(position).getPosition(), 14f));
                        markers.get(position).visible(true);
                        Toast.makeText(getApplicationContext(), rec.getVenue().getName(), Toast.LENGTH_SHORT).show();


                    }

                });

        new RecommendationService(this).execute(latitude,longitude);

        this.googleMaps = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng point = new LatLng(Double.parseDouble(this.contextManager.getString("latitude")),
                Double.parseDouble(this.contextManager.getString("longitude")));
        googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 14f));

        googleMaps.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                Log.i(TAG,"onInfoWindowClick");

                AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationActivity.this);
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

    }

    private void hideList(){
       this.listView.setVisibility(View.GONE);

        this.up.setVisibility(View.VISIBLE);
        this.down.setVisibility(View.GONE);
    }

    private void showList(){
        this.listView.setVisibility(View.VISIBLE);

        this.up.setVisibility(View.GONE);
        this.down.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if(view == up){
            showList();
            return;
        }
        if(view == down){
            hideList();
            return;
        }
    }

    public class RecommendationAdapter extends ArrayAdapter<Recommendation>{
        private View.OnClickListener onClickListener;
        public RecommendationAdapter(List<Recommendation> list){
            super(RecommendationActivity.this,R.layout.list_recommendation,list);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.list_recommendation, parent, false);
                holder = new ViewHolder();
                holder.txtPlaceName = (TextView) convertView
                        .findViewById(R.id.row_placename);
                holder.txtPlaceAddress = (TextView) convertView
                        .findViewById(R.id.row_placeaddress);
                holder.layout = (RelativeLayout) convertView
                        .findViewById(R.id.row_layout);

//                holder.txtPlaceRating = (TextView) convertView
//                        .findViewById(R.id.row_rating);

                holder.txtPlaceDistance = (TextView)convertView.findViewById(R.id.row_placedistance);
                convertView.setTag(holder);

                holder.heart1 = (ImageView) convertView.findViewById(R.id.heart1);
                holder.heart1.setOnClickListener(new ImageViewClickListener(position));

                holder.heart2 = (ImageView) convertView.findViewById(R.id.heart2);
                holder.heart2.setOnClickListener(new ImageViewClickListener(position));

                holder.heart3 = (ImageView) convertView.findViewById(R.id.heart3);
                holder.heart3.setOnClickListener(new ImageViewClickListener(position));

                holder.heart4 = (ImageView) convertView.findViewById(R.id.heart4);
                holder.heart4.setOnClickListener(new ImageViewClickListener(position));

                holder.heart5 = (ImageView) convertView.findViewById(R.id.heart5);
                holder.heart5.setOnClickListener(new ImageViewClickListener(position));

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Recommendation rec = getRecMapFromAdapter(position);

            try {
                holder.txtPlaceName.setText(rec.getVenue().getName());
                double distance = rec.getVenue().getDistance();
                holder.txtPlaceDistance.setText(distance+" metros ");
                if (rec.getVenue().getAddress() != null
                        && rec.getVenue().getAddress().length() > 0) {
                    holder.txtPlaceAddress.setText(rec.getVenue().getAddress());
                } else {
                    holder.txtPlaceAddress.setText("Endereço não disponível");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return (convertView);
        }
    }

    private Recommendation getRecMapFromAdapter(int position) {
        return (((RecommendationAdapter) this.listView.getAdapter()).getItem(position));
    }



    public static class ViewHolder {
        TextView txtPlaceName;
        TextView txtPlaceAddress;
        TextView txtPlaceDistance;
        TextView txtPlaceRating;
        RelativeLayout layout;
        ImageView heart1;
        ImageView heart2;
        ImageView heart3;
        ImageView heart4;
        ImageView heart5;
    }

    class RecommendationService  extends AbstractService<List<JSONObject>> {

        private final FragmentActivity activity;
        private final String TAG = "RecommendationService";
        private static final String VENUE_URL     = "http://rai-server.herokuapp.com/poi/buscar/";
        private static final String VENUE_TAG_URL = "http://rai-server.herokuapp.com/poi/buscar-por-tag/";

        public RecommendationService(FragmentActivity activity) {
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
            ProgressBar bar = (ProgressBar)this.activity.findViewById(R.id.recommendation_progress_bar);
            bar.setVisibility(View.VISIBLE);


        }

        @Override
        protected List<JSONObject> doInBackground(String... strings) {


            String url = "";

            url = VENUE_URL+""+strings[0]+"/"+strings[1];



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
            ProgressBar bar = (ProgressBar)this.activity.findViewById(R.id.recommendation_progress_bar);
            bar.setVisibility(View.GONE);
            if(result !=null){

                Toast.makeText(this.activity.getApplicationContext(), "Foram encontrado(s) " + result.size() + " poi(s)", Toast.LENGTH_SHORT).show();

                markers = new ArrayList<MarkerOptions>();
                list = new ArrayList<Recommendation>();

                for(JSONObject venueJSON : result){
                    try {

                        String id        = venueJSON.getString("id");
                        String nome      = venueJSON.getString("nome");
//                    String distance  = venueJSON.getString("distance");
                        Double latitude  = venueJSON.getDouble("latitude");
                        Double longitude = venueJSON.getDouble("longitude");
                        Double rating    = venueJSON.getDouble("avaliacao");

                        Venue venue = new Venue();
                        venue.setName(nome);
                        venue.setId(id);
                        venue.setDistance(1.0);
                        venue.setAddress("Endereço");
                        venue.setLatitude(latitude);
                        venue.setLongitude(longitude);

                        Recommendation rec = new Recommendation();
                        rec.setRating(2.0);
                        rec.setVenue(venue);

                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(nome.toUpperCase())
                                .snippet("Avaliação: " + rating);

                        googleMaps.addMarker(marker);

                        list.add(rec);


                    } catch (JSONException e) {
                        Log.e(TAG,"Json Problem",e);
                    }

                }

                RecommendationActivity.RecommendationAdapter adapter = new RecommendationActivity.RecommendationAdapter(list);
                listView.setAdapter(adapter);


            }
            else{
                Toast.makeText(this.activity.getApplicationContext(),"Não foi possível encontrar nenhum poi",Toast.LENGTH_SHORT).show();
            }

        }
    }

    class ImageViewClickListener implements View.OnClickListener {
        private int position;
        public ImageViewClickListener(int pos)
        {
            this.position = pos;
        }

        public void onClick(View v) {
            Recommendation rec = list.get(position);
//            Set<ImageView> views = new HashSet<ImageView>();

            ImageView imageView = (ImageView)v;
            switch (v.getId()){
                case R.id.heart1:
                    imageView.setBackgroundColor(Color.rgb(255,18,0));
                    Toast.makeText(getApplicationContext(), "1 estrela para "+rec.getVenue().getName(), Toast.LENGTH_SHORT).show();
//                    views.add(imageView);
                    break;

                case R.id.heart2:
//                    ImageView imageView = (ImageView)v;
                    Toast.makeText(getApplicationContext(), "2 estrelas para "+rec.getVenue().getName(), Toast.LENGTH_SHORT).show();

                    break;

                case R.id.heart3:
                    Toast.makeText(getApplicationContext(), "3 estrelas para "+rec.getVenue().getName(), Toast.LENGTH_SHORT).show();
                    break;

                case R.id.heart4:
                    Toast.makeText(getApplicationContext(), "4 estrelas para "+rec.getVenue().getName(), Toast.LENGTH_SHORT).show();
                    break;

                case R.id.heart5:
                    Toast.makeText(getApplicationContext(), "5 estrelas para "+rec.getVenue().getName(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}