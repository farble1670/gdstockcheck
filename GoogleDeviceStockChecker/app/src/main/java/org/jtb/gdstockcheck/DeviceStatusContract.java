package org.jtb.gdstockcheck;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DeviceStatusContract {
  public static final String AUTHORITY = "org.jtb.gdstockcheck";
  public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

  public interface DeviceStatusColumns {
    public static final String DEVICE = "device";
    public static final String STATUS = "status";
    public static final String TIME = "time";
  }

  public static final class DeviceStatus implements BaseColumns, DeviceStatusColumns {
    private DeviceStatus() {
    }

    public static final String CONTENT_DIRECTORY = "device_status";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_DIRECTORY);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/device_status";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/device_status";
  }
}
