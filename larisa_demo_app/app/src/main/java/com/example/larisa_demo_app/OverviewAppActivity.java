package com.example.larisa_demo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OverviewAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_app);
    }
    public void OverviewProcessTreeLauncher(View v) {
        Intent i = new Intent(this, ProcessTreeAppActivity.class);
        startActivity(i);
    }
}