package edu.hawaii.maui.index.educationalbeacons;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchURL (View v){
        Intent activity = null;
        activity = new Intent(this, EddystoneURL.class);
        try {
            startActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchEID (View v){
        Intent activity = null;
        activity = new Intent(this,EddystoneEID.class);
        try {
            startActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setup(View v) {
        Intent activity = null;
        activity = new Intent(this,SetupActivity.class);
        try {
            startActivity(activity);
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
