package com.example.mediateka.data.repository;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;



import com.example.mediateka.domain.SystemTimeCalculator;
import com.example.mediateka.models.model.DateAndTimeInfo;
import com.example.mediateka.presentation.cinemadetail.CinemaDetailsActivity;
import com.example.mediateka.presentation.main.MainActivity;

import java.util.Calendar;
import java.util.Date;

public class CinemaNotificationReceiver extends BroadcastReceiver implements SystemTimeCalculator {

    private Vibrator vibrator;
    private SharedPreferences preferences;

    public static final String CINEMA_NOTIFICATION_ACTION = "com.david.mediateka.cinema.notification.action";
    public static final String CINEMA_NOTIFICATION_DATE = appendCinemaNotificationPrefix("date");
    public static final String CINEMA_NOTIFICATION_ID = appendCinemaNotificationPrefix("id");
    public static final String CINEMA_NOTIFICATION_TITLE = appendCinemaNotificationPrefix("title");
    public static final String CINEMA_NOTIFICATION_CONTENT = appendCinemaNotificationPrefix("content");

    private static final String CINEMA_NOTIFICATION_ACTION_CREATE = "com.david.mediateka.cinema.notification.action.create";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (CINEMA_NOTIFICATION_ACTION.equals(action)){
            DateAndTimeInfo dateAndTimeInfo = (DateAndTimeInfo) intent.getSerializableExtra(CINEMA_NOTIFICATION_DATE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, createScheduledIntent(intent , dateAndTimeInfo) , PendingIntent.FLAG_UPDATE_CURRENT);

            if (alarmManager != null) {
                long specificTimeInMillis = futureTimeInMillisFromDateAndTimeInfo(dateAndTimeInfo);
                if (specificTimeInMillis > currentTimeInMillis()){
                    long offsetBetweenSpecificTimeAndCurrentInMillis = SystemClock.elapsedRealtime() + (specificTimeInMillis - currentTimeInMillis());
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP , offsetBetweenSpecificTimeAndCurrentInMillis, pendingIntent);
                }
            }

        } else if (CINEMA_NOTIFICATION_ACTION_CREATE.equals(action)){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            Intent cinemaDetailsIntent = CinemaDetailsActivity.makeIntent(context , getCinemaId(intent));
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntent(new Intent(context , MainActivity.class));
            taskStackBuilder.addNextIntent(cinemaDetailsIntent);

            Notification.Builder notificationBuilder = playSoundWhenNotificationCome(preferences.getBoolean(context.getString(R.string.pref_key_play_sound_on_notification),
                    true),
                    context,
                    intent,
                    taskStackBuilder);

            notificationManager.notify(getCinemaId(intent), notificationBuilder.build());
            vibrate();
        }
    }

    @Override
    public long futureTimeInMillisFromDateAndTimeInfo(DateAndTimeInfo dateAndTimeInfo){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR , dateAndTimeInfo.getYear());
        calendar.set(Calendar.MONTH , dateAndTimeInfo.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH , dateAndTimeInfo.getDay());
        calendar.set(Calendar.HOUR_OF_DAY , dateAndTimeInfo.getHour());
        calendar.set(Calendar.MINUTE , dateAndTimeInfo.getMinute());

        return calendar.getTimeInMillis();
    }

    @Override
    public long currentTimeInMillis(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.getTimeInMillis();
    }

    private Notification.Builder playSoundWhenNotificationCome(boolean playSound ,
                                                               Context context ,
                                                               Intent intent ,
                                                               TaskStackBuilder taskStackBuilder) {
        Notification.Builder notificationBuilder = new Notification.Builder(context);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_cinema)
                .setContentTitle(getCinemaTitle(intent))
                .setContentText(getCinemaDesc(intent))
                .setTicker("Mediateka: " + getCinemaTitle(intent))
                .setAutoCancel(true)
                .setContentIntent(taskStackBuilder.getPendingIntent(0 , PendingIntent.FLAG_UPDATE_CURRENT));
        if (playSound){
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder
                    .setSound(soundUri);
        }
        return notificationBuilder;
    }

    private Intent createScheduledIntent(Intent intent , DateAndTimeInfo dateAndTimeInfo){
        String cinemaTitle = getCinemaTitle(intent);
        String cinemaDesc = getCinemaDesc(intent);
        int cinemaId = getCinemaId(intent);

        Intent scheduledCinemaIntent = new Intent(CINEMA_NOTIFICATION_ACTION_CREATE);
        scheduledCinemaIntent.putExtra(CINEMA_NOTIFICATION_TITLE , cinemaTitle);
        scheduledCinemaIntent.putExtra(CINEMA_NOTIFICATION_CONTENT , cinemaDesc);
        scheduledCinemaIntent.putExtra(CINEMA_NOTIFICATION_DATE , dateAndTimeInfo);
        scheduledCinemaIntent.putExtra(CINEMA_NOTIFICATION_ID , cinemaId);

        return scheduledCinemaIntent;
    }

    private void vibrate(){
        if (vibrator.hasVibrator()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                vibrator.vibrate(VibrationEffect.createOneShot(200 , VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(200);
            }
        }
    }

    private int getCinemaId(Intent intent) {
        return intent.getIntExtra(CINEMA_NOTIFICATION_ID , 0);
    }

    private String getCinemaDesc(Intent intent) {
        return intent.getStringExtra(CINEMA_NOTIFICATION_CONTENT);
    }

    private String getCinemaTitle(Intent intent) {
        return intent.getStringExtra(CINEMA_NOTIFICATION_TITLE);
    }

    private static String appendCinemaNotificationPrefix(String str){
        return "cinema_notification_" + str;
    }
}
