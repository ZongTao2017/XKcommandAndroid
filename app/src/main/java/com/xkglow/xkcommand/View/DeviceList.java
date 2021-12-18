package com.xkglow.xkcommand.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;

import com.xkglow.xkcommand.DevicePairActivity;
import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.DeviceData;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.R;

import java.util.HashMap;

public class DeviceList extends ScrollView {
    HashMap<String, PairedDeviceView> pairedDeviceViewMap;
    HashMap<String, UnpairedDeviceView> unpairedDeviceViewMap;
    LinearLayout pairedLayout, unpairedLayout;


    public DeviceList(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.device_list, this);

        pairedDeviceViewMap = new HashMap<>();
        unpairedDeviceViewMap= new HashMap<>();
        pairedLayout = findViewById(R.id.paired_device_layout);
        unpairedLayout = findViewById(R.id.unpaired_device_layout);
    }

    public void update() {
        HashMap<String, DeviceData> pairedDeviceMap = AppGlobal.getPairedDeviceMap();
        HashMap<String, DeviceData> unpairedDeviceMap = AppGlobal.getUnpairedDeviceMap();
        for (String address : pairedDeviceViewMap.keySet()) {
            if (!pairedDeviceMap.containsKey(address)) {
                PairedDeviceView pairedDeviceView = pairedDeviceViewMap.get(address);
                pairedLayout.removeView(pairedDeviceView);
                pairedDeviceViewMap.remove(address);
            }
        }
        for (String address : pairedDeviceMap.keySet()) {
            DeviceData deviceData = pairedDeviceMap.get(address);
            DeviceData currentDevice = AppGlobal.getCurrentDevice();
            PairedDeviceView pairedDeviceView;
            if (!pairedDeviceViewMap.containsKey(address)) {
                pairedDeviceView = new PairedDeviceView(getContext());
                pairedDeviceView.setDeviceName(deviceData.systemData.name);
                pairedDeviceView.setDeviceState(deviceData.deviceState);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(getContext(), 5);
                pairedLayout.addView(pairedDeviceView);
                pairedDeviceViewMap.put(address, pairedDeviceView);
            } else {
                pairedDeviceView = pairedDeviceViewMap.get(address);
                pairedDeviceView.setDeviceName(deviceData.systemData.name);
                pairedDeviceView.setDeviceState(deviceData.deviceState);
            }
            pairedDeviceView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppGlobal.setCurrentDevice(deviceData);
                    setCurrent(address);
                }
            });
            pairedDeviceView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showMenu(deviceData, pairedDeviceView);
                    return true;
                }
            });
            if (currentDevice == null) {
                AppGlobal.setCurrentDevice(deviceData);
                pairedDeviceView.setCurrent(true);
            }
        }
        for (String address : unpairedDeviceViewMap.keySet()) {
            if (!unpairedDeviceMap.containsKey(address)) {
                UnpairedDeviceView unpairedDeviceView = unpairedDeviceViewMap.get(address);
                unpairedLayout.removeView(unpairedDeviceView);
                unpairedDeviceViewMap.remove(address);
            }
        }
        for (String address : unpairedDeviceMap.keySet()) {
            DeviceData deviceData = unpairedDeviceMap.get(address);
            UnpairedDeviceView unpairedDeviceView;
            if (!unpairedDeviceViewMap.containsKey(address)) {
                unpairedDeviceView = new UnpairedDeviceView(getContext());
                unpairedDeviceView.setDeviceName(deviceData.systemData.name);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(getContext(), 5);
                unpairedLayout.addView(unpairedDeviceView);
                unpairedDeviceViewMap.put(address, unpairedDeviceView);
            } else {
                unpairedDeviceView = unpairedDeviceViewMap.get(address);
            }
            unpairedDeviceView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppGlobal.setCurrentPairingDevice(deviceData);
                    getContext().startActivity(new Intent(getContext(), DevicePairActivity.class));
                }
            });
        }
    }

    public void setCurrent(String address) {
        for (String address1 : pairedDeviceViewMap.keySet()) {
            PairedDeviceView pairedDeviceView1 = pairedDeviceViewMap.get(address1);
            pairedDeviceView1.setCurrent(address1.equalsIgnoreCase(address));
        }
    }

    public void setCurrent(int index) {

    }

    private void showMenu(final DeviceData deviceData, View view) {
        Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenu);
        final PopupMenu popup = new PopupMenu(wrapper, view);
        popup.getMenuInflater().inflate(R.menu.device_pop_up_menu,
                popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String selection = item.getTitle().toString();
                if (selection.equals(getContext().getResources().getString(
                        R.string.details))) {

                } else if (selection.equals(getContext().getResources().getString(
                        R.string.unpair))) {
                    unpair(deviceData);
                }
                popup.dismiss();
                return true;
            }
        });
        popup.show();
    }

    private void unpair(final DeviceData deviceData) {
        if (deviceData == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("Unpair controller");
        builder.setMessage("Do you want to unpair \"" + deviceData.systemData.name + "\"? All settings for the controller will be lost.");
        builder.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                unpairDevice(deviceData);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void unpairDevice(DeviceData deviceData) {
        AppGlobal.setCurrentDevice(null);
        AppGlobal.disconnect(deviceData, true);
    }
}
