package org.jtb.gdstockcheck;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.util.List;

public class CheckStatusService extends IntentService {
  public CheckStatusService() {
    super(CheckStatusService.class.getName());
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    ActivityManager am = (ActivityManager) this .getSystemService(ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
    ComponentName componentInfo = taskInfo.get(0).topActivity;
    if (componentInfo.getClassName().equals(CheckStatusActivity.class.getName())) {
      return;
    }

    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = connManager.getActiveNetworkInfo();
    if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
      return;
    }

    for (Device device: Device.values()) {
      Status status = new CheckStatus(device).call();
      if (status == Status.IN_STOCK) {
        showNotification(device, status);
      }
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
    notificationManager.notify(device.ordinal(), notification);
  }

}
