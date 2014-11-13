package org.jtb.gdstockcheck;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

class Alert {
  private final Context context;

  Alert(Context context) {
    this.context = context;
  }

  void play() {
    try {
      Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      Ringtone r = RingtoneManager.getRingtone(context, notification);
      r.play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
