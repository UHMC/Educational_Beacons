package edu.hawaii.maui.index.educationalbeacons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileWriter;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

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
}
