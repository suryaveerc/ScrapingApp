package com.gpadcat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.analyze.AnalysisFacade;
import com.scrape.GenerateXML;
import com.scrape.ScrapeMain;

import java.io.File;
import java.io.IOException;


public class MyActivity extends Activity {

    private String fileName;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.main);
    }

    public void handleSelectCSVClick(View arg0) {
        Button btn = (Button) arg0;

        Intent myIntent = new Intent(this, FileChooser.class);


        int resultCode = 0;
        Log.d("debug", "here");
        this.startActivityForResult(myIntent, resultCode);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fileName = data.getStringExtra("MESSAGE");
        Log.d("debug", fileName);
        if (fileName.contains(".csv")) {
            Button buttonStartScrapping = (Button) findViewById(R.id.buttonStartScrapping);
            Button buttonViewCSV = (Button) findViewById(R.id.buttonViewCSV);
            TextView tv = (TextView) findViewById(R.id.textView1);
            tv.setText("Selected file is: " + fileName);
            buttonStartScrapping.setEnabled(true);
            buttonViewCSV.setEnabled(true);
        } else if (fileName.contains(".xml")) {
            Button buttonViewXML = (Button) findViewById(R.id.buttonViewXML);
            Button buttonAnalyze = (Button) findViewById(R.id.buttonAnalyze);
            GenerateXML.saveXML(fileName);
            buttonViewXML.setEnabled(true);
            buttonAnalyze.setEnabled(true);
            TextView tv = (TextView) findViewById(R.id.textView1);
            tv.setText("File Saved at : " + fileName);
            Toast.makeText(this, "File Saved at : " + fileName, Toast.LENGTH_SHORT).show();
        }

        //Write code here to readCSV
    }

    public void handleStartScrappingClick(View arg0) {
        Button buttonSaveXML = (Button) findViewById(R.id.buttonSaveXML);
        Log.d("handleStartScrapping", fileName);
        try {
            ScrapeMain.scrapeFile(new File(fileName));
            Toast.makeText(this, "Scrapping Done ", Toast.LENGTH_SHORT).show();
            TextView tv = (TextView) findViewById(R.id.textView1);
            tv.setText("Scrapping Done.");
            buttonSaveXML.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
//Write code here to startScrapping
    }

    public void handleSaveXMLClick(View arg0) {

        Intent myIntent = new Intent(this, FileChooser.class);
        int resultCode = 0;
        Log.d("debug", "here");
        this.startActivityForResult(myIntent, resultCode);


//Write code here to startScrapping
    }

    public void handleViewXMLClick(View arg0) {
        Log.d("handleViewXMLClick", fileName);
        //source: http://stackoverflow.com/questions/7009452/how-to-launch-browser-to-open-local-file#
        final Uri uri = Uri.fromFile(new File(fileName));
        try {
            final Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            browserIntent.setData(uri);
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            final Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setDataAndType(Uri.fromFile(new File(fileName)), "text/xml");
            startActivity(browserIntent);
        }
    }

    public void handleAnalyzeClick(View arg0) {

        AnalysisFacade facade = AnalysisFacade.getInstance();
        facade.analyseData(fileName);
        Intent myIntent = new Intent(this, AnalysisUI.class);
        //myIntent.putExtra("key", value); //Optional parameters
        this.startActivity(myIntent);
    }
}
