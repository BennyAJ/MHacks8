package brbsolutions.myo_muscle;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import emgvisualizer.model.RawDataPoint;
import emgvisualizer.model.Sensor;
import emgvisualizer.model.SensorUpdateEvent;
import emgvisualizer.ui.MySensorManager;

/**
 * Created by Benny on 10/8/16.
 */

public class Data_Handler extends AppCompatActivity{

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
                if (elapsedTime < collectionTime) {
                    System.out.println("Continuing Handler");
                    handler.postDelayed(dataCollector, sampleDelay);
                }
                else {
                    System.out.println("Moving to save trial");
                    Trial[] tempTrial = {new Trial(0, rawData)};
                    ArrayList<RawDataPoint> printList = new ArrayList<RawDataPoint>(Arrays.asList(rawData));
                    RawDataPoint.printList(printList);

                    databaseHelper = new DatabaseHelper(MyApp.getContext());
                    databaseHelper.storeSession(packageSessionData(0, tempTrial));
                    databaseHelper.close();
                    System.out.println("This reaches the rear end of the data collector");
                }
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
        for(int i = 0; i < currentPoint.getValues().length; i++) {
            //Log.d("SI POOP:", String.valueOf(currentPoint.getValues()[i]));
        }
        if (currentPoint == null) {
            //Log.d("Null?", "YES BOOTY");
        }
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
        //email(new Trial(0, rawData));
        return (new Trial(0, rawData));
    }

    /*//Sends an email report to a doctor upon collection of data
    public void email(Trial data){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"roshinan@yahoo.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Myo Muscle Report");
        i.putExtra(Intent.EXTRA_TEXT, data.to_string());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        }catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Data_Handler.this, "There are no email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
        return;
    } */

    public Session packageSessionData(int routine, Trial[] trials) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return (new Session(day, month, year, 0, trials));
    }

    public DataPoint generateGraphPoint(ArrayList<RawDataPoint> raw, float index) {
        DataPoint point;
        float[] magnitudes = new float[raw.size()];

        for(int i = 0; i < raw.size(); i++) {
            RawDataPoint currentPoint = raw.get(i);
            float[] channels = currentPoint.getValues();
            magnitudes[i] = getAverageMagnitude(channels);
        }
        float overallMagnitude = getAverageMagnitude(magnitudes);
        point = new DataPoint(index, overallMagnitude);
        return point;
    }

    public LineGraphSeries<DataPoint> generateGraph(ArrayList<DataPoint> points) {
        LineGraphSeries<DataPoint> graph = new LineGraphSeries<DataPoint>();
        for(int i = 0; i < points.size(); i++) {
            graph.appendData(points.get(i), false, points.size(), false);
        }
        return graph;
    }

    public void storeSession(Session session) {
        databaseHelper.storeSession(session);
    }

    public RawDataPoint getCurrentPoint() {
        return currentPoint;
    }

    public RawDataPoint[] getRawData() {
        return rawData;
    }

}
