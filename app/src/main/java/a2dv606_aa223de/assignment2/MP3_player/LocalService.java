package a2dv606_aa223de.assignment2.MP3_player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import a2dv606_aa223de.assignment2.R;

public class LocalService extends Service {
    private final IBinder mBinder = new LocalBinder();
    public static MediaPlayer mediaPlayer;
    private int currentId=-1;
    private ArrayList<File> songFileList;
    private Uri uri;
    private static final String LOG_TAG = "ForegroundService";


    @Override
    public void onCreate() {
        System.out.println("onCreate()");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("service onStartCommand(...)");
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");
        }
        if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
               playPrev();

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
          playSong();

        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
           playNext();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        LocalService.this.stopSelf();


    }


    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Bundle bundle = intent.getExtras();
        songFileList = (ArrayList) bundle.getParcelableArrayList("song_arr");
      //  Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show();

        return mBinder;
    }



    public void pause(){
        if(mediaPlayer!=null){
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();

            }
        }
    }
    public void playSong(){
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
            else{
                mediaPlayer.start();}
        }}

    public void seekForward (){
        if (mediaPlayer!=null){
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
        }
    }

    public void seekBackward(){
        if (mediaPlayer!=null){
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
        }}

    public void startPlaying(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            createUri();
        }
        else {
            createUri();
        }

    }
    private void createUri(){

        uri = Uri.parse(songFileList.get(getCurrentId()).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() // handle the completion
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                playNext();

            }
        });
        mediaPlayer.start();
        showNotification();
    }

    public void playNext() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (currentId + 1 >= songFileList.size()) {
                currentId = 0;
            } else {
                currentId = currentId + 1;
            }
            createUri();
        }

    }


    public void playPrev( ){
        if (mediaPlayer != null&&mediaPlayer.isPlaying()) {
        mediaPlayer.stop();
        mediaPlayer.release();
        if(currentId-1<0){
            currentId = songFileList.size()-1;
        }
        else{
            currentId=currentId-1;
        }
        createUri();}
    }
    public void stop(){

        if (mediaPlayer.isPlaying()) mediaPlayer.stop(); // stop the current song
        stopNotificaton();

    }
    public void showNotification(){
        Intent notificationIntent = new Intent(this, MP3Player.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, LocalService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, LocalService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, LocalService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.musical_notes);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Music Player")
                .setTicker(" Music Player")
                .setContentText(getText()).setContentInfo("Back!")
                .setSmallIcon(R.drawable.musical_notes)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous, "",
                        ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play, "",
                        pplayIntent)
                .addAction(android.R.drawable.ic_media_next, "",
                        pnextIntent).build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);
    }
    public void setCurrentId(int id){
        currentId= id;
    }
    public String getText(){
        if(songFileList!=null){
        return songFileList.get(currentId).getName().toString().replace(".mp3","");
    }else return "";}
    public int getCurrentId(){
        return currentId;
    }


    private void stopNotificaton() {
        stopForeground(true);

    }

}