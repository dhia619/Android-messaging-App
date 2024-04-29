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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        TextInputLayout full_name_input = findViewById(R.id.fullNameFieldLayout);
        TextInputLayout email_input = findViewById(R.id.emailFieldLayout);
        TextInputLayout phone_input = findViewById(R.id.phoneFieldLayout);
        TextInputLayout birthdate_input = findViewById(R.id.birthFieldLayout);
        TextInputLayout pwd_input = findViewById(R.id.pwdFieldLayout);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String full_name = full_name_input.getEditText().getText().toString();
                String email = email_input.getEditText().getText().toString();
                String ph_num = phone_input.getEditText().getText().toString();
                String date_naiss = birthdate_input.getEditText().getText().toString();
                String pwd = pwd_input.getEditText().getText().toString();
                //check if user input is valid
                if(full_name.isEmpty()){
                    full_name_input.setError("type your full name");
                }
                else if(email.isEmpty()){
                    email_input.setError("type your email");
                }
                else if(!isValidNewEmail(email)){
                    email_input.setError("invalid email address");
                }
                else if(ph_num.isEmpty()){
                    phone_input.setError("type your phone number");
                }
                else if(!isValidPhone(ph_num)){
                    phone_input.setError("invalid phone number");
                }
                else if(date_naiss.isEmpty()){
                    birthdate_input.setError("type your birthdate");
                }
                else if(!isValidBirthdate(date_naiss)){
                    birthdate_input.setError("birthdate must be dd/mm/yyyy");
                }
                else if(pwd.isEmpty()){
                    pwd_input.setError("type your password");
                }
                else if(!isValidPassword(pwd)){
                    pwd_input.setError("password must contain at least 6 alphabets and digits");
                }
                else{
                    // Valid input
                    user.setProfile_image("https://www.youtube.com/watch?v=lHZwlzOUOZ4");
                    Toast.makeText(SignUpActivity.this,user.getEmail()+" | "+user.getFull_name(),Toast.LENGTH_LONG);
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

        Objects.requireNonNull(birthdate_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String date_naiss = s.toString().trim();
                if (date_naiss.isEmpty() || !isValidBirthdate(date_naiss)) {
                    birthdate_input.setError("Invalid birthdate");
                }
                else {
                    LocalDate birthdatee = LocalDate.parse(date_naiss, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate currentDate = LocalDate.now();
                    if (birthdatee.isAfter(currentDate)) {
                        birthdate_input.setError("Birthdate cannot be in the future");
                    } else {
                        birthdate_input.setError(null);
                        user.setBirthdate(date_naiss);
                    }
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
        String regex = "^(0[1-9]|[1-2][0-9]|3[01])/(0[1-9]|1[0-2])/((19|20)\\d\\d)$";
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