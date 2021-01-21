package com.ihome.smarthome.applockscreen.activity;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.smarthome.R;
import com.ihome.smarthome.applockscreen.service.LockScreenService;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "LOCK";
    private LockScreenService service;
    private Button btn_open;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lock);

        btn_open = (Button) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, LockScreenService.class));
            }
        });



    }




}
