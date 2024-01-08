package com.example.larisa_demo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ProcessTreeAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_tree_app);
    }
    public void homeLauncher(View v) {
        Intent i = new Intent(this, OverviewAppActivity.class);
        startActivity(i);
    }
}