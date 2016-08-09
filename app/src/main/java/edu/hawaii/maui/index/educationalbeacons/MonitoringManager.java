package edu.hawaii.maui.index.educationalbeacons;
import android.app.Application;
public class MonitoringManager extends Application{
    private static MonitoringManager mInstance=null;
    // Disallow direct instantiation.
    private MonitoringManager(){}
    public MonitoringManager getInstanceForApplication(){
        if(mInstance==null)mInstance=new MonitoringManager();
        return mInstance;
    }
    public void startMonitoring(){

    }
    public void stopMonitoring(){

    }
}
