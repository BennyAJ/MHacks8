package brbsolutions.myo_muscle;

import android.util.Log;

import com.squareup.otto.Subscribe;

import emgvisualizer.model.Sensor;
import emgvisualizer.model.SensorUpdateEvent;
import emgvisualizer.ui.MySensorManager;

/**
 * Created by Benny on 10/8/16.
 */

public class Data_Handler {

    private Sensor sensor;

    private float spread;

    private float[] normalized;
    /**
     * Public constructor to create a new  GraphFragment
     */
    public Data_Handler() {
        this.sensor = MySensorManager.getInstance().getMyo();

        this.spread = sensor.getMaxValue() - sensor.getMinValue();
        Log.d("Spread", String.valueOf(spread));
        this.normalized = new float[sensor.getChannels()];
    }
    /**
     * Callback for sensor updated
     * @param event Just received event
     */
    @Subscribe
    public void onSensorUpdatedEvent(SensorUpdateEvent event) {
        if (!event.getSensor().getName().contentEquals(sensor.getName())) return;
        for (int i = 0; i < sensor.getChannels(); i++) {
            //normalized[i] = (event.getDataPoint().getValues()[i] - sensor.getMinValue()) / spread;
            normalized[i] = event.getDataPoint().getValues()[i];
            Log.d("Sensor" + i, String.valueOf(normalized[i]));
        }
    }
}
