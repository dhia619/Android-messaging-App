package com.example.messenger;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ChangeUsernameActivity extends AppCompatActivity {

    private DatabaseReference reference;
    boolean x = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        ImageButton backbtn = findViewById(R.id.backButton);
        Button savebtn = findViewById(R.id.saveButton);
        TextInputLayout name_input = findViewById(R.id.fullNameFieldLayout);
        // Initialize Firebase components
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recupérer valeur du champ nouveau nom
                String name = name_input.getEditText().getText().toString().trim();
                if (name.isEmpty()) {
                    name_input.setError("Type a new username");
                } else {
                    name_input.setError(null);
                    updateUsername(currentUser,name);
                    /*boolean y = updateUsername(currentUser,name);
                    if(y){
                        Toast.makeText(ChangeUsernameActivity.this, "done", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ChangeUsernameActivity.this, "undone", Toast.LENGTH_SHORT).show();
                    }*/
                    finish();
                }
            }
        });

        Objects.requireNonNull(name_input.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = s.toString().trim();
                if (pwd.isEmpty()) {
                    name_input.setError("Type a new username");
                } else {
                    name_input.setError(null);
                    //set new name logic
                }
            }
        });

    }
    private void updateUsername(FirebaseUser user, String newUsername) {
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference userRef = reference.child("users").child(uid);
            //Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
            userRef.child("full_name").setValue(newUsername)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Username updated successfully
                                Toast.makeText(ChangeUsernameActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                                //x =  true;
                            } else {
                                // Failed to update username
                                Toast.makeText(ChangeUsernameActivity.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                                //x =  false;
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log the error
                            Log.e(TAG, "Error updating username", e);
                        }
                    });
        }
        //return x;
    }
}