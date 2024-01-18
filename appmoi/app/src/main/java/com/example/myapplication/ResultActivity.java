/*
 * LibreAV - Anti-malware for Android using machine learning
 * Copyright (C) 2020 Project Matris
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;


public class ResultActivity extends AppCompatActivity {
    RecyclerView resultList;
    RecyclerView.LayoutManager layoutManager;
    AppsAdapter appsAdapter;
    public static ArrayList<AppInfo> apps;
    private Button btnList;
    private PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        btnList=findViewById(R.id.btnList);
        resultList = findViewById(R.id.resultList);
        pieChart = findViewById(R.id.pieChart);

        Button btnPieChart = findViewById(R.id.btnPieChart);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(this.getString(R.string.report));
        }
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart.setVisibility(View.GONE);
                resultList.setVisibility(View.VISIBLE);
                layoutManager = new LinearLayoutManager(ResultActivity.this);
                resultList.setLayoutManager(layoutManager);
                appsAdapter = new AppsAdapter(ResultActivity.this, apps);
                resultList.setAdapter(appsAdapter);
            }
        });
        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultList.setVisibility(View.GONE);
                showPieChart();
            }
        });
    }
    private void showPieChart() {
        pieChart.setVisibility(View.VISIBLE);
        int maudo = 0;
        int mauvang = 0;
        int mauxanh=0;

        for (AppInfo app : apps) {
            if (app.prediction.equalsIgnoreCase("malware")) {
                maudo++;
            } else if (app.prediction.equalsIgnoreCase("safe")) {
                mauxanh++;
            }
            else if(app.prediction.equalsIgnoreCase("risky")){
                mauvang++;
            }
        }

        List<PieEntry> entries = new ArrayList<>();
        PieDataSet dataSet = new PieDataSet(entries, "");
        List<Integer> colors = new ArrayList<>();
        if(maudo>0){
            entries.add(new PieEntry(maudo , "MALWARE"));
            colors.add(Color.RED);
        }
        if(mauvang>0){
            entries.add(new PieEntry(mauvang, "RISK"));
            colors.add(Color.YELLOW);
        }
        if(mauxanh>0){
            entries.add(new PieEntry(mauxanh, "SAFE"));
            colors.add(Color.GREEN);
        }
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.invalidate();
    }
}
