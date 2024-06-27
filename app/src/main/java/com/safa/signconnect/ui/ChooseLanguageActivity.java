package com.safa.signconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safa.signconnect.DBHelper;
import com.safa.signconnect.R;
import com.safa.signconnect.ui.adapter.LanguageAdapter;

public class ChooseLanguageActivity extends AppCompatActivity {
    private final String TAG = "ChooseLanguageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        // Display Value for Language Type
        Intent intent = getIntent();
        boolean isSigned = intent.getBooleanExtra("isSigned", false);

        // Setup Recycler View Adapter
        RecyclerView languageRecycler = findViewById(R.id.rvLanguages);
        LanguageAdapter languageAdapter;
        if (isSigned) {
            languageAdapter = new LanguageAdapter(
                    getResources().getStringArray(R.array.lang_sign_names),
                    getResources().obtainTypedArray(R.array.lang_sign_imgs),
                    new LanguageAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String languageName) {
                            Log.d(TAG, "Signed Language Selected : " + languageName);

                            DBHelper dbHelper = new DBHelper(getApplicationContext());
                            dbHelper.setLanguage("signedLanguage", languageName);

                            Intent intent = new Intent(getApplicationContext(), ChooseLanguageActivity.class);
                            intent.putExtra("isSigned", false);
                            startActivity(intent);
                            finish();
                        }
                    }
            );
        } else {
            languageAdapter = new LanguageAdapter(
                    getResources().getStringArray(R.array.lang_spoken),
                    new LanguageAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String languageName) {
                            Log.d(TAG, "Spoken Language Selected : " + languageName);
                            DBHelper dbHelper = new DBHelper(getApplicationContext());
                            dbHelper.setLanguage("spokenLanguage", languageName);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
            );
        }
        languageRecycler.setAdapter(languageAdapter);
        languageRecycler.setLayoutManager(new GridLayoutManager(this, 2));
    }
}