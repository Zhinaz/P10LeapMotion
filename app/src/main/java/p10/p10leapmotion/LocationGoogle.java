package p10.p10leapmotion;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static p10.p10leapmotion.MainActivity.LAST_LOCATION_LATITUDE;
import static p10.p10leapmotion.MainActivity.LAST_LOCATION_LONGITUDE;
import static p10.p10leapmotion.MainActivity.LAST_LOCATION_SPEED;
import static p10.p10leapmotion.MainActivity.LOCATION_CHANGED;

public class LocationGoogle implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // LocationGoogle updates intervals in sec
    private static int UPDATE_INTERVAL = 80000; // 10 sec
    private static int FATEST_INTERVAL = 4000; // 5 sec
    private static int DISPLACEMENT = 5; // 10 meters

    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    public android.location.Location mLastLocation;
    public boolean mRequestingLocationUpdates = false;

    private Context mContext;
    private Activity mActivity;

    public LocationGoogle(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    public void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    public void startLocationUpdates() {
        mRequestingLocationUpdates = true;
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else if (mGoogleApiClient.isConnecting()) {
            Log.w("FeedbackApp", "GoogleApiClient is NOT connecting");
            Toast.makeText(mContext.getApplicationContext(), "GoogleApiClient is NOT connecting", Toast.LENGTH_LONG).show();
        } else {
            Log.w("FeedbackApp", "GoogleApiClient is NOT connected");
            Toast.makeText(mContext.getApplicationContext(), "GoogleApiClient is NOT connected", Toast.LENGTH_LONG).show();
        }
    }

    public void stopLocationUpdates() {
        mRequestingLocationUpdates = false;
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
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
                } else {
                    System.out.print("LastLocation is Null!");
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
        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
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
