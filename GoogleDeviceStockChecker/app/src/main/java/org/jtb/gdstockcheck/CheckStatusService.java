package org.jtb.gdstockcheck;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.util.List;
import java.util.Random;

public class CheckStatusService extends IntentService {
  private static int NOTIFICATION_ID_BASE = new Random(System.currentTimeMillis()).nextInt();

  static final long INTERVAL_FOREGROUND = 20 * 1000;
  static final long INTERVAL_BACKGROUND = 1 * 60 * 1000;

  public CheckStatusService() {
    super(CheckStatusService.class.getName());
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    try {
      ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = connManager.getActiveNetworkInfo();
      if (info == null || !info.isConnected()) {
        return;
      }

      boolean foreground = isForeground(this);

      if (!foreground && info.getType() == ConnectivityManager.TYPE_MOBILE) {
        return;
      }

      boolean alertSound = false;

      for (Device device : Device.values()) {
        DeviceStatus loadingStatus = new DeviceStatus(device, Status.LOADING);
        getContentResolver().insert(DeviceStatusContract.DeviceStatus.CONTENT_URI, loadingStatus.toContentValues());

        Status status = new CheckStatus(device).call();
        DeviceStatus deviceStatus = new DeviceStatus(device, status);
        ContentValues values = deviceStatus.toContentValues();
        getContentResolver().insert(DeviceStatusContract.DeviceStatus.CONTENT_URI, values);

        if (status == Status.IN_STOCK) {
          if (!foreground) {
            showNotification(device, status);
          } else {
            alertSound = true;
          }
        }
      }

      if (alertSound) {
        new Alert(this).play();
      }
    } finally {
      schedule(this);
    }
  }

  public void showNotification(Device device, Status status) {
    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    Notification.Builder mBuilder = new Notification.Builder(this)
        .setSmallIcon(android.R.drawable.stat_sys_warning)
        .setContentTitle("In stock: " + device.name)
        .setDefaults(Notification.DEFAULT_ALL)
        .setWhen(System.currentTimeMillis())
        .setContentText("Your device is in stock. Go buy it.");

    Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(device.pageUrl));
    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    mBuilder.setContentIntent(contentIntent);
    mBuilder.setAutoCancel(true);

    Notification notification = mBuilder.build();
    notificationManager.notify(NOTIFICATION_ID_BASE + device.ordinal(), notification);
  }


  static void schedule(Context context) {
    Intent intent = new Intent(context, CheckStatusReceiver.class);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (isForeground(context) ? INTERVAL_FOREGROUND : INTERVAL_BACKGROUND), alarmIntent);
  }


  static boolean isForeground(Context context) {
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
    ComponentName componentInfo = taskInfo.get(0).topActivity;
    if (componentInfo.getPackageName().equals(context.getPackageName()) && componentInfo.getClassName().equals(CheckStatusActivity.class.getName())) {
      return true;
    }
    return false;
  }

}
