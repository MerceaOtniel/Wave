package com.spotify.sdk.demo.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Gesture {

    OTHER_THINGS("Doing other things"),
    DRUMMING_FINGERS("Drumming Fingers"),
    NO_GESTURE("No gesture"),
    PULL_HAND_IN("Pulling Hand In"),
    PULL_TWO_FINGERS_IN("Pulling Two Fingers In"),
    PUSH_HAND_AWAY("Pushing Hand Away"),
    PUSH_TWO_FINGERS_AWAY("Pushing Two Fingers Away"),
    ROLL_HAND_BACKWARD("Rolling Hand Backward"),
    ROLL_HAND_FORWARD("Rolling Hand Forward"),
    SHAKING_HAND("Shaking Hand"),
    SLIDE_TWO_FINGER_DOWN("Sliding Two Fingers Down"),
    SLIDE_TWO_FINGER_LEFT("Sliding Two Fingers Left"),
    SLIDE_TWO_FINGER_RIGHT("Sliding Two Fingers Right"),
    SLIDE_TWO_FINGER_UP("Sliding Two Fingers Up"),
    STOP("Stop Sign"),
    SWIPE_DOWN("Swiping Down"),
    SWIPE_LEFT("Swiping Left"),
    SWIPE_RIGHT("Swiping Right"),
    SWIPE_UP("Swiping Up"),
    THUMB_DOWN("Thumb Down"),
    THUMB_UP("Thumb Up"),
    TURN_HAND_CLOCKWISE("Turning Hand Clockwise"),
    TURN_HAND_COUNTERCLOCKWISE("Turning Hand Counterclockwise"),
    ZOOM_IN_FULL_HAND("Zooming In With Full Hand"),
    ZOOM_IN_TWO_FINGERS("Zooming In With Two Fingers"),
    ZOOM_OUT_FULL_HAND("Zooming Out With Full Hand"),
    ZOOM_OUT_TWO_FINGERS("Zooming Out With Two Fingers");

    public static volatile String LAST_ACTION = "";
    public static volatile long thumbUpLastTimeStamp;
    public static volatile long slideTwoFingerDownLastTimeStamp;
    public static volatile long slideTwoFingerUpLastTimeStamp;
    public static volatile long slideTwoFingerLeftLastTimeStamp;
    public static volatile long slideTwoFingerRightLastTimeStamp;
    public static volatile long swipeLeftTimeStamp;
    public static volatile long swipeRightTimeStamp;
    public static volatile long swipeUpTimeStamp;
    public static volatile long swipeDownTimeStamp;
    public static volatile long muteTimeStamp;

    public static volatile Map<Gesture, LastTime> lastTimeMap = new HashMap<Gesture, LastTime>();

    static {
        List lst1 = new ArrayList<>();
        lst1.add(SLIDE_TWO_FINGER_UP);
        lst1.add(SWIPE_UP);
        lastTimeMap.put(SLIDE_TWO_FINGER_DOWN, new LastTime(lst1));
        lastTimeMap.put(SWIPE_DOWN, new LastTime(lst1));
        List lst2 = new ArrayList<>();
        lst2.add(SLIDE_TWO_FINGER_DOWN);
        lst2.add(SWIPE_DOWN);
        lastTimeMap.put(SLIDE_TWO_FINGER_UP, new LastTime(lst2));
        lastTimeMap.put(SWIPE_UP, new LastTime(lst2));
        List lst3 = new ArrayList<>();
        lst3.add(SWIPE_LEFT);
        lastTimeMap.put(SWIPE_RIGHT, new LastTime(lst3));
        List lst4 = new ArrayList<>();
        lst4.add(SWIPE_RIGHT);
        lastTimeMap.put(SWIPE_LEFT, new LastTime(lst4));
        List lst5 = new ArrayList<>();
        lst5.add(SLIDE_TWO_FINGER_LEFT);
        lastTimeMap.put(SLIDE_TWO_FINGER_RIGHT, new LastTime(lst5));
        List lst6 = new ArrayList<>();
        lst6.add(SLIDE_TWO_FINGER_LEFT);
        lastTimeMap.put(SLIDE_TWO_FINGER_RIGHT, new LastTime(lst6));
    }

    public static LastTime getLastTime(Gesture gesture){
        return lastTimeMap.get(gesture);
    }

    private String action;

    private Gesture(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public static Gesture value(String value){
        for (Gesture gesture : Gesture.values()) {
            if(gesture.getAction().equals(value)){
                return gesture;
            }
        }
        return Gesture.valueOf(value);
    }
}
