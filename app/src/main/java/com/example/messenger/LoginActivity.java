package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button log_in = findViewById(R.id.loginbtn);
        TextView sign_in = findViewById(R.id.sign_in);

        //Redirection to Sign in
        sign_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        //Redirection to Home
        log_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //check if user input is valid
                if (!user.getEmail().isEmpty() && isValidEmail(user.getEmail()) && !user.getPwd().isEmpty() && isValidPassword(user.getPwd())) {

                    //access db to see if user exist or not


                    //set intent to move to the home page (chats)
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                //user input not valid so login fails
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "o93od 8adi ya boujadi", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        //Control on the Inputs (email Only)
        TextInputLayout email_input = findViewById(R.id.emailFieldLayout);
        TextInputLayout pwd_input = findViewById(R.id.pwdFieldLayout);

        Objects.requireNonNull(email_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                if (!email.isEmpty() && !isValidEmail(email)) {
                    email_input.setError("Invalid email address");
                } else {
                    email_input.setError(null);
                    user.setEmail(email_input.getEditText().getText().toString());
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
                String pwd = s.toString().trim();
                if (!pwd.isEmpty() && !isValidPassword(pwd)) {
                    pwd_input.setError("Invalid Password");
                } else {
                    pwd_input.setError(null);
                    user.setPwd(pwd_input.getEditText().getText().toString());
                }

            }
        });
    }
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 6) {
            return false;
        }
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).+$";
        return password.matches(regex);
    }
}