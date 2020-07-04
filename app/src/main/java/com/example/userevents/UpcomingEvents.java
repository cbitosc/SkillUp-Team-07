package com.example.userevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class UpcomingEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        final Intent uintent = getIntent();
        String allevents = uintent.getStringExtra("events");
        ListView listView = (ListView) findViewById(R.id.upcomingevents);
        final ArrayList<String> arrayList = new ArrayList<String>();
        try {
            int i;
            final JSONArray jsonArray = new JSONArray(allevents);
            for (i=0; i < jsonArray.length(); i++)
            {
                arrayList.add(jsonArray.getJSONObject(i).get("eventname").toString());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(UpcomingEvents.this,arrayList.get(i), Toast.LENGTH_SHORT).show();
                    Intent u = new Intent(getApplicationContext(),EventsInfo.class);
                    try {
                        u.putExtra("access_token",uintent.getStringExtra("access_token"));
                        u.putExtra("rollno",uintent.getStringExtra("rollno"));
                        u.putExtra("event",jsonArray.getJSONObject(i).toString());
                        u.putExtra("password",uintent.getStringExtra("password"));
                        startActivity(u);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(u);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}