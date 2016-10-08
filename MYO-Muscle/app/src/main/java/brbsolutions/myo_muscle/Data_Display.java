package brbsolutions.myo_muscle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import junit.framework.Test;

public class Data_Display extends AppCompatActivity {
    public static String results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_results_page);
        setResults();
        TextView resultsDisplay = (TextView) findViewById(R.id.results_display);
        resultsDisplay.setText(getResults());

        Button button_cancel_test = (Button) findViewById(R.id.discard_data);
        button_cancel_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("clicks","You Clicked Discard Data");
                Intent i=new Intent(
                        Data_Display.this,
                        MainActivity.class);
                startActivity(i);
            }
        });
    }
    public void setResults() {
        results = "Sample Results";
    }
    public String getResults(){
        return results;
    }
}