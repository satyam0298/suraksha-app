package com.example.satyam.besafe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register2 extends AppCompatActivity {

    EditText e1,e2,e3;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        b1=(Button)findViewById(R.id.ButtonForChange);

        e1=(EditText)findViewById(R.id.key);
        e2=(EditText)findViewById(R.id.NewContact);
        e3=(EditText)findViewById(R.id.New);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1=e1.getText().toString();
                String s2=e2.getText().toString();
                String s3=e3.getText().toString();
                if(s1.equals("")||s2.equals(""))
                {
                    Toast.makeText(Register2.this,"Fill in completely",Toast.LENGTH_LONG).show();
                }
                else
                {
                    SQLiteDatabase db;
                    db = openOrCreateDatabase( "Satyam.db" , SQLiteDatabase.CREATE_IF_NECESSARY  , null  );

                    String tmp="SELECT * FROM contact WHERE Name='" + s1 + "' AND Contact='" + s2 + "' ";
                    Cursor cursor=db.rawQuery(tmp,null);
                    if(cursor.getCount()==0)
                    {
                        Toast.makeText(Register2.this,"USER DOES NOT EXISTS",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        e1.setText("");
                        e2.setText("");
                        String sql =
                                "update contact set Contact='"+s3+"' where Name = '"+s1+"' " ;
                        db.execSQL(sql);
                        Intent i=new Intent(Register2.this,SavedContact.class);
                        startActivity(i);
                    }
                }
            }
        });
    }
}
