package com.qidian.kuaitui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.king.zxing.CameraScan;
import com.qidian.base.base.BaseActivity;
import com.qidian.base.base.BaseFragment;
import com.qidian.base.utils.BottomSheetDialogUtils;
import com.qidian.base.utils.SharedInfo;
import com.qidian.base.views.SelectRecyclerView;
import com.qidian.kuaitui.databinding.ActivityMainBinding;
import com.qidian.kuaitui.module.home.model.ProjectItem;
import com.qidian.kuaitui.module.main.view.HomeFragment;
import com.qidian.kuaitui.module.main.view.JobFragment;
import com.qidian.kuaitui.module.main.view.MineFragment;
import com.qidian.kuaitui.utils.ApiUtils;
import com.qidian.kuaitui.utils.CommenSetUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    HomeFragment homeFrag;
    JobFragment jobFragment;
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
                    fragment = HomeFragment.newInstance();
                    homeFrag = (HomeFragment) fragment;
                    break;
                case TAG_JOB:
                    fragment = JobFragment.newInstance();
                    jobFragment = (JobFragment) fragment;
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
        String topTitle = CommenSetUtils.getProjectTitle();
        if (homeFrag != null) {
            homeFrag.setTitle(topTitle);
        }
        if (jobFragment != null) {
            jobFragment.setTitle(topTitle);
        }

    }

    @Override
    protected void bindView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initTab();
    }

    private void initTab() {
        binding.tabs
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor(R.color.main_blue)
                .setInActiveColor(R.color.tab_text_normal)
                .setLabelTextSize(10)
                .setBarBackgroundColor(R.color.white)
                .addItem(new BottomNavigationItem(R.drawable.menu_home_fill, "首页")//这里表示选中的图片
                        .setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.menu_home)))
                .addItem(new BottomNavigationItem(R.drawable.ic_task_select, "任务")//这里表示选中的图片
                        .setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.ic_task_unselect)))//非选中的图片)
                .addItem(new BottomNavigationItem(R.drawable.menu_user_fill, "我的")//这里表示选中的图片
                        .setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.menu_user)))//非选中的图片)
                .setTabSelectedListener(listener)
                .setFirstSelectedPosition(0)

                .initialise();
        this.binding.tabs.selectTab(0);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data!=null){
            switch (requestCode){
                case MineFragment.REQUEST_CODE_SCAN:
                    String result = CameraScan.parseScanResult(data);

                     ApiUtils.uploadMemUserID(result);
                    break;

            }

        }
    }


}
