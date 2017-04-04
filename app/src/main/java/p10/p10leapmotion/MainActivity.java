package p10.p10leapmotion;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    public TextView txt_location;
    public TextView txt_pairedDevices;

    Bluetooth bluetooth;
    ConnectBluetooth connectBluetooth;

    public static final String LOCATION_CHANGED = "LOCATION_CHANGED";
    public static final String LAST_LOCATION_SPEED = "LAST_LOCATION_SPEED";
    public static final String LAST_LOCATION_LONGITUDE = "LAST_LOCATION_LONGITUDE";
    public static final String LAST_LOCATION_LATITUDE = "LAST_LOCATION_LATITUDE";
    public static final String BLUETOOTH_PAIRED_DEVICES = "BLUETOOTH_PAIRED_DEVICES";

    //ArrayList bluetoothDevices;
    ArrayList<String> bluetoothDevices = new ArrayList<>();

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseComponents();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new GPS();

        LocalBroadcastManager.getInstance(this).registerReceiver(gpsReceiver, new IntentFilter(LOCATION_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(bluetoothReceiver, new IntentFilter(BLUETOOTH_PAIRED_DEVICES));

        try {
            bluetooth.startBluetooth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //bluetooth.publicBluetooth();
        bluetooth.updateBluetoothList();
    }

    private void initialiseComponents() {
        // UI Elements
        txt_location = (TextView)findViewById(R.id.txt_location);
        txt_pairedDevices = (TextView)findViewById(R.id.txt_pairedDevices);

        bluetooth = new Bluetooth(this,this);
    }

    public void requestGPSLocationUpdates() {
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0,
                locationListener);
    }

    public BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bluetoothDevices = intent.getStringArrayListExtra(BLUETOOTH_PAIRED_DEVICES);
            if(bluetoothDevices != null) {
                System.out.println("Listview adapter is set: " + bluetoothDevices.get(0));
                String pairedDevices = "";

                for (String device : bluetoothDevices) {
                    pairedDevices = pairedDevices + device + " ";
                    connectBluetooth = new ConnectBluetooth(bluetooth.getFirstDevice(), bluetooth.mBluetoothAdapter);
                    connectBluetooth.run();
                }
                txt_pairedDevices.setText(pairedDevices);

            } else {
                System.out.println("bluetoothDevices is null");
            }
        }
    };

    public BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("gpsReceiver");
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

    @Override
    protected void onStart() {
        super.onStart();

        requestGPSLocationUpdates();

        if (!bluetooth.mBluetoothAdapter.isEnabled()) {
            bluetooth.startBluetooth();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bluetooth.mBluetoothAdapter.isEnabled()) {
            bluetooth.stopBluetooth();
        }
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gpsReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bluetoothReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bluetooth.mBluetoothAdapter.isEnabled()) {
            bluetooth.stopBluetooth();
        }
    }
}