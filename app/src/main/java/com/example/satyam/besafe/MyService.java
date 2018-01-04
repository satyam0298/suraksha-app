package com.example.satyam.besafe;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.ArrayList;

public class MyService extends Service implements ActivityCompat.OnRequestPermissionsResultCallback {
    public MyService() {
    }
    static SpeechRecognizerManager mSpeechManager;
    boolean times=true;
    Handler mHandler;
    private void SetSpeechListener() {
        mSpeechManager = new SpeechRecognizerManager(getApplicationContext(), new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {

                if (results != null && results.size() > 0) {

                    switch(results.get(0))
                    {
                        case "home":
                            Intent dialogIntent = new Intent(MyService.this, MainActivity.class);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialogIntent);
                            break;
                        case "record":
                            Intent dialogIntent2 = new Intent(MyService.this, AudioRecorder.class);
                            dialogIntent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialogIntent2);
                            break;
                        case "contact":
                            Intent dialogIntent3 = new Intent(MyService.this, LoginActivity.class);
                            dialogIntent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialogIntent3);
                            break;
                        case "emergency call":
                            Intent dialogIntent4 = new Intent(MyService.this, Emergency.class);
                            dialogIntent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialogIntent4);
                            break;
                        default:
                            Toast.makeText(MyService.this, "Do Not match any command!", Toast.LENGTH_SHORT).show();
                    }


                    if (results.size() == 1) {
                        mSpeechManager.destroy();
                        mSpeechManager = null;

                    } else {

                        StringBuilder sb = new StringBuilder();
                        if (results.size() > 5) {
                            results = (ArrayList<String>) results.subList(0, 5);
                        }
                        for (String result : results) {
                            sb.append(result).append("\n");
                        }

                    }
                }
            }
        });
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return  null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Toast.makeText(MyService.this,"startService",Toast.LENGTH_SHORT).show();
        if(times)
        {
            times=false;
            this.mHandler = new Handler();
            m_Runnable.run();
        }

      return START_STICKY;
    }
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()
        {
            if(mSpeechManager==null)
            {
               // Toast.makeText(MyService.this,"null", Toast.LENGTH_SHORT).show();
                SetSpeechListener();
            }
            else{
               // Toast.makeText(MyService.this,"not null", Toast.LENGTH_SHORT).show();
                mSpeechManager.destroy();
                mSpeechManager=null;
                SetSpeechListener();
            }
            MyService.this.mHandler.postDelayed(m_Runnable,3000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case PermissionHandler.RECORD_AUDIO:
                if(grantResults.length>0) {
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                        if(mSpeechManager==null)
                        {
                            Toast.makeText(MyService.this,"null", Toast.LENGTH_SHORT).show();
                            SetSpeechListener();
                        }
                        else{
                            Toast.makeText(MyService.this,"not null", Toast.LENGTH_SHORT).show();
                            mSpeechManager.destroy();
                            mSpeechManager=null;
                            SetSpeechListener();
                        }
                    }
                }
                break;

        }
    }
}
