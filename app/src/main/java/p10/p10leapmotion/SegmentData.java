package p10.p10leapmotion;

import android.location.Location;

import java.util.ArrayList;

import static p10.p10leapmotion.MainActivity.ATTENTIVE;
import static p10.p10leapmotion.MainActivity.INATTENTIVE;

public class SegmentData {
    private Location startLocation;
    private Location endLocation;
    private float speed;
    private String attentiveState;
    private float score;
    private ArrayList<String> attentivePredStates = new ArrayList<>();
    private ArrayList<String> rightPredStates = new ArrayList<>();
    private ArrayList<String> leftPredStates = new ArrayList<>();

    public SegmentData(Location startLocation, Location endLocation, ArrayList<String> attentivePredStates, ArrayList<String> rightPredStates, ArrayList<String> leftPredStates) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.attentivePredStates = attentivePredStates;
        this.rightPredStates = rightPredStates;
        this.leftPredStates = leftPredStates;

        this.speed = calculateSpeed(startLocation, endLocation);
        this.score = calculateScore(attentivePredStates);
    }

    private float calculateSpeed(Location startLocation, Location endLocation) {
        double timeDiff = startLocation.getTime() - endLocation.getTime();
        double distDiff = startLocation.distanceTo(endLocation);

        return (float) (distDiff / timeDiff * 3600);
    }

    private float calculateScore(ArrayList<String> attentivePredStates) {
        float totalStates = attentivePredStates.size();
        float attentiveStates = 0;

        if (totalStates > 0) {
            for (String state : attentivePredStates) {
                if (state.equals(ATTENTIVE)) {
                    attentiveStates++;
                }
            }
            return (attentiveStates / totalStates) * 100;
        }
        return 0;
    }

    private String calculateAttentiveState(float score) {
        if (score >= 80) {
            return "kæft det lækkert";
        } else if (score >= 60) {
            return "Decent sager";
        } else if (score >= 40) {
            
        }

        return "";
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public Float getSpeed() {
        return speed;
    }

    public String getAttentiveState() {
        return attentiveState;
    }

    public float getScore() {
        return score;
    }

    public ArrayList<String> getAttentivePredStates() {
        return attentivePredStates;
    }

    public ArrayList<String> getRightPredStates() {
        return rightPredStates;
    }

    public ArrayList<String> getLeftPredStates() {
        return leftPredStates;
    }
}
