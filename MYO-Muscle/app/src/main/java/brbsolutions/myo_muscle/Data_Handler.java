package brbsolutions.myo_muscle;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.Calendar;

import emgvisualizer.model.RawDataPoint;
import emgvisualizer.model.Sensor;
import emgvisualizer.model.SensorUpdateEvent;
import emgvisualizer.ui.MySensorManager;

/**
 * Created by Benny on 10/8/16.
 */

public class Data_Handler {

    private Sensor sensor;

    // Contains only the most recent output from each data channel
    private RawDataPoint currentPoint;

    // Contains data collected by dataCollector
    private RawDataPoint[] rawData;

    // Used for async timing of trials
    private Handler handler;
    private Runnable dataCollector;

    // Used to keep track of timing in trials
    private int sampleDelay;
    private int elapsedTime;
    private int collectionTime;

    private Context context;
    private DatabaseHelper databaseHelper;
    private Calendar calendar;

    // General class for processing and saving data from the myo armband
    public Data_Handler(Context context) {
        this.sensor = MySensorManager.getInstance().getMyo();
        this.handler = new Handler();

        this.context = context;

        databaseHelper = new DatabaseHelper(context);
        calendar = Calendar.getInstance();

        elapsedTime = 0;

        // Collects and stores raw data for collectionTime seconds when called by handler
        dataCollector = new Runnable() {
            @Override
            public void run() {
                // Place current raw data point into the array
                int index = elapsedTime / sampleDelay;
                rawData[index] = currentPoint;


                // Add to elapsedTime and repeat if needed
                elapsedTime += sampleDelay;
                if (elapsedTime < collectionTime)
                    handler.postDelayed(dataCollector, sampleDelay);
            }
        };

    }
    /**
     * Callback for sensor updated
     * @param event Just received event
     */
    @Subscribe
    public void onSensorUpdatedEvent(SensorUpdateEvent event) {
        if (!event.getSensor().getName().contentEquals(sensor.getName())) return;
        this.currentPoint = event.getDataPoint();
    }

    public static float getAverageMagnitude(float[] values) {
        float average = 0;
        for (int i = 0; i < values.length; i++) {
            // Add up absolute value of all values in the given channel
            average += Math.abs(values[i]);
        }
        return (average / values.length);
    }

    // Runs dataCollector for milliseconds and returns an array of the samples taken
    public Trial collectData(int milliseconds, int sampleDelay) {
        this.collectionTime = milliseconds;
        this.sampleDelay = sampleDelay;

        // Initialize array for raw data points
        int length = collectionTime / sampleDelay;
        rawData = new RawDataPoint[length];

        handler.post(dataCollector);
        // Reset elapsed time when data is done collecting
        elapsedTime = 0;
        return (new Trial(0, rawData));
    }

    public Session packageSessionData(int routine, Trial[] trials) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return (new Session(day, month, year, 0, trials));
    }
}
