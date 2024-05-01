package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_forgot_password);

        ImageButton backbtn = findViewById(R.id.backButton);
        Button sendbtn = findViewById(R.id.sendButton);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainForgotPasswordActivity.this, SecondForgotPasswordActivity.class);
                startActivity(intent1);
            }
        });


    }
}