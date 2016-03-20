package com.gpadcat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


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
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText("You pressed " + fileName);
        //Write code here to readCSV
    }

    public void handleStartScrappingClick(View arg0) {
        Button btn = (Button) arg0;
//Write code here to startScrapping
    }
}
