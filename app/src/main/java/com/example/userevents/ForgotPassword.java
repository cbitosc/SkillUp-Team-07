package com.example.userevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ForgotPassword extends AppCompatActivity {

    EditText oldpassword,newpassword,confnewpassword;
    JSONObject jsonObject;
    JsonObjectRequest objectRequest;
    private RequestQueue queue;
    String URL;
    String access_token;
    String rollno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        URL="https://cbitevents.herokuapp.com/forgotpassword";
        final Intent change = getIntent();
        access_token=change.getStringExtra("access_token");
        rollno=change.getStringExtra("rollno");
        oldpassword = (EditText)findViewById(R.id.oldpassword);
        newpassword = (EditText)findViewById(R.id.newpassword);
        confnewpassword = (EditText)findViewById(R.id.confnewpassword);
        Button reset =(Button)findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(oldpassword.getText().toString()+" "+newpassword.getText().toString()+" "+confnewpassword.getText().toString());
                if(newpassword.getText().toString().equals(confnewpassword.getText().toString())) {
                    if (oldpassword.getText().toString().equals(change.getStringExtra("password"))) {

                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("rollno",rollno);
                            jsonObject.put("pwd",newpassword.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        queue = Volley.newRequestQueue(getApplicationContext());
                        objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Toast.makeText(ForgotPassword.this,response.get("message").toString(), Toast.LENGTH_LONG).show();
                                    System.out.println(response.get("message").toString());
                                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("access_token", access_token);
                                    intent.putExtra("rollno", rollno);
                                    intent.putExtra("password", newpassword.getText().toString());
                                    getApplicationContext().startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ForgotPassword.this, error.toString(), Toast.LENGTH_SHORT).show();
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

                        queue.add(objectRequest);

                    } else {
                        Toast.makeText(getApplicationContext(), "Old Passwords Didnt match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "New Passwords Didnt match", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}