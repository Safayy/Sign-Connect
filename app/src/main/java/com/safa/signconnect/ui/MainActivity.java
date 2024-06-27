package com.safa.signconnect.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.safa.signconnect.ui.HistoryActivity;
import com.safa.signconnect.ui.ProfileActivity;
import com.safa.signconnect.R;
import com.safa.signconnect.TokenManager;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private TextView tvMainToken;

    private void updateToken(){
        Log.d(TAG,"ON UPDATE TOKEN" + String.valueOf(TokenManager.getInstance().getTokens()));
        tvMainToken.setText(
                getResources().getString(R.string.prompt_tokens_short,
                        TokenManager.getInstance().getTokens())
        );
    }

    @Override
    protected void onResume() {
        updateToken();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout clTranslate = findViewById(R.id.clTranslate);
        ConstraintLayout clHistory = findViewById(R.id.clHistory);
        ConstraintLayout clProfile = findViewById(R.id.clProfile);
        TextView tvGreetUser = findViewById(R.id.tvGreetUser);
        tvMainToken = findViewById(R.id.tvMainToken);
        updateToken();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userEmailWithoutDomain = userEmail.split("@")[0];
        tvGreetUser.setText(
                getResources().getString(R.string.greet_user,
                        userEmailWithoutDomain)
        );


        // Manage Navigation
        clTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TranslateActivity.class); //TranslateActivity
                startActivity(intent);
            }
        });
        clHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        clProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}