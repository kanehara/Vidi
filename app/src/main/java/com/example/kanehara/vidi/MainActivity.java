package com.example.kanehara.vidi;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder audioRecorder;
    private String outputFile;
    private Button recButton;
    private Button stopButton;
    private Button playButton;

    private static final int VIDI_PERM_RECORD_AUDIO = 1;
    private static final int VIDI_PERM_EXT_STORAGE = 2;

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
        int permCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);
        if (permCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO},
                    VIDI_PERM_RECORD_AUDIO);
        }
        else {
            this.audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            this.audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            this.audioRecorder.setOutputFile(outputFile);
        }
    }

    private void initRecButton() {
        recButton = (Button) findViewById(R.id.recButton);
        int permCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    VIDI_PERM_EXT_STORAGE);
        }
        else {
            recButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        audioRecorder.prepare();
                        audioRecorder.start();
                        recButton.setEnabled(false);
                        stopButton.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_SHORT).show();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void initStopButton() {
        stopButton = (Button) findViewById(R.id.stopButton);
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
        playButton = (Button) findViewById(R.id.playButton);
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
