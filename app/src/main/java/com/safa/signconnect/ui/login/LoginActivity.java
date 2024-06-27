package com.safa.signconnect.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.safa.signconnect.DBHelper;
import com.safa.signconnect.R;
import com.safa.signconnect.ui.DisclaimerActivity;
import com.safa.signconnect.ui.MainActivity;
import com.safa.signconnect.ui.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private TextView formToggleTextView;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        if (currentUser != null) {
            DBHelper dbHelper = new DBHelper(getApplicationContext());

            // Retrieve language from database
            String signedLanguage = dbHelper.getLanguage("signedLanguage");
            String spokenLanguage = dbHelper.getLanguage("spokenLanguage");
            Log.d("TAG", "SIGNED LANGUAGE "+signedLanguage);

            if(signedLanguage.isEmpty() || spokenLanguage.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), DisclaimerActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        emailEditText = findViewById(R.id.etFormEmail);
        passwordEditText = findViewById(R.id.etFormPassword);
        loginButton = findViewById(R.id.btnFormSubmitLogin);
        loadingProgressBar = findViewById(R.id.loading);
        formToggleTextView = findViewById(R.id.tvFormToggle);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "PKEASE");
            }
        });
        formToggleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Log.d("TAG", "Starting login");
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    String TAG = "LoginActivity";
                                    loadingProgressBar.setVisibility(View.GONE);
                                    Log.d(TAG, "On complete");
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), DisclaimerActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Login Failed. If you don't have an account create one.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Please include all required information.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    emailEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
    }
}