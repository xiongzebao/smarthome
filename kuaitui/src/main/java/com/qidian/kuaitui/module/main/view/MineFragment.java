package com.qidian.kuaitui.module.main.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.king.zxing.CaptureActivity;
import com.qidian.base.base.BaseFragment;
import com.qidian.base.common.Constant;
import com.qidian.base.utils.DialogUtils;
import com.qidian.base.utils.SharedInfo;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.base.MyApplication;
import com.qidian.kuaitui.databinding.FragHomeBinding;
import com.qidian.kuaitui.databinding.FragMineBinding;
import com.qidian.kuaitui.module.mine.model.LoginBean;
import com.qidian.kuaitui.module.mine.view.ExceptionActivity;
import com.qidian.kuaitui.module.mine.view.FeedBackActivity;
import com.qidian.kuaitui.module.mine.view.LoginActivity;
import com.qidian.kuaitui.utils.ActivityUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MineFragment extends BaseFragment {
    public static final int RC_CAMERA = 0X01;
    FragMineBinding binding;

    public static final String KEY_TITLE = "key_title";
    public static final String KEY_IS_QR_CODE = "key_code";
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";

    public static final int REQUEST_CODE_SCAN = 0X03;
    public static final int REQUEST_CODE_PHOTO = 0X02;



    public static final int RC_READ_PHOTO = 0X02;

    private Class<?> cls;
    private String title;
    private boolean isContinuousScan;


    public static MineFragment newInstance() {
        MineFragment homeFrag = new MineFragment();
        return homeFrag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_mine, null, false);

        setBinding();

        return binding.getRoot();
    }


    /**
     * 检测拍摄权限
     */
    @AfterPermissionGranted(RC_CAMERA)
    public void checkCameraPermissions(){
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {//有权限
            try {
                startScanCode();
            }catch (Exception e){
                MyLog.e(e.getMessage());
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "App扫码需要用到拍摄权限",
                    RC_CAMERA, perms);
        }
    }


    public void startScanCode(){
        startScan(CaptureActivity.class, "扫描二位码");
    }

    public void startScan(Class<?> cls,String title){
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(getContext(),R.anim.in,R.anim.out);
        Intent intent = new Intent(getContext(), cls);
        intent.putExtra(KEY_TITLE,title);
        intent.putExtra(KEY_IS_CONTINUOUS,isContinuousScan);
        getActivity().startActivityForResult(intent,REQUEST_CODE_SCAN,optionsCompat.toBundle());
    }



    private void setUserInfo(){
        LoginBean loginBean =  SharedInfo.getInstance().getEntity(LoginBean.class);
        if(loginBean!=null){
            binding.tvName.setText(loginBean.getStaffName());
            binding.tvPhone.setText(loginBean.getStaffPhone());
        }

    }

    private void setBinding(){

        setUserInfo();

        binding.rlLeaveConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.rlExceptionUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startActivity(ExceptionActivity.class);
            }
        });

        binding.rlVersionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast("当前版本已是最新版本");
            }
        });


        binding.rlFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ActivityManager.startActivity(FeedBackActivity.class);
            }
        });

        binding.rlScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermissions();

            }
        });

        binding.rlExitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.signOutToLogin(getActivity());
            }
        });



    }

 

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }
}
