package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class MainForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_forgot_password);

        ImageButton backbtn = findViewById(R.id.backButton);
        Button sendbtn = findViewById(R.id.sendButton);

        TextInputLayout email_input = findViewById(R.id.emailFieldLayout);

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

                String email = email_input.getEditText().getText().toString();
                if (email.isEmpty() || !isValidEmail(email)) {
                    email_input.setError("Invalid email address");
                } else {
                    email_input.setError(null);
                    //send email logic
                    Intent intent1 = new Intent(MainForgotPasswordActivity.this, SecondForgotPasswordActivity.class);
                    startActivity(intent1);
                }
            }
        });
        Objects.requireNonNull(email_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                if (!email.isEmpty() && !isValidEmail(email)) {
                    email_input.setError("Invalid email address");
                } else {
                    email_input.setError(null);
                    //send email logic
                }
            }
        });

    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}