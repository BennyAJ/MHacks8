package brbsolutions.myo_muscle;

import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Created by Alex on 10/8/2016.
 */
public final class DataPointContract {
    public static String init_table = "CREATE TABLE " + DataPointEntry.table_name + " ( " +
            DataPointEntry.column_trial + " INT, " +
            DataPointEntry.column_key + " INT UNIQUE AUTO_INCREMENT, " +
            DataPointEntry.column_channel_1 + " REAL , " +
            DataPointEntry.column_channel_2 + " REAL , " +
            DataPointEntry.column_channel_3 + " REAL , " +
            DataPointEntry.column_channel_4 + " REAL , " +
            DataPointEntry.column_channel_5 + " REAL , " +
            DataPointEntry.column_channel_6 + " REAL , " +
            DataPointEntry.column_channel_7 + " REAL , " +
            DataPointEntry.column_channel_8 + " REAL )";

    public static String drop_table = "DROP TABLE IF EXISTS " + DataPointEntry.table_name;

    public static abstract class DataPointEntry implements BaseColumns{
        public static String table_name = "data";

        public static String column_trial = "trial_key";
        public static String column_key = "key";
        public static String column_channel_1 = "ch1";
        public static String column_channel_2 = "ch2";
        public static String column_channel_3 = "ch3";
        public static String column_channel_4 = "ch4";
        public static String column_channel_5 = "ch5";
        public static String column_channel_6 = "ch6";
        public static String column_channel_7 = "ch7";
        public static String column_channel_8 = "ch8";
    }
}
