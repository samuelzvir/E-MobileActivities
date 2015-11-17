package br.com.samuelzvir.meuabc.activities;



import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import br.com.samuelzvir.meuabc.R;

public class StatsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setDescription("Tries");
        chart.setUsePercentValues(true);
        chart.setDescription("");
        chart.setDrawHoleEnabled(true);
        chart.setHoleColorTransparent(true);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        chart.setOnChartValueSelectedListener(
                new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                    }

                    @Override
                    public void onNothingSelected() {

                    }
                }
        );

        chart.setDragDecelerationFrictionCoef(0.95f);
        List<Entry> data = new ArrayList<>();

        data.add(new Entry(1,1));
//        data.add(new Entry(12,27));
        PieDataSet pieDataSet = new PieDataSet(data,"tries");


        PieData pieData = new PieData();
        pieData.setDataSet(pieDataSet);
        chart.setData(pieData);
    }

}
