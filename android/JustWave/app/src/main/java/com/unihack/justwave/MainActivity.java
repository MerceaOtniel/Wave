package com.unihack.justwave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.unihack.justwave.api.Gesture;
import com.unihack.justwave.api.WaveTask;
import com.unihack.justwave.spotify.SpotifyManager;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeWidgets();

        //new Timer().schedule(new WaveTask(), 0, 500);
    }


    private void initializeWidgets(){
        startButton = (Button) findViewById(R.id.button_start) ;
        stopButton = (Button) findViewById(R.id.button_stop) ;

        startButton.setOnClickListener(startClickListener);
        stopButton.setOnClickListener(stopClickListener);
    }

    private View.OnClickListener startClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //change color / push notifications
            SpotifyManager manager = new SpotifyManager();
            manager.initializeConnection(Gesture.THUMBS_UP);
        }
    };

    private View.OnClickListener stopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //change color / remove notifications
        }
    };
}
