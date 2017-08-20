package a2dv606_aa223de.assignment2.Alarm_Clock;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.widget.Toast;

import java.util.Calendar;
import android.os.Handler;
import java.util.logging.LogRecord;

import a2dv606_aa223de.assignment2.R;
/**
 * Created by Abeer on 2/28/2017.
 */


public class MainActivityAlarm extends Activity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TimePicker timePicker;
    TextView currentTime ;
    Context context;
    Intent intent;
    java.util.Calendar calendar;
    Handler handler;
    boolean keep_working = true;
    Button startAlarm, stopAlarm;
    private static final String TIME_FORMAT = "HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alarm);
        initComponent();

        handler =new Handler();
        Thread thr = new Thread(null, currentTimeDisplay, "Current Time Display");
        thr.start();


        startAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                long alarmMillis = calendar.getTimeInMillis();
                if (calendar.before(now))
                {alarmMillis+= 86400000L; } //Add 1 day if time selected before now

                String hour = String. valueOf(timePicker.getCurrentHour());
                String second = String. valueOf(timePicker.getCurrentMinute());
                intent.putExtra("alarm_on",true);

                pendingIntent = PendingIntent.getBroadcast(MainActivityAlarm.this,
                        0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP,alarmMillis,pendingIntent);

                Toast.makeText(MainActivityAlarm.this,"Alarm set to: "+hour+":"+second, Toast.LENGTH_SHORT).show();

            }


        });
        stopAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                alarmManager.cancel(pendingIntent);

                intent.putExtra("alarm_on",false);
                sendBroadcast(intent);
                Toast.makeText(MainActivityAlarm.this,"Alarm off" , Toast.LENGTH_SHORT).show();

            }


        });
    }

    private void initComponent() {
        this.context= this;
        currentTime =( TextView) findViewById(R.id.current_time);
        timePicker =(TimePicker) findViewById(R.id.timePicker);
         startAlarm = (Button ) findViewById(R.id.alarm_on);
          stopAlarm = (Button ) findViewById(R.id.alarm_off);
        calendar = java.util.Calendar.getInstance();
         alarmManager= (AlarmManager)getSystemService(ALARM_SERVICE);
          intent = new Intent(context,AlarmReciever.class);
    }


    private Runnable currentTimeDisplay = new Runnable() {

        public void run() {
            while (keep_working) {
                handler.post( new Runnable() {
                    public void run() {
                        currentTime.setText("Current Time: "+DateFormat.format(TIME_FORMAT, Calendar.getInstance().getTime()));
                    }
                }
                );
                SystemClock.sleep(5000);
            }
        }
    };





}
