package com.example.messenger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.model.content.CircleShape;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MessagingActivity extends AppCompatActivity {

    ImageView profile_Image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.ToolbarMsg);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profile_Image = findViewById(R.id.profile_img_msg);
        username = findViewById(R.id.usernamechat);

        intent = getIntent();
        String userId = intent.getStringExtra("userId");
        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (userId != null) {
            reference = FirebaseDatabase
                    .getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("users").child(userId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        username.setText(user.getFull_name());
                        if (user.getProfile_image().equals("default")) {
                            profile_Image.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Glide.with(MessagingActivity.this).load(user.getProfile_image()).into(profile_Image);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            // Handle the case when userid is null
        }
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
}
