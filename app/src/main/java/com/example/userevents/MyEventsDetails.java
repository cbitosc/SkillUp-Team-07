package com.example.userevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyEventsDetails extends AppCompatActivity {
    String access_token;
    String rollno;
    String URL;
    JSONObject jsonObject1;
    JsonObjectRequest objectRequest;
    private RequestQueue queue;
    Button unroll;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events_details);
        final Intent myeventDetails = getIntent();
        access_token= myeventDetails.getStringExtra("access_token");
        rollno=myeventDetails.getStringExtra("rollno");
        URL = "https://cbitevents.herokuapp.com/unroll";
        unroll = (Button)findViewById(R.id.unroll);
        TextView eventname = (TextView)findViewById(R.id.meventname);
        TextView eventdesc = (TextView)findViewById(R.id.meventdesc);
        TextView clubname = (TextView)findViewById(R.id.mclubname);
        TextView eventincharge = (TextView)findViewById(R.id.meventincharge);
        TextView contact = (TextView)findViewById(R.id.mcontact);
        TextView lastregdate = (TextView)findViewById(R.id.mlastregdate);
        TextView startdate = (TextView)findViewById(R.id.mstartdate);
        TextView enddate = (TextView)findViewById(R.id.menddate);
        String event = myeventDetails.getStringExtra("event");
        queue = Volley.newRequestQueue(this);
        try {
            jsonObject = new JSONObject(event);
            eventname.setText(jsonObject.get("eventname").toString());
            eventdesc.setText(jsonObject.get("eventdesc").toString());
            clubname.setText(jsonObject.get("clubname").toString());
            eventincharge.setText(jsonObject.get("eventincharge").toString());
            contact.setText(jsonObject.get("contact").toString());
            lastregdate.setText(jsonObject.get("lastregdate").toString());
            startdate.setText(jsonObject.get("startdate").toString());
            enddate.setText(jsonObject.get("enddate").toString());

            jsonObject1 =new JSONObject();
            jsonObject1.put("rollno",rollno);
            jsonObject1.put("eventname",jsonObject.get("eventname").toString());

            objectRequest= new JsonObjectRequest(Request.Method.POST, URL, jsonObject1, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Toast.makeText(MyEventsDetails.this,response.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("access_token",access_token);
                        intent.putExtra("rollno",rollno);
                        intent.putExtra("password",myeventDetails.getStringExtra("password"));
                        getApplicationContext().startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MyEventsDetails.this,error.toString(), Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    //params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Authorization", "Bearer " + access_token);
                    System.out.println(params);
                    return params;
                }
            };

        } catch (JSONException e) {
            e.printStackTrace();
        }
        unroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
                queue.add(objectRequest);
            }
        });

    }
}