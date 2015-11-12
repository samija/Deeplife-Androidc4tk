package deeplife.gcme.com.deeplife.Alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by media on 11/12/15.
 */

    public class Alarm_Service extends IntentService {
        private NotificationManager alarmNotificationManager;
    private Intent intent;

    public Alarm_Service() {
            super("AlarmService");
        }

        @Override
        public void onHandleIntent(Intent intent) {
            sendNotification("Wake Up! Wake Up!");
        }

        private void sendNotification(String msg) {
            Log.d("AlarmService", "Preparing to send notification...: " + msg);
            alarmNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, AlarmActivity.class), 0);

            NotificationCompat.Builder alamNotificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setContentTitle("DeepLife Alarm").setSmallIcon(R.drawable.logoicon_ldpi)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg);


            alamNotificationBuilder.setContentIntent(contentIntent);
            alarmNotificationManager.notify(1, alamNotificationBuilder.build());
            Log.d("AlarmService", "Notification sent.");
        }

}
