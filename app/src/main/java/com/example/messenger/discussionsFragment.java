package com.example.messenger;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapter.UserAdapter;

public class discussionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private User user;
    String userId = "";



    public discussionsFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discussions, container, false);
        CircularProgressIndicator progressIndicator;
        progressIndicator = view.findViewById(R.id.home_loader);
        progressIndicator.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view);  // Ensure correct ID
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        Bundle bundle = getArguments();

        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
            if (user != null) {
                // Use the user object as needed
                userId = user.getId();
            }
        }

        if (!isDeviceOnline()){

            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }else{
            readUsers(progressIndicator);

        }


        progressIndicator.setVisibility(View.GONE);
        return view;
    }

    private void readUsers( CircularProgressIndicator loader) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://messaging-app-d78bd-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        user.setId(snapshot.getKey());
                        if (firebaseUser != null && user.getId().equals(firebaseUser.getUid())) {
                            user.setOnline(true); // Set online attribute to true for the current user locally
                            // Update online status in the Firebase Realtime Database
                            DatabaseReference userRef = snapshot.getRef();
                            userRef.child("online").setValue(user.getOnline());
                        }
                        mUsers.add(user);
                    }
                }

                // Update RecyclerView with the updated user list
                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
    private boolean isDeviceOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null) {
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return false;
    }

}