package edu.hawaii.maui.index.educationalbeacons;


import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collection;

public class MonitoringActivity extends AppCompatActivity implements BeaconConsumer,RangeNotifier{
    private static final String TAG = "Monitoring";
    private BeaconManager beaconManager;
    private Identifier websight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            File file = new File(getFilesDir().getPath().toString() + "/website.txt");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
            String url = bfr.readLine();
            byte[] urlBytes = UrlBeaconUrlCompressor.compress(url);
            websight = Identifier.fromBytes(urlBytes, 0, urlBytes.length, false);
            Log.d(TAG, "Uncompressed Beacon URL: " + UrlBeaconUrlCompressor.uncompress(urlBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Monitoring started!", Toast.LENGTH_SHORT).show();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT)); // "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11"
        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }


    @Override
    public void onBeaconServiceConnect() {
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("urlRegion", null, null, null));
            beaconManager.setRangeNotifier(this);
        } catch (RemoteException e) {    }
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Log.d(TAG,"Hits here.");
        for (Beacon beacon : beacons) {
            Log.d(TAG,UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray())+" and " + websight.toString());
            if(beacon.getId1() == websight){
                Intent intent = new Intent(this,EddystoneURL.class);
                startActivity(intent);
            }

        }
    }
}
