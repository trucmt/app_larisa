package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.PieChart;
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

        btnPieChart.setVisibility(View.VISIBLE);
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
