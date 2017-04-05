package p10.p10leapmotion;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 137;
    // UI Elements
    public TextView txt_location;
    public TextView txt_pairedDevices;
    public Button test_button;
    public Button radio_button;
    public GifImageView gifImageView;

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

    private LocationManager locationManager;
    private LocationListener locationListener;

    private BluetoothServices mBluetoothServices = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private String mConnectedDeviceName = null;

    ArrayList<BluetoothDevice> pairedDevices = new ArrayList<BluetoothDevice>();
    ArrayAdapter<BluetoothDevice> pairedDeviceAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseComponents();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG);
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
        }

        pairedDevices.addAll(mBluetoothAdapter.getBondedDevices());

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, filter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new GPS();

        LocalBroadcastManager.getInstance(this).registerReceiver(gpsReceiver, new IntentFilter(LOCATION_CHANGED));
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestGPSLocationUpdates();

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
        locationManager.removeUpdates(locationListener);
        if (mBluetoothServices != null) {
            mBluetoothServices.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(gpsReceiver);

        unregisterReceiver(bluetoothReceiver);

        if (mBluetoothServices != null) {
            mBluetoothServices.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        radio_button = (Button)findViewById(R.id.btn_radio);
        radio_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //connectDevice(false);
            }
        });
        gifImageView = (GifImageView) findViewById(R.id.GifImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.connect:
                chooseBluetoothDevice();
                return true;
            case R.id.discover:
                ensureDiscoverable();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ASyncTask for update UI  // new ImageViewTask().execute(warning, null, null);
    private class ImageViewTask extends AsyncTask<Integer, Void, Void> {
        protected void onPreExecute() {
            gifImageView.setGifImageResource(R.drawable.gif_hypetrain);
        }

        protected Void doInBackground(Integer... params) {

            Integer warning = params[0];
            switch (warning) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            // Show closing gif
        }
    }

    // Start Location section
    public void requestGPSLocationUpdates() {
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0,
                locationListener);
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
    // end location section

    // Start bluetooth section
    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Establish connection with other device
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(int deviceNumber, boolean secure) {
        if (pairedDevices.size() > 0) {
            String deviceAddress = pairedDevices.get(deviceNumber).getAddress();
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
            mBluetoothServices.connect(device, false);
        }
    }

    // Create dialog to choose bluetooth device - Only paired devices show up!
    public Dialog chooseBluetoothDevice() {
        ArrayList<String> tempList = new ArrayList<>();
        for (BluetoothDevice bluetoothDevice : pairedDevices) {
            tempList.add(bluetoothDevice.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Bluetooth device");
        builder.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tempList), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        connectDevice(i, false);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return builder.create();
    }

    // Send a message to connected bluetooth device8
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
                    Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_SHORT).show();
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