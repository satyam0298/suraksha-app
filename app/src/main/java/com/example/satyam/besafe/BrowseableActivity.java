package com.example.satyam.besafe;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BrowseableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browseable);
        Intent intent=getIntent();
        String action=intent.getAction();
        Uri data=intent.getData();

        TextView tw=(TextView)findViewById(R.id.action_and_data);
        tw.setText("Action :"+ action + "\nData :" + data.toString());
    }
}
