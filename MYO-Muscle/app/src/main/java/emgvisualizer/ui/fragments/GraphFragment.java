/* This file is part of EmgVisualizer.

    EmgVisualizer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    EmgVisualizer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with EmgVisualizer.  If not, see <http://www.gnu.org/licenses/>.
*/
package emgvisualizer.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.otto.Subscribe;

import java.text.MessageFormat;
import java.util.LinkedList;

import brbsolutions.myo_muscle.Data_Handler;
import brbsolutions.myo_muscle.R;
import emgvisualizer.model.EventBusProvider;
import emgvisualizer.model.RawDataPoint;
import emgvisualizer.model.Sensor;
import emgvisualizer.model.SensorConnectEvent;
import emgvisualizer.model.SensorMeasuringEvent;
import emgvisualizer.model.SensorRangeEvent;
import emgvisualizer.model.SensorUpdateEvent;
import emgvisualizer.ui.MySensorManager;
import emgvisualizer.ui.views.SensorGraphView;

/**
 * Fragment for displaying sensors raw data
 * @author Nicola
 */
public class GraphFragment extends Fragment {

    /** TAG for debugging purpose */
    private static final String TAG = "GraphFragment";

    /** Framerate ms gap */
    private static final int FRAMERATE_SKIP_MS = 20;

    /** Reference to sensor graph custom view */
    private SensorGraphView graph;
    /** Reference to histtory line graph custom view */
    private GraphView lineGraph;
    /** Reference to sensor */
    private Sensor sensor;
    /** Reference to layout for error message */
    private RelativeLayout errorMessage;

    /** Point spread */
    private float spread = 0;
    /** Reference to thread handler */
    private Handler handler;
    /** Reference to runnable for graph timing */
    private Runnable runner;

    /** Array of normalized points */
    private float[] normalized;

    private Data_Handler data_handler = new Data_Handler(getActivity());
    private final float triggerAmplitude = 80;
    private final int collectionTime = 5000;
    private final int sampleDelay = 50;
    private int elapsedTime = 0;
    private boolean triggered = false;
    private float[] points;

    /**
     * Public constructor to create a new  GraphFragment
     */
    public GraphFragment() {
        this.sensor = MySensorManager.getInstance().getMyo();
        this.normalized = new float[sensor.getChannels()];
        EventBusProvider.register(data_handler);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_graph, container, false);
        graph = (SensorGraphView) view.findViewById(R.id.graph_sensorgraphview);
        errorMessage = (RelativeLayout) view.findViewById(R.id.graph_error_view);
        lineGraph = (GraphView) view.findViewById(R.id.line_graph);

        checkForErrorMessage();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0,1),
                new DataPoint(1,7),
                new DataPoint(2,8),
                new DataPoint(3,4),
                new DataPoint(4,9),
                new DataPoint(5,15)
        });
        lineGraph.addSeries(series);

        return view;
    }

    /**
     * Private method to check if an error message must be displayed
     */
    private void checkForErrorMessage() {
        Log.d(TAG, "Measuring: " + sensor.isMeasuring() + " Status conn: " + sensor.isConnected());
        if (sensor.isMeasuring() && sensor.isConnected()) {
            errorMessage.setVisibility(View.GONE);
            graph.setVisibility(View.VISIBLE);
            lineGraph.setVisibility(View.VISIBLE);
        } else {
            errorMessage.setVisibility(View.VISIBLE);
            graph.setVisibility(View.GONE);
            lineGraph.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBusProvider.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBusProvider.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseSensorData();
    }

    /**
     * Method to initialize sensor data to be displayed
     */
    protected void initialiseSensorData() {
        spread = sensor.getMaxValue() - sensor.getMinValue();
        LinkedList<RawDataPoint> dataPoints = sensor.getDataPoints();

        if (dataPoints == null || dataPoints.isEmpty()) {
            Log.w("sensor data", "no data found for sensor " + sensor.getName());
            return;
        }

        int channels = sensor.getChannels();

        LinkedList<Float>[] normalisedValues = new LinkedList[channels];
        for (int i = 0; i < channels; ++i) {
            normalisedValues[i] = new LinkedList<Float>();
        }


        for (RawDataPoint dataPoint : dataPoints) {
            for (int i = 0; i < channels; ++i) {
                float normalised = (dataPoint.getValues()[i] - sensor.getMinValue()) / spread;
                normalisedValues[i].add(normalised);
            }
        }

        this.graph.setNormalisedDataPoints(normalisedValues, sensor);

        this.graph.setZeroLine((0 - sensor.getMinValue()) / spread);

        this.graph.setMaxValueLabel(MessageFormat.format("{0,number,#}", sensor.getMaxValue()));
        this.graph.setMinValueLabel(MessageFormat.format("{0,number,#}", sensor.getMinValue()));
    }

    /**
     * Callback for sensor connect event
     * @param event Just received event
     */
    @Subscribe
    public void onSensorConnectEvent(SensorConnectEvent event) {
        if (event.getSensor().getName().contentEquals(sensor.getName())) {
            checkForErrorMessage();
            Log.d(TAG, "Event connected received " + event.getState());
        }
    }

    /**
     * Callback for sensor measuring event
     * @param event Just received event
     */
    @Subscribe
    public void onSensorMeasuringEvent(SensorMeasuringEvent event) {
        if (event.getSensor().getName().contentEquals(sensor.getName())) {
            checkForErrorMessage();
            Log.d(TAG, "Event measuring received " + event.getState());
        }
    }

    /**
     * Callback for sensor updated
     * @param event Just received event
     */
    @Subscribe
    public void onSensorUpdatedEvent(SensorUpdateEvent event) {
        if (!event.getSensor().getName().contentEquals(sensor.getName())) return;
        points = new float[sensor.getChannels()];
        for (int i = 0; i < sensor.getChannels(); i++) {
            if(data_handler.getCurrentPoint() != null) {
                normalized[i] = (event.getDataPoint().getValues()[i] - sensor.getMinValue()) / spread;
                points[i] = data_handler.getCurrentPoint().getValues()[i];
            }
            else {
                points[i] = 0;
            }
            if(!triggered) {
                Log.d("FOODOO",String.valueOf(points[i]));
                if (points[i] > triggerAmplitude) {
                    triggered = true;
                    data_handler.collectData(collectionTime, sampleDelay);
                    Log.d("TRIGGER WARNING:", String.valueOf(triggered));
                    runner = new Runnable() {
                        long last = System.currentTimeMillis();
                        long actual;

                        public void run() {
                            graph.invalidate();
                            actual = System.currentTimeMillis();
                            if (actual - last > FRAMERATE_SKIP_MS)
                                handler.postDelayed(this, actual - last);
                            else
                                handler.postDelayed(this, FRAMERATE_SKIP_MS);
                            last = actual;
                            elapsedTime += sampleDelay;
                            if(elapsedTime >= collectionTime) {
                                graph.setRunning(false);
                                handler.removeCallbacks(runner);
                                elapsedTime = 0;
                            }
                        }
                    };
                    graph.setRunning(true);
                    handler.post(runner);

                }
            }
        }
        if(triggered) {
            this.graph.addNewDataPoint(normalized);
        }
    }

    /**
     * Callback for sensor connect event
     * @param event Just received event
     */
    @Subscribe
    public void onSensorRangeEvent(SensorRangeEvent event) {
        if (event.getSensor().getName().contentEquals(sensor.getName()))

            initialiseSensorData();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.graph_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_start_graph:
                runner = new Runnable() {
                    long last = System.currentTimeMillis();
                    long actual;

                    public void run() {
                        graph.invalidate();
                        actual = System.currentTimeMillis();
                        if (actual - last > FRAMERATE_SKIP_MS)
                            handler.postDelayed(this, actual - last);
                        else
                            handler.postDelayed(this, FRAMERATE_SKIP_MS);
                        last = actual;
                    }
                };
                graph.setRunning(true);
                handler.post(runner);
                return true;
            case R.id.action_pause_graph:
                graph.setRunning(false);
                handler.removeCallbacks(runner);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
