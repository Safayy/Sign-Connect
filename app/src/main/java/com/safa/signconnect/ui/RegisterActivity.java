package com.safa.signconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.safa.signconnect.R;
import com.safa.signconnect.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private EditText nameEditText;
    private TextView formToggleTextView;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.etFormEmail);
        passwordEditText = findViewById(R.id.etFormPassword);
        loginButton = findViewById(R.id.btnFormSubmitRegister);
        loadingProgressBar = findViewById(R.id.loading);
        formToggleTextView = findViewById(R.id.tvFormToggle);
        passwordConfirmEditText = findViewById(R.id.etFormPasswordConfirm);;
        nameEditText = findViewById(R.id.etFormName);

        formToggleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String passwordConfirm = passwordConfirmEditText.getText().toString();

                if (!passwordConfirm.equals(password)){
                    Toast.makeText(RegisterActivity.this,
                            "Confirmation password must match password.",
                            Toast.LENGTH_SHORT).show();
                }
                else if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    String TAG = "RegisterActivity";
                                    loadingProgressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                    Log.d(TAG, "Registered User Successfully");
                                        Toast.makeText(RegisterActivity.this, "SUCCESSFUL REGISTER",
                                                Toast.LENGTH_SHORT).show();

                                        // Prompt user to login
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.d(TAG, "Register User Failure");
                                        Toast.makeText(RegisterActivity.this, "Authentication failed. If you already have an account, please login.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Valid information required for Registration.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}