package com.example.beacon_zone;

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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Test3 extends AppCompatActivity {

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000; //10 seconds
    private boolean search = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        setTitle("TEST");
        detect();

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
                    if (ActivityCompat.checkSelfPermission(Test3.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
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
                    if (ActivityCompat.checkSelfPermission(Test3.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
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
                        if (ActivityCompat.checkSelfPermission(Test3.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
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
                        double distance = calculateAccuracy(txPower,rssi);

                        String mes1 = "Name：" + device.getName() + "\nMac：" + mac
                                + " \nUUID：" + uuid + "\nMajor：" + major + "\nMinor："
                                + minor + "\nTxPower：" + txPower + "\nrssi：" + rssi;

                        String mes2 = "distance："+calculateAccuracy(txPower,rssi);
                        TextView t = (TextView) findViewById(R.id.ttt);
                        t.setText(mes2);
                        System.out.println(mes1);
                        System.out.println(mes2);


                    }

                }
            };

    public void detect(){
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
            Toast.makeText(this, "請開啟藍芽裝置", Toast.LENGTH_SHORT).show();
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