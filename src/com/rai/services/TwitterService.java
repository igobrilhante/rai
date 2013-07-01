package com.rai.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.rai.context.ContextManager;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 27/06/13
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
public class TwitterService extends AbstractService {

    private Twitter twitter;
    private Context context;
    private static final String TAG = "TwitterService";

    public TwitterService(Context context){
        this.context = context;

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("oguoB9SQWIMOq2kfWgvbuQ")
                .setOAuthConsumerSecret("KsEvJfiRioDfO8DSOSKTaJAYFu1bBzgxQj4z5j36U")
                .setOAuthAccessToken("RF9ee2Hh4BzjEFFKWz567hBQ5F3mLrZaKDKBmsAu")
                .setOAuthAccessTokenSecret("tLMxWBD90uGcMMBGPqbV8DEMkMd73qtlZWjsVyP4jY");
        TwitterFactory tf = new TwitterFactory(cb.build());

        this.twitter = tf.getInstance();
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
    protected Map<String,String> doInBackground(String... strings) {
        Log.d(TAG,"doInBackground");

//        Query query = new Query("geocode:"+strings[0]+","+strings[1]+","+strings[2]);
        Query query = new Query("source:twitter4j yusukey");
        try {
            QueryResult result = twitter.search(query);
            Log.i(TAG,"Tweets: "+result.getCount());
            for (twitter4j.Status status : result.getTweets()) {
                Log.i(TAG,"Tweet: "+status.getUser().getScreenName()+" "+status.getText()+" "+status.getGeoLocation());
            }
        } catch (TwitterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (IllegalArgumentException a){
            a.printStackTrace();
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onPostExecute(Map<String,String> result) {
        Toast.makeText(this.context, "Tweets encontrados", Toast.LENGTH_SHORT).show();

        ContextManager contextManager = ContextManager.instance();


        Log.i(TAG, "RESULT " + result);
    }
}
