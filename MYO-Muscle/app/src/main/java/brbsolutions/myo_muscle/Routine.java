package brbsolutions.myo_muscle;

import android.database.Cursor;

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
}
