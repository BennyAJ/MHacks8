package brbsolutions.myo_muscle;

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

    public String getStepName(int index){
        String exploded[] = name.split(";");
        return exploded[index].split(":")[0];
    }

    public int getStepLength(int index){
        String exploded[] = name.split(";");
        return Integer.parseInt(exploded[index].split(":")[1]);
    }
}
