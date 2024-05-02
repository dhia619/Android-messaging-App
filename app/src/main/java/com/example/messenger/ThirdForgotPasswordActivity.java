package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ThirdForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_forgot_password);


        ImageButton backbtn = findViewById(R.id.backButton);
        Button savebtn = findViewById(R.id.saveButton);
        TextInputLayout pwd_input = findViewById(R.id.pwdFieldLayout);
        TextInputLayout retype_pwd_input = findViewById(R.id.retypePwdFieldLayout);

        Objects.requireNonNull(pwd_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = s.toString().trim();
                if (!pwd.isEmpty() && !isValidPassword(pwd)) {
                    pwd_input.setError("Invalid Password");
                } else {
                    pwd_input.setError(null);
                }
            }
        });

        Objects.requireNonNull(pwd_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String rpwd = s.toString().trim();
                if (rpwd.isEmpty() || !isValidPassword(rpwd)) {
                    retype_pwd_input.setError("Invalid Password");
                }else if(!rpwd.equals(pwd_input.getEditText().getText().toString().trim())){
                    retype_pwd_input.setError("Passwords does not match");
                }
                else {
                    retype_pwd_input.setError(null);
                }
            }
        });

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

                String pwd = pwd_input.getEditText().getText().toString();
                String rpwd = retype_pwd_input.getEditText().getText().toString();
                if (pwd.isEmpty() || !isValidPassword(pwd)) {
                    pwd_input.setError("Password must contain at least 6 alphabets and digits");
                } else {
                    pwd_input.setError(null);
                }
                if (rpwd.isEmpty() || !isValidPassword(rpwd)) {
                    retype_pwd_input.setError("Password must contain at least 6 alphabets and digits");
                }else if(!rpwd.equals(pwd_input.getEditText().getText().toString().trim())){
                    retype_pwd_input.setError("Passwords does not match");
                }
                else {
                    retype_pwd_input.setError(null);
                    //reset password logic
                    Intent intent = new Intent(ThirdForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });


    }
    private boolean isValidPassword(String password) {
        if (password.length() < 6) {
            return false;
        }
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).+$";
        return password.matches(regex);
    }
}