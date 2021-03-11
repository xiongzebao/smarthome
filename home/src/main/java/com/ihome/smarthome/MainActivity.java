package com.ihome.smarthome;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ihome.smarthome.module.base.LogFragment;
import com.ihome.smarthome.module.base.SmartHomeFragment;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.utils.BitmapUtil;
import com.ihome.smarthome.utils.CommonUtil;
import com.ihome.base.base.BaseActivity;
import com.ihome.base.base.BaseFragment;
import com.ihome.smarthome.base.MyApplication;
import com.ihome.smarthome.databinding.ActivityMainBinding;

import com.ihome.smarthome.module.main.view.MineFragment;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    SmartHomeFragment homeFrag;
    LogFragment jobFragment;
    MineFragment mineFragment;

    final String TAG_HOME = "tag_home";
    final String TAG_JOB = "tag_job";
    final String TAG_MINE = "tag_mine";

    public BottomNavigationBar.OnTabSelectedListener listener = new BottomNavigationBar.OnTabSelectedListener() {


        @Override
        public void onTabSelected(int position) {
            switch (position) {
                case 0:
                    showFragment(homeFrag, TAG_HOME);
                    break;
                case 1:
                    showFragment(jobFragment, TAG_JOB);
                    jobFragment.refresh();
                    break;
                case 2:
                    showFragment(mineFragment, TAG_MINE);
                    break;
            }
        }

        @Override
        public void onTabUnselected(int position) {
            switch (position) {
                case 0:
                    hideFragment(homeFrag, TAG_HOME);
                    break;
                case 1:
                    hideFragment(jobFragment, TAG_JOB);
                    break;
                case 2:
                    hideFragment(mineFragment, TAG_MINE);
                    break;
            }
        }

        @Override
        public void onTabReselected(int position) {

        }
    };


    private void hideFragment(BaseFragment fragment, String tag) {
        FragmentManager manager = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (null == fragment) {
            fragment = (BaseFragment) manager.findFragmentByTag(tag);
        }
        if (null != fragment) {
            transaction.hide(fragment);
        }
        transaction.commitAllowingStateLoss();
    }


    private void showFragment(BaseFragment fragment, String tag) {
        FragmentManager manager = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (null == fragment) {
            fragment = (BaseFragment) manager.findFragmentByTag(tag);
        }
        if (null == fragment) {
            switch (tag) {
                case TAG_HOME:
                    fragment = SmartHomeFragment.newInstance();
                    homeFrag = (SmartHomeFragment) fragment;
                    break;
                case TAG_JOB:
                    fragment =LogFragment.newInstance(LogEvent.LOG_NOTICE);
                    jobFragment = (LogFragment) fragment;
                    break;
                case TAG_MINE:
                    fragment = MineFragment.newInstance();
                    mineFragment = (MineFragment) fragment;
                    break;
            }

            transaction.add(R.id.content, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        transaction.commitAllowingStateLoss();

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void bindView() {
        MyApplication.mainActivity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initTab();
        BitmapUtil.init();

    }




    private void initTab() {
        binding.tabs
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor(R.color.main_blue)
                .setInActiveColor(R.color.tab_text_normal)
                .setLabelTextSize(10)
                .setBarBackgroundColor(R.color.white)
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_select, "首页")//这里表示选中的图片
                        .setInactiveIcon(ContextCompat.getDrawable(this,  R.mipmap.ic_home_unselect)))
                .addItem(new BottomNavigationItem(R.drawable.ic_task_select, "任务")//这里表示选中的图片
                        .setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.ic_task_unselect)))//非选中的图片)
                .addItem(new BottomNavigationItem(R.mipmap.ic_mine_select, "我的")//这里表示选中的图片
                        .setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.ic_mine_unselect)))//非选中的图片)
                .setTabSelectedListener(listener)
                .setFirstSelectedPosition(0)

                .initialise();
        this.binding.tabs.selectTab(0);


    }




    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        CommonUtil.saveCurrentLocation(MyApplication.application);
        if (MyApplication.application.trackConf.contains("is_trace_started")
                && MyApplication.application.trackConf.getBoolean("is_trace_started", true)) {
            // 退出app停止轨迹服务时，不再接收回调，将OnTraceListener置空
            MyApplication.application.mClient.setOnTraceListener(null);
            MyApplication.application.mClient.stopTrace(MyApplication.application.mTrace, null);
        } else {
            MyApplication.application.mClient.clear();
        }
        MyApplication.application.isTraceStarted = false;
        MyApplication.application.isGatherStarted = false;
        SharedPreferences.Editor editor = MyApplication.application.trackConf.edit();
        editor.remove("is_trace_started");
        editor.remove("is_gather_started");
        editor.apply();
        BitmapUtil.clear();
        MyApplication.mainActivity = null;
        BitmapUtil.clear();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.ihome.smarthome.module.base.SmartHomeFragment.REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                //bindFloatingService();
               // SmartHomeFragment.startFloatingService();
            }
        }
    }


}
