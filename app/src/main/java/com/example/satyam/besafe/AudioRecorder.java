package com.example.satyam.besafe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioRecorder extends AppCompatActivity {


    Button start,stop,play;
    File file1,file2;
    MediaRecorder myAudioRecorder;
    String outputFile=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);
        myAudioRecorder =new MediaRecorder();
        start=(Button)findViewById(R.id.ButtonForStartAudio);
        stop=(Button)findViewById(R.id.ButtonForStopAudio);
        play=(Button)findViewById(R.id.ButtonForPlayAudio);
        stop.setEnabled(false);
        play.setEnabled(false);
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
       // startService(new Intent(AudioRecorder.this,MyService.class));
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                file1= new File("sdcard/MyRecording");
                if(!file1.exists())
                {
                   file1.mkdir();
                }
                SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd_mmss");
                Date todayDate = new Date();
                String thisDate = currentDate.format(todayDate);
                file2 = new File(file1,thisDate+".3gp");

                    outputFile=file2.getAbsolutePath();
                    myAudioRecorder.setOutputFile(outputFile);

                try {
                    myAudioRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myAudioRecorder.start();
                start.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(AudioRecorder.this,"RECORDING STARTED",Toast.LENGTH_SHORT).show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                stop.setEnabled(false);
                start.setEnabled(true);
                play.setEnabled(true);
                Toast.makeText(AudioRecorder.this,"RECORDING COMPLETED",Toast.LENGTH_SHORT).show();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer m=new MediaPlayer();
                try {
                    m.setDataSource(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m.start();
                Toast.makeText(AudioRecorder.this,"AUDIO STARTED",Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(AudioRecorder.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


}
