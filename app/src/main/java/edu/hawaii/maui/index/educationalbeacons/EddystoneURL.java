package edu.hawaii.maui.index.educationalbeacons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class EddystoneURL extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eddystone_url);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://beaconcafeteria.wordpress.com");
    }
}
