package edu.hawaii.maui.index.educationalbeacons;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collection;

public class MonitoringActivity extends AppCompatActivity implements BootstrapNotifier {
    private static final String TAG = "Monitoring";
    private RegionBootstrap regionBootstrap;
    private BeaconManager beaconManager;
    private Identifier websight;
    private Beacon beacon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "App started up");
        beaconManager = BeaconManager.getInstanceForApplication(this);


        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        // wake up the app when any beacon is seen (you can specify specific id filers in the parameters below)

        try {
            File file = new File(getFilesDir().getPath().toString() + "/website.txt");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
            String url = bfr.readLine();
            byte[] urlBytes = UrlBeaconUrlCompressor.compress(url);
            websight = Identifier.fromBytes(urlBytes,0,urlBytes.length,false);
            Log.d(TAG, "Uncompressed Beacon URL: " + UrlBeaconUrlCompressor.uncompress(urlBytes));
        }catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(this, "Monitoring started!", Toast.LENGTH_SHORT).show();
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(new BeaconParser().EDDYSTONE_URL_LAYOUT));
        Region region = new Region("bootstrapRegion", websight , null, null);
        regionBootstrap = new RegionBootstrap(this, region);
        finish();
    }




    @Override
    public void didDetermineStateForRegion(int arg0, Region arg1) {
        // Don't care
    }
    @Override
    public void didEnterRegion(Region arg0) {
        Log.d(TAG, "Got a didEnterRegion call");

//        // This call to disable will make it so the activity below only gets launched the first time a beacon is seen (until the next time the app is launched)
//        // if you want the Activity to launch every single time beacons come into view, remove this call.
//        regionBootstrap.disable();
//        Intent intent = new Intent(this, NotifyActivity.class);
//        // IMPORTANT: in the AndroidManifest.xml definition oqf this activity, you must set android:launchMode="singleInstance" or you will get two instances
//        // created when a user launches the activity manually and it gets launched from here.
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(intent);
        Intent resultIntent = new Intent(this, EddystoneURL.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
                        .setContentTitle("You're near the cafeteria!")
                        .setContentText("Check out what's cookin'")
                        .setContentIntent(resultPendingIntent);

// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.

// Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    @Override
    public void didExitRegion(Region arg0) {
        // Don't care
    }

}
