package com.example.userevents;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistrationForm extends AppCompatActivity {
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    String URL;
    JSONObject jsonObject=new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        final Intent regform = getIntent();
        final String rollno = regform.getStringExtra("rollno");
        final String access_token = regform.getStringExtra("access_token");
        final String eventname = regform.getStringExtra("eventname");
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText emailid = (EditText) findViewById(R.id.emailid);
        final EditText contactno = (EditText) findViewById(R.id.contactno);
        final EditText year = (EditText) findViewById(R.id.year);
        final EditText branch = (EditText) findViewById(R.id.branch);
        final EditText section = (EditText) findViewById(R.id.section);
        Button register = (Button) findViewById(R.id.register);


        register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                URL = "https://cbitevents.herokuapp.com/enroll";
                try {

                    jsonObject.put("rollno", rollno);
                    jsonObject.put("name", name.getText().toString());
                    jsonObject.put("emailid", emailid.getText().toString());
                    jsonObject.put("eventname", eventname);
                    jsonObject.put("contactno", contactno.getText().toString());
                    jsonObject.put("year", year.getText().toString());
                    jsonObject.put("branch", branch.getText().toString());
                    jsonObject.put("section", section.getText().toString());
                    createNotificationChannel();
                    Intent notificationIntent = new Intent(getApplicationContext() ,BroadcastManager.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    notificationIntent.putExtra("eventname",regform.getStringExtra("eventname").toString());
                    // PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(RegistrationForm.this,0,notificationIntent,0);

                    AlarmManager alarmManager =(AlarmManager) getSystemService(ALARM_SERVICE);


                    Date startdate=new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.ENGLISH).parse((String)regform.getStringExtra("startdate") );

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startdate);
                    calendar.add(Calendar.DATE, -1);
                    Date onedaybefore = calendar.getTime();
                    long onedaybeforeinmillis= onedaybefore.getTime();
                    alarmManager.set(AlarmManager.RTC_WAKEUP,onedaybeforeinmillis,pendingIntent);
                    queue = Volley.newRequestQueue(getApplicationContext());
                    objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("message");
                                Toast.makeText(RegistrationForm.this, message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("access_token", access_token);
                                intent.putExtra("rollno", rollno);
                                intent.putExtra("password",regform.getStringExtra("password"));
                                getApplicationContext().startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegistrationForm.this, error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            //params.put("Content-Type", "application/json; charset=utf-8");
                            params.put("Authorization", "Bearer " + access_token);
                            System.out.println(params);
                            return params;
                        }
                    };


                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                queue.add(objectRequest);
            }
        });
    }
        private void createNotificationChannel(){
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "mychannel";
                String description = "MYChannel";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = null;
                try {
                    channel = new NotificationChannel((String) jsonObject.get("eventname"), name, importance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }





}


