package com.example.userevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final Intent i = getIntent();

        Button logout = (Button) findViewById(R.id.logout);
        Button goback = (Button) findViewById(R.id.goback);
        Button myevents = (Button) findViewById(R.id.myevents);
        Button changepassword = (Button) findViewById(R.id.changepassword);

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change = new Intent(getApplicationContext(),ForgotPassword.class);
                change.putExtra("access_token",i.getStringExtra("access_token"));
                change.putExtra("rollno",i.getStringExtra("rollno"));
                change.putExtra("password",i.getStringExtra("password"));
                startActivity(change);
            }
        });

        myevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent me = new Intent(getApplicationContext(),MyEvents.class);
                me.putExtra("access_token",i.getStringExtra("access_token"));
                me.putExtra("rollno",i.getStringExtra("rollno"));
                me.putExtra("password",i.getStringExtra("password"));
                startActivity(me);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}