package brbsolutions.myo_muscle;

/**
 * Created by Alex on 10/8/2016.
 */
public class Session {
    public int day;
    public int month;
    public int year;

    public Routine routine;
    public Trial[] trials;

    public Session(){
        day = month = year = 0;
    }

    public Session(int d, int m, int y, Routine get_routine, Trial[] get_trials){
        day = d;
        month = m;
        year = y;

        routine = get_routine;
        System.arraycopy(get_trials, 0, trials, 0, get_trials.length);
    }
}