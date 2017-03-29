package p10.p10leapmotion;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import static p10.p10leapmotion.MainActivity.LAST_LOCATION_LATITUDE;
import static p10.p10leapmotion.MainActivity.LAST_LOCATION_LONGITUDE;
import static p10.p10leapmotion.MainActivity.LAST_LOCATION_SPEED;
import static p10.p10leapmotion.MainActivity.LOCATION_CHANGED;

public class GPS implements LocationListener {

    private Context mContext;

    public void updateDisplay(Location location) {
        System.out.println("UpdateDisplay");
        Intent notifyIntent = new Intent(LOCATION_CHANGED);
        notifyIntent.putExtra(LAST_LOCATION_SPEED, location.getSpeed());
        notifyIntent.putExtra(LAST_LOCATION_LATITUDE, location.getLatitude());
        notifyIntent.putExtra(LAST_LOCATION_LONGITUDE, location.getLongitude());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(notifyIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("onLocationChanged");
        updateDisplay(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mContext, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mContext, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
