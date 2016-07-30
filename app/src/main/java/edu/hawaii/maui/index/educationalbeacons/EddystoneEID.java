package edu.hawaii.maui.index.educationalbeacons;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.eddystoneeid.EidResolver;

import java.util.Collection;

public class EddystoneEID extends AppCompatActivity implements BeaconConsumer, RangeNotifier {
    private static final String TAG = "EddystoneEID";
    private static final boolean DEBUG = true;
    private BeaconManager mBeaconManager;
    private static final String GOOGLE_API_KEY = "AIzaSyDPC7KjBKOUlbrEFvptg3qtYBk3_CJgULU";
    private static final String RESOLUTION_NAMESPACED_TYPE = "eddystoneeid-1342/eid";
    private EidResolver resolver;
    private String data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eddystone_eid);
        if (DEBUG) Log.d(TAG, "onCreate");
        resolver = EidResolver.getInstanceWithGoogleApiKey(GOOGLE_API_KEY, RESOLUTION_NAMESPACED_TYPE);

    }

    @Override
    public void onResume() {
        super.onResume();
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect Eddystone-EID
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_EID_LAYOUT)); // "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11"
        mBeaconManager.bind(this);
        if (DEBUG) Log.d(TAG, "onResume");
    }

    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
            if (DEBUG) Log.d(TAG, "Error");
        }
        mBeaconManager.setRangeNotifier(this);
        if (DEBUG) Log.d(TAG, "onBeaconServiceConnect");
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (DEBUG) Log.d(TAG, "didRangeBeaconsInRegion");
        for (Beacon beacon : beacons) {
            if (DEBUG) Log.d(TAG, "Another beacon");
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x30) {
                Identifier ephemeralId = beacon.getId1();
                if(resolver.getResolvedIdentifierString(ephemeralId) != null) {
                    Log.d(TAG,getAttachmentInfo(ephemeralId));
                }
                if (DEBUG) Log.d(TAG, "I see a beacon transmitting ephemeral id: " + ephemeralId + " approximately " + beacon.getDistance() + " meters away.");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBeaconManager.unbind(this);
        if (DEBUG) Log.d(TAG, "onPause");
    }

    public String getAttachmentInfo(Identifier ephemeralId) {
        try {
            byte[] attachmentData = Base64.decode(resolver.getResolvedIdentifierString(ephemeralId), Base64.NO_WRAP);
            String decoded = new String(attachmentData, "UTF-8");
            data = decoded;
        } catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
}
