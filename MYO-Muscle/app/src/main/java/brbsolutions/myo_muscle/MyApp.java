package brbsolutions.myo_muscle;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alex on 10/9/2016.
 */
public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate(){
        instance = this;
        super.onCreate();
    }
}
