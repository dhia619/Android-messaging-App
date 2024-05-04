package com.example.messenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import adapter.UserAdapter;

public class profileFragment extends Fragment {
    private ImageView profile_Image;
    private DatabaseReference usersRef;
    private User user;

    private SwitchMaterial status_switcher;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        status_switcher = view.findViewById(R.id.status_switcher);






        LinearLayout usernameLay = view.findViewById(R.id.usernameLayout);
        LinearLayout statusLay = view.findViewById(R.id.statusLayout);
        LinearLayout friendsLay = view.findViewById(R.id.friendsLayout);
        LinearLayout logoutLay = view.findViewById(R.id.logoutLayout);

        TextView usernamelbl = view.findViewById(R.id.usernameView);
        TextView onofflbl = view.findViewById(R.id.OnOfflbl);

        // Initialize intent
        Bundle bundle = getArguments();
        String userId = "";
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
            if (user != null) {
                // Use the user object as needed
                userId = user.getId();
            }
        }


        String finalUserId = userId;
        status_switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                usersRef = FirebaseDatabase.getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("users").child(finalUserId);

                usersRef.child("online").setValue(isChecked);
            }
        });


        if (!TextUtils.isEmpty(userId)) {
            usersRef = FirebaseDatabase.getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("users").child(userId);
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userData = snapshot.getValue(User.class);
                    if (userData != null) {
                        usernamelbl.setText(userData.getFull_name());
                        profile_Image = view.findViewById(R.id.profilePicture);
                        String profileImage = userData.getProfile_image();
                        if(userData.getOnline()){
                            onofflbl.setText("On");
                            onofflbl.setTextColor(0xFF00FF00);
                        }
                        else{
                            onofflbl.setText("Off");
                            onofflbl.setTextColor(0xFFFF0000);
                        }
                        if (profileImage == null || profileImage.isEmpty()) {
                            // If profile image is null or empty, set default image
                            profile_Image.setImageResource(R.mipmap.ic_default_avatar_round);
                            //Toast.makeText(getContext(), "No profile image found", Toast.LENGTH_SHORT).show();
                        } else {
                            // Load profile image from base64 string
                            byte[] image_data = ImageHandling.getImageBytesFromBase64(profileImage);
                            if (image_data != null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(image_data, 0, image_data.length);
                                if (bitmap != null) {
                                    profile_Image.setImageBitmap(bitmap);
                                    //Toast.makeText(getContext(), "Profile image loaded successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(getContext(), "Failed to decode profile image", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //Toast.makeText(getContext(), "Failed to retrieve profile image data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        //Toast.makeText(getContext(), "Failed to retrieve user data from Firebase", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Error fetching user data: " + error.getMessage());
                }
            });
        } else {
            // Handle the case when userId is null
            Log.e("Fragment", "User ID is null");
        }

        statusLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ChangeOnlineStatusActivity.class);
                startActivity(intent);
            }
        });

        friendsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),FriendsActivity.class);
                startActivity(intent);
            }
        });

        usernameLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ChangeUsernameActivity.class);
                startActivity(intent);
            }
        });

        logoutLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectUser();
            }
        });

        return view;
    }

    // Method to delete user data from Firebase Realtime Database
    private void disconnectUser() {
        // Redirect to sign-in activity
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
            String currentUserId = currentUser.getUid();
            // Update user's online status to false in the Realtime Database
            usersRef.child(currentUserId).child("online").setValue(false)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Online status updated successfully, now sign out the user
                            FirebaseAuth.getInstance().signOut(); // Sign out the user after disconnecting
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Signed out", Toast.LENGTH_SHORT).show();
                            }
                            // Redirect to sign-in activity
                            SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());
                            sharedPreferencesManager.setRememberMeChecked(false);

                            Intent intent = new Intent(requireActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Failed to sign out", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }




}