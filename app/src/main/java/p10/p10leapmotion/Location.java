package p10.p10leapmotion;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static p10.p10leapmotion.MainActivity.LAST_LOCATION_LATITUDE;
import static p10.p10leapmotion.MainActivity.LAST_LOCATION_LONGITUDE;
import static p10.p10leapmotion.MainActivity.LAST_LOCATION_SPEED;
import static p10.p10leapmotion.MainActivity.LOCATION_CHANGED;

public class Location implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Location updates intervals in sec
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;
    boolean mRequestingLocationUpdates = false;

    private Context mContext;
    private Activity mActivity;

    public Location(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;

        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        createLocationRequest();
    }

    protected void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(mContext.getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
                mActivity.finish();
            }
            return false;
        }
        return true;
    }

    public void togglePeriodicLocationUpdates() {
        // Used to be a toggle button
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();

        } else {
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    public void displayLocation() {

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    } else {
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (mLastLocation != null) {
                    Intent notifyIntent = new Intent(LOCATION_CHANGED);
                    notifyIntent.putExtra(LAST_LOCATION_SPEED, mLastLocation.getSpeed());
                    notifyIntent.putExtra(LAST_LOCATION_LATITUDE, mLastLocation.getLatitude());
                    notifyIntent.putExtra(LAST_LOCATION_LONGITUDE, mLastLocation.getLongitude());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(notifyIntent);
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle arg0) {
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) { }

    @Override
    public void onLocationChanged(android.location.Location location) {
        // Assign the new location
        mLastLocation = location;

        // Displaying the new location on UI
        displayLocation();
        // Broadcast to listener
    }
}
