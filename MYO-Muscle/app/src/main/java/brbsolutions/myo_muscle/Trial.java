package brbsolutions.myo_muscle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
        data = new RawDataPoint[get_data.length];
        step = get_step;
        System.arraycopy(get_data, 0, data, 0, get_data.length);
    }

    public Trial(Cursor c, ArrayList<RawDataPoint> rdps){
        step = c.getInt(c.getColumnIndexOrThrow(TrialContract.TrialEntry.column_step));
        data = new RawDataPoint[rdps.size()];
        rdps.toArray(data);
    }

    public String to_string(){
        return "(Trial) step = " + step + "\n";
    }

    LinearLayout getLayout(Context context){
        LinearLayout layout = new LinearLayout(context);
        ((Activity) context).getLayoutInflater().inflate(R.layout.layout_trial, layout);

        ((TextView)layout.findViewById(R.id.trial_name_target)).setText("Exercise " + String.valueOf(step+1));

        return layout;
    }
}
