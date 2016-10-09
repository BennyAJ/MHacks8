package brbsolutions.myo_muscle;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Alex on 10/8/2016.
 */
public class Routine {
    public String name;
    public String procedure;
    public int steps;
    int id;

    public Routine(){
        name = "null routine";
        procedure = "null";
        id = steps = 0;
    }

    public Routine(String get_name, String get_procedure, int get_steps){
        name = get_name;
        procedure = get_procedure;
        steps = get_steps;
        id = 0;
    }

    public Routine(Cursor c){
        name = c.getString(c.getColumnIndexOrThrow(RoutineContract.RoutineEntry.column_name));
        procedure = c.getString(c.getColumnIndexOrThrow(RoutineContract.RoutineEntry.column_procedure));
        id = c.getInt(c.getColumnIndexOrThrow(RoutineContract.RoutineEntry.column_key));
        steps = c.getInt(c.getColumnIndexOrThrow(RoutineContract.RoutineEntry.column_steps));
    }

    public String getStepName(int index){
        String exploded[] = name.split(";");
        return exploded[index].split(":")[0];
    }

    public int getStepLength(int index){
        String exploded[] = name.split(";");
        return Integer.parseInt(exploded[index].split(":")[1]);
    }

    public LinearLayout getLayout(Context context){
        LinearLayout layout = new LinearLayout(context);

        ((Activity) context).getLayoutInflater().inflate(R.layout.layout_routine, layout);

        ((TextView) layout.findViewById(R.id.routine_title_target)).setText(name);

        if(steps == 1) {
            ((TextView) layout.findViewById(R.id.routine_step_target)).setText("1 Step");
        }else{
            ((TextView) layout.findViewById(R.id.routine_step_target)).setText(String.valueOf(steps) + " Steps");
        }

        DatabaseHelper dbh = new DatabaseHelper(context);
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(" + SessionContract.SessionEntry.column_routine +
                                                " == " + String.valueOf(id) + ") FROM " +
                                                SessionContract.SessionEntry.table_name, null);
        c.moveToFirst();
        int sessions = c.getInt(0);

        c.close();
        db.close();
        dbh.close();

        if(steps == 1) {
            ((TextView) layout.findViewById(R.id.routine_step_target)).setText("1 Step");
        }else{
            ((TextView) layout.findViewById(R.id.routine_step_target)).setText(String.valueOf(steps) + " Steps");
        }

        if(sessions == 1) {
            ((TextView) layout.findViewById(R.id.routine_trial_target)).setText("1 Session completed");
        }else{
            ((TextView) layout.findViewById(R.id.routine_trial_target)).setText(String.valueOf(steps) + " Sessions completed");
        }

        return layout;
    }
}
