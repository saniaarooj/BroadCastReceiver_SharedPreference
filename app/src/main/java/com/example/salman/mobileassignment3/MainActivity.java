package com.example.salman.mobileassignment3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity {

    Switch airplane_state;
    Switch wifi_switch;
    Boolean aBoolean;
    Boolean bBoolean;
    WifiManager wifiManager;
    EditText etName;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView tvBattery;
    NotificationManager notificationManager;
    String Id = "chanel 1";
    Intent intent;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        etName = findViewById(R.id.etName);
        tvBattery = findViewById(R.id.battery_status);
        airplane_state = findViewById(R.id.airplane_switch);
        wifi_switch = findViewById(R.id.wifi_switch);

        sp = getSharedPreferences("Info", MODE_PRIVATE);
        etName.setText(sp.getString("myName", "not found"));

        intent = new Intent(MainActivity.this, Settings.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        wifi_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked && !wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, Id)
                            .setContentTitle("Wifi Notification")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentText("Wifi On")
                            .setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    notificationManager.notify(1, notification);


                } else if (!isChecked && wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, Id)
                            .setContentTitle("Wifi Notification")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentText("Wifi Off")
                            .setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    notificationManager.notify(0, notification);
                }
            }
        });
    }

    @OnTextChanged(value = R.id.etName, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void TextChanged(CharSequence text) {

        editor = sp.edit();
        editor.putString("myName", text.toString());
        editor.apply();
    }

    @Subscribe
    public void onEvent(CustomMessageEvent event) {
        aBoolean = event.getaBoolean();
        airplane_state.setChecked(aBoolean);
        bBoolean = event.getbBoolean();
        wifi_switch.setChecked(bBoolean);
        tvBattery.setText(event.getS1().toString());

        if (event.getS1() == "Low") {
            tvBattery.setTextColor(Color.RED);
        } else {
            tvBattery.setTextColor(Color.GREEN);
        }
    }
}
