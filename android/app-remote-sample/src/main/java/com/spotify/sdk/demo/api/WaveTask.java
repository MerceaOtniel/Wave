package com.spotify.sdk.demo.api;

import android.os.AsyncTask;

import com.spotify.sdk.demo.RemotePlayerActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class WaveTask extends TimerTask {

    private RemotePlayerActivity activity;

    public WaveTask(RemotePlayerActivity activity){
        this.activity = activity;
    }

    public void run() {
        new GetWaveRequest(activity).execute("http://localhost:5000");
    }
}
