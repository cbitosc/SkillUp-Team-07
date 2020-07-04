package com.example.userevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText rollno,password;
    String URL;
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    JSONObject data;
    String access_token;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rollno = (EditText)findViewById(R.id.rollno);
        password = (EditText)findViewById(R.id.password);
        URL = "https://cbitevents.herokuapp.com/userlogin";
        data = new JSONObject();



        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    data.put("rollno",rollno.getText().toString());
                    data.put("pwd",password.getText().toString());
                    System.out.println(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                queue = Volley.newRequestQueue(getApplicationContext());
                objectRequest = new JsonObjectRequest(Request.Method.POST, URL, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            access_token=response.getString("access_token");
                            //Toast.makeText(MainActivity.this, access_token, Toast.LENGTH_LONG).show();
                            if(access_token!=null){
                                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                System.out.println(access_token);
                                intent.putExtra("access_token",access_token);
                                intent.putExtra("rollno",rollno.getText().toString());
                                intent.putExtra("password",password.getText().toString());
                                Toast.makeText(MainActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast toast = Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
                        //toast.show();
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(objectRequest);


            }
        });




    }

    }

