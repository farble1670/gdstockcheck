package org.jtb.gdstockcheck;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DeviceStatusCursorAdapter extends CursorAdapter {
  public DeviceStatusCursorAdapter(Context context) {
    super(context, null, true);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    DeviceStatus deviceStatus = new DeviceStatus(cursor);

    ImageView image = (ImageView) view.findViewById(R.id.image);
    image.setImageResource(deviceStatus.device.imageId);

    TextView name = (TextView) view.findViewById(R.id.name);
    name.setText(deviceStatus.device.name);

    final TextView status = (TextView) view.findViewById(R.id.status);
    status.setText(deviceStatus.status.stringId);
    status.setTextColor(deviceStatus.status.color);

    ViewGroup progressLayout = (ViewGroup) view.findViewById(R.id.progress);
    if (deviceStatus.status == Status.LOADING) {
      progressLayout.setVisibility(View.VISIBLE);
    } else {
      progressLayout.setVisibility(View.GONE);
    }

    TextView timeText = (TextView) view.findViewById(R.id.time);

    DateFormat dateFormat = DateFormat.getDateTimeInstance();
    dateFormat.setTimeZone(TimeZone.getDefault());
    Date date = new Date(Long.valueOf(deviceStatus.time.getTime()));
    String timeString = dateFormat.format(date);

    timeText.setText(timeString);
  }
}
