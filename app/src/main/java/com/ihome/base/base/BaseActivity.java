package com.ihome.base.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.erongdu.wireless.views.PlaceholderLayout;
import com.ihome.base.R;
import com.ihome.base.common.ui.ListFragmentModel;
import com.ihome.base.utils.ScenceUtils;
import com.ihome.base.utils.SystemBarUtils;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity extends AppCompatActivity implements PlaceholderLayout.OnReloadListener {

    final static int REQUEST_CODE_ASK_WRITE_SETTINGS = 1001;
     PowerManager powerManager = null;
    @Override
   final  protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EventBus.getDefault().register(this);
        ActivityManager.push(this);
        setSystemBar();
        bindView();
        setOnBack();
    }

    protected String getTAG(){
        return this.getClass().getSimpleName();
    }

    @Override
   final public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
       // EventBus.getDefault().register(this);
        powerManager=(PowerManager) getSystemService(Context.POWER_SERVICE);
        ActivityManager.push(this);
        setSystemBar();
        bindView();
        setOnBack();

    }


    /*
       子类实现此方法绑定view
     */
    protected abstract void bindView();

    private void setSystemBar(){
  /*      if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }*/
        SystemBarUtils.setAndroidNativeLightStatusBar(this,true);
    }

    private void setOnBack(){
      ImageView iv=  findViewById(R.id.iv_back);
      if(iv!=null){
          iv.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  onBack();
              }
          });
      }
    }

    final  protected void setTitle(String title){
       TextView titleView =  findViewById(R.id.tv_title) ;
       if(titleView!=null){
           titleView.setText(title);
       }
    }


    protected void  onBack(){
       finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        ScenceUtils.closeCutScence(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager.pop();
      //  EventBus.getDefault().unregister(this);
    }

    protected ListFragmentModel model = new ListFragmentModel(this);


    @Override
    public void onReload(View v) {

    }

    @Override
    public void onApply(View v) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    protected void requestBatteryOptimizations() {


        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName =  getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            0);
                }
            }
        }
    }




    protected void requestWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, REQUEST_CODE_ASK_WRITE_SETTINGS);
            } else {
                //有了权限，你要做什么呢？具体的动作
                ToastUtil.toast("已有写系统设置权限");
            }

        }
    }


    String TAG = "xiong";

    class MyOrientoinListener extends OrientationEventListener {

        public MyOrientoinListener(Context context) {
            super(context);
        }

        public MyOrientoinListener(Context context, int rate) {
            super(context, rate);
        }

        @Override
        public void onOrientationChanged(int orientation) {

            int screenOrientation = getResources().getConfiguration().orientation;
            if (((orientation >= 0) && (orientation < 45)) || (orientation > 315)) {//设置竖屏
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    Log.d(TAG, "设置竖屏");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    //oriBtn.setText("竖屏");
                }
            } else if (orientation > 225 && orientation < 315) { //设置横屏
                Log.d(TAG, "设置横屏");
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    // oriBtn.setText("横屏");
                }
            } else if (orientation > 45 && orientation < 135) {// 设置反向横屏
                Log.d(TAG, "反向横屏");
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    // oriBtn.setText("反向横屏");
                }
            } else if (orientation > 135 && orientation < 225) {
                Log.d(TAG, "反向竖屏");
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    // oriBtn.setText("反向竖屏");
                }
            }
        }
    }
    MyOrientoinListener myOrientoinListener;


    protected void openAutoRetoate() {
        myOrientoinListener = new MyOrientoinListener(this);
        Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
        boolean autoRotateOn = (android.provider.Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
        //检查系统是否开启自动旋转
        if (autoRotateOn) {
            myOrientoinListener.enable();
        }
    }

    protected void closeAutoRetoate(){
        Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        boolean autoRotateOn = (android.provider.Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
        //检查系统是否开启自动旋转
        if (autoRotateOn) {
            myOrientoinListener.disable();
        }

    }




}

