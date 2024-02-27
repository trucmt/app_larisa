package com.example.myapplication;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
public class ScanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(this.getString(R.string.scanning));
        }
        boolean withSysApps = getIntent().getBooleanExtra("withSysApps", false);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView percentText = findViewById(R.id.percentText);
        TextView statusText = findViewById(R.id.statusText);
        TextView secondarystatusText = findViewById(R.id.secondaryStatusText);
        Button stopButton = findViewById(R.id.stopButton);

        progressBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        progressBar.setProgress(0);


        final ScannerTask scanner = new ScannerTask(this, ScanActivity.this);
        scanner.setProgressBar(progressBar);
        scanner.setPercentText(percentText);
        scanner.setStatusText(statusText);
        scanner.setSecondaryStatusText(secondarystatusText);
        scanner.setWithSysApps(withSysApps);
        scanner.execute();

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner.cancel(true);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Back button is disabled while scanning
    }
}