package com.example.sikumbang.radiosafasindo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    Button btn_playpause;
    MediaPlayer mediaPlayer;
    String stream = "http://radio.safasindo.com:7044/";
    boolean prepared    =false;
    boolean started     =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_playpause = (Button) findViewById(R.id.btn_playpause);
        btn_playpause.setEnabled(false);
        btn_playpause.setText("LOADING");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(stream);

        btn_playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started){
                    started = false;
                    mediaPlayer.pause();
                    btn_playpause.setText("PLAY");
                } else{
                    started = true;
                    mediaPlayer.start();
                    btn_playpause.setText("PAUSE");
                }
            }
        });
    }

    class PlayerTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared= true;
            }catch (IOException e){
                e.printStackTrace();
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            btn_playpause.setEnabled(true);
            btn_playpause.setText("PLAY");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prepared){
            mediaPlayer.release();
        }
    }
}
