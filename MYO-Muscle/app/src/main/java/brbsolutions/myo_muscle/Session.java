package brbsolutions.myo_muscle;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alex on 10/8/2016.
 */
public class Session {
    public int day;
    public int month;
    public int year;

    public int routine;

    public Trial[] trials;

    public Session(){
        day = month = year = 0;
    }

    public Session(int d, int m, int y, int get_routine, Trial[] get_trials){
        trials = new Trial[get_trials.length];
        day = d;
        month = m;
        year = y;

        routine = get_routine;

        System.arraycopy(get_trials, 0, trials, 0, get_trials.length);
    }

    public Session(Cursor c, ArrayList<Trial> get_trials){
        day = c.getInt(c.getColumnIndexOrThrow(SessionContract.SessionEntry.column_day));
        month = c.getInt(c.getColumnIndexOrThrow(SessionContract.SessionEntry.column_month));
        year = c.getInt(c.getColumnIndexOrThrow(SessionContract.SessionEntry.column_year));
        routine = c.getInt(c.getColumnIndexOrThrow(SessionContract.SessionEntry.column_routine));

        trials = new Trial[get_trials.size()];
        get_trials.toArray(trials);
    }

    public String to_string(){
        return "(Session) day = " + String.valueOf(day) + " month = " + String.valueOf(month) +
                " year = " + year + " routine = " + routine + "\n";
    }

    LinearLayout getLayout(Context context){
        LinearLayout layout = new LinearLayout(context);

        ((Activity) context).getLayoutInflater().inflate(R.layout.layout_session, layout);

        ((TextView) layout.findViewById(R.id.session_date_target)).setText(String.valueOf(month) + "/" +
            String.valueOf(day) + "/" + String.valueOf(year));

        return layout;
    }
}