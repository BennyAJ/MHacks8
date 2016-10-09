package brbsolutions.myo_muscle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alex on 10/8/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int database_version = 1;
    public static final String database_file = "myo.db";

    public DatabaseHelper(Context context){
        super(context, database_file, null, database_version);
    }

    public void onCreate(SQLiteDatabase db){
        System.out.println("Creating Database");
        db.execSQL(TrialContract.init_table);
        db.execSQL(DataPointContract.init_table);
        db.execSQL(RoutineContract.init_table);
        db.execSQL(SessionContract.init_table);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        System.out.println("Upgrading Database");
        db.execSQL(TrialContract.drop_table);
        db.execSQL(DataPointContract.drop_table);
        db.execSQL(RoutineContract.drop_table);
        db.execSQL(SessionContract.drop_table);

        db.execSQL(TrialContract.init_table);
        db.execSQL(DataPointContract.init_table);
        db.execSQL(RoutineContract.init_table);
        db.execSQL(SessionContract.drop_table);

    }
}
