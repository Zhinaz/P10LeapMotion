package p10.p10leapmotion;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    public TextView txt_location;
    ListView lst_btdevices;

    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    p10.p10leapmotion.Location location;

    public static final String LOCATION_CHANGED = "LOCATION_CHANGED";
    public static final String LAST_LOCATION_SPEED = "LAST_LOCATION_SPEED";
    public static final String LAST_LOCATION_LONGITUDE = "LAST_LOCATION_LONGITUDE";
    public static final String LAST_LOCATION_LATITUDE = "LAST_LOCATION_LATITUDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseComponents();

        location.togglePeriodicLocationUpdates();
        try {
            startBluetooth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateBluetoothList();
    }

    private void initialiseComponents() {
        // UI Elements
        txt_location = (TextView)findViewById(R.id.txt_location);
        lst_btdevices = (ListView)findViewById(R.id.lst_btdevices);

        location = new p10.p10leapmotion.Location(this, this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmCalledReceiver, new IntentFilter(LOCATION_CHANGED));
    }

    public void startBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 0);
            Toast.makeText(getApplicationContext(), "Bluetooth enabled",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth already enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopBluetooth() {
        mBluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Bluetooth disabled",Toast.LENGTH_SHORT).show();
    }

    public void publicBluetooth() {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void updateBluetoothList() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();

        ArrayList list = new ArrayList();

        for (BluetoothDevice bt : pairedDevices){
            list.add(bt.getName());
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        lst_btdevices.setAdapter(adapter);
    }

    public BroadcastReceiver alarmCalledReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            float locationSpeed = intent.getFloatExtra(LAST_LOCATION_SPEED, -1);
            float locationLatitude = intent.getFloatExtra(LAST_LOCATION_LATITUDE, 999);
            float locationLongitude = intent.getFloatExtra(LAST_LOCATION_LONGITUDE, 999);

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
        if (location.mGoogleApiClient != null) {
            location.mGoogleApiClient.connect();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            startBluetooth();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (location.mGoogleApiClient.isConnected()) {
            location.mGoogleApiClient.disconnect();
        }
        if (mBluetoothAdapter.isEnabled()) {
            stopBluetooth();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        location.isGooglePlayServicesAvailable(this);
        if (location.mGoogleApiClient.isConnected() && location.mRequestingLocationUpdates) {
            location.startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!location.mGoogleApiClient.isConnected()) {
            location.stopLocationUpdates();
        }

    }

}