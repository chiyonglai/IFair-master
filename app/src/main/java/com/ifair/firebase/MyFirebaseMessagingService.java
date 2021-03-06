package com.ifair.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.ifair.R;
import com.ifair.activity.LogoActivity;
import com.ifair.activity.NewsDetailActivity;
import com.ifair.module.FireBaseMessage;
import com.orhanobut.logger.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 接收推播訊息
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        handleIntent();

        Logger.d(remoteMessage.getFrom());

        Logger.d("remoteMessage.getData().size " + String.valueOf(remoteMessage.getData().size()));
        //確認資料，是否正確
        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData());
        }
    }

    /**
     * 送資訊
     * @param remoteMessage
     */
    private void sendNotification(Map<String, String> remoteMessage) {
        try {

            Intent newIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("message_id",remoteMessage.get("message_id"));
            newIntent.putExtras(bundle);
            newIntent.setClass(this, LogoActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            bitmap = getBitmapFromUrl(remoteMessage.get("image"));

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setSmallIcon(R.drawable.notification)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))/*Notification icon image*/
                    .setContentTitle(remoteMessage.get("title"))//title
                    .setContentText(remoteMessage.get("message"))
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            manager.notify(0, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *To get a Bitmap image from the URL received
    **/
    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = getCompressBitmapByScale(BitmapFactory.decodeStream(input),100,100);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public static Bitmap getCompressBitmapByScale(Bitmap bitmap, int maxW, int maxH) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float sx = (float) maxW / (float) w;
        float sy = (float) maxH / (float) h;
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
