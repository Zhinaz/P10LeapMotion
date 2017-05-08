package p10.p10leapmotion;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static p10.p10leapmotion.MainActivity.ATTENTIVE;
import static p10.p10leapmotion.MainActivity.GOOD;
import static p10.p10leapmotion.MainActivity.INATTENTIVE;
import static p10.p10leapmotion.MainActivity.NEUTRAL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<SegmentData> mapData = new ArrayList<>();

    File root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        root = (File) intent.getExtras().get("root");

        chooseDatasetDialog();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
/*
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
        attentivePredictedStates2.add(ATTENTIVE);
        attentivePredictedStates2.add(ATTENTIVE);
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
        SegmentData tempData2 = new SegmentData(loc2, loc3, attentivePredictedStates2, rightPredictedStates2, leftPredictedStates2);

        mapData.add(tempData);
        mapData.add(tempData2);
*/
        int i = 0;
        int n = mapData.size();

        PolylineOptions optionsGreen = new PolylineOptions();
        optionsGreen.width(5);
        optionsGreen.visible(true);
        optionsGreen.color(Color.GREEN);
        PolylineOptions optionsYellow = new PolylineOptions();
        optionsYellow.width(5);
        optionsYellow.visible(true);
        optionsYellow.color(Color.YELLOW);
        PolylineOptions optionsRed = new PolylineOptions();
        optionsRed.width(5);
        optionsRed.visible(true);
        optionsRed.color(Color.RED);

        for (SegmentData s : mapData) {

            LatLng tempStart = new LatLng(s.getStartLocation().getLatitude(), s.getStartLocation().getLongitude());
            LatLng tempEnd = new LatLng(s.getEndLocation().getLatitude(), s.getEndLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(tempStart));

            if (i == n - 1) {
                mMap.addMarker(new MarkerOptions().position(tempEnd));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(tempEnd));
            }

            if (s.getAttentiveState().equals(GOOD)) {
                optionsGreen.add(tempStart);
                optionsGreen.add(tempEnd);
            } else if (s.getAttentiveState().equals(NEUTRAL)) {
                optionsYellow.add(tempStart);
                optionsYellow.add(tempEnd);
            } else {
                optionsRed.add(tempStart);
                optionsRed.add(tempEnd);
            }

            i++;
        }

        mMap.addPolyline(optionsGreen);
        mMap.addPolyline(optionsYellow);
        mMap.addPolyline(optionsRed);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng markerLocation = marker.getPosition();
                for (SegmentData s : mapData) {
                    if (markerLocation.latitude == s.getStartLocation().getLatitude() && markerLocation.longitude == s.getStartLocation().getLongitude()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder
                                .setTitle(R.string.show_information_dialog_title)
                                .setMessage("\n" + s.scoreString() + "\n\n" + s.additionalDataString())
                                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                        break;
                    } else if (s == mapData.get(mapData.size() - 1)) {
                        Toast.makeText(MapsActivity.this, "Last location have no value!", Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });
    }

    public void onDataSetChanged() {
        int i = 0;
        int n = mapData.size();

        PolylineOptions optionsGreen = new PolylineOptions();
        optionsGreen.width(5);
        optionsGreen.visible(true);
        optionsGreen.color(Color.GREEN);
        PolylineOptions optionsYellow = new PolylineOptions();
        optionsYellow.width(5);
        optionsYellow.visible(true);
        optionsYellow.color(Color.YELLOW);
        PolylineOptions optionsRed = new PolylineOptions();
        optionsRed.width(5);
        optionsRed.visible(true);
        optionsRed.color(Color.RED);

        for (SegmentData s : mapData) {

            LatLng tempStart = new LatLng(s.getStartLocation().getLatitude(), s.getStartLocation().getLongitude());
            LatLng tempEnd = new LatLng(s.getEndLocation().getLatitude(), s.getEndLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(tempStart));

            if (i == n - 1) {
                mMap.addMarker(new MarkerOptions().position(tempEnd));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(tempEnd));
            }

            if (s.getAttentiveState().equals(GOOD)) {
                optionsGreen.add(tempStart);
                optionsGreen.add(tempEnd);
            } else if (s.getAttentiveState().equals(NEUTRAL)) {
                optionsYellow.add(tempStart);
                optionsYellow.add(tempEnd);
            } else {
                optionsRed.add(tempStart);
                optionsRed.add(tempEnd);
            }

            i++;
        }

        mMap.addPolyline(optionsGreen);
        mMap.addPolyline(optionsYellow);
        mMap.addPolyline(optionsRed);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng markerLocation = marker.getPosition();
                for (SegmentData s : mapData) {
                    if (markerLocation.latitude == s.getStartLocation().getLatitude() && markerLocation.longitude == s.getStartLocation().getLongitude()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder
                                .setTitle(R.string.show_information_dialog_title)
                                .setMessage("\n" + s.scoreString() + "\n\n" + s.additionalDataString())
                                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                        break;
                    } else if (s == mapData.get(mapData.size() - 1)) {
                        Toast.makeText(MapsActivity.this, "Last location have no value!", Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });
    }

    // Create dialog to choose bluetooth device - Only paired devices show up!
    public Dialog chooseDatasetDialog() {
        final ArrayList<String> tempList = new ArrayList<>();

        for (File f : root.listFiles()) {
            tempList.add(f.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose dataset");
        builder.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tempList), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mapData = readSelectedFile(tempList.get(i));
                onDataSetChanged();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return builder.create();
    }

    private ArrayList<SegmentData> readSelectedFile(String fileName) {
        ArrayList<SegmentData> tempList = new ArrayList<>();
        //57.0,9.9 58.0,10.9 126478.29 Infinity GOOD 100.0 (ATTENTIVE,ATTENTIVE,ATTENTIVE,ATTENTIVE,ATTENTIVE) (1.0,1.0,2.0,2.0,3.0) (1.0,1.0,1.0,1.0,2.0)
        //58.0,10.9 59.0,11.9 125720.805 Infinity NEUTRAL 60.000004 (ATTENTIVE,ATTENTIVE,ATTENTIVE,INATTENTIVE,INATTENTIVE) (1.0,4.0,3.0,3.0,3.0) (1.0,2.0,2.0,2.0,2.0)

        for (File f : root.listFiles()) {
            if (f.getName().equals(fileName)) {

                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        String[] temp = line.split(" ");
                        System.out.println(line);

                        // Start location
                        String[] startLoc = temp[0].split(",");
                        Location startLocation = new Location("Temp");
                        startLocation.setLatitude(Double.parseDouble(startLoc[0]));
                        startLocation.setLongitude(Double.parseDouble(startLoc[1]));

                        // End location
                        String[] endLoc = temp[1].split(",");
                        Location endLocation = new Location("Temp");
                        endLocation.setLatitude(Double.parseDouble(endLoc[0]));
                        endLocation.setLongitude(Double.parseDouble(endLoc[1]));

                        // distance and speed
                        float dist = Float.parseFloat(temp[2]);
                        float speed = 0.0f;
                        if (!temp[3].equals("Infinity")) {
                            speed = Float.parseFloat(temp[3]);
                        }

                        // Attentive state and score
                        String attentiveState = temp[4];
                        float score = Float.parseFloat(temp[5]);

                        // Attentive list
                        String[] attentiveness = temp[6].replace("(", "").replace(")", "").split(",");
                        ArrayList<String> attentiveList = new ArrayList<>();
                        for (String s : attentiveness) {
                            attentiveList.add(s);
                        }

                        // right list
                        String[] rightPreds = temp[7].replace("(", "").replace(")", "").split(",");
                        ArrayList<String> rightPredictions = new ArrayList<>();
                        for (String s : rightPreds) {
                            rightPredictions.add(s);
                        }

                        // left list
                        String[] leftPreds = temp[8].replace("(", "").replace(")", "").split(",");
                        ArrayList<String> leftPredictions = new ArrayList<>();
                        for (String s : leftPreds) {
                            leftPredictions.add(s);
                        }

                        SegmentData tempSegment = new SegmentData(startLocation, endLocation, attentiveList, rightPredictions, leftPredictions);
                        tempList.add(tempSegment);
                    }
                    br.close();
                } catch (IOException e) {
                    //You'll need to add proper error handling here
                }
            }
        }

        return tempList;
    }

}
