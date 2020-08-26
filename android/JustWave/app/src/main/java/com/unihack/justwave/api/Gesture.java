package com.unihack.justwave.api;

public enum Gesture {

    OK("ok"),
    THUMBS_UP("thumbs_up"),
    THUMBS_DOWN("thumbs_down");

    private String action;

    private Gesture(String action){
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
