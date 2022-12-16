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

    String[][] dates = {
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
            , {null, "2022-12-12", "2022-12-13", "2022-12-14", "2022-12-15", "2022-12-16"}
            , {null, "2022-12-19", "2022-12-20", "2022-12-21", "2022-12-22", "2022-12-23"}
            , {null, "2022-12-26", "2022-12-27", "2022-12-28", "2022-12-29", "2022-12-30"}
            , {null, "2023-01-02", "2023-01-03", "2023-01-04", "2023-01-05", "2023-01-06"}
            , {null, "2023-01-09", "2023-01-10", "2023-01-11", "2023-01-12", "2023-01-13"}
    };

    String[] times1 = {null, "08:10:00", "09:10:00", "10:10:00", "11:10:00", null, "13:30:00", "14:30:00", "15:30:00", "16:30:00"};
    String[] times2 = {null, "09:00:00", "10:00:00", "11:00:00", "12:00:00", null, "14:20:00", "15:20:00", "16:20:00", "17:20:00"};

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
                    int startByte = 2;
                    boolean patternFound = false;
                    // 尋找ibeacon
                    // 先依序尋找第2到第8陣列的元素
                    while (startByte <= 5) {
                        // Identifies an iBeacon
                        if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 &&
                                // Identifies correct data length
                                ((int) scanRecord[startByte + 3] & 0xff) == 0x15) {

                            patternFound = true;
                            break;
                        }
                        startByte++;
                    }

                    // 如果找到了的话
                    if (patternFound) {
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
//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        // 轉換16進制
                        byte[] uuidBytes = new byte[16];
                        // 來源、起始位置
                        System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                        String hexString = bytesToHex(uuidBytes);

                        // UUID
                        String uuid = hexString.substring(0, 8) + "-"
                                + hexString.substring(8, 12) + "-"
                                + hexString.substring(12, 16) + "-"
                                + hexString.substring(16, 20) + "-"
                                + hexString.substring(20, 32);

                        // Major
                        int major = (scanRecord[startByte + 20] & 0xff) * 0x100
                                + (scanRecord[startByte + 21] & 0xff);

                        // Minor
                        int minor = (scanRecord[startByte + 22] & 0xff) * 0x100
                                + (scanRecord[startByte + 23] & 0xff);

                        String mac = device.getAddress();
                        // txPower
                        int txPower = (scanRecord[startByte + 24]);
//                        double distance = calculateAccuracy(txPower, rssi);

//                        String mes1 = "Name：" + device.getName() + "\n   Mac：" + mac
//                                + " \n   UUID：" + uuid + "\n   Major：" + major + "\n   Minor："
//                                + minor + "\n   TxPower：" + txPower + "\n   rssi：" + rssi + "\n   distance : "+ calculateAccuracy(txPower, rssi);
//
//                        System.out.println(mes1);

                        // check
                        if (uuid.equals("2036CD68-35A3-4CD6-8124-04DB468787FC") && calculateAccuracy(txPower, rssi) < DISTANCE) {
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
        if (!getPackageManager().hasSystemFeature
                (PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "硬體不支援", Toast.LENGTH_SHORT).show();
            finish();

        }

        // 檢查手機是否開啟藍芽裝置
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//            Toast.makeText(this, "請開啟藍芽裝置", Toast.LENGTH_SHORT).show();
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);

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

        char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0)
        {
            return -1.0;
        }

        double ratio = rssi * 1.0 / txPower;

        if (ratio < 1.0)
        {
            return Math.pow(ratio, 10);
        }
        else
        {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }


}