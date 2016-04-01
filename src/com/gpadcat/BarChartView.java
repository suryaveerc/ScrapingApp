package com.gpadcat;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;
import com.analyze.AnalysisFacade;
import com.models.ScrappedModel;
import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.renderer.category.BarRenderer;
import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by chauh on 2016-04-01.
 */
public class BarChartView extends DemoView {

    static HashMap<String, ArrayList<String>> scrappedData;
    static ArrayList<CheckBox> checkBoxes;
    static ArrayList<String> stringKeys = new ArrayList<String>() {{
        add("contentRating");
        add("numDownloads");
        add("operatingSystems");
        add("datePublished");
        add("AppName");
        add("Url");
    }};
    private static String title = "";
    private static ArrayList<String> dataPoints;
    ScrappedModel scrappedModel;
    AnalysisFacade analysisFacade;

    public BarChartView(Context context, ArrayList<CheckBox> checkBoxes) {
        super(context);
        System.out.println("In CONSTRUCTOR");
        this.checkBoxes = checkBoxes;
        analysisFacade = AnalysisFacade.getInstance();
        scrappedData = analysisFacade.getScrappedModel();
        System.out.println("!!!!!BarChartView!!!!!!!!" + scrappedData.toString());
        CategoryDataset dataset = createDataset();
        AFreeChart chart = createChart(dataset);


        setChart(chart);


    }

    static CategoryDataset createDataset() {
        System.out.println("In CategoryDS Start");
        ArrayList<ArrayList<String>> selectedCheckBoxes = new ArrayList<>();
        dataPoints = new ArrayList<>();
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


        System.out.println("Datapoints after: " + selectedCheckBoxes);
        // ArrayList<Integer> applicationParamInteger = new ArrayList<Integer>();


        // TODO add your handling code here:

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        System.out.println(totalValues);
        for (int k = 0; k < totalValues; k++) {
            System.out.println(selectedCheckBoxes.get(0).get(k) + "***");
            System.out.println(selectedCheckBoxes.get(1).get(k));
            dataset.addValue(Double.parseDouble(selectedCheckBoxes.get(1).get(k)), "Ratings", selectedCheckBoxes.get(0).get(k));
        }


        return dataset;
    }

    private static AFreeChart createChart(CategoryDataset categoryDataset) {
        AFreeChart chart = ChartFactory.createBarChart(title, dataPoints.get(0), dataPoints.get(1), categoryDataset, PlotOrientation.VERTICAL, true, true, false);
        // AFreeChart chart = ChartFactory.createBarChart(title, "A", "B", categoryDataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);


        GradientColor gp1 = new GradientColor(Color.RED, Color.rgb(255, 0, 0));
        renderer.setSeriesPaintType(0, gp1);

       /* CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));*/
        return chart;
    }
}
