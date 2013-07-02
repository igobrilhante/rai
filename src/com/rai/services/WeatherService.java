package com.rai.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.rai.R;
import com.rai.context.ContextApp;
import com.rai.context.ContextManager;
import com.rai.utils.Utils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 23/06/13
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */

public class WeatherService  extends AbstractService<Map<String,String>> {

    private static final String TAG = "WeatherService";
    private String API = "811d5f0068823a1f95b9e6412ed32d56";
    private String BASIC_URL = "https://api.forecast.io/forecast/"+API+"/";
    private Context context;
    private ContextManager contextManager;
    private Activity requester;

    public WeatherService(Activity requester){
        super();
        this.contextManager = ContextManager.instance();
        this.context = this.contextManager.getContext();
        this.requester = requester;
    }

    @Override
    protected void onPreExecute(){
        ProgressBar bar = (ProgressBar)this.requester.findViewById(R.id.progressBarWeather);
        bar.setVisibility(ViewStub.VISIBLE);

        LinearLayout linearLayout = (LinearLayout)this.requester.findViewById(R.id.box1);
        linearLayout.setClickable(false);
    }

    @Override
    protected Map<String,String> doInBackground(String... params) {
        Log.i(TAG,"doInBackground "+params[0]+" "+params[1]);

        String url = BASIC_URL+params[0]+","+params[1]+"/?units=si";

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
            JSONObject currently    = jsonObject.getJSONObject("currently");
            String summary          = currently.getString("summary");
            String temperature      = currently.getString("temperature");
            String humidity         = currently.getString("humidity");
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM HH:mm");
            String date = formato.format(new Date());

            data.put("summary",summary);
            data.put("temperature",temperature);
            data.put("humidity",humidity);
            data.put("date",date);

            return data;

        }
        catch (Exception e){
            Log.e(TAG,"Json Problem",e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Map<String,String> result) {
//        Toast.makeText(this.context,"Clima Atualizado",Toast.LENGTH_SHORT).show();


        ProgressBar bar = (ProgressBar)this.requester.findViewById(R.id.progressBarWeather);
        bar.setVisibility(ViewStub.GONE);

        if(result !=null){

        String date        = result.get("date");
        String temperature = result.get("temperature");
        String humidity    = result.get("humidity");

        TextView dateView        = (TextView)this.requester.findViewById(R.id.textViewDate);
        dateView.setText(date);

        TextView temperatureView = (TextView)this.requester.findViewById(R.id.textViewDegree);
        temperatureView.setText(Utils.formatTemperature(temperature));

//        TextView humidityView    = (TextView)this.requester.findViewById(R.id.textViewHumidity);
//        humidityView.setText(humidity);

        Log.i(TAG, "RESULT ");
        }
        else{
            Toast.makeText(this.requester.getApplicationContext(),"Não foi possível atualizar o clima",Toast.LENGTH_SHORT).show();
        }

        LinearLayout linearLayout = (LinearLayout)this.requester.findViewById(R.id.box1);
        linearLayout.setClickable(true);
    }

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    protected Class<? extends AbstractService> getClassName() {
        return WeatherService.class;
    }

}
