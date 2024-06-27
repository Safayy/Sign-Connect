/**
 *  Shows splashscreen image while application loads.
 *  Forwards users to their appropriate activity.
 */
package com.safa.signconnect.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.safa.signconnect.DBHelper;

import java.util.Timer;
public class SplashActivity extends Activity
{
    private static final long DELAY = 3000;
    private boolean scheduled = false;
    private Timer splashTimer;

    @Override
    public void onStart(){
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Retrieve languages from database
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String signedLanguage = dbHelper.getLanguage("signedLanguage");
        String spokenLanguage = dbHelper.getLanguage("spokenLanguage");
        Log.d("TAG", "Received from DB Signed Language : " + signedLanguage);
        Log.d("TAG", "Received from DB Spoken Language : " + spokenLanguage);

        // If preferences unset, send user to Disclaimer
        Intent intent;
        if(signedLanguage == null || spokenLanguage == null) {
            intent = new Intent(getApplicationContext(), DisclaimerActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}