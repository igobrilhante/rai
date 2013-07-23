package com.rai;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.rai.context.ContextManager;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 23/07/13
 * Time: 01:09
 * To change this template use File | Settings | File Templates.
 */
public class WebActivity extends Activity {

    private static final String TAG = "WEBACTIVITY";
    private WebView webview;
    private ContextManager contextManager;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Log.d(TAG, "On create");
        String url ="http://rai-server.herokuapp.com/login";

        this.contextManager = ContextManager.instance();

        WebView webview = (WebView)findViewById(R.id.webview);
        webview.clearHistory();
        webview.clearView();
        webview.clearFormData();
        webview.clearCache(true);

        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                String fragment = "#access_token=";
                int start = url.indexOf(fragment);
                if (start > -1) {

                    String accessToken = url.substring(start + fragment.length(), url.length());

                    contextManager.getPreferences().edit().putString("user",accessToken).commit();

//                    Log.v(TAG, "OAuth complete, token: [" + accessToken + "].");
//                    sharedPrefs.edit().putString("user.foursquare.token", accessToken).commit();

                    Toast.makeText(WebActivity.this, "Token: " + accessToken, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(ActivityWebView.this, getResources().getText(R.string.suc_login).toString(), Toast.LENGTH_SHORT).show();
//                    Utilities.toastMensage(ActivityWebView.this, getResources().getText(R.string.suc_login).toString()).show();
                    Intent intent = new Intent(getApplicationContext(),MapActivity.class);
                    startActivity(intent);

                }
            }
            public void onPageFinished(WebView view, String url){
//            	ProgressBar pb = (ProgressBar)findViewById(R.id.web_progress_bar);
//            	pb.setEnabled(false);
//            	pb.setIndeterminate(false);
//            	pb.setVisibility(View.INVISIBLE);

            }
        });
        webview.loadUrl(url);

    }
}