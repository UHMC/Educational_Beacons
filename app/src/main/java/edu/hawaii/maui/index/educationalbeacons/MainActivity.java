package edu.hawaii.maui.index.educationalbeacons;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean DEBUG = false;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
//        Intent activity = null;
//        activity = new Intent(this,MonitoringActivity.class);
//        try {
//            startActivity(activity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){/* All good. */}
            else Toast.makeText(this, "Beacon discovery disallowed!", Toast.LENGTH_SHORT).show();
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

    public void launchURL(View v) {
        Intent activity = null;
        activity = new Intent(this, EddystoneURL.class);
        try {
            startActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchEID(View v) {
        Intent activity = null;
        activity = new Intent(this, EddystoneEID.class);
        try {
            startActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchEIDTestDemo(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void monitor(View v) {
        Intent activity = null;
        activity = new Intent(this, MonitoringActivity.class);
        try {
            startActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setup(View v) {
        Intent activity = null;
        activity = new Intent(this, SetupActivity.class);
        try {
            startActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
