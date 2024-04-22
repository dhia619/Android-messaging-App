package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SymbolTable;
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
    MyDataBase db = new MyDataBase(this);
    Toast invalid_input_toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button sign_up_button = findViewById(R.id.signup);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if user input is valid
                if(user.getFull_name().isEmpty()){
                    Toast.makeText(getApplicationContext(),"type your full name",Toast.LENGTH_LONG).show();
                }
                else if(user.getEmail().isEmpty()){
                    Toast.makeText(getApplicationContext(),"type your email",Toast.LENGTH_LONG).show();
                }
                else if(!isValidNewEmail(user.getEmail())){
                    Toast.makeText(getApplicationContext(),"email is not valid",Toast.LENGTH_LONG).show();
                }
                else if(user.getPhone_number().isEmpty()){
                    Toast.makeText(getApplicationContext(),"type your phone number",Toast.LENGTH_LONG).show();
                }
                else if(!isValidPhone(user.getPhone_number())){
                    Toast.makeText(getApplicationContext(),"phone number is not valid",Toast.LENGTH_LONG).show();
                }
                else if(user.getBirthdate().isEmpty()){
                    Toast.makeText(getApplicationContext(),"type your birthdate",Toast.LENGTH_LONG).show();
                }
                else if(!isValidBirthdate(user.getBirthdate())){
                    Toast.makeText(getApplicationContext(),"birthdate must be dd/mm/yyyy",Toast.LENGTH_LONG).show();
                }
                else if(user.getPwd().isEmpty()){
                    Toast.makeText(getApplicationContext(),"type your password",Toast.LENGTH_LONG).show();
                }
                else if(!isValidPassword(user.getPwd())){
                    Toast.makeText(getApplicationContext(),"password must contain at least 6 alphabets and digits",Toast.LENGTH_LONG).show();
                }
                else{
                    // Valid input
                    user.setProfile_image("https://www.youtube.com/watch?v=lHZwlzOUOZ4");
                    if (db.user_exist(user) == 0) {
                        // User doesn't exist, proceed with sign-up
                        db.insertUser(user);
                        Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_LONG).show();
                    } else {
                        // User already exists, show error message
                        Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show();
                    }
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
                if (email.isEmpty() || !isValidNewEmail(email)) {
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
                if (phone.isEmpty() || !isValidPhone(phone)) {
                    phone_input.setError("Invalid Phone Number");
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
                if (birthdate.isEmpty() || !isValidBirthdate(birthdate)) {
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
                if (pwd.isEmpty() || !isValidPassword(pwd)) {
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

    private boolean isValidBirthdate(String birthdate) {
        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/((19|20)\\d\\d)$";
        return birthdate.matches(regex);
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 6) {
            return false;
        }
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).+$";
        return password.matches(regex);
    }
}