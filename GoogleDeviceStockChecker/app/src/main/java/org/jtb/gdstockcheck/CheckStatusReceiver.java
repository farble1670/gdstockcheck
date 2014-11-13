package org.jtb.gdstockcheck;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CheckStatusReceiver extends BroadcastReceiver {
  static final long INTERVAL = 5 * 60 * 1000;

  @Override
  public void onReceive(Context context, Intent intent) {
    if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
      schedule(context);
    } else {
      context.startService(new Intent(context, CheckStatusService.class));
    }
  }

  static void schedule(Context context) {
    Intent intent = new Intent(context, CheckStatusReceiver.class);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, 0, INTERVAL, alarmIntent);
  }
}
