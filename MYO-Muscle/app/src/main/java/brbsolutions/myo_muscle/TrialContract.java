package brbsolutions.myo_muscle;

import android.provider.BaseColumns;

/**
 * Created by Alex on 10/8/2016.
 */
public final class TrialContract {
    public static String init_table = "CREATE TABLE " + TrialEntry.table_name + " ( " +
            TrialEntry.column_step + " INT, " +
            TrialEntry.column_session + " INT, " +
            TrialEntry.column_key + " INTEGER PRIMARY KEY );";

    public static String drop_table = "DROP TABLE IF EXISTS " + TrialEntry.table_name;

    public static abstract class TrialEntry implements BaseColumns {
        public static String table_name = "trials";

        public static String column_step = "step";
        public static String column_session = "sesson";
        public static String column_key = "key";
    }
}
