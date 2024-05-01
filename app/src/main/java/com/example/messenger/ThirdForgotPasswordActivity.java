package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ThirdForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_forgot_password);


        ImageButton backbtn = findViewById(R.id.backButton);
        Button savebtn = findViewById(R.id.saveButton);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}