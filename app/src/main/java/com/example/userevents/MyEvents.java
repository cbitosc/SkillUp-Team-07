package com.example.userevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class MyEvents extends AppCompatActivity {
    ListView listView;
    String access_token;
    String rollno;
    String URL;
    private RequestQueue queue;
    JSONObject jsonObject;
    JSONArray jsonArray;
    JsonObjectRequest objectRequest;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        final Intent me = getIntent();
        access_token = me.getStringExtra("access_token");
        rollno = me.getStringExtra("rollno");
        listView = (ListView) findViewById(R.id.listview);
        URL = "https://cbitevents.herokuapp.com/getuserevents";
        jsonObject = new JSONObject();

        try {

            jsonObject.put("rollno", rollno);
            jsonObject.put("getpost", "post");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(this);
        objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    arrayList = new ArrayList<String>();
                    jsonArray = new JSONArray(response.getString("events"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(jsonArray.getJSONObject(i).get("eventname").toString());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                    listView.setAdapter(arrayAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyEvents.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Authorization", "Bearer " + access_token);
                System.out.println(params);
                return params;
            }

        };

        queue.add(objectRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent my = new Intent(getApplicationContext(), MyEventsDetails.class);
                try {
                    my.putExtra("event", jsonArray.getJSONObject(i).toString());
                    my.putExtra("access_token", access_token);
                    my.putExtra("rollno", rollno);
                    my.putExtra("password",me.getStringExtra("password"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(my);

            }
        });
    }


}
