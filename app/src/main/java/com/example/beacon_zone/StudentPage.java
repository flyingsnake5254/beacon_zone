package com.example.beacon_zone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StudentPage extends AppCompatActivity {
    Button bSignRecord, bAbsentRecord;
    SimpleDateFormat simpleDateFormat;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    FirebaseDatabase firebaseDatabase;
    private static final long SCAN_PERIOD = 10000; //10 seconds

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
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    String[][] dates = Variable.dates;

    String[] times1 = Variable.times1;
    String[] times2 = Variable.times;

    private static final double DISTANCE = 0.1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);

        setTitle("Student Version");
        checkPermissions();

        detect();

        bAbsentRecord = (Button) findViewById(R.id.b_show_absent);
        bAbsentRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentPage.this, ShowAbsent.class));
            }
        });

        bSignRecord = (Button) findViewById(R.id.b_show_sign);
        bSignRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentPage.this, StudentShowSign.class));
            }
        });

    }

    private void checkPermissions(){
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {

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

    @Override
    protected void onPause() {
        super.onPause();
//        scanLeDevice(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
        detect();
    }

    // 掃描藍芽裝置
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(StudentPage.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     final byte[] scanRecord) {
                    // 搜尋回饋
                    if (ActivityCompat.checkSelfPermission(StudentPage.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    // 尋找ibeacon
                    // https://os.mbed.com/blog/entry/BLE-Beacons-URIBeacon-AltBeacons-iBeacon/
                    boolean patternFound = false;
                    String identify = "";
                    byte[] identifyBytes = new byte[9];
                    for(int i = 0 ; i < 9 ; i ++){
                        identifyBytes[i] = scanRecord[i];
                    }
                    identify = bytesToHex(identifyBytes);
                    if(identify.equals("0201061AFF4C000215")){
                        patternFound = true;
                    }



                    // if find ibeacon
                    if (patternFound) {

//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        // 轉換16進制
                        byte[] uuidBytes = new byte[16];
                        // 來源、起始位置
                        System.arraycopy(scanRecord, 9, uuidBytes, 0, 16);
                        String hexString = bytesToHex(uuidBytes);

                        // UUID
                        String uuid = hexString.substring(0, 8) + "-"
                                + hexString.substring(8, 12) + "-"
                                + hexString.substring(12, 16) + "-"
                                + hexString.substring(16, 20) + "-"
                                + hexString.substring(20, 32);

                        // Major
                        byte[] majorBytes = {scanRecord[25], scanRecord[26]};
                        int major = Integer.parseInt(bytesToHex(majorBytes));


                        // Minor
                        byte[] minorBytes = {scanRecord[27], scanRecord[28]};
                        int minor = Integer.parseInt(bytesToHex(minorBytes));

                        String mac = device.getAddress();
                        // txPower
                        int txPower = (scanRecord[29]);
//                        double distance = calculateDistance(txPower, rssi);
//
//                        String mes1 = "Name：" + device.getName() + "\n   Mac：" + mac
//                                + " \n   UUID：" + uuid + "\n   Major：" + major + "\n   Minor："
//                                + minor + "\n   TxPower：" + txPower + "\n   rssi：" + rssi + "\n   distance : "+ calculateDistance(txPower, rssi);
//
//                        System.out.println(mes1);


                        // check
                        if (uuid.equals(Variable.BEACON_UUID) && calculateDistance(txPower, rssi) < DISTANCE) {
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference dr = firebaseDatabase.getReference("account").child("student1").child("sign");
                            dr.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date d = new Date();
                                        String s = "";
                                        s = simpleDateFormat.format(d); // now time

                                        try {
                                            d = simpleDateFormat.parse(s);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        STOP_SEARCH:
                                        for (int i = 15; i <= 19; i++) {
                                            for (int j = 1; j <= 5; j++) {
                                                for (int k = 1; k <= 9; k++) {
                                                    if (k == 5)
                                                        continue;
                                                    Date d1 = new Date();
                                                    Date d2 = new Date();
                                                    String s1 = dates[i][j] + " " + times1[k];
                                                    String s2 = dates[i][j] + " " + times2[k];

                                                    try {
                                                        d1 = simpleDateFormat.parse(s1);
                                                        d2 = simpleDateFormat.parse(s2);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
//
                                                    if (d.getTime() >= d1.getTime() && d.getTime() <= d2.getTime()) {
                                                        String state = "";
                                                        for (DataSnapshot ds : task.getResult().getChildren()) {
                                                            if (ds.getKey().equals(String.valueOf(i))) {
                                                                for (DataSnapshot ds2 : ds.getChildren()) {
                                                                    if (ds2.getKey().equals(String.valueOf(j))) {
                                                                        for (DataSnapshot ds3 : ds2.getChildren()) {
                                                                            if (ds3.getKey().equals(String.valueOf(k)) && ds3.getValue().toString().equals("null")) {
//                                                                                Toast.makeText(StudentPage.this, "Success", Toast.LENGTH_SHORT).show();
                                                                                FirebaseDatabase f = FirebaseDatabase.getInstance();
                                                                                DatabaseReference dr2 = f.getReference("account").child("student1").child("sign").child(String.valueOf(i)).child(String.valueOf(j)).child(String.valueOf(k));
                                                                                dr2.setValue(s);
//                                                                                dr.child(String.valueOf(i)).child(String.valueOf(j)).child(String.valueOf(k)).setValue(d.toString());
                                                                                break STOP_SEARCH;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }

                }
            };

    public void detect() {
        mHandler = new Handler();

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 檢查手機硬體是否為BLE裝置
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "硬體不支援", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 檢查手機是否開啟藍芽裝置
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//            Toast.makeText(this, "請開啟藍芽裝置", Toast.LENGTH_SHORT).show();
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);

        } else {
            scanLeDevice(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
        detect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart");
        detect();
    }

    public String bytesToHex(byte[] bytes) {

        String result = "";
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF; // to unsigned byte

            String bin = Integer.toBinaryString(v);
            String zero = "";
            for(int s = 0 ; s < 8 - bin.length() ; s ++)
                zero += "0";
            bin = zero + bin;
            String s1 = "", s2 = "";
            for (int s = 0 ; s < 4 ; s ++){
                s1 += bin.charAt(s);
                s2 += bin.charAt(4 + s);
            }
            result += Integer.toHexString(Integer.parseInt(s1, 2)).toUpperCase();
            result += Integer.toHexString(Integer.parseInt(s2, 2)).toUpperCase();

        }
        return result;
    }

    public double calculateDistance(int txPower, double rssi) {
        return Math.pow(10, ((double) (txPower - rssi) / (10 * 2)));
    }


}