package a2dv606_aa223de.assignment2;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a2dv606_aa223de.assignment2.Alarm_Clock.MainActivityAlarm;
import a2dv606_aa223de.assignment2.MP3_player.MP3Player;
import a2dv606_aa223de.assignment2.My_Countries.My_Countries;

public class MainList extends ListActivity {

    private List<String> activities = new ArrayList<String>();
    private Map<String,Class> name2class = new HashMap<String,Class>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Add Activities to list */
        setup_activities();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main_list, activities));

        /* Attach list item listener */
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClick());
    }



    /* Private Help Entities */
    private class OnItemClick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		/* Find selected activity */
            String activity_name = activities.get(position);
            Class activity_class = name2class.get(activity_name);

    		/* Start new Activity */
            Intent intent = new Intent(MainList.this,activity_class);
            MainList.this.startActivity(intent);
        }
    }

    /* Diagnostics while developing */
    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void setup_activities() {
        addActivity("My Countries", My_Countries.class);
        addActivity("Alarm Clock",MainActivityAlarm.class);
        addActivity("Mp3 Player",MP3Player.class);



    }

    private void addActivity(String name, Class activity) {
        activities.add(name);
        name2class.put(name, activity);
    }



}