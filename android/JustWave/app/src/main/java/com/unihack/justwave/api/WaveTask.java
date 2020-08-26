package com.unihack.justwave.api;

import android.os.AsyncTask;

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

    public void run() {
        while(true){
            new GetWaveRequest().execute("http://");
        }
    }
}
