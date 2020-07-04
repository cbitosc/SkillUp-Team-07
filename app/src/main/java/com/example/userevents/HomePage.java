package com.example.userevents;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomePage extends AppCompatActivity {
    String access_token;
    String rollno;
    String password;
    JSONArray pastevents= new JSONArray();
    JSONArray ongoingevents=new JSONArray();
    JSONArray upcomingevents = new JSONArray();
    private RequestQueue queue;
    JSONObject data;
    JsonObjectRequest objectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        final Intent intent = getIntent();
        final Intent uintent = new Intent(getApplicationContext(),UpcomingEvents.class);
        final Intent ointent = new Intent(getApplicationContext(),OngoingEvents.class);
        final Intent pintent = new Intent(getApplicationContext(),PastEvents.class);
        access_token = intent.getStringExtra("access_token");
        rollno = intent.getStringExtra("rollno");

        ImageView imageMenu = (ImageView) findViewById(R.id.imageMenu);
        ImageView past = (ImageView) findViewById(R.id.past);
        ImageView ongoing = (ImageView) findViewById(R.id.ongoing);
        ImageView upcoming = (ImageView) findViewById(R.id.upcoming);
        access_token = intent.getStringExtra("access_token");
        rollno = intent.getStringExtra("rollno");
        password = intent.getStringExtra("password");
        String URL = "https://cbitevents.herokuapp.com/getuserevents";
        data = new JSONObject();

        try {
            data.put("rollno", rollno);
            data.put("getpost","get");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(this);
        objectRequest = new JsonObjectRequest(Request.Method.POST, URL, data, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String result = response.getString("events");
                    JSONArray jsonArray = new JSONArray(result);
                    System.out.println(jsonArray);
                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    Date today = Date.from(java.time.LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
                    Date two = Date.from(java.time.LocalDate.now().plusDays(2).atStartOfDay(defaultZoneId).toInstant());
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        Date startdate=new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.ENGLISH).parse((String) jsonArray.getJSONObject(i).get("startdate"));
                        Date enddate=new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.ENGLISH).parse((String) jsonArray.getJSONObject(i).get("enddate"));
                        System.out.println(startdate+" "+enddate);

                        //Calendar c = Calendar.getInstance();
                        //Date currentDate = new Date();
                        //DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

                        if (enddate.after(today) && startdate.before(today)) {
                            Log.i("message","ongoing");
                            ongoingevents.put(jsonArray.getJSONObject(i));
                        }
                        else if(startdate.after(today))
                        {

                            Log.i("object",jsonArray.getJSONObject(i).toString());
                            Log.i("message","upcoming");
                            upcomingevents.put(jsonArray.getJSONObject(i));
                            Log.i("events", upcomingevents.toString());
                        }
                        else if(enddate.before(today)){
                            Log.i("message","past");
                            pastevents.put(jsonArray.getJSONObject(i));
                        }
                    }
                   // String eventname = (String) jsonArray.getJSONObject(0).get("eventname");
                   // System.out.println(eventname);
                    Log.d("result : ", result);
                    System.out.println(jsonArray.getClass().getName());


                    //Toast.makeText(HomePage.this, result, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(HomePage.this, error.toString(), Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Authorization", "Bearer " + access_token);
                System.out.println(params);
                return params;
            }


        };
        queue.add(objectRequest);



         imageMenu.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent dintent = new Intent(getApplicationContext(),Dashboard.class);
                 dintent.putExtra("access_token",access_token);
                 dintent.putExtra("rollno",rollno);
                 dintent.putExtra("password",password);
                 startActivity(dintent);
             }
         });
         past.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 pintent.putExtra("access_token",access_token);
                 pintent.putExtra("rollno",rollno);
                 pintent.putExtra("events",pastevents.toString());
                 pintent.putExtra("password",password);
                 startActivity(pintent);
             }
         });
        ongoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ointent.putExtra("access_token",access_token);
                ointent.putExtra("rollno",rollno);
                ointent.putExtra("events",ongoingevents.toString());
                ointent.putExtra("password",password);
                startActivity(ointent);
            }
        });
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uintent.putExtra("access_token",access_token);
                uintent.putExtra("rollno",rollno);
                uintent.putExtra("events",upcomingevents.toString());
                uintent.putExtra("password",password);
                startActivity(uintent);
            }
        });
    }


}