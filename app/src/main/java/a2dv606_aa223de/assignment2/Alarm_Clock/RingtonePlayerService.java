package a2dv606_aa223de.assignment2.Alarm_Clock;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import a2dv606_aa223de.assignment2.R;

/**
 * Created by Abeer on 2/28/2017.
 */

public class RingtonePlayerService extends Service  {
    MediaPlayer mediaPlayer;
    boolean canStop=false;
    private int count = 0;
    private boolean keepWorking = true;
    private NotificationManager notifManager;
    public static final int NOTIFICATION_ID = 1543;
    Thread thr;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand(...)");
        boolean isOn = intent.getExtras().getBoolean("alarm_on");

        if(isOn){
            canStop=true;
            System.out.println("playing");
            mediaPlayer = MediaPlayer.create(this, R.raw.elegant_ringtone);
            keepWorking=true;
            showNotif();
            thr = new Thread(null, work, "Slow Counting");
            thr.start();

        }
        else if(!isOn){
            System.out.println("not playing");
            keepWorking = false;
            if(canStop){
            mediaPlayer.stop();}

        }
        //return Service.START_STICKY;
        return Service.START_NOT_STICKY;
    }

    private void showNotif() {
        		/* 1. Setup Notification Builder */
        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(R.drawable.ic_action_done)
                .setWhen(System.currentTimeMillis())
                .setTicker("Notification no. "+(count++))
                .setAutoCancel(true);

    		/* 3. Configure Drop-down Action */
        builder.setContentTitle("Alarm Clock")
                .setContentText("Click to stop the alarm!")
                .setContentInfo("Click!");
        Intent intent = new Intent(this.getApplicationContext(), MainActivityAlarm.class);   // Notification intent
        PendingIntent notifIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(notifIntent);
    		/* 4. Create Notification and use Manager to launch it */
        Notification notification = builder.build();
        String ns = Context.NOTIFICATION_SERVICE;
        notifManager = (NotificationManager) getSystemService(ns);
        notifManager.notify(0, notification);
    }


    @Override
    public void onCreate() {
        // Tell the user we started.
        System.out.println("onCreate()");

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.



    }

    /* To pull data in case of binding  */
    public int getCount() {return count;}


    @Override
    public void onDestroy() {
        keepWorking = false; // Stop thread
        System.out.println("onDestroy(...)");
    }

    /**
     * The function that runs in our worker thread
     */
    Runnable work = new Runnable() {
        public void run() {
            // Normally we would do some work here...  for our sample, we will
            // just repeatedly sleep for 3 seconds.
            while (keepWorking) {
                if(!mediaPlayer.isPlaying())
                {  mediaPlayer.start();
                    System.out.println("ringtone is started ");}
                SystemClock.sleep(3000);   // Sleep 3 seconds

            }

        }
    };



}
