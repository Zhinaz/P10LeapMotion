package p10.p10leapmotion;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 137;
    // UI Elements
    public TextView txt_location;
    public TextView txt_pairedDevices;

    public Button test_button;

    Bluetooth bluetooth;
    //ConnectBluetooth connectBluetooth;

    public static final String LOCATION_CHANGED = "LOCATION_CHANGED";
    public static final String LAST_LOCATION_SPEED = "LAST_LOCATION_SPEED";
    public static final String LAST_LOCATION_LONGITUDE = "LAST_LOCATION_LONGITUDE";
    public static final String LAST_LOCATION_LATITUDE = "LAST_LOCATION_LATITUDE";
    public static final String BLUETOOTH_PAIRED_DEVICES = "BLUETOOTH_PAIRED_DEVICES";

    public final static int MESSAGE_STATE_CHANGE = 1337;
    public final static String DEVICE_NAME = "1337 mmkay";
    public final static int MESSAGE_DEVICE_NAME = 1338;
    public final static String TOAST = "1339 mmkay";
    public final static int MESSAGE_TOAST = 1339;
    public final static int MESSAGE_READ = 1340;
    public final static int MESSAGE_WRITE = 1341;

    //ArrayList bluetoothDevices;
    ArrayList<String> bluetoothDevices = new ArrayList<>();

    private LocationManager locationManager;
    private LocationListener locationListener;

    private BluetoothServices mBluetoothServices = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private String mConnectedDeviceName = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseComponents();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new GPS();

        LocalBroadcastManager.getInstance(this).registerReceiver(gpsReceiver, new IntentFilter(LOCATION_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(bluetoothReceiver, new IntentFilter(BLUETOOTH_PAIRED_DEVICES));

        // Make device discoverable
        ensureDiscoverable();

        /*
        try {
            bluetooth.startBluetooth();
        } catch (Exception e) {
            e.printStackTrace();
        }

        bluetooth.updateBluetoothList();
        */

        try {
            bluetooth.startBluetooth();
            bluetooth.publicBluetooth();
            bluetooth.updateBluetoothList();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initialiseComponents() {
        // UI Elements
        txt_location = (TextView)findViewById(R.id.txt_location);
        txt_pairedDevices = (TextView)findViewById(R.id.txt_pairedDevices);
        test_button = (Button)findViewById(R.id.btn_media);
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("Hård pik på alle måder!");
            }
        });

        //bluetooth = new Bluetooth(this,this);
    }

    public void requestGPSLocationUpdates() {
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0,
                locationListener);
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }


    public BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bluetoothDevices = intent.getStringArrayListExtra(BLUETOOTH_PAIRED_DEVICES);
            if(bluetoothDevices != null) {
                System.out.println("Listview adapter is set: " + bluetoothDevices.get(0));
                String pairedDevices = "";

                for (String d : bluetoothDevices) {
                    pairedDevices = pairedDevices + d + " ";
                    //connectBluetooth = new ConnectBluetooth(bluetooth.getFirstDevice(), bluetooth.mBluetoothAdapter);
                    //connectBluetooth.run();

                    mBluetoothServices.connect(bluetooth.getFirstDevice(), false);
                }
                txt_pairedDevices.setText(pairedDevices);

            } else {
                System.out.println("bluetoothDevices is null");
            }
        }
    };

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString("EXTRA_DEVICE_ADDRESS");
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothServices.connect(device, secure);
    }

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

        /*if (!bluetooth.mBluetoothAdapter.isEnabled()) {
            bluetooth.startBluetooth();
        }*/

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if (mBluetoothServices == null) {
            mBluetoothServices = new BluetoothServices(this, mHandler);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBluetoothServices != null) {
            if (mBluetoothServices.getState() == BluetoothServices.STATE_NONE) {
                mBluetoothServices.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if (bluetooth.mBluetoothAdapter.isEnabled()) {
            bluetooth.stopBluetooth();
        }*/
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gpsReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bluetoothReceiver);

        if (mBluetoothServices != null) {
            mBluetoothServices.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if (bluetooth.mBluetoothAdapter.isEnabled()) {
            bluetooth.stopBluetooth();
        }*/
    }

    // Send a message to connected bluetooth device
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBluetoothServices.getState() != BluetoothServices.STATE_CONNECTED) {
            Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBluetoothServices.write(send);
        }
    }

    /**
     * The Handler that gets information back from the BluetoothServices
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //FragmentActivity activity = this;
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothServices.STATE_CONNECTED:
                            System.out.println("Connected_to: " + mConnectedDeviceName);
                            break;
                        case BluetoothServices.STATE_CONNECTING:
                            System.out.println("Connecting");
                            break;
                        case BluetoothServices.STATE_LISTEN:
                        case BluetoothServices.STATE_NONE:
                            System.out.println("Not_connected");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    System.out.println("Me:  " + writeMessage);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    System.out.println(mConnectedDeviceName + ":  " + readMessage);
                    test_button.setText(readMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MESSAGE_TOAST:
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}