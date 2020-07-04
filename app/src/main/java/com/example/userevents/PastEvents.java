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

public class PastEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_events);

        final Intent pintent = getIntent();
        final String allevents = pintent.getStringExtra("events");
        ListView listView = (ListView) findViewById(R.id.pastevents);
        final ArrayList<String> arrayList = new ArrayList<String>();

        try {
            int i;

            final JSONArray jsonArray = new JSONArray(allevents);
            for (i=0; i < jsonArray.length(); i++)
            {
                arrayList.add(jsonArray.getJSONObject(i).get("eventname").toString());

            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(PastEvents.this, arrayList.get(i), Toast.LENGTH_SHORT).show();
                    Intent p = new Intent(getApplicationContext(), EventsInfo.class);
                    try {
                        p.putExtra("access_token",pintent.getStringExtra("access_token"));
                        p.putExtra("rollno",pintent.getStringExtra("rollno"));
                        p.putExtra("event",jsonArray.getJSONObject(i).toString());
                        p.putExtra("password",pintent.getStringExtra("password"));
                        startActivity(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(p);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
}