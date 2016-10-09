/* This file is part of EmgVisualizer.

    EmgVisualizer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    EmgVisualizer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with EmgVisualizer.  If not, see <http://www.gnu.org/licenses/>.
*/
package brbsolutions.myo_muscle;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;

import brbsolutions.myo_muscle.R;
import emgvisualizer.model.RawDataPoint;
import emgvisualizer.ui.views.SensorGraphView;

/**
 * Fragment for showing home information.
 * @author Nicola
 */
public class HistoryFragment extends Fragment {

    /**
     * Public constructor to create a new HomeFragment
     */
    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void layerTwo(LinearLayout target, int id){
        DatabaseHelper dbh = new DatabaseHelper(getActivity());
        ArrayList<Session> sessions = dbh.getSessionsFromRoutine(id);
        dbh.close();

        // Assuming we only graph trial one
        ArrayList<Trial> trials = new ArrayList<>();

        for(int i = 0; i < sessions.size(); ++i){
            DatabaseHelper dbh2 = new DatabaseHelper(getActivity());
            ArrayList<Trial> tmp = dbh2.getTrialsFromSession(sessions.get(i).id);
            dbh2.close();
            trials.add(tmp.get(0));
        }

        //This is the stuff needed to make the graph
        Data_Handler data_handler = new Data_Handler(getActivity());
        ArrayList<DataPoint> graphPoints = new ArrayList<DataPoint>();
        for(int i = 0; i < trials.size(); i++) {
            ArrayList<RawDataPoint> trialPoints = new ArrayList<RawDataPoint>(Arrays.asList(trials.get(i).data));
            graphPoints.add(data_handler.generateGraphPoint(trialPoints, i));
        }
        LineGraphSeries<DataPoint> series = data_handler.generateGraph(graphPoints);

        // Graphing information goes here
        // Generate your graph and add it to target layout
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history, container, false);

        ScrollView scroll = (ScrollView) view.findViewById(R.id.history_target);

        DatabaseHelper dbh = new DatabaseHelper(getActivity());
        ArrayList<Routine> routines = dbh.getRoutines();
        dbh.close();

        for(int i = 0; i < routines.size(); ++i){
            View theView = routines.get(i).getLayout(getActivity());
            theView.setTag(routines.get(i).id);

            theView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int tag = (int)view.getTag();
                    LinearLayout target = (LinearLayout) view.findViewById(R.id.session_target);
                    layerTwo(target, tag);
                }
            });

            scroll.addView(theView);
        }


        /*
        Button btnGithub = (Button) view.findViewById(R.id.btn_github);
        btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/cortinico/myo-emg-visualizer";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        */

        return view;
    }
}
