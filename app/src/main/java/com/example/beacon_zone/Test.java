package com.example.beacon_zone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class Test extends AppCompatActivity implements BeaconConsumer{

    private static final String TAG = "MainActivity";

    private Button startButton;
    private Button stopButton;

    private BeaconManager beaconManager = null;
    private Region beaconRegion = null;

    private static final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        checkPermissions();
        startButton = (Button) findViewById(R.id.bStart);
        stopButton = (Button) findViewById(R.id.bStop);
        startButton.setOnClickListener(view -> startBeaconMonitoring());
        stopButton.setOnClickListener(view -> stopBeaconMonitoring());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
            requestPermissions(new String[] {Manifest.permission.BLUETOOTH_SCAN}, 5678);
        }

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        beaconManager.bind(this);
    }

    private Boolean entryMessageRaised = false;
    private Boolean exitMessageRaised = false;
    private Boolean rangingMessageRaised = false;
    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect called");

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                if (!entryMessageRaised){
                    showAlert("didEnterRegion??", "Entering region" + region.getUniqueId() +
                            " Beacon detected UUID/major/minor : " + region.getId1() + "/" + region.getId2() + "/" + region.getId3());
                    entryMessageRaised = true;
                }
            }

            @Override
            public void didExitRegion(Region region) {
                if (!exitMessageRaised){
                    showAlert("didExitRegion", "exiting region" + region.getUniqueId() +
                            " Beacon detected UUID/major/minor : " + region.getId1() + "/" + region.getId2() + "/" + region.getId3());
                    exitMessageRaised = true;
                }
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {

            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (!rangingMessageRaised && beacons != null && !beacons.isEmpty()){
                    for (Beacon beacon : beacons){
                        showAlert("didExitRegion", "Ranging region" + region.getUniqueId() +
                                " Beacon detected UUID/major/minor : " + region.getId1() + "/" + region.getId2() + "/" + region.getId3());
                    }
                    rangingMessageRaised = true;
                }
            }
        });

    }

    private void startBeaconMonitoring(){
        Log.d(TAG, "startBeaconMonitoring called");
        try{
            beaconRegion = new Region("MyBeacon", Identifier.parse("2036cd68-35a3-4cd6-8124-04db468787fc"),
                    Identifier.parse("0"), Identifier.parse("1"));
            beaconManager.startMonitoringBeaconsInRegion(beaconRegion);
            beaconManager.startRangingBeaconsInRegion(beaconRegion);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void stopBeaconMonitoring(){
        Log.d(TAG, "stopBeaconMonitoring called");

        try{
            beaconManager.stopMonitoringBeaconsInRegion(beaconRegion);
            beaconManager.stopRangingBeaconsInRegion(beaconRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showAlert(String s1, String s2){
        System.out.println(s1 + " : " + s2);
    }

    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    private void checkPermissions(){
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }
}