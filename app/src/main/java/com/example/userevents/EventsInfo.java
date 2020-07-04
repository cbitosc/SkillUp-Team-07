package com.example.userevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EventsInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_info);
        final Intent eventsinfo =getIntent();
        Button registration = (Button) findViewById(R.id.registration);
        Button eventDetails = (Button) findViewById(R.id.eventDetails);
        //Toast.makeText(this,eventsinfo.getStringExtra("event"), Toast.LENGTH_SHORT).show();

        eventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventDetails = new Intent(getApplicationContext(),EventDetails.class);
                eventDetails.putExtra("event",eventsinfo.getStringExtra("event"));
                startActivity(eventDetails);
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(getApplicationContext(),RegistrationForm.class);
                reg.putExtra("access_token",eventsinfo.getStringExtra("access_token"));
                reg.putExtra("rollno",eventsinfo.getStringExtra("rollno"));

                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(eventsinfo.getStringExtra("event"));
                    reg.putExtra("eventname",jsonObject.get("eventname").toString());
                    reg.putExtra("startdate",jsonObject.get("startdate").toString());
                    reg.putExtra("password",eventsinfo.getStringExtra("password"));
                    startActivity(reg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}