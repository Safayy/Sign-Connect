package com.safa.signconnect.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.safa.signconnect.DBHelper;
import com.safa.signconnect.R;
import com.safa.signconnect.model.Translation;
import com.safa.signconnect.ui.adapter.HistoryAdapter;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private final String TAG = "HistoryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        ArrayList<Translation> translations = dbHelper.getAllTranslations();

        RecyclerView historyRecycler = findViewById(R.id.rvHistory);
        HistoryAdapter historyAdapter = new HistoryAdapter(translations, getApplicationContext());
        historyRecycler.setAdapter(historyAdapter);
        historyRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}