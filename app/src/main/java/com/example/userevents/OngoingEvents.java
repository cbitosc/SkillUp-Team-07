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

public class OngoingEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_events);
        final Intent ointent = getIntent();
        String allevents = ointent.getStringExtra("events");
        ListView listView = (ListView) findViewById(R.id.ongoingevents);
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
                    Toast.makeText(OngoingEvents.this,arrayList.get(i), Toast.LENGTH_SHORT).show();
                    Intent o = new Intent(getApplicationContext(),EventsInfo.class);
                    try {
                        o.putExtra("access_token",ointent.getStringExtra("access_token"));
                        o.putExtra("rollno",ointent.getStringExtra("rollno"));
                        o.putExtra("event",jsonArray.getJSONObject(i).toString());
                        o.putExtra("password",ointent.getStringExtra("password"));

                        startActivity(o);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(o);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}