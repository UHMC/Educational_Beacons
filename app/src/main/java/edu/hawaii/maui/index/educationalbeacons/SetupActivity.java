package edu.hawaii.maui.index.educationalbeacons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        // This block creates a file to keep track of the website.
        FileWriter fw;
        File file = new File(getFilesDir().getPath().toString() + "/website.txt");
        try {
            file.createNewFile();
            fw = new FileWriter(file, true);
            fw.write("http://yourschool.com");
            fw.flush();
            fw.close();

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
            fw = new FileWriter(file,true);
            fw.write(website.getText().toString());
            fw.flush();
            fw.close();


        } catch(Exception e){
            e.printStackTrace();
        }




    }
}
