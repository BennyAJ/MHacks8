package brbsolutions.myo_muscle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import emgvisualizer.model.RawDataPoint;

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

    private void storeDataPoints(RawDataPoint[] data, int trialIndex){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            for (int i = 0; i < data.length; ++i) {
                float[] values = {0, 0, 0, 0, 0, 0, 0, 0};
                System.arraycopy(data[i].getValues(), 0, values, 0, 8);
                ContentValues cv = new ContentValues();
                cv.put(DataPointContract.DataPointEntry.column_trial, trialIndex);
                cv.put(DataPointContract.DataPointEntry.column_channel_1, values[0]);
                cv.put(DataPointContract.DataPointEntry.column_channel_2, values[1]);
                cv.put(DataPointContract.DataPointEntry.column_channel_3, values[2]);
                cv.put(DataPointContract.DataPointEntry.column_channel_4, values[3]);
                cv.put(DataPointContract.DataPointEntry.column_channel_5, values[4]);
                cv.put(DataPointContract.DataPointEntry.column_channel_6, values[5]);
                cv.put(DataPointContract.DataPointEntry.column_channel_7, values[6]);
                cv.put(DataPointContract.DataPointEntry.column_channel_8, values[7]);

                db.insert(DataPointContract.DataPointEntry.table_name, null, cv);
            }
            db.setTransactionSuccessful();
        }catch(Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();

        db.close();
    }

    private void storeTrials(Trial[] trials, int sessionIndex){
        SQLiteDatabase db = getWritableDatabase();

        for(int i = 0; i < trials.length; ++i){
            ContentValues cv = new ContentValues();
            cv.put(TrialContract.TrialEntry.column_step, trials[i].step);
            cv.put(TrialContract.TrialEntry.column_session, sessionIndex);
            db.insert(TrialContract.TrialEntry.table_name, null, cv);

            Cursor c = db.rawQuery("SELECT MAX(" + TrialContract.TrialEntry.column_key +
                    ") FROM " + TrialContract.TrialEntry.table_name, null);

            int index = c.getInt(0);
            storeDataPoints(trials[i].data, index);

            c.close();
        }

        db.close();
    }

    public void storeRoutine(Routine r){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(RoutineContract.RoutineEntry.column_steps, r.steps);
        cv.put(RoutineContract.RoutineEntry.column_name, r.name);
        cv.put(RoutineContract.RoutineEntry.column_procedure, r.procedure);

        db.insert(RoutineContract.RoutineEntry.table_name, null, cv);

        db.close();
    }

    public void storeSession(Session s){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SessionContract.SessionEntry.column_day, s.day);
        cv.put(SessionContract.SessionEntry.column_month, s.month);
        cv.put(SessionContract.SessionEntry.column_year, s.year);
        cv.put(SessionContract.SessionEntry.column_routine, s.routine.id);

        db.insert(SessionContract.SessionEntry.table_name, null, cv);

        Cursor c = db.rawQuery("SELECT MAX(" + SessionContract.SessionEntry.column_key +
            ") FROM " + SessionContract.SessionEntry.table_name, null);

        int index = c.getInt(0);

        c.close();
        db.close();

        storeTrials(s.trials, index);
    }
}
