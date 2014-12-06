package com.quitevis.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmNotificationReceiver extends BroadcastReceiver {
	private static final int MY_NOTIFICATION_ID = 1;

	// Notification Action Elements
	private Intent notificationIntent;
	private PendingIntent contentIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		// The Intent to be used when the user clicks on the Notification View
		notificationIntent = new Intent(context, MainActivity.class);

		// The PendingIntent that wraps the underlying Intent
		contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		// Build the Notification
		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker("Heya!")
				.setSmallIcon(android.R.drawable.ic_menu_camera)
				.setAutoCancel(true).setContentTitle("Daily Selfie")
				.setContentText("Time for another selfie").setContentIntent(contentIntent);

		// Get the NotificationManager
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Pass the Notification to the NotificationManager:
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());
		
	}

}
