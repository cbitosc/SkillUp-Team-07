package com.example.userevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class EventDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent eventDetails = getIntent();
        TextView eventname = (TextView)findViewById(R.id.eventname);
        TextView eventdesc = (TextView)findViewById(R.id.eventdesc);
        TextView clubname = (TextView)findViewById(R.id.clubname);
        TextView eventincharge = (TextView)findViewById(R.id.eventincharge);
        TextView contact = (TextView)findViewById(R.id.contact);
        TextView lastregdate = (TextView)findViewById(R.id.lastregdate);
        TextView startdate = (TextView)findViewById(R.id.startdate);
        TextView enddate = (TextView)findViewById(R.id.enddate);
        String event = eventDetails.getStringExtra("event");
        try {
            JSONObject jsonObject = new JSONObject(event);
            eventname.setText(jsonObject.get("eventname").toString());
            eventdesc.setText(jsonObject.get("eventdesc").toString());
            clubname.setText(jsonObject.get("clubname").toString());
            eventincharge.setText(jsonObject.get("eventincharge").toString());
            contact.setText(jsonObject.get("contact").toString());
            lastregdate.setText(jsonObject.get("lastregdate").toString());
            startdate.setText(jsonObject.get("startdate").toString());
            enddate.setText(jsonObject.get("enddate").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}