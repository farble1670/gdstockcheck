package org.jtb.gdstockcheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CheckStatusReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
      CheckStatusService.schedule(context);
    } else {
      context.startService(new Intent(context, CheckStatusService.class));
    }
  }
}
