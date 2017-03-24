package p10.p10leapmotion;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import static p10.p10leapmotion.MainActivity.BLUETOOTH_PAIRED_DEVICES;

public class Bluetooth {

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;

    private Context mContext;
    private Activity mActivity;

    public Bluetooth(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void startBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(enableBT, 0);
            Toast.makeText(mContext.getApplicationContext(), "Bluetooth enabled",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext.getApplicationContext(), "Bluetooth already enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopBluetooth() {
        mBluetoothAdapter.disable();
        Toast.makeText(mContext.getApplicationContext(), "Bluetooth disabled",Toast.LENGTH_SHORT).show();
    }

    public void publicBluetooth() {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        mActivity.startActivityForResult(getVisible, 0);
    }

    public void updateBluetoothList() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        for (BluetoothDevice bt : pairedDevices){
            list.add(bt.getName());
        }

        Intent notifyIntent = new Intent(BLUETOOTH_PAIRED_DEVICES);
        notifyIntent.putStringArrayListExtra(BLUETOOTH_PAIRED_DEVICES, list);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(notifyIntent);
    }
}
