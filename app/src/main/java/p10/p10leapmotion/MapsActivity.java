package p10.p10leapmotion;

import android.content.DialogInterface;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static p10.p10leapmotion.MainActivity.ATTENTIVE;
import static p10.p10leapmotion.MainActivity.INATTENTIVE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<SegmentData> mapData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Location loc = new Location("Dummy variable");
        loc.setLatitude(57);
        loc.setLongitude(9.9);
        Location loc2 = new Location("Dummy variable 2");
        loc2.setLatitude(58);
        loc2.setLongitude(10.9);
        ArrayList<String> attentivePredictedStates = new ArrayList<>();
        attentivePredictedStates.add(ATTENTIVE);
        attentivePredictedStates.add(ATTENTIVE);
        attentivePredictedStates.add(ATTENTIVE);
        attentivePredictedStates.add(ATTENTIVE);
        attentivePredictedStates.add(ATTENTIVE);
        ArrayList<String> rightPredictedStates = new ArrayList<>();
        rightPredictedStates.add("1.0");
        rightPredictedStates.add("1.0");
        rightPredictedStates.add("2.0");
        rightPredictedStates.add("2.0");
        rightPredictedStates.add("3.0");
        ArrayList<String> leftPredictedStates = new ArrayList<>();
        leftPredictedStates.add("1.0");
        leftPredictedStates.add("1.0");
        leftPredictedStates.add("1.0");
        leftPredictedStates.add("1.0");
        leftPredictedStates.add("2.0");
        SegmentData tempData = new SegmentData(loc, loc2, attentivePredictedStates, rightPredictedStates, leftPredictedStates);

        Location loc3 = new Location("Dummy variable 3");
        loc3.setLatitude(59);
        loc3.setLongitude(11.9);
        ArrayList<String> attentivePredictedStates2 = new ArrayList<>();
        attentivePredictedStates2.add(ATTENTIVE);
        attentivePredictedStates2.add(INATTENTIVE);
        attentivePredictedStates2.add(INATTENTIVE);
        attentivePredictedStates2.add(INATTENTIVE);
        attentivePredictedStates2.add(INATTENTIVE);
        ArrayList<String> rightPredictedStates2 = new ArrayList<>();
        rightPredictedStates2.add("1.0");
        rightPredictedStates2.add("4.0");
        rightPredictedStates2.add("3.0");
        rightPredictedStates2.add("3.0");
        rightPredictedStates2.add("3.0");
        ArrayList<String> leftPredictedStates2 = new ArrayList<>();
        leftPredictedStates2.add("1.0");
        leftPredictedStates2.add("2.0");
        leftPredictedStates2.add("2.0");
        leftPredictedStates2.add("2.0");
        leftPredictedStates2.add("2.0");
        SegmentData tempData2 = new SegmentData(loc2, loc3, attentivePredictedStates, rightPredictedStates, leftPredictedStates);

        mapData.add(tempData);
        mapData.add(tempData2);

        int i = 0;
        int n = mapData.size();

        for (SegmentData s : mapData) {

            LatLng temp = new LatLng(s.getStartLocation().getLatitude(), s.getStartLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(temp).title(s.toString()));

            if (i == n-1) {
                temp = new LatLng(s.getEndLocation().getLatitude(), s.getEndLocation().getLongitude());
                mMap.addMarker(new MarkerOptions().position(temp).title(s.toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
            }

            i++;
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng markerLocation = marker.getPosition();
                for (SegmentData s : mapData) {
                    if (markerLocation.latitude == s.getStartLocation().getLatitude() && markerLocation.longitude == s.getStartLocation().getLongitude()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder
                                .setTitle(R.string.show_information_dialog_title)
                                .setMessage(s.toString() + "\n" + s.additionalDataString())
                                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                        break;
                    }
                }

                return true;
            }
        });
    }
}
