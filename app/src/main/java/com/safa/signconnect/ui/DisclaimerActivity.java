package com.safa.signconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.safa.signconnect.R;

public class DisclaimerActivity extends AppCompatActivity {
    private final String TAG = "DisclaimerActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        CheckBox checkBox1 = findViewById(R.id.checkBox1);
        CheckBox checkBox2 = findViewById(R.id.checkBox2);
        Button btnDisclaimerAccept = findViewById(R.id.btnDisclaimerAccept);
        btnDisclaimerAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBox1.isChecked() || !checkBox2.isChecked())
                    Toast.makeText(DisclaimerActivity.this, "Terms and conditions must be accepted to proceed.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(DisclaimerActivity.this, ChooseLanguageActivity.class);
                    intent.putExtra("isSigned", true);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}