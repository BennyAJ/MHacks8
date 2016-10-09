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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import brbsolutions.myo_muscle.R;

/**
 * Fragment for showing home information.
 * @author Nicola
 */
public class RoutineFragment extends Fragment {

    String name;
    String procedure;
    public int steps;
    int id;

    /**
     * Public constructor to create a new HomeFragment
     */
    public RoutineFragment(){
        name = procedure = "NULL";
        steps = 0;
        id = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.layout_routine, container, false);
        ((TextView) view.findViewById(R.id.routine_title_target)).setText(name);

        if(steps == 1) {
            ((TextView) view.findViewById(R.id.routine_step_target)).setText("1 Step");
        }else{
            ((TextView) view.findViewById(R.id.routine_step_target)).setText(String.valueOf(steps) + " Steps");
        }

        DatabaseHelper dbh = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(" + SessionContract.SessionEntry.column_routine +
                " == " + String.valueOf(id) + ") FROM " +
                SessionContract.SessionEntry.table_name, null);
        c.moveToFirst();
        int sessions = c.getInt(0);

        c.close();
        db.close();
        dbh.close();

        if(steps == 1) {
            ((TextView) view.findViewById(R.id.routine_step_target)).setText("1 Step");
        }else{
            ((TextView) view.findViewById(R.id.routine_step_target)).setText(String.valueOf(steps) + " Steps");
        }

        if(sessions == 1) {
            ((TextView) view.findViewById(R.id.routine_trial_target)).setText("1 Session completed");
        }else{
            ((TextView) view.findViewById(R.id.routine_trial_target)).setText(String.valueOf(steps) + " Sessions completed");
        }

        return view;
    }
}
