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
import android.util.Log;


public class SignUpActivity extends AppCompatActivity {

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button sign_up_button = findViewById(R.id.signup);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if user input is valid
                if (!user.getFull_name().isEmpty() && !user.getEmail().isEmpty() && isValidNewEmail(user.getEmail()) && !user.getPhone_number().isEmpty() && isValidPhone(user.getPhone_number()) && !user.getBirthdate().isEmpty() && isValidBirthdate(user.getBirthdate()) && !user.getPwd().isEmpty() && isValidPassword(user.getPwd())){

                    //check if user exists
                    Toast toast = Toast.makeText(getApplicationContext(), "account created successfully", Toast.LENGTH_LONG);
                    toast.show();

                    Log.d("SignUpActivity", "All input fields are valid");

                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "o93od 8adi ya boujadi", Toast.LENGTH_LONG);
                    toast.show();

                    Log.d("SignUpActivity", "Some input fields are invalid");
                }

            }
        });

        //Redirection to Log in
        TextView sign_in = findViewById(R.id.log_in);
        sign_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        //Control on the inputs (full_name, email, phone_number, birthdate, password)
        TextInputLayout full_name_input = findViewById(R.id.fullNameFieldLayout);
        TextInputLayout email_input = findViewById(R.id.emailFieldLayout);
        TextInputLayout phone_input = findViewById(R.id.phoneFieldLayout);
        TextInputLayout birthdate_input = findViewById(R.id.birthFieldLayout);
        TextInputLayout pwd_input = findViewById(R.id.pwdFieldLayout);

        Objects.requireNonNull(full_name_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String full_name = s.toString().trim();
                if (full_name.isEmpty()) {
                    full_name_input.setError("Invalid user name");
                } else {
                    full_name_input.setError(null);
                    user.setFull_name(full_name);
                }
            }
        });

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
                if (!email.isEmpty() && !isValidNewEmail(email)) {
                    email_input.setError("Invalid email address");
                } else {
                    email_input.setError(null);
                    user.setEmail(email);
                }
            }
        });
        Objects.requireNonNull(phone_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim();
                if (!phone.isEmpty() && !isValidPhone(phone)) {
                    phone_input.setError("Invalid Phone Number");
                    user.setPhone_number(phone);
                } else {
                    phone_input.setError(null);
                    user.setPhone_number(phone);
                }
            }
        });

        Objects.requireNonNull(email_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String birthdate = s.toString().trim();
                if (!birthdate.isEmpty() && !isValidBirthdate(birthdate)) {
                    birthdate_input.setError("Invalid birthdate");
                } else {
                    birthdate_input.setError(null);
                    user.setBirthdate(birthdate);
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
                    pwd_input.setError("Weak Password");
                } else {
                    pwd_input.setError(null);
                    user.setPwd(pwd);
                }
            }
        });
    }
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
    }
    private boolean isValidNewEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidPhone(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    private  boolean isValidBirthdate(String birthdate){
        return true;
    }
    private boolean isValidPassword(String password) {
        if (password.length() < 6) {
            return false;
        }
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).+$";
        return password.matches(regex);
    }
}