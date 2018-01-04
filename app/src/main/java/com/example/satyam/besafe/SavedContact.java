package com.example.satyam.besafe;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedContact extends AppCompatActivity {

    Button b1,b2,b3;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_contact);
        b1=(Button)findViewById(R.id.ButtonForAddingContact);
        b2=(Button)findViewById(R.id.ButtonForEditContact);
        b3=(Button)findViewById(R.id.ButtonForDeleteContact);
        lv=(ListView)findViewById(R.id.listView);

        ArrayList<String> arrayList=new ArrayList<>();
        StringBuffer stringBuffer= new StringBuffer();
        StringBuffer stringBuffer1= new StringBuffer();
        Cursor cursor2 = Getdata2();
        ArrayList<String> arrayList1=new ArrayList<>();

        if(cursor2.getCount()==0)
        {
            Toast.makeText(SavedContact.this,"No extra contact has been saved",Toast.LENGTH_LONG).show();
        }
        else
        {
            while(cursor2.moveToNext())
            {
                stringBuffer1.append("Name :"+cursor2.getString(0)+"\n");
                stringBuffer1.append("Contact :"+cursor2.getString(1)+"\n");

                arrayList1.add(stringBuffer1.toString());
                stringBuffer1.delete(0,100);
                ListAdapter listAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList1);
                lv.setAdapter(listAdapter);
            }

        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedContact.this,Register.class));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedContact.this,Register2.class));
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedContact.this,Register3.class));
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent i=new Intent(SavedContact.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        finish();
        startActivity(i);
    }
    private Cursor Getdata1()
    {
        SQLiteDatabase data=openOrCreateDatabase("Satyam.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);
        Cursor cursor=data.rawQuery("select * from tbl",null);
        return cursor;
    }
    private Cursor Getdata2()
    {
        SQLiteDatabase data=openOrCreateDatabase("Satyam.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);
        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS contact ("
                    + "Name TEXT,"
                    + "Contact INTEGER);";
            data.execSQL(CREATE_TABLE_CONTAIN);

        }
        catch (Exception e) {
            Toast.makeText(SavedContact.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
        }
        Cursor cursor1 = Getdata1();
        while(cursor1.moveToNext())
        {
            String a=cursor1.getString(0);
            String b=cursor1.getString(3);
            String sql = "INSERT or replace INTO contact (Name, Contact) VALUES('"+a+"','"+b+"')" ;
            Cursor c=data.rawQuery("select * from contact where Name='"+a+"' and Contact ='"+b+"' ",null);
            if(c.getCount()==0){
                data.execSQL(sql);
            }

        }

        Cursor cursor=data.rawQuery("select * from contact",null);
        return cursor;
    }

}
