package com.example.satyam.besafe;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    Button b;
    EditText e1,e2,e3,e4,e5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        b=(Button)findViewById(R.id.ButtonForSubmit);
        e1=(EditText) findViewById(R.id.name);
        e2=(EditText)findViewById(R.id.password);
        e3=(EditText)findViewById(R.id.confirm);
        e4=(EditText)findViewById(R.id.contact);
        e5=(EditText)findViewById(R.id.email);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=e1.getText().toString();
                String s2=e2.getText().toString();
                String s3=e3.getText().toString();
                String s4=e4.getText().toString();
                String s5=e5.getText().toString();
                if(!s2.equals(s3))
                {
                    Toast.makeText(LoginPage.this,"passwords don't match",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(s1.equals("")||s2.equals("")||s3.equals("")||s4.equals("")||s5.equals(""))
                    {
                        Toast.makeText(LoginPage.this,"Please fill complete details",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(s4.length()>=10)
                        {
                            SQLiteDatabase db;
                            db = openOrCreateDatabase( "Satyam.db" , SQLiteDatabase.CREATE_IF_NECESSARY  , null  );
                            try {
                                final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS tbl ("
                                        + "Name TEXT,"
                                        + "Password TEXT,"
                                        + "Confirm TEXT,"
                                        + "Contact INTEGER,"
                                        + "Email TEXT);";
                                db.execSQL(CREATE_TABLE_CONTAIN);
                            }
                            catch (Exception e) {
                                Toast.makeText(LoginPage.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
                            }
                            String s="SELECT * FROM tbl WHERE Name='" +s1+ "' AND Password = '" +s2+ "'";
                            Cursor cursor =db.rawQuery(s,null);
                            if(cursor.getCount()>0)
                            {
                                Toast.makeText(LoginPage.this,"user exists.Try new username and password",Toast.LENGTH_LONG).show();
                                e1.setText("");
                                e2.setText("");
                                e3.setText("");
                            }
                            else
                            {
                                String sql =
                                        "INSERT or replace INTO tbl (Name, Password, Confirm, Contact,Email) VALUES('"+s1+"','"+s2+"','"+s3+"','"+s4+"','"+s5+"')" ;
                                db.execSQL(sql);
                                Intent intent=new Intent();
                                PendingIntent pendingIntent=PendingIntent.getActivity(LoginPage.this,0,intent,0);
                                NotificationCompat.Builder notification = new NotificationCompat.Builder(LoginPage.this)
                                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                                        .setContentTitle("Succesfully Registered")
                                        .setContentText("Thank you!")
                                        .setContentIntent(pendingIntent);
                                notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                                Notification n=notification.build();
                                NotificationManagerCompat.from(LoginPage.this).notify(0,n);
                                startActivity(new Intent(LoginPage.this,MainActivity.class));
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginPage.this,"mobile number is not valid",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });


    }

}
