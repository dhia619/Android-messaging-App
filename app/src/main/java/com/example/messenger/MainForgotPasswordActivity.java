package com.example.messenger;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

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
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = email_input.getEditText().getText().toString().trim();
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    /*Toast.makeText(MainForgotPasswordActivity.this, "verification email sent", Toast.LENGTH_SHORT).show();
                                    //Intent intent1 = new Intent(MainForgotPasswordActivity.this, SecondForgotPasswordActivity.class);
                                    //Toast.makeText(MainForgotPasswordActivity.this, emailAddress, Toast.LENGTH_SHORT).show();
                                    startActivity(intent1);*/
                                    finish();
                                }
                            });
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