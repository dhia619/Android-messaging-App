package com.example.messenger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import adapter.MessageAdapter;

import com.example.messenger.ImageHandling;
public class MessagingActivity extends AppCompatActivity {

    ImageView profile_Image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    ImageButton send_btn;
    EditText send_text;

    MessageAdapter msgAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

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


        recyclerView = findViewById(R.id.msg_container);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_Image = findViewById(R.id.profile_img_msg);
        username = findViewById(R.id.usernamechat);

        send_btn = findViewById(R.id.send_btn);
        send_text = findViewById(R.id.msg_input);



        intent = getIntent();
        String userId = intent.getStringExtra("userId");
        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = send_text.getText().toString().trim();
                if (!msg.equals("")){
                    sendMessage(firebaseUser.getUid(), userId, msg);
                }else{
                    Toast.makeText(MessagingActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
                send_text.setText("");
            }
        });

        if (userId != null) {
            reference = FirebaseDatabase
                    .getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("users").child(userId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Toast.makeText(MessagingActivity.this, "misssage", Toast.LENGTH_SHORT).show();
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        username.setText(user.getFull_name());
                        if (user.getProfile_image().equals("")) {
                            profile_Image.setImageResource(R.mipmap.ic_default_avatar_round);
                        } else {
                            //Glide.with(MessagingActivity.this).load(user.getProfile_image()).into(profile_Image);
                            byte[] image_data = ImageHandling.getImageBytesFromBase64(user.getProfile_image());
                            Bitmap bitmap = BitmapFactory.decodeByteArray(image_data, 0, image_data.length);
                            profile_Image.setImageBitmap(bitmap);
                        }
                    }
                    assert user != null;
                    displayMsgs(firebaseUser.getUid(), userId, user.getProfile_image());
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

    private void sendMessage(String sender, String receiver, String msg){

        DatabaseReference reference = FirebaseDatabase.getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        HashMap<String,Object> hashmap = new HashMap<>();
        hashmap.put("sender", sender);
        hashmap.put("receiver", receiver);
        hashmap.put("message", msg);

        reference.child("chats").push().setValue(hashmap);
    }

    private void displayMsgs(String myId, String userId, String Img){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/").getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId) ){
                        mChat.add(chat);
                    }
                    msgAdapter = new MessageAdapter(MessagingActivity.this,mChat , Img);
                    recyclerView.setAdapter(msgAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
