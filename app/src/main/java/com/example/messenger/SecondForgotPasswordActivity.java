package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SecondForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_forgot_password);

        ImageButton backbtn = findViewById(R.id.backButton);
        Button verifybtn = findViewById(R.id.verifyButton);
        //
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        verifybtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondForgotPasswordActivity.this, ThirdForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}