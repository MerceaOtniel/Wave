package com.spotify.sdk.demo.api;

import java.util.List;

public class LastTime {

    private volatile List<Gesture> lastGestures;
    private long lastTime;

    public LastTime(List<Gesture> lastGestures) {
        this.lastGestures = lastGestures;
    }

    public List<Gesture> getLastGesture() {
        return lastGestures;
    }

    public void setLastGesture(List<Gesture> lastGestures) {
        this.lastGestures = lastGestures;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
