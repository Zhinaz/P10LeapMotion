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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
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
    }

    public void onDataSetChanged() {
        int i = 0;
        int n = mapData.size();

        for (SegmentData s : mapData) {

            LatLng tempStart = new LatLng(s.getStartLocation().getLatitude(), s.getStartLocation().getLongitude());
            LatLng tempEnd = new LatLng(s.getEndLocation().getLatitude(), s.getEndLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(tempStart));

            if (i == n - 1) {
                mMap.addMarker(new MarkerOptions().position(tempEnd));

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(tempEnd.latitude, tempEnd.longitude));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }

            if (s.getAttentiveState().equals(GOOD)) {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(tempStart, tempEnd)
                        .width(7)
                        .color(Color.GREEN));
            } else if (s.getAttentiveState().equals(NEUTRAL)) {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(tempStart, tempEnd)
                        .width(7)
                        .color(Color.YELLOW));
            } else {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(tempStart, tempEnd)
                        .width(7)
                        .color(Color.RED));
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
                        int leftSteeringCounter = 0;
                        int leftRestCounter = 0;
                        int leftCannotSee = 0;
                        int leftMax = 0;

                        int rightSteeringCounter = 0;
                        int rightRestCounter = 0;
                        int rightSecondaryCounter = 0;
                        int rightGearCounter = 0;
                        int rightCannotSee = 0;
                        int rightMax = 0;

                        float scoreAvg = 0;
                        float distance = 0;
                        float speedAvg = 0;

                        for (SegmentData segment : mapData) {
                            for (String str : segment.getLeftPredStates()) {
                                if (str.equals("1.0")) {
                                    leftSteeringCounter++;
                                } else if (str.equals("2.0")) {
                                    leftRestCounter++;
                                } else if (str.equals("-1.0")) {
                                    leftCannotSee++;
                                }
                                leftMax++;
                            }

                            for (String str : segment.getRightPredStates()) {
                                if (str.equals("1.0")) {
                                    rightSteeringCounter++;
                                } else if (str.equals("2.0")) {
                                    rightRestCounter++;
                                } else if (str.equals("3.0")) {
                                    rightSteeringCounter++;
                                } else if (str.equals("4.0")) {
                                    rightGearCounter++;
                                } else if (str.equals("-1.0")) {
                                    rightCannotSee++;
                                }
                                rightMax++;
                            }

                            scoreAvg = scoreAvg + segment.getScore();
                            distance = distance + segment.getDistance();
                            speedAvg = speedAvg + segment.getSpeed();
                        }

                        scoreAvg = scoreAvg / mapData.size();
                        speedAvg = speedAvg / mapData.size();

                        String startString = "";

                        if (scoreAvg >= 80) {
                            startString = "Good job!";
                        } else if (scoreAvg >= 60) {
                            startString = "Could be better!";
                        } else {
                            startString = "You need to improve those hand positions!";
                        }

                        String dataString = "Average score: \t\t\t\t" + (int) scoreAvg + "\n"
                                + "Average speed: \t\t\t\t" + (int) speedAvg + "\n"
                                + "Total distance: \t\t\t\t\t" + (int) distance + "\n\n"
                                + "Left: \n"
                                + "Steering: \t\t\t\t\t" + leftSteeringCounter + "/" + leftMax + "\n"
                                + "Rest: \t\t\t\t\t\t\t\t" + leftRestCounter + "/" + leftMax + "\n"
                                + "Cannot see hand: \t\t\t" + leftCannotSee + "/" + leftMax + "\n"
                                + "Not accurate enough: \t" + (leftMax - (leftSteeringCounter + leftRestCounter + leftCannotSee)) + "/" + leftMax + "\n"
                                + "\n\n" + "Right: \n"
                                + "Steering: \t\t\t\t\t" + rightSteeringCounter + "/" + rightMax + "\n"
                                + "Rest: \t\t\t\t\t\t\t\t" + rightRestCounter + "/" + rightMax + "\n"
                                + "Secondary: \t\t\t" + rightSecondaryCounter + "/" + rightMax + "\n"
                                + "Gear: \t\t\t\t\t\t\t\t" + rightGearCounter + "/" + rightMax + "\n"
                                + "Cannot see hand: \t\t\t" + rightCannotSee + "/" + rightMax + "\n"
                                + "Not accurate enough: \t" + (rightMax - (rightSteeringCounter + rightRestCounter + rightSecondaryCounter + rightGearCounter + rightCannotSee)) + "/" + rightMax + "\n";

                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder
                                .setTitle(R.string.show_information_dialog_title)
                                .setMessage("\n" + startString + "\n\n" + dataString)
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
                        if (Float.parseFloat(temp[3]) != Float.POSITIVE_INFINITY) {
                            speed = Float.parseFloat(temp[3]);
                        } else {
                            speed = 0.0f;
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

                        SegmentData tempSegment = new SegmentData(startLocation, endLocation, attentiveList, rightPredictions, leftPredictions, speed, score, dist, attentiveState);
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
