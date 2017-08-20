package a2dv606_aa223de.assignment2.MP3_player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import a2dv606_aa223de.assignment2.R;

import static android.os.SystemClock.sleep;

public class MP3Player  extends Activity implements View.OnClickListener {
    LocalService mService;
    boolean mBound = false;
    ListView listView;
    String[] items;
    ArrayList<String> songsList;
    ArrayList<File> songsArr = new ArrayList<File>();
    ArrayList<File> songFileList;
    Activity main_activity;
    TextView songLabel;
    ImageButton play_button, next_button, per_button, forward_button ,
            backward_button, pause_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("on create MP3 player activity!");
        setContentView(R.layout.activity_mp3_player);
        main_activity = this;
        initComponents();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.song_row, R.id.song_id, items);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mService.setCurrentId(position);
                if(mService!=null){
                    mService.startPlaying();
                    songLabel.setText(items[position].toString());
                }

            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mService!=null&&mService.getCurrentId()!=-1){

            songLabel.setText(items[mService.getCurrentId()]);}
                handler.postDelayed(this, 3000);
            }
        }, 10);


    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("in start binding activitiy ");
        Intent intent = new Intent(this,LocalService.class);
        intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        intent.putExtra("song_arr",songFileList);
        startService(intent);
        // Bind to LocalService
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }


    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            System.out.print("close binding from service");

            unbindService(mConnection);
            mBound = false;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mp3_player, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop_player:
                mService.stop();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initComponents() {
        play_button = (ImageButton) findViewById(R.id.play_button);
        pause_button = (ImageButton) findViewById(R.id.pause_button);
        per_button = (ImageButton) findViewById(R.id.pre_button);
        next_button = (ImageButton) findViewById(R.id.next_button);
        forward_button = (ImageButton) findViewById(R.id.forward_button);
        backward_button = (ImageButton) findViewById(R.id.backward_button);
        songLabel = (TextView) findViewById(R.id.music_text);
        listView = (ListView) findViewById(R.id.listview_songs);
        play_button.setOnClickListener(this);
        pause_button.setOnClickListener(this);
        per_button.setOnClickListener(this);
        next_button.setOnClickListener(this);
        forward_button.setOnClickListener(this);
        backward_button.setOnClickListener(this);


        songsList = new ArrayList<String>();
        songFileList = findSongs(Environment.getExternalStorageDirectory());
        items = new String[songFileList.size()];
        for (int i = 0; i < songFileList.size(); i++) {
            items[i] = songFileList.get(i).getName().toString().replace(".mp3", "");
        }


    }
    private void toast(String name) {
        Toast.makeText(getApplicationContext(),name, Toast.LENGTH_SHORT).show();
    }

    private ArrayList<File> findSongs(File root) {
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                findSongs(file);
            }
            else{
                if (file.getName().endsWith(".mp3")) {
                    songsArr.add(file);
                }
            }
        }

        return songsArr;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("my activity is destoried ");

      //  Intent service = new Intent(MP3Player.this, LocalService.class);
     //   stopService(service);

    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(mService!=null){
            switch (id){
                case R.id.play_button:
                    mService.playSong();
                    break;
                case R.id.pause_button:
                    mService.pause();
                    break;
                case R.id.forward_button:
                    mService.seekForward();
                    break;
                case R.id.backward_button:
                    mService.seekBackward();
                    break;
                case R.id.next_button:
                    mService.playNext();
                    if(mService.getCurrentId()!=-1){
                    songLabel.setText(items[mService.getCurrentId()].toString());}
                    break;
                case R.id.pre_button:
                    mService.playPrev();
                    if(mService.getCurrentId()!=-1){
                    songLabel.setText(items[mService.getCurrentId()].toString());}
                    break;


            }






        }

    }
}
