package com.youthfimodd.elenges.custom.chat_model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.youthfimodd.elenges.ChatActivity;
import com.youthfimodd.elenges.SOSChatActivity;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "com.youthfim.elenge";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SharedPreferences preferences = getSharedPreferences("PRETS", MODE_PRIVATE);
        String currentUser = preferences.getString("currentUser", "none");

        //pour SOS notification
        String Title = remoteMessage.getData().get("typenotif");
        if (Title.equals("MessageSOS")){
            //Send SOS Notification
            String sender = remoteMessage.getData().get("sented");
            String user = remoteMessage.getData().get("user");

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser != null && sender.equals(firebaseUser.getUid())) {
                if (!currentUser.equals(user)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        SOSsendOreoNotification(remoteMessage);
                    } else {
                        SOSsendNotification(remoteMessage);
                    }
                }
            }

        }else if (Title.equals("MessageChat")){
            //Send Message Notification
            String sender = remoteMessage.getData().get("sented");
            String user = remoteMessage.getData().get("user");

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser != null && sender.equals(firebaseUser.getUid())) {
                if (!currentUser.equals(user)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        sendOreoNotification(remoteMessage);
                    } else {
                        sendNotification(remoteMessage);
                    }
                }
            }
        }

    }
    //SOS OreoNotification
    private void SOSsendOreoNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        assert user != null;
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, SOSChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("SOS_user_id", user);/*user_id vennant de la classe SOSChatActivity*/
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, icon);

        int i = 0;
        if (j > 0){
            i = j;
        }
        //J'ai ajouter
        Notification notification1 = new Notification.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                /*n'est pas use*/
                //.setContentTitle(title)
                .setGroup(title)
                .setGroup(CHANNEL_ID)
                .setContentText(body)
                .setStyle(new Notification.BigTextStyle().bigText(body)
                        .setSummaryText(title))
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .build();
        for (int k = 0; k <1 ; k++) {
            SystemClock.sleep(2000);
            //doit etre hors for
            oreoNotification.getManager().notify(i, builder.build());
        }
    }
    //SOS Notification
    private void SOSsendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        assert user != null;
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, SOSChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("SOS_user_id", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        assert icon != null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body)
                        .setSummaryText(title))
                /*n'est pas use*/
                //.setContentTitle(title)
                .setGroup(CHANNEL_ID)
                .setGroupSummary(true)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0){
            i = j;
        }
        //J'ai ajouter
        Notification notification1 = new Notification.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                /*n'est pas use*/
                //.setContentTitle(title)
                .setGroup(CHANNEL_ID)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .build();
        for (int k = 0; k <1 ; k++) {
            SystemClock.sleep(2000);
            //doit etre hors for
            noti.notify(i, builder.build());
        }
    }
    //
    //OreoNotification
    private void sendOreoNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        assert user != null;
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user);/*user_id vennant de la classe ChatActivity*/
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, icon);

        int i = 0;
        if (j > 0){
            i = j;
        }
        //J'ai ajouter
        Notification notification1 = new Notification.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                /*n'est pas use*/
                //.setContentTitle(title)
                .setGroup(title)
                .setGroup(CHANNEL_ID)
                .setContentText(body)
                .setStyle(new Notification.BigTextStyle().bigText(body)
                        .setSummaryText(title))
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .build();
        for (int k = 0; k <1 ; k++) {
            SystemClock.sleep(2000);
            //doit etre hors for
            oreoNotification.getManager().notify(i, builder.build());
        }
    }
    //Notification
    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        assert user != null;
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        assert icon != null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body)
                        .setSummaryText(title))
                /*n'est pas use*/
                //.setContentTitle(title)
                .setGroup(CHANNEL_ID)
                .setGroupSummary(true)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0){
            i = j;
        }
        //J'ai ajouter
        Notification notification1 = new Notification.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                /*n'est pas use*/
                //.setContentTitle(title)
                .setGroup(CHANNEL_ID)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .build();
        for (int k = 0; k <1 ; k++) {
            SystemClock.sleep(2000);
            //doit etre hors for
            noti.notify(i, builder.build());
        }
    }
    //
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //update user token
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            //signed in, update token
            updateToken(s);
        }
    }

    private void updateToken(String tokenRefresh) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tokenRefresh);
        ref.child(user.getUid()).setValue(token);
    }
}
