package p10.p10leapmotion;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static p10.p10leapmotion.MainActivity.GOOD;
import static p10.p10leapmotion.MainActivity.NEUTRAL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<SegmentData> mapData = new ArrayList<>();
    private ArrayList<Marker> markerList = new ArrayList<>();
    private Button btn_markers;
    private Button btn_dataset;
    private Button btn_between;
    private Boolean markersVisible = true;

    File root;
    private boolean secondMarkerFeature = false;
    private boolean firstSelected = false;
    private int firstSelection = 0;

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

        btn_markers = (Button) findViewById(R.id.btn_markers);
        btn_markers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!markersVisible) {
                    for (Marker mark : markerList) {
                        mark.setVisible(true);
                        markersVisible = true;
                    }
                } else {
                    for (Marker mark : markerList) {
                        mark.setVisible(false);
                        markersVisible = false;
                    }
                }
            }
        });

        btn_dataset = (Button) findViewById(R.id.btn_dataset);
        btn_dataset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDatasetDialog();
            }
        });

        btn_between = (Button) findViewById(R.id.btn_between);
        btn_between.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondMarkerFeature = true;
                Toast.makeText(MapsActivity.this, "Select the first marker", Toast.LENGTH_SHORT).show();
            }
        });

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

            Marker tempMark = mMap.addMarker(new MarkerOptions().position(tempStart));
            markerList.add(tempMark);

            if (i == n - 1) {
                mMap.addMarker(new MarkerOptions().position(tempEnd).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(tempEnd.latitude, tempEnd.longitude));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }

            switch (s.getAttentiveState()) {
                case GOOD: mMap.addPolyline(new PolylineOptions()
                        .add(tempStart, tempEnd)
                        .width(10)
                        .color(Color.GREEN));
                    break;
                case NEUTRAL: mMap.addPolyline(new PolylineOptions()
                        .add(tempStart, tempEnd)
                        .width(10)
                        .color(Color.YELLOW));
                    break;
                default: mMap.addPolyline(new PolylineOptions()
                        .add(tempStart, tempEnd)
                        .width(10)
                        .color(Color.RED));
                    break;
            }

            i++;
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng markerLocation = marker.getPosition();
                int counter = 0;
                for (SegmentData s : mapData) {

                    // Between two markers, second selection
                    if (firstSelected && markerLocation.latitude == s.getStartLocation().getLatitude() && markerLocation.longitude == s.getStartLocation().getLongitude()) {
                        openDetailsDialog(calculateDialogString(firstSelection, counter));

                        firstSelected = false;
                        secondMarkerFeature = false;
                        firstSelection = 0;
                        break;
                    }
                    // Between two markers, first selection
                    else if (secondMarkerFeature && markerLocation.latitude == s.getStartLocation().getLatitude() && markerLocation.longitude == s.getStartLocation().getLongitude()) {
                        firstSelection = counter;
                        Toast.makeText(MapsActivity.this, "Choose second marker", Toast.LENGTH_LONG).show();
                        firstSelected = true;
                        break;
                    }
                    // Single marker selection
                    else if (markerLocation.latitude == s.getStartLocation().getLatitude() && markerLocation.longitude == s.getStartLocation().getLongitude()) {
                        openDetailsDialog(calculateDialogString(counter, counter + 1) + "\nNumber: " + (counter + 1));
                        break;
                    }
                    // Last marker
                    else if (s == mapData.get(mapData.size() - 1)) {
                        openDetailsDialog(calculateDialogString(0, mapData.size()));
                        break;
                    }
                    counter++;
                }

                return true;
            }
        });
    }

    private void openDetailsDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder
                .setTitle(R.string.show_information_dialog_title)
                .setMessage(str)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private String calculatePercentage(int value, int total) {
        float result = ((float) value / (float) total) * 100;
        return String.valueOf(new DecimalFormat("##.#").format(result)) + "%";
    }

    private String calculateDialogString(int firstIndex, int secondIndex) {
        List<SegmentData> tempData = mapData.subList(firstIndex, secondIndex);

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

        for (SegmentData segment : tempData) {
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
                    rightSecondaryCounter++;
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

        scoreAvg = scoreAvg / tempData.size();
        speedAvg = speedAvg / tempData.size();

        String startString;

        if (scoreAvg >= 80) {
            startString = "Good job!";
        } else if (scoreAvg >= 60) {
            startString = "Could be better!";
        } else {
            startString = "You need to improve those hand positions!";
        }

        return startString + "\n\n"
                + "Average score: \t\t\t\t" + (int) scoreAvg + "\n"
                + "Average speed: \t\t\t\t" + (int) speedAvg + " km/t \n"
                + "Total distance: \t\t\t\t" + (int) distance + " meters \n\n"
                + "Left: \n"
                + "Steering: \t\t\t\t\t" + leftSteeringCounter + "/" + leftMax + "\t\t\t\t" + calculatePercentage(leftSteeringCounter, leftMax) + "\n"
                + "Rest: \t\t\t\t\t\t\t\t" + leftRestCounter + "/" + leftMax + "\t\t\t\t\t\t" + calculatePercentage(leftRestCounter, leftMax) + "\n"
                + "Cannot see: \t\t" + leftCannotSee + "/" + leftMax + "\t\t\t\t\t\t" + calculatePercentage(leftCannotSee, leftMax) + "\n"
                + "Not accurate: \t" + (leftMax - (leftSteeringCounter + leftRestCounter + leftCannotSee)) + "/" + leftMax + "\t\t\t\t\t\t" + calculatePercentage((leftMax - (leftSteeringCounter + leftRestCounter + leftCannotSee)), leftMax) + "\n"
                + "\n\n" + "Right: \n"
                + "Steering: \t\t\t\t\t" + rightSteeringCounter + "/" + rightMax + "\t\t\t\t" + calculatePercentage(rightSteeringCounter, rightMax) + "\n"
                + "Rest: \t\t\t\t\t\t\t\t" + rightRestCounter + "/" + rightMax + "\t\t\t\t" + calculatePercentage(rightRestCounter, rightMax) + "\n"
                + "Secondary: \t\t\t" + rightSecondaryCounter + "/" + rightMax + "\t\t\t\t\t\t" + calculatePercentage(rightSecondaryCounter, rightMax) + "\n"
                + "Gear: \t\t\t\t\t\t\t\t" + rightGearCounter + "/" + rightMax + "\t\t\t\t" + calculatePercentage(rightGearCounter, rightMax) + "\n"
                + "Cannot see: \t\t" + rightCannotSee + "/" + rightMax + "\t\t\t\t" + calculatePercentage(rightCannotSee, rightMax) + "\n"
                + "Not accurate: \t" + (rightMax - (rightSteeringCounter + rightRestCounter + rightSecondaryCounter + rightGearCounter + rightCannotSee)) + "/" + rightMax + "\t\t\t\t\t\t" + calculatePercentage((rightMax - (rightSteeringCounter + rightRestCounter + rightSecondaryCounter + rightGearCounter + rightCannotSee)), rightMax) + "\n"
                + calculateHandsOnWheel(firstIndex, secondIndex);
    }

    public String calculateHandsOnWheel(int startIndex, int endIndex) {
        List<SegmentData> tempData = mapData.subList(startIndex, endIndex);

        String temp = "";
        int noHandsCounter = 0;

        for (SegmentData segmentData : tempData) {

            for (int i = 0; i < segmentData.getLeftPredStates().size(); i++) {
                if (segmentData.getLeftPredStates().get(i).equals("2.0") && (segmentData.getRightPredStates().get(i).equals("2.0") || segmentData.getRightPredStates().get(i).equals("3.0"))) {
                    noHandsCounter++;
                }
            }
        }
        if (noHandsCounter <= 4) {
            temp = "\nNo hands: \t\t\t" + noHandsCounter + " times\t\t\t\t~ " + String.valueOf(new DecimalFormat("##.#").format((float) noHandsCounter * 0.25)) + "second";
        } else if (noHandsCounter > 4) {
            temp = "\nNo hands: \t\t\t" + noHandsCounter + " times\t\t\t\t~ " + String.valueOf(new DecimalFormat("##.#").format((float) noHandsCounter * 0.25)) + "seconds";
        }

        return temp;
    }

    // Create dialog to choose bluetooth device - Only paired devices show up!
    public Dialog chooseDatasetDialog() {
        final ArrayList<String> tempList = new ArrayList<>();

        for (File f : root.listFiles()) {
            tempList.add(f.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose dataset");
        builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, tempList), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                markerList = new ArrayList<>();
                markersVisible = true;
                mMap.clear();

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
        // Example data
        //57.0,9.9 58.0,10.9 126478.29 Infinity GOOD 100.0 (ATTENTIVE,ATTENTIVE,ATTENTIVE,ATTENTIVE,ATTENTIVE) (1.0,1.0,2.0,2.0,3.0) (1.0,1.0,1.0,1.0,2.0)
        //58.0,10.9 59.0,11.9 125720.805 Infinity NEUTRAL 60.000004 (ATTENTIVE,ATTENTIVE,ATTENTIVE,INATTENTIVE,INATTENTIVE) (1.0,4.0,3.0,3.0,3.0) (1.0,2.0,2.0,2.0,2.0)

        for (File f : root.listFiles()) {
            if (f.getName().equals(fileName)) {

                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String line;

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
                        float speed;
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
                        attentiveList.addAll(Arrays.asList(attentiveness));

                        // right list
                        String[] rightPreds = temp[7].replace("(", "").replace(")", "").split(",");
                        ArrayList<String> rightPredictions = new ArrayList<>();
                        rightPredictions.addAll(Arrays.asList(rightPreds));

                        // left list
                        String[] leftPreds = temp[8].replace("(", "").replace(")", "").split(",");
                        ArrayList<String> leftPredictions = new ArrayList<>();
                        leftPredictions.addAll(Arrays.asList(leftPreds));

                        SegmentData tempSegment = new SegmentData(startLocation, endLocation, attentiveList, rightPredictions, leftPredictions, speed, score, dist, attentiveState);
                        tempList.add(tempSegment);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return tempList;
    }
}
