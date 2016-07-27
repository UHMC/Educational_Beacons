package edu.hawaii.maui.index.educationalbeacons;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.eddystoneeid.EidResolver;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

public class EddystoneEID extends AppCompatActivity implements BeaconConsumer, RangeNotifier {
    private static final String TAG = "EddystoneEID";
    private static final String DEBUG_TAG = "EddystoneEID_Debug";
    private static final boolean DEBUG = true;
    private BeaconManager mBeaconManager;
    private static final String GOOGLE_API_KEY = "AIzaSyDPC7KjBKOUlbrEFvptg3qtYBk3_CJgULU";
    private static final String RESOLUTION_NAMESPACED_TYPE = "eddystoneeid-1342/eid";
    private EidResolver resolver;
    private byte[] attachmentData;
    private String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eddystone_eid);
        if (DEBUG) Log.d(DEBUG_TAG, "onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect Eddystone-EID
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_EID_LAYOUT)); // "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11"
        mBeaconManager.bind(this);
        if (DEBUG) Log.d(DEBUG_TAG, "onResume");
    }

    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        resolver = EidResolver.getInstanceWithGoogleApiKey(GOOGLE_API_KEY, RESOLUTION_NAMESPACED_TYPE);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
            if (DEBUG) Log.d(DEBUG_TAG, "Error");
        }
        mBeaconManager.setRangeNotifier(this);
        if (DEBUG) Log.d(DEBUG_TAG, "onBeaconServiceConnect");
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (DEBUG) Log.d(DEBUG_TAG, "didRangeBeaconsInRegion");
        for (Beacon beacon : beacons) {
            if (DEBUG) Log.d(DEBUG_TAG, "Another beacon");
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x30) {
                if (DEBUG) Log.d(DEBUG_TAG, "Matches EID type");
                // This is a Eddystone-EID frame
                Identifier ephemeralId = beacon.getId1();
                Log.d(TAG, "I see a beacon transmitting ephemeral id: " + ephemeralId +
                        " approximately " + beacon.getDistance() + " meters away.");
                Log.d(DEBUG_TAG, "B64_OUT: [" + Base64.encodeToString(beacon.getId1().toByteArray(), Base64.NO_WRAP) + "]");
                if (resolver.getResolvedIdentifierString(beacon.getId1()) != null) {
                    attachmentData = Base64.decode(resolver.getResolvedIdentifierString(beacon.getId1()).getBytes(),Base64.NO_WRAP);
                    try {
                        data = new String(attachmentData,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d(DEBUG_TAG, "Resolved identifier for: " + beacon.getId1() + " is " + data);

                }
                if (DEBUG) Log.d(DEBUG_TAG, "Logged some range info");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBeaconManager.unbind(this);
        if (DEBUG) Log.d(DEBUG_TAG, "onPause");
    }

    public void advertise(View view) {
        if (DEBUG) Log.d(DEBUG_TAG, "Advertise");
        Beacon beacon = new Beacon.Builder()
                .setId1("0x0001020304050607") // Ephemeral Identifier
                .setManufacturer(0x0118) // Radius Networks or any 2-byte Bluetooth SIG company code
                .setTxPower(-59)
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_EID_LAYOUT);
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {

            @Override
            public void onStartFailure(int errorCode) {
                Log.e(TAG, "Advertisement start failed with code: " + errorCode);
            }

            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.i(TAG, "Advertisement start succeeded.");
            }
        });
        if (DEBUG) Log.d(DEBUG_TAG, "Exiting advertise");
    }
}
