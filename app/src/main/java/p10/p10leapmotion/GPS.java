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

    public LocalBroadcastManager broadcaster;
    public Boolean requestingLocations = false;
    private Context mContext;
    private Criteria criteria;
    private Location location;
    private LocationManager locationManager;
    private String provider;

    public GPS(Context context) {
        mContext = context;

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);
        broadcaster = LocalBroadcastManager.getInstance(mContext);

        if (location != null) {
            onLocationChanged(location);
        }
    }

    public void requestUpdates() {
        requestingLocations = true;
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    public void stopUpdates() {
        requestingLocations = false;
        locationManager.removeUpdates(this);
    }

    public void updateDisplay(Location location) {
        Intent notifyIntent = new Intent(LOCATION_CHANGED);
        notifyIntent.putExtra(LAST_LOCATION_SPEED, location.getSpeed());
        notifyIntent.putExtra(LAST_LOCATION_LATITUDE, location.getLatitude());
        notifyIntent.putExtra(LAST_LOCATION_LONGITUDE, location.getLongitude());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(notifyIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
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
