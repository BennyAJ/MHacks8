package brbsolutions.myo_muscle;

import android.provider.BaseColumns;

/**
 * Created by Alex on 10/8/2016.
 */
public final class SessionContract {

    public static String init_table = "CREATE TABLE " + SessionEntry.table_name + " ( " +
            SessionEntry.column_routine + " INT, " +
            SessionEntry.column_day + " INT," +
            SessionEntry.column_month + " INT," +
            SessionEntry.column_year + " INT," +
            SessionEntry.column_key + " INT UNIQUE AUTO_INCREMENT );";

    public static String drop_table = "DROP TABLE IF EXISTS " + SessionEntry.table_name;

    public static abstract class SessionEntry implements BaseColumns{
        public static String table_name = "sessions";
        public static String column_routine = "routine";
        public static String column_day = "day";
        public static String column_month = "month";
        public static String column_year = "year";
        public static String column_key = "key";
    }
}
