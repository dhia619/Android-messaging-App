package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase authentication and database reference
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/");
        usersRef = database.getReference("users");

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
                    User user = new User(); // Create the user object with data
                    user.setFull_name(full_name);
                    user.setEmail(email);
                    user.setPhone_number(ph_num);
                    user.setBirthdate(date_naiss);
                    user.setPwd(pwd);

                    mAuth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    String userId = firebaseUser.getUid();

                                    // Write user data to the database outside the nested listener
                                    usersRef.child(userId).setValue(user)
                                            .addOnCompleteListener(databaseTask -> {
                                                if (databaseTask.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_LONG).show();
                                                    // Redirect to another activity after successful sign up
                                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Failed to write user data to the database", Toast.LENGTH_LONG).show();
                                                    Log.e("Firebase", "Error writing user data to the database", databaseTask.getException());
                                                }
                                            });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to create account. Please try again.", Toast.LENGTH_LONG).show();
                                    Log.e("Firebase", "Error creating user account", task.getException());
                                }
                            });
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

        Objects.requireNonNull(full_name_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String full_name = s.toString().trim();
                if (full_name.isEmpty()) {
                    full_name_input.setError("Invalid user name");
                } else {
                    full_name_input.setError(null);
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
                if (email.isEmpty() || !isValidNewEmail(email)) {
                    email_input.setError("Invalid email address");
                } else {
                    email_input.setError(null);
                }
            }
        });

        Objects.requireNonNull(phone_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim();
                if (phone.isEmpty() || !isValidPhone(phone)) {
                    phone_input.setError("Invalid Phone Number");
                } else {
                    phone_input.setError(null);
                }
            }
        });

        Objects.requireNonNull(birthdate_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String date_naiss = s.toString().trim();
                if (date_naiss.isEmpty() || !isValidBirthdate(date_naiss)) {
                    birthdate_input.setError("Invalid birthdate");
                } else {
                    birthdate_input.setError(null);
                }
            }
        });

        Objects.requireNonNull(pwd_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = s.toString().trim();
                if (pwd.isEmpty() || !isValidPassword(pwd)) {
                    pwd_input.setError("Weak Password");
                } else {
                    pwd_input.setError(null);
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
        if (password.length()< 6) {
            return false;
        }
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).+$";
        return password.matches(regex);
    }
}

