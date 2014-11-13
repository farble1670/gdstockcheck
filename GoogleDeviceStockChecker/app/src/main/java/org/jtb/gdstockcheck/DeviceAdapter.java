package org.jtb.gdstockcheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class DeviceAdapter extends BaseAdapter {
  private static final Executor exec = Executors.newCachedThreadPool();

  private final Context context;
  private final List<Device> devices;

  DeviceAdapter(Context context, Collection<Device> devices) {
    this.context = context;
    this.devices = new ArrayList<Device>(devices);
  }

  DeviceAdapter(Context context, Device[] devices) {
    this.context = context;
    this.devices = Arrays.asList(devices);
  }

  @Override
  public int getCount() {
    return devices.size();
  }

  @Override
  public Object getItem(int i) {
    return devices.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    if (view == null) {
      view = LayoutInflater.from(context).inflate(R.layout.item_device, viewGroup, false);
    }

    Device device = devices.get(i);
    view.setTag(device);

    ImageView image = (ImageView) view.findViewById(R.id.image);
    image.setImageResource(device.imageId);

    TextView name = (TextView) view.findViewById(R.id.name);
    name.setText(device.name);

    final TextView status = (TextView) view.findViewById(R.id.status);
    status.setText(Status.UNKNOWN.name());
    status.setTextColor(Status.UNKNOWN.color);

    new CheckStatusTask(context, device, view) {
      @Override
      public void onStatus(Device device, org.jtb.gdstockcheck.Status status) {
        if (context instanceof StatusListener) {
          StatusListener listener = (StatusListener)context;
          listener.onStatus(device, status);
        }
      }
    }.executeOnExecutor(exec);

    return view;
  }
}
