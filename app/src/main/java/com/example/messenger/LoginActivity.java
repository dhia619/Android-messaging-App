package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import kotlin.jvm.Volatile;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private User user = new User();

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AuthenticationManager mAuthManager = new AuthenticationManager(this);

        mAuth = FirebaseAuth.getInstance();

        MyDataBase localDB = new MyDataBase(getApplicationContext());

        Button log_in = findViewById(R.id.loginbtn);
        TextView sign_in = findViewById(R.id.sign_in);

        TextInputLayout email_input = findViewById(R.id.emailFieldLayout);
        TextInputLayout pwd_input = findViewById(R.id.pwdFieldLayout);

        TextView forgot_pwd = findViewById(R.id.forgotPWD);

        CheckBox rememberme = findViewById(R.id.rememberme);

        // Retrieve the stored value of remember me from shared preferences

        boolean rememberMeChecked = SharedPreferencesManager.getInstance(getApplicationContext()).getRememberMeChecked();
        rememberme.setChecked(rememberMeChecked);


        /*if(rememberMeChecked){
            User old_user = localDB.retrieveFirstUser();
            mAuthManager.authenticateOffline(old_user.getEmail(), old_user.getPwd());
        }*/

        // Check connectivity status
        if (!isDeviceOnline()) {
            Snackbar.make(findViewById(android.R.id.content), "No internet connection you cant load Resources", Snackbar.LENGTH_LONG).show();
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null && rememberMeChecked){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }


        // Add OnCheckedChangeListener to remember me checkbox
        // Assuming sharedPreferences is initialized somewhere in your activity
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        rememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Store the value of remember me in shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("rememberMe", isChecked);
                editor.apply();
            }
        });


        // Redirection to Sign in
        sign_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Attempt login
        log_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = email_input.getEditText().getText().toString();
                String pwd = pwd_input.getEditText().getText().toString();

                // Check if user input is valid
                if (email.isEmpty()) {
                    email_input.setError("Type your email");
                } else if (!isValidEmail(email)) {
                    email_input.setError("Invalid email address");
                } else if (pwd.isEmpty()) {
                    pwd_input.setError("Type your password");
                } else if (!isValidPassword(pwd)) {
                    pwd_input.setError("Password must contain at least 6 alphabets and digits");
                } else {
                    // Authenticate user
                    mAuthManager.authenticate(email, pwd);
                }
            }
        });

        forgot_pwd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Control on the Inputs
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
                    user.setEmail(email_input.getEditText().getText().toString());
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
                if (!pwd.isEmpty() && !isValidPassword(pwd)) {
                    pwd_input.setError("Password must contain at least 6 alphabets and digits");
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    // Method to handle authentication result
    public void handleAuthenticationResult(boolean isAuthenticated) {
        if (isAuthenticated) {
            // Authentication successful, proceed to home activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish(); // Optionally finish LoginActivity to prevent back navigation
            // Insert user data into the local database
        } else {
            // Authentication failed, show appropriate message to the user
            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isDeviceOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null) {
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return false;
    }


}
