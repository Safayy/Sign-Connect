/**
 * Profile Activity to display user information
 */
package com.safa.signconnect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.safa.signconnect.R;
import com.safa.signconnect.TokenManager;
import com.safa.signconnect.ui.MainActivity;
import com.safa.signconnect.ui.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity {
    private final String TAG = "ProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvTitleProfile = findViewById(R.id.tvTitleProfile);
        TextView tvLogout = findViewById(R.id.tvLogout);
        EditText etTokens = findViewById(R.id.etTokens);
        etTokens.setText(String.valueOf(TokenManager.getInstance().getTokens()));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userEmailWithoutDomain = userEmail.split("@")[0];
        tvTitleProfile.setText(userEmailWithoutDomain);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                mAuth.signOut();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}