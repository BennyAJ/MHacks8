package brbsolutions.myo_muscle;

import emgvisualizer.model.RawDataPoint;

/**
 * Created by Alex on 10/8/2016.
 */
public class Trial {
    public int step;
    public RawDataPoint[] data;

    public Trial(){
        step = 0;
    }

    public Trial(int get_step, RawDataPoint[] get_data){
        step = get_step;
        System.arraycopy(get_data, 0, data, 0, get_data.length);
    }
}
