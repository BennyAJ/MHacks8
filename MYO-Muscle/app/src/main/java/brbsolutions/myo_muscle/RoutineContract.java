package brbsolutions.myo_muscle;

import android.provider.BaseColumns;

/**
 * Created by Alex on 10/8/2016.
 */
public final class RoutineContract {
    public static String init_table = "CREATE TABLE " + RoutineEntry.table_name + " ( " +
            RoutineEntry.column_name + " TEXT, " +
            RoutineEntry.column_procedure + " TEXT, " +
            RoutineEntry.column_steps + " INT, " +
            RoutineEntry.column_key + " INTEGER PRIMARY KEY );";

    public static String drop_table = "DROP TABLE IF EXISTS" + RoutineEntry.table_name;

    public static abstract class RoutineEntry implements BaseColumns {
        public static String table_name = "routine";

        public static String column_key = "key";
        public static String column_name = "name";
        public static String column_procedure = "procedure";
        public static String column_steps = "steps";
    }
}
