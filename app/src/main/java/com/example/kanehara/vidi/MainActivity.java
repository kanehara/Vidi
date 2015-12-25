package com.example.kanehara.vidi;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder audioRecorder;
    private String outputFile;
    private final Button recButton = (Button) findViewById(R.id.recButton);
    private final Button stopButton = (Button) findViewById(R.id.stopButton);
    private final Button playButton = (Button) findViewById(R.id.playButton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAudioRecorder();
        initRecButton();
        initStopButton();
        initPlayButton();
    }

    private void initAudioRecorder() {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.3gp";
        this.audioRecorder = new MediaRecorder();
        this.audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        this.audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        this.audioRecorder.setOutputFile(outputFile);
    }

    private void initRecButton() {
        recButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    audioRecorder.prepare();
                    audioRecorder.start();
                }
                catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                recButton.setEnabled(false);
                stopButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initStopButton() {
        stopButton.setEnabled(false);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                audioRecorder.stop();
                audioRecorder.release();
                recButton.setEnabled(true);
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPlayButton() {
        playButton.setEnabled(false);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer player = new MediaPlayer();
                try {
                    player.setDataSource(outputFile);
                    player.prepare();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                player.start();
                Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
            }
        });
    }
}
