package com.rai.test;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 27/06/13
 * Time: 12:54
 * To change this template use File | Settings | File Templates.
 */
public class TwitterTest {

    public static void main(String[] args) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("oguoB9SQWIMOq2kfWgvbuQ")
                .setOAuthConsumerSecret("KsEvJfiRioDfO8DSOSKTaJAYFu1bBzgxQj4z5j36U")
                .setOAuthAccessToken("RF9ee2Hh4BzjEFFKWz567hBQ5F3mLrZaKDKBmsAu")
                .setOAuthAccessTokenSecret("tLMxWBD90uGcMMBGPqbV8DEMkMd73qtlZWjsVyP4jY");
        TwitterFactory tf = new TwitterFactory(cb.build());

        Twitter twitter = tf.getInstance();

        Query query = new Query("source:twitter4j yusukey");
        QueryResult result = twitter.search(query);
        for(Status tweet : result.getTweets()){
            System.out.println(tweet);
        }

    }
}
