package com.gpadcat;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.*;
import com.analyze.AnalysisFacade;
import com.models.ScrappedModel;
import org.afree.chart.ChartFactory;
import org.afree.chart.AFreeChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by chauh on 2016-03-31.
 */
public class AnalysisUI extends Activity {

    ArrayList<CheckBox> checkBoxes;
    ScrappedModel scrappedModel;
    HashMap<String, ArrayList<String>> scrappedData;
    AnalysisFacade analysisFacade;
    ArrayList<String> stringKeys = new ArrayList<String>() {{
        add("contentRating");
        add("numDownloads");
        add("operatingSystems");
        add("datePublished");
        add("AppName");
        add("Url");
    }};

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
        analysisFacade = AnalysisFacade.getInstance();
        scrappedData = analysisFacade.getScrappedModel();

    }

    public void handleBarChartClick(View arg0) {
        ArrayList<ArrayList<String>> selectedCheckBoxes = new ArrayList<>();
        ArrayList<String> dataPoints = new ArrayList<>();
        int tag = -1; //keep track of string key
        int selectedCount = 0;
        String title = "";
        Iterator itr = checkBoxes.iterator();
        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                if (stringKeys.contains(cb.getText().toString()))
                    tag = selectedCount;
                selectedCheckBoxes.add(scrappedData.get(cb.getText().toString()));
                System.out.println("Adding: " + cb.getText());
                dataPoints.add(cb.getText().toString());
                title = title.concat(cb.getText().toString()).concat(" vs ");
                System.out.println(title);
                selectedCount++;
            }
        }
        title = title.substring(0, (title.length()) - 3);
        System.out.println(title);
        System.out.println("Tag= " + tag + " SelectedCount= " + selectedCount);
        System.out.println("Datapoints before: " + selectedCheckBoxes);
        int totalValues = selectedCheckBoxes.get(0).size();
        if (tag >= 0) {
            Collections.swap(selectedCheckBoxes, tag, selectedCount - 1);
            Collections.swap(dataPoints, tag, selectedCount - 1);
        }


        System.out.println("Datapoints after: " + selectedCheckBoxes);
        // ArrayList<Integer> applicationParamInteger = new ArrayList<Integer>();


        // TODO add your handling code here:

        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        System.out.println(totalValues);
        for (int k = 0; k < totalValues; k++) {
            System.out.println(selectedCheckBoxes.get(0).get(k) + "***");
            System.out.println(selectedCheckBoxes.get(1).get(k));
            ds.setValue(Double.parseDouble(selectedCheckBoxes.get(0).get(k)), "Downloads", selectedCheckBoxes.get(1).get(k));
        }

        JFreeChart chart = ChartFactory.createBarChart(title, dataPoints.get(1), dataPoints.get(0), ds, PlotOrientation.HORIZONTAL, false, true, false);
        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.BLACK);
        //ChartFrame frame = new ChartFrame("Bar Chart for Apps", chart);
        ChartPanel CP = new ChartPanel(chart);
    }
}
