package com.example.satyam.besafe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button b1;
    EditText e1,e2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        b1=(Button)findViewById(R.id.ButtonForLogin);

        e1=(EditText)findViewById(R.id.username);
        e2=(EditText)findViewById(R.id.password);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=e1.getText().toString();
                String s2=e2.getText().toString();
                if(s1.equals("")||s2.equals(""))
                {
                    Toast.makeText(LoginActivity.this,"Fill in completely",Toast.LENGTH_LONG).show();
                }
                else
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

                       /* String sql =
                                "INSERT or replace INTO tbl (Name, Password, Confirm, Contact,Email) VALUES('a','a','a','7033107323','a')" ;
                        db.execSQL(sql);*/
                     //  Toast.makeText(LoginActivity.this, "table  ", Toast.LENGTH_LONG).show();

                   }
                    catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
                    }
                    String tmp="SELECT * FROM tbl WHERE Name='" + s1 + "' AND Password='" + s2 + "' ";
                    Cursor cursor=db.rawQuery(tmp,null);
                    if(cursor.getCount()==0)
                    {
                        Toast.makeText(LoginActivity.this,"INVALID DETAILS",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        e1.setText("");
                        e2.setText("");
                        Intent i=new Intent(LoginActivity.this,SavedContact.class);
                        startActivity(i);
                    }
                }
            }
        });
    }

}
