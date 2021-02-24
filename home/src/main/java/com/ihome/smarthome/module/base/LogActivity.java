package com.ihome.smarthome.module.base;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.erongdu.wireless.tools.log.MyLog;
import com.google.android.material.tabs.TabLayout;
import com.ihome.base.base.BaseActivity;
import com.ihome.smarthome.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogActivity extends BaseActivity {
    ViewPager viewPager;
    TabLayout tabLayout;

    public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;
        List<String> titleList;
        FragmentManager fragmentManager;
        public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
            this.fragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            LogFragment logFragment = (LogFragment) fragmentList.get(position);
            return logFragment ;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_log);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.indicator);
        FragmentViewPagerAdapter pagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),getFragments(),getTitles());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               LogFragment fragment = (LogFragment) pagerAdapter.getItem(position);
               fragment.refresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    List<Fragment> getFragments(){
        ArrayList<Fragment> list = new ArrayList<>();
        for (int i=0;i<getTitles().size();i++){
            list.add(new LogFragment(i));
        }

        return list;
    }

  /*  final public static int LOG_DEBUG=1;
    final public static int LOG_FAILED=2;
    final public static int LOG_SUCCESS=3;
    final public static int LOG_EVENT=4;
    final public static int LOG_IMPORTANT=5;*/

    List<String> getTitles(){
        List<String> list =   Arrays.asList("全部","debug","failed","success","event","important","notice");
        return list;
    }
}