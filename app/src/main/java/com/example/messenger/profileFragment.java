package com.example.messenger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileFragment extends Fragment {
    private ImageView profile_Image;
    private DatabaseReference usersRef;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        LinearLayout usernameLay = view.findViewById(R.id.usernameLayout);
        LinearLayout statusLay = view.findViewById(R.id.statusLayout);
        LinearLayout friendsLay = view.findViewById(R.id.friendsLayout);
        LinearLayout logoutLay = view.findViewById(R.id.logoutLayout);

        // Initialize intent
        Intent intent = getActivity().getIntent();
        Bundle bundle = getArguments();
        String userId = "";
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
            if (user != null) {
                // Use the user object as needed
                userId = user.getId();
            }
        }
        if (userId != "") {
            usersRef = FirebaseDatabase.getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("users").child(userId);
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userData = snapshot.getValue(User.class);
                    if (userData != null) {
                        profile_Image = view.findViewById(R.id.profilePicture);
                        String profileImage = userData.getProfile_image();
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
                Intent intent = new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
