package edu.hawaii.maui.index.educationalbeacons;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
    private boolean DEBUG = false;
    private static final int REQUEST_ENABLE_BT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Intent activity = null;
        activity = new Intent(this,MonitoringActivity.class);
        try {
            startActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) Log.d("Beacons", "Bluetooth enabled.");
            else Log.d("Beacons", "Bluetooth not enabled!");
        }
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

    public void monitor(View v) {
        Intent activity = null;
        activity = new Intent(this,MonitoringActivity.class);
        try {
            startActivity(activity);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setup(View v){
        Intent activity = null;
        activity = new Intent(this,SetupActivity.class);
        try {
            startActivity(activity);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
