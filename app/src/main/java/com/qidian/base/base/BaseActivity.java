package com.qidian.base.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.views.PlaceholderLayout;
import com.qidian.base.R;
import com.qidian.base.common.ui.ListFragmentModel;
import com.qidian.base.utils.SystemBarUtils;

public abstract class BaseActivity extends AppCompatActivity implements PlaceholderLayout.OnReloadListener {


    @Override
   final  protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.e("  onCreate");
        ActivityManager.push(this);
        setSystemBar();
        bindView();
        setOnBack();
    }

    @Override
   final public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        MyLog.e("  onCreate");
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
        SystemBarUtils.setAndroidNativeLightStatusBar(this,false);
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
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.pop();
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
}

