package com.example.mate.Activity.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.mate.Activity.FragmentMain;
import com.example.mate.R;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by 가애 on 2018-02-19.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    // [START receive_message]


    /* 메세지 받았을때 처리 */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        sendPushNotification(remoteMessage.getData().get("message"));
        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_stat_onesignal_default) // 알림 영역에 노출 될 아이콘.
                    .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                    .setContentText(body); // Firebase Console 에서 사용자가 전달한 메시지내용

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(0x1001, notificationBuilder.build());


            sendPushNotification(body);
        }
    }

    private void sendPushNotification(String message) {
        Intent intent;
        intent = new Intent(this, FragmentMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_stat_onesignal_default).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_ban))
                .setContentTitle("반쪽 알람")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255, 500, 2000)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}