package brbsolutions.myo_muscle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import junit.framework.Test;

public class Test_Runner extends AppCompatActivity {
    public static String instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_test_page);
        setInstructions();
        TextView instructionsDisplay = (TextView) findViewById(R.id.instructions_display);
        instructionsDisplay.setText(getInstructions());

        Button button_cancel_test = (Button) findViewById(R.id.cancel_test);
        button_cancel_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("clicks","You Clicked Cancel Test");
                Intent i=new Intent(
                        Test_Runner.this,
                        MainActivity.class);
                startActivity(i);
            }
        });
    }
    public void setInstructions() {
        instructions = "1. Find a water bottle\n" +
                "2. Grip as tightly as you can.";
    }
    public String getInstructions(){
        return instructions;
    }
}
