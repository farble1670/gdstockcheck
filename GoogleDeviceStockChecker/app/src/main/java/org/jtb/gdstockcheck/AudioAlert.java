package org.jtb.gdstockcheck;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

class AudioAlert {
  private static final Handler handler = new Handler(Looper.getMainLooper());

  private final Context context;

  AudioAlert(Context context) {
    this.context = context;
  }

  void play() {
    handler.post(new Runnable() {
      @Override
      public void run() {
        try {
          Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
          Ringtone r = RingtoneManager.getRingtone(context, notification);
          r.play();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

  }
}
