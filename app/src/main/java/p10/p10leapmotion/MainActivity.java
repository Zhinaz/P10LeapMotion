package p10.p10leapmotion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    public TextView txt_location;
    ListView lst_btdevices;

    GPS gps;
    Bluetooth bluetooth;

    public BroadcastReceiver bluetoothReceiver;
    public BroadcastReceiver gpsReceiver;

    public static final String LOCATION_CHANGED = "LOCATION_CHANGED";
    public static final String LAST_LOCATION_SPEED = "LAST_LOCATION_SPEED";
    public static final String LAST_LOCATION_LONGITUDE = "LAST_LOCATION_LONGITUDE";
    public static final String LAST_LOCATION_LATITUDE = "LAST_LOCATION_LATITUDE";
    public static final String BLUETOOTH_PAIRED_DEVICES = "BLUETOOTH_PAIRED_DEVICES";

    ArrayList bluetoothDevices;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseComponents();

        try {
            bluetooth.startBluetooth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bluetooth.updateBluetoothList();
    }

    private void initialiseComponents() {
        // UI Elements
        txt_location = (TextView)findViewById(R.id.txt_location);
        lst_btdevices = (ListView)findViewById(R.id.lst_btdevices);

        gps = new GPS(this);
        bluetooth = new Bluetooth(this,this);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevices);

        //LocalBroadcastManager.getInstance(this).registerReceiver(gpsReceiver, new IntentFilter(LOCATION_CHANGED));
        //LocalBroadcastManager.getInstance(this).registerReceiver(bluetoothReceiver, new IntentFilter(BLUETOOTH_PAIRED_DEVICES));

        bluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                bluetoothDevices = intent.getStringArrayListExtra(BLUETOOTH_PAIRED_DEVICES);
                if(!bluetoothDevices.isEmpty()) {
                    lst_btdevices.setAdapter(adapter);
                }
            }
        };

        gpsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                float locationSpeed = intent.getFloatExtra(LAST_LOCATION_SPEED, -1);
                double locationLatitude = intent.getDoubleExtra(LAST_LOCATION_LATITUDE, 999);
                double locationLongitude = intent.getDoubleExtra(LAST_LOCATION_LONGITUDE, 999);

                if (locationLatitude == 999 || locationLongitude == 999) {
                    txt_location.setText("error, error");
                } else {
                    txt_location.setText(String.valueOf(locationLatitude) + ", " + String.valueOf(locationLongitude));
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(gpsReceiver, new IntentFilter(LOCATION_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(bluetoothReceiver, new IntentFilter(BLUETOOTH_PAIRED_DEVICES));
        if (!bluetooth.mBluetoothAdapter.isEnabled()) {
            bluetooth.startBluetooth();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        gps.requestUpdates();
    }

    @Override
    protected void onStop() {
        if (bluetooth.mBluetoothAdapter.isEnabled()) {
            bluetooth.stopBluetooth();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gpsReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bluetoothReceiver);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gps.stopUpdates();
    }
}