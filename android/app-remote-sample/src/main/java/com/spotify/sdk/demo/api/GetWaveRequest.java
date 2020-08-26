package com.spotify.sdk.demo.api;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.spotify.sdk.demo.RemotePlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class GetWaveRequest extends AsyncTask<String, Integer, String> {

    private RemotePlayerActivity activity;

    public GetWaveRequest(RemotePlayerActivity activity) {
        this.activity = activity;
    }

    public void onProgressUpdate(Integer... progress) {
        //do something
    }

    @Override
    protected String doInBackground(String... urls) {
        URL myurl = null;
        try {
            myurl = new URL(urls[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection connection = null;
        try {
            connection = myurl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        httpConnection.setRequestProperty("Content-Type", "application/json");

        int responseCode = -1;
        try {
            responseCode = httpConnection.getResponseCode();
        } catch (SocketTimeoutException ste) {
            ste.printStackTrace();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuilder answer = new StringBuilder(100000);

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String inputLine;

            try {
                while ((inputLine = in.readLine()) != null) {
                    answer.append(inputLine);
                    answer.append("\n");
                }
                String result = answer.toString();
                if(result != null && !result.isEmpty()){
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray jsonArray = jsonObj.getJSONArray("queue");
                    if(jsonArray != null && jsonArray.length() > 0){
                        Log.e("#######", answer.toString());
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String action = jsonArray.getString(i);

                        Gesture gesture = Gesture.value(action);
                        View view = activity.getmPlayPauseButton().getRootView();
                        Gesture.LAST_ACTION = action;
                        Long time = new Date().getTime();
                        switch (gesture){
                            case THUMB_UP:
                                if(time - Gesture.thumbUpLastTimeStamp < 2500){
                                    continue;
                                }
                                activity.onPlayPauseButtonClicked(view);
                                Gesture.thumbUpLastTimeStamp = time;
//                                Log.e("----", "clicked");
                                break;
                            case SLIDE_TWO_FINGER_UP:
                                if(time - Gesture.slideTwoFingerDownLastTimeStamp < 1500 || time - Gesture.slideTwoFingerUpLastTimeStamp < 500) {
                                    continue;
                                }
                                activity.turnVolumeUp();
                                Gesture.slideTwoFingerUpLastTimeStamp = time;
                                break;
                            case SLIDE_TWO_FINGER_DOWN:
                                if(time - Gesture.slideTwoFingerUpLastTimeStamp < 1500 || time - Gesture.slideTwoFingerDownLastTimeStamp < 500) {
                                    continue;
                                }
                                activity.turnVolumeDown();;
                                Gesture.slideTwoFingerDownLastTimeStamp = time;
                                break;
                            case TURN_HAND_CLOCKWISE:
                                activity.turnVolumeUp();
                                break;
                            case TURN_HAND_COUNTERCLOCKWISE:
                                activity.turnVolumeDown();
                                break;
                            case PUSH_TWO_FINGERS_AWAY:
                                activity.turnVolumeUp();
                                break;
                            case SWIPE_UP:
                                if(time - Gesture.swipeDownTimeStamp < 1500 || time - Gesture.swipeUpTimeStamp < 500) {
                                    continue;
                                }
                                activity.turnVolumeUp();
                                Gesture.swipeUpTimeStamp = time;
                                break;
                            case SWIPE_DOWN:
                                if(time - Gesture.swipeUpTimeStamp < 1500 || time - Gesture.swipeDownTimeStamp < 500) {
                                    continue;
                                }
                                activity.turnVolumeDown();
                                Gesture.swipeDownTimeStamp = time;
                                break;
                            case STOP:
                                if(time - Gesture.muteTimeStamp < 2000) {
                                    continue;
                                }
                                activity.muteOrUnmuteVolume();
                                Gesture.muteTimeStamp = time;
                                break;
                            case SWIPE_LEFT:
                                if(time - Gesture.swipeRightTimeStamp < 1500 || time - Gesture.swipeLeftTimeStamp < 500) {
                                    continue;
                                }
                                activity.onSkipPreviousButtonClicked(view);
                                Gesture.swipeLeftTimeStamp = time;
                                break;
                            case SWIPE_RIGHT:
                                if(time - Gesture.swipeLeftTimeStamp < 1500 || time - Gesture.swipeRightTimeStamp < 500) {
                                    continue;
                                }
                                activity.onSkipNextButtonClicked(view);
                                Gesture.swipeRightTimeStamp = time;
                                break;
                            case ROLL_HAND_FORWARD:
                                activity.onToggleRepeatButtonClicked(view);
                                break;
                            case SLIDE_TWO_FINGER_LEFT:
                                if(time - Gesture.slideTwoFingerRightLastTimeStamp < 1500 || time - Gesture.slideTwoFingerLeftLastTimeStamp < 500) {
                                    continue;
                                }
                                activity.onSeekBack(view);
                                Gesture.slideTwoFingerLeftLastTimeStamp = time;
                                break;
                            case SLIDE_TWO_FINGER_RIGHT:
                                if(time - Gesture.slideTwoFingerLeftLastTimeStamp < 1500 || time - Gesture.slideTwoFingerRightLastTimeStamp < 500) {
                                    continue;
                                }
                                activity.onSeekForward(view);
                                Gesture.slideTwoFingerRightLastTimeStamp = time;
                                break;
//                            case DRUMMING_FINGERS:
//                                activity.onSetShuffleTrueButtonClicked(view);
//                                break;
                            default:{
                                break;
                            }
                        }
                    }
                }
                cancel(true);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpConnection.disconnect();
            return answer.toString();
        }
        else
        {
            //connection is not OK
            httpConnection.disconnect();
            return null;
        }

    }

    public void onPostExecute(String result) {
        // this is executed on the main thread after the process is over
        // update your UI here

        //displayMessage(result);
    }
}
