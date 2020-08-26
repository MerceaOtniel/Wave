package com.unihack.justwave.api;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetWaveRequest extends AsyncTask<String, Integer, String> {
    public String doInBackground(String... urls) {
        String content = "";
        URL url = null;
        try {
            url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public void onProgressUpdate(Integer... progress) {
        //do something
    }

    public void onPostExecute(String result) {
        // this is executed on the main thread after the process is over
        // update your UI here
        Gesture gesture = Gesture.valueOf(result);
        switch (gesture) {
            case OK:
                break;
            case THUMBS_UP:
                break;
            case THUMBS_DOWN:
                break;
        }
        //displayMessage(result);
    }
}
