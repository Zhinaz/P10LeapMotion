package p10.p10leapmotion;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class RouteData {
    private Location startLocation;
    private Location endLocation;
    private Float speed;
    private String attentiveState;
    private ArrayList<String> attentivePredStates;
    private ArrayList<String> rightPredStates;
    private ArrayList<String> leftPredStates;

    // Start Location
    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    // End Location
    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    // Speed
    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    // AttentiveState
    public String getAttentiveState() {
        return attentiveState;
    }

    public void setAttentiveState(String attentiveState) {
        this.attentiveState = attentiveState;
    }

    // Attentive Predicted States
    public ArrayList<String> getAttentivePredStates() {
        return attentivePredStates;
    }

    public void setAttentivePredStates(ArrayList<String> attentivePredStates) {
        this.attentivePredStates = attentivePredStates;
    }

    // Right Hand Predicted States
    public ArrayList<String> getRightPredStates() {
        return rightPredStates;
    }

    public void setRightPredStates(ArrayList<String> rightPredStates) {
        this.rightPredStates = rightPredStates;
    }

    // Left Hand Predicted States
    public ArrayList<String> getLeftPredStates() {
        return leftPredStates;
    }

    public void setLeftPredStates(ArrayList<String> leftPredStates) {
        this.leftPredStates = leftPredStates;
    }
}
