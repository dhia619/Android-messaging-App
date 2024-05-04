package com.example.messenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.messenger.MyDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationManager {

    private static final String TAG = "AuthenticationManager";
    private FirebaseAuth mAuth;
    private Context mContext;
    private MyDataBase mDb;

    public AuthenticationManager(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mDb = new MyDataBase(context);
    }

    public void authenticate(String email, String password) {
        if (isDeviceOnline()) {
            authenticateOnline(email, password);
        } else {
            authenticateOffline(email, password);
        }
    }

    private void authenticateOnline(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            ((LoginActivity)mContext).handleAuthenticationResult(true); // Pass authentication result to LoginActivity
                        } else {
                            ((LoginActivity)mContext).handleAuthenticationResult(false); // Pass authentication result to LoginActivity
                        }
                    }
                });
    }

    void authenticateOffline(String email, String password) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE email = ? AND pwd = ?", new String[]{email, password});
        boolean isAuthenticated = cursor.getCount() > 0;
        ((LoginActivity)mContext).handleAuthenticationResult(isAuthenticated); // Pass authentication result to LoginActivity
        cursor.close();
    }

    @SuppressLint("ObsoleteSdkInt")
    private boolean isDeviceOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }
}
