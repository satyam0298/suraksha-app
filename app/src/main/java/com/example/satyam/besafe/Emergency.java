package com.example.satyam.besafe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Emergency extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView l;
    Cursor cursor22;
    int number = 0;
    ArrayList<String> arrayList11;
    ArrayList<String> cnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        l = (ListView) findViewById(R.id.EContact);


        StringBuffer stringBuffer11 = new StringBuffer();
        cursor22 = Getdata22();
         arrayList11 = new ArrayList<>();
         cnum=new ArrayList<>();

        if (cursor22.getCount() == 0) {
            Toast.makeText(Emergency.this, "No extra contact has been saved", Toast.LENGTH_LONG).show();
        } else {
            while (cursor22.moveToNext()) {
                number++;
                if (number <= 4) {
                    stringBuffer11.append("Name :" + cursor22.getString(0) + "\n");
                    stringBuffer11.append("Contact :" + cursor22.getString(1) + "\n");

                }
                cnum.add(cursor22.getString(1));
                arrayList11.add(stringBuffer11.toString());
                stringBuffer11.delete(0, 100);
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList11);
                l.setAdapter(listAdapter);
                l.setOnItemClickListener(Emergency.this);
            }

        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Emergency.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        finish();
        startActivity(i);
    }

    private Cursor Getdata11() {
        SQLiteDatabase data = openOrCreateDatabase("Satyam.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor cursor = data.rawQuery("select * from tbl", null);
        return cursor;
    }

    private Cursor Getdata22() {
        SQLiteDatabase data = openOrCreateDatabase("Satyam.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS contact ("
                    + "Name TEXT,"
                    + "Contact INTEGER);";
            data.execSQL(CREATE_TABLE_CONTAIN);

        } catch (Exception e) {
            Toast.makeText(Emergency.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
        }
        Cursor cursor1 = Getdata11();
        while (cursor1.moveToNext()) {
            String a = cursor1.getString(0);
            String b = cursor1.getString(3);
            String sql = "INSERT or replace INTO contact (Name, Contact) VALUES('" + a + "','" + b + "')";
            Cursor c = data.rawQuery("select * from contact where Name='" + a + "' and Contact ='" + b + "' ", null);
            if (c.getCount() == 0) {
                data.execSQL(sql);
            }

        }

        Cursor curs = data.rawQuery("select * from contact", null);
        return curs;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                String string=cnum.get(0);

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+string));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                break;
            case 1:
                String string1=cnum.get(1);

                Intent intent1 = new Intent(Intent.ACTION_CALL);
                intent1.setData(Uri.parse("tel:"+string1));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent1);
                break;
            case 2:
                String string2=cnum.get(2);

                Intent intent2 = new Intent(Intent.ACTION_CALL);
                intent2.setData(Uri.parse("tel:"+string2));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent2);
                break;
            case 3:
                String string3=cnum.get(3);

                Intent intent3 = new Intent(Intent.ACTION_CALL);
                intent3.setData(Uri.parse("tel:"+string3));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent3);
                break;

        }
    }
}
