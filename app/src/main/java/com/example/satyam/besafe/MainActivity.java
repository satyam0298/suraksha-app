package com.example.satyam.besafe;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton plus,facebook,twitter;
    Animation Fabopen,Fabclose,rotateclock,rotateanti;
    boolean isOpen=false;
    Button mapB,police;
    MediaPlayer mp;
    int cnt=0;
    String s;
    boolean state;
    LocationManager locationManager;
    private Camera camera;
    Camera.Parameters parameters;
    public static boolean isFlash=false;
    public  static boolean isOn=false;
     public static boolean sirenStatus=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sharedPrefs = getSharedPreferences("ADefault", MODE_PRIVATE);
        SharedPreferences.Editor ed;
        if(!sharedPrefs.contains("state")) {
          state=true;
            ed = sharedPrefs.edit();
            ed.putBoolean("state", false);
            ed.commit();
        }
        else{
            state = sharedPrefs.getBoolean("state", true);
        }
        if(state){
            if(MyService.mSpeechManager!=null)
            {
                MyService.mSpeechManager.destroy();
                MyService.mSpeechManager=null;
                //Remove in case of error  ATTENTION ATTENTION     -------------------------------------------------------------
               // stopService(new Intent(MainActivity.this,MyService.class));
            }
            state=false;
           // startService(new Intent(MainActivity.this,MyService.class));

        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        police=(Button)findViewById(R.id.ButtonForPolicSiren);
        mp=MediaPlayer.create(this,R.raw.police);

        plus = (FloatingActionButton) findViewById(R.id.fab);
       facebook = (FloatingActionButton) findViewById(R.id.fab_facebook);
        twitter = (FloatingActionButton) findViewById(R.id.fab_twitter);
        Fabopen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        Fabclose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotateclock= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotateanti= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sirenStatus)
                {
                    sirenStatus=true;
                    mp.start();
                    mp.setLooping(true);

                }
                else
                {
                    sirenStatus=false;
                    mp.pause();

                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(isOpen)
                {
                    facebook.startAnimation(Fabclose);
                    twitter.startAnimation(Fabclose);
                    plus.startAnimation(rotateanti);
                    facebook.setClickable(false);
                    twitter.setClickable(false);
                    isOpen=false;
                }
                else
                {
                    facebook.startAnimation(Fabopen);
                    twitter.startAnimation(Fabopen);
                    plus.startAnimation(rotateclock);
                    facebook.setClickable(true);
                    twitter.setClickable(true);
                    isOpen=true;
                }
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString="https://www.facebook.com/";
                Uri uri=Uri.parse(urlString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                facebook.startAnimation(Fabclose);
                twitter.startAnimation(Fabclose);
                plus.startAnimation(rotateanti);
                isOpen=false;
                startActivity(intent);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString="https://www.twitter.com/";
                Uri uri=Uri.parse(urlString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                facebook.startAnimation(Fabclose);
                twitter.startAnimation(Fabclose);
                plus.startAnimation(rotateanti);
                isOpen=false;
                startActivity(intent);

            }
        });

        mapB=(Button)findViewById(R.id.messageButton);
        mapB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt=0;
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String str=Double.toString(latitude);
                            String str1=Double.toString(longitude);
                            s=String.format("Location:\nLatitude:%s\nLongitude:%s\nDo you want to send sms to your saved contacts",str,str1);
                            if(cnt==0)
                            {
                                getNotification();
                                cnt++;
                            }

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }
                else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String str=Double.toString(latitude);
                            String str1=Double.toString(longitude);
                            s=String.format("Location:\nLatitude:%s\nLongitude:%s\nDo you want to send sms to your saved contacts",str,str1);
                            if(cnt==0)
                            {
                                getNotification();
                                cnt++;
                            }

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }
            }
        });
        Button call=(Button)findViewById(R.id.ButtonForEmergencyCall);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Emergency.class));
            }
        });

       /* this.mHandler = new Handler();
        m_Runnable.run();*/
    }
  /* private final Runnable m_Runnable = new Runnable()
    {
        public void run()
        {
            if(MyService.mSpeechManager!=null)
            {
                MyService.mSpeechManager.destroy();
                MyService.mSpeechManager=null;
            }

           // stopService(new Intent(MainActivity.this,MyService.class));
            startService(new Intent(MainActivity.this,MyService.class));
            MainActivity.this.mHandler.postDelayed(m_Runnable,4000);
        }

    };*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private File getFile()
    {
        File folder= new File("sdcard/camera_app");
        if(!folder.exists())
        {
            folder.mkdir();
        }
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd_mmss");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        return new File(folder,thisDate+".jpg");
    }
    public void CCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            if (camera != null) {
                try {
                    camera.release();
                    camera = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getFile();
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivity(i);
        }
    }
   public  void flash()
    {
        if(isFlash)
        {
            if(!isOn)
            {
                //  imageButton.setImageResource(R.drawable.on);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();
                isOn=true;
            }
            else
            {
                // imageButton.setImageResource(R.drawable.off);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
                isOn=false;
            }
        }
        else
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Error!....");
            builder.setMessage("Flash is not supported by your device...");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }

            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
    }
    public void share()
    {
        Intent i=new Intent( android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT,"My app");
        startActivity(Intent.createChooser(i,"Share via"));
    }
    void getNotification() {
        SQLiteDatabase data=openOrCreateDatabase("Satyam.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);
        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS contact ("
                    + "Name TEXT,"
                    + "Contact INTEGER);";
            data.execSQL(CREATE_TABLE_CONTAIN);

        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
        }
        final Cursor cursor=data.rawQuery("select * from contact",null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(s).setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent();
                        PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,0,intent,0);
                        while(cursor.moveToNext())
                        {
                            String temp=cursor.getString(1);
                            Toast.makeText(MainActivity.this,temp,Toast.LENGTH_SHORT).show();
                            temp+=" notification";
                            NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this)
                                    .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                                    .setContentTitle(temp)
                                    .setContentText("your message will be send")
                                    .setContentIntent(pendingIntent);
                            notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                            Notification n=notification.build();
                            NotificationManagerCompat.from(MainActivity.this).notify(0,n);
                        }

                       // startActivity(new Intent(MainActivity.this,MainActivity.class));
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        //startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Attention!!!");
        alertDialog.show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            CCamera();
            //Toast.makeText(MainActivity.this,"camera",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_audio) {
             startActivity(new Intent(MainActivity.this,AudioRecorder.class));
        }
        else if (id == R.id.nav_flash) {
            if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            {
                if (camera != null) {
                    try {
                        camera.release();
                        camera = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                camera= Camera.open();
                parameters=camera.getParameters();
                isFlash=true;
            }
           flash();
           // Toast.makeText(MainActivity.this,"flash",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_Map) {
            String urlString="https://www.maps.google.com/";
            Uri uri=Uri.parse(urlString);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        else if (id == R.id.nav_AddContacts) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        } else if (id == R.id.nav_share) {
            share();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   @Override
    protected void onStop() {
        super.onStop();
        if(camera!=null)
        {
            camera.release();
            camera=null;
        }
    }


}
