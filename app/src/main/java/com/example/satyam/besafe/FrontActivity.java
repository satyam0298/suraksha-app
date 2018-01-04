package com.example.satyam.besafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FrontActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

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
                    Toast.makeText(FrontActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
                }
                String s="SELECT * FROM tbl";

                final Cursor cursor =db.rawQuery(s,null);
        Thread thread=new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(500);
                    if(cursor.getCount()>0)
                    {

                        Intent i=new Intent(FrontActivity.this,MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i=new Intent(FrontActivity.this,LoginPage.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
                        startActivity(i);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        }
}
