package brbsolutions.myo_muscle;

import android.provider.BaseColumns;

/**
 * Created by Alex on 10/8/2016.
 */
public final class TrialContract {

    public static String init_table = "CREATE TABLE " + TrialEntry.table_name + " ( " +
            TrialEntry.column_routine + " TEXT, " +
            TrialEntry.column_day + " INT," +
            TrialEntry.column_month + " INT," +
            TrialEntry.column_year + " INT," +
            TrialEntry.column_key + " UNIQUE AUTO_INCREMENT );";

    public static String drop_table = "DROP TABLE IF EXISTS " + TrialEntry.table_name;

    public static abstract class TrialEntry implements BaseColumns{
        public static String table_name = "trials";
        public static String column_routine = "routine";
        public static String column_day = "day";
        public static String column_month = "month";
        public static String column_year = "year";
        public static String column_key = "key";
    }
}
