package com.example.messenger;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Intent intent;
    public User user = new User();

    // Create a bundle to hold the user object
    Bundle bundle = new Bundle();
    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (!isDeviceOnline()) {
            Snackbar.make(findViewById(R.id.home_container), "No internet connection you cant load Resources", Snackbar.LENGTH_LONG).show();
        }

        if (isDeviceOnline()){
            user.setId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }


        bundle.putSerializable("user", user);
        // Initialize selectedFragment before calling setArguments()
        selectedFragment = new discussionsFragment();
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        intent = getIntent();
        String profile_img = intent.getStringExtra("profile_image");
        topAppBar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.more) {
                    // Handle menu item 1 click
                    return true;
                }
                // Add more conditions for other menu items if needed
                return false;
            }
        });

        // Getting bottom navigation view and attaching the listener
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, selectedFragment).commit();
    }


    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.discussions) {
            selectedFragment = new discussionsFragment();
            selectedFragment.setArguments(bundle);
        } else if (itemId == R.id.profile) {
            selectedFragment = new profileFragment();
            selectedFragment.setArguments(bundle);
        }
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, selectedFragment).commit();
        return true;
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item selection here
        return true; // Return true to indicate that the event was handled
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