package edu.hawaii.maui.index.educationalbeacons;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        // This block creates a file to keep track of the website.
        File file = new File(getFilesDir().getPath().toString() + "/website.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
            TextView currentSite = (TextView) findViewById(R.id.currentWebsite);
            currentSite.setText("Current website: \n" + bfr.readLine());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void submit(View v) {
        // Add a textview to pull the website inputted
        TextView website = (TextView) findViewById(R.id.website);
        // Make a filewriter object to save the website
        FileWriter fw;
        File file = new File(getFilesDir().getPath().toString() + "/website.txt");
        // Write website to file
        try {
            // Delete the file first.
            file.delete();
            // Set up I/O and write the info from EditText into the file
            fw = new FileWriter(file, true);
            fw.write(website.getText().toString());
            fw.flush();
            fw.close();
            // Update current website
            FileInputStream fis = new FileInputStream(file);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
            TextView currentSite = (TextView) findViewById(R.id.currentWebsite);
            currentSite.setText("Current website: " + bfr.readLine());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
