package com.gpadcat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.*;
import com.analyze.AnalysisFacade;
import com.models.ScrappedModel;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by chauh on 2016-03-31.
 */
public class AnalysisUI extends Activity {

    static ArrayList<String> stringKeys = new ArrayList<String>() {{
        add("contentRating");
        add("numDownloads");
        add("operatingSystems");
        add("datePublished");
        add("AppName");
        add("Url");
    }};
    ArrayList<CheckBox> checkBoxes;
    ScrappedModel scrappedModel;
    HashMap<String, ArrayList<String>> scrappedData;
    AnalysisFacade analysisFacade;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.analysis);
        analysisFacade = AnalysisFacade.getInstance();

        ArrayList<String> keys = analysisFacade.getAvailableKeys();
        FrameLayout flCB = (FrameLayout) findViewById(R.id.flCB);
        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        flCB.addView(sv);
        Iterator itr = keys.iterator();
        checkBoxes = new ArrayList<>();
        System.out.println("***************" + keys.size());
        while (itr.hasNext()) {

            String name = (String) itr.next();
            System.out.println(name);
            TableRow tbrow = new TableRow(this);
            CheckBox cb = new CheckBox(this);
            cb.setText(name);
            tbrow.addView(cb);
            ll.addView(tbrow);
            checkBoxes.add(cb);
        }
        scrappedData = analysisFacade.getScrappedModel();
        System.out.println("!!!!!!!!!!!!!!!!!!" + scrappedData.toString());
    }

    public void handleBarChartClick(View arg0) {

       /* BarChartView barChartView = new BarChartView(this, checkBoxes);
        FrameLayout flChart = (FrameLayout) findViewById(R.id.flChart);
        flChart.addView(barChartView);*/
        //ChartFrame frame = new ChartFrame("Bar Chart for Apps", chart);
        openChart();
    }

    private void openChart() {

        String title = "";
        ArrayList<ArrayList<String>> selectedCheckBoxes = new ArrayList<>();
        ArrayList<String> dataPoints = new ArrayList<>();
        int tag = -1; //keep track of string key
        int selectedCount = 0;

        Iterator itr = checkBoxes.iterator();
        System.out.println("In CategoryDS Start" + checkBoxes.size());
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                System.out.println("CHECKED: " + cb.getText().toString());
                if (stringKeys.contains(cb.getText().toString()))
                    tag = selectedCount;
                selectedCheckBoxes.add(scrappedData.get(cb.getText().toString()));
                System.out.println("Adding: " + cb.getText());
                dataPoints.add(cb.getText().toString());
                title = title.concat(cb.getText().toString()).concat(" vs ");
                System.out.println("Title: " + title);
                selectedCount++;
            }
        }
        title = title.substring(0, (title.length()) - 3);
        System.out.println(title);
        System.out.println("Tag= " + tag + " SelectedCount= " + selectedCount);
        System.out.println("Datapoints before: " + selectedCheckBoxes);
        int totalValues = selectedCheckBoxes.get(0).size();
    /*    if (tag >= 0) {
            Collections.swap(selectedCheckBoxes, tag, selectedCount - 1);
            Collections.swap(dataPoints, tag, selectedCount - 1);
        }*/


        double[] ratings = new double[totalValues];
        String appName[] = new String[totalValues];
        System.out.println(totalValues);
        for (int k = 0; k < totalValues; k++) {
            System.out.println(selectedCheckBoxes.get(0).get(k) + "***");
            System.out.println(selectedCheckBoxes.get(1).get(k));
            ratings[k] = Double.parseDouble(selectedCheckBoxes.get(1).get(k));
            appName[k] = selectedCheckBoxes.get(0).get(k);
            //dataset.addValue(Double.parseDouble(selectedCheckBoxes.get(1).get(k)), "Ratings", selectedCheckBoxes.get(0).get(k));
        }


        // Creating an  XYSeries for Income
        XYSeries ratingsSeries = new XYSeries("ratings");
        // Creating an  XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("Expense");
        // Adding data to Income and Expense Series
        for (int i = 0; i < ratings.length; i++) {
            ratingsSeries.add(i, ratings[i]);

        }

        // Creating a dataset to hold each series

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(ratingsSeries);

        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer ratingsRenderer = new XYSeriesRenderer();
        ratingsRenderer.setColor(Color.rgb(130, 130, 230));
        ratingsRenderer.setFillPoints(true);
        ratingsRenderer.setLineWidth(1);
        ratingsRenderer.setDisplayChartValues(true);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Rating vs AppName");
        multiRenderer.setYTitle("Rating");
        multiRenderer.setXTitle("ApName");
        multiRenderer.setBarSpacing(.5);
        multiRenderer.setZoomButtonsVisible(true);
        for (int i = 0; i < appName.length; i++) {
            multiRenderer.addXTextLabel(i, appName[i]);
        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(ratingsRenderer);


        // Creating an intent to plot bar chart using dataset and multipleRenderer
        Intent intent = org.achartengine.ChartFactory.getBarChartIntent(getBaseContext(), dataset, multiRenderer, BarChart.Type.HEAPED);

        // Start Activity
        startActivity(intent);

    }
}
