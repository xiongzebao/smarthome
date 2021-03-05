package com.ihome.smarthome.bleaction

import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.ihome.smarthome.R
import kotlinx.android.synthetic.main.activity_main_ble.*

class BleMainActivity : AppCompatActivity() {
    //
    private val titles = arrayOf("扫描", "广播")
    //viewpager的fragment列表
    private lateinit var mFragments:ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ble)
        //初始化组件
        initView()
    }

    /**
      * 初始化组件
      */
    private fun initView(){
        //初始化Fragment
        mFragments = ArrayList()
        mFragments.add(BleScanPageFragment.newInstance())
        mFragments.add(BleServerPageFragment.newInstance())
        //初始化ViewPager
        mViewPager.adapter = object :FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mFragments.size
            }
        }
        //初始化TabLayout
        for (i in 0 until titles.size) {
            mTabLayout.addTab(mTabLayout.newTab())
        }
        mTabLayout.setupWithViewPager(mViewPager,false)
        for (i in titles.indices){
            mTabLayout.getTabAt(i)?.text = titles[i]
        }
    }
}
