package org.jtb.gdstockcheck;

import android.content.ContentValues;
import android.database.Cursor;

import java.sql.Timestamp;

class DeviceStatus {
  final Device device;
  final Status status;
  final Timestamp time;

  DeviceStatus(Device device, Status status) {
    this(device, status, new Timestamp(System.currentTimeMillis()));
  }

  DeviceStatus(Device device, Status status, Timestamp time) {
    this.device = device;
    this.status = status;
    this.time = time;
  }

  DeviceStatus(Cursor c) {
    this.device = Device.valueOf(c.getString(c.getColumnIndex(DeviceStatusContract.DeviceStatus.DEVICE)));
    this.status = Status.valueOf(c.getString(c.getColumnIndex(DeviceStatusContract.DeviceStatus.STATUS)));
    this.time = Timestamp.valueOf(c.getString(c.getColumnIndex(DeviceStatusContract.DeviceStatus.TIME)));
  }

  ContentValues toContentValues() {
    ContentValues values = new ContentValues();
    values.put(DeviceStatusContract.DeviceStatus.DEVICE, device.name());
    values.put(DeviceStatusContract.DeviceStatus.STATUS, status.name());
    values.put(DeviceStatusContract.DeviceStatus.TIME, time.toString());
    return values;
  }
}
