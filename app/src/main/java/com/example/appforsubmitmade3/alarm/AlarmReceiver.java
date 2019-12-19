package com.example.appforsubmitmade3.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.appforsubmitmade3.BuildConfig;
import com.example.appforsubmitmade3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver {
    private final static String TIME_FORMAT = "HH:mm";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_TYPE = "type";
    public static final String TYPE_REMINDER = "type reminder";
    public static final String TYPE_REQUEST = "type request";
    public static final int ID_REQUEST = 101;
    public static final int ID_REMINDER = 100;
    private final static int JOB_ID = 10;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(EXTRA_TYPE).equals(TYPE_REMINDER)) {
            String title = intent.getStringExtra(EXTRA_TITLE);
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            showAlarmNotification(context, title, message, 100);
            Log.d("masuk", "alhamdulillah");
        }else if(intent.getStringExtra(EXTRA_TYPE).equals(TYPE_REQUEST)){
            ComponentName componentName = new ComponentName(context, GetMovieReleaseNow.class);
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setRequiresDeviceIdle(false);
            builder.setRequiresCharging(false);
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
        }
    }

    public boolean isDateInvalid(String date, String format){
        try{
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private static void showAlarmNotification(Context context, String title, String message, int id){
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager Channel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if(notificationManager != null){
            notificationManager.notify(id, notification);
        }
    }

    public void setRepeatingAlarm(Context context, String time, String message, String title, String type, int idRequest){
        if (isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idRequest, intent, 0);
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelAlarm(Context context, int idRequest) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idRequest, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public void cancelJob(Context context){
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
    }

    public static class GetMovieReleaseNow extends JobService{

        @Override
        public boolean onStartJob(JobParameters params) {
            getMovie(params, getApplicationContext());
            return true;
        }

        @Override
        public boolean onStopJob(JobParameters params) {
            return true;
        }

        public void getMovie(final JobParameters jobParameters, final Context context){
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://api.themoviedb.org/3/discover/movie?api_key="+ BuildConfig.API_KEY+
                    "&primary_release_date.gte="+getDate()+"&primary_release_date.lte="+getDate();
            Log.d("url", url);
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try{
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");
                        int n = list.length();
                        if (list.length()>5)
                            n=5;
                        for (int i=0; i<n; i++){
                            JSONObject itemObject = list.getJSONObject(i);
                            String titleMovie = itemObject.getString("original_title");
                            showAlarmNotification(context, titleMovie, titleMovie+" has been release today!", i+1);
                        }
                        jobFinished(jobParameters, false);
                    } catch (JSONException e) {
                        Log.d("Exception", e.getMessage());
                        jobFinished(jobParameters, true);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    jobFinished(jobParameters, true);
                }
            });
        }

        public String getDate(){
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }
    }

}
