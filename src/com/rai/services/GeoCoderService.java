package com.rai.services;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.rai.R;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 01/07/13
 * Time: 21:17
 * To change this template use File | Settings | File Templates.
 */
public class GeoCoderService extends AbstractService<Map<String,String>> {

    private final Activity activity;
    private static final String TAG = "GeoCoderService";
    private static final String BASIC_URL = "http://maps.googleapis.com/maps/api/geocode/json?";

    public GeoCoderService(Activity activity){
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
    protected Map<String, String> doInBackground(String... strings) {
        Log.i(TAG,"doInBackground");
//        Log.i(TAG,"doInBackground "+params[0]+" "+params[1]);

        String url = BASIC_URL+"latlng="+strings[0]+","+strings[1]+"&sensor=false";

        Log.i(TAG,"URL "+url);
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

            Map<String,String> data = new HashMap<String, String>();
            JSONArray results = jsonObject.getJSONArray("results");
            JSONArray addressComponents = results.getJSONObject(0).getJSONArray("address_components");

            int size = addressComponents.length();
            String city = "";
            for(int i=0;i<size;i++){
                JSONArray types = addressComponents.getJSONObject(i).getJSONArray("types");
                int typesCount = types.length();
                for(int j=0;j<typesCount;j++){
                    if(types.getString(j).equalsIgnoreCase("locality")){
                        city = addressComponents.getJSONObject(i).getString("long_name");
                    }
                }


            }
            Log.i(TAG,"City found "+city);

            data.put("city",city);

            return data;

        }
        catch (Exception e){
            Log.e(TAG,"Json Problem",e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Map<String,String> result) {
        Log.i(TAG,"onPostExecute");
        if(result !=null ){
            TextView cityView = (TextView)this.activity.findViewById(R.id.cityView);
            cityView.setText(result.get("city"));
        }
        else{
            Toast.makeText(this.activity.getApplicationContext(),"Não foi possível fazer o geocoder",Toast.LENGTH_SHORT);
        }

    }
}
