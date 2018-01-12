package com.example.salman.mobileassignment3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Salman on 12/7/2017.
 */

public class MyReceiver extends BroadcastReceiver {

    Boolean isAirplaneEnabled;
    WifiManager wifiManager;
    Boolean isWifiEnabled;
    String BatteryStatus;

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "OnReceive", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
            BatteryStatus = "Low";
        }
        if (intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)) {
            BatteryStatus = "Okay";
        }

        isAirplaneEnabled = Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        isWifiEnabled = wifiManager.isWifiEnabled();

        CustomMessageEvent customMessageEvent = new CustomMessageEvent();
        customMessageEvent.setbBoolean(isWifiEnabled);
        EventBus.getDefault().post(customMessageEvent);
        customMessageEvent.setaBoolean(isAirplaneEnabled);
        EventBus.getDefault().post(customMessageEvent);
        customMessageEvent.setS1(BatteryStatus);
        EventBus.getDefault().post(customMessageEvent);
    }
}
