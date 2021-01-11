package com.ihome.base.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.erongdu.wireless.tools.utils.TextUtil;
import com.ihome.base.R;


public class CutscenesProgress extends Dialog {
    private static CutscenesProgress cutscenesProgress = null;

    public CutscenesProgress(Context context) {
        super(context);
    }

    public CutscenesProgress(Context context, int theme) {
        super(context, theme);
    }

    public static CutscenesProgress createDialog(Context context) {
        cutscenesProgress = new CutscenesProgress(context, R.style.CustomDialog);
        cutscenesProgress.setContentView(R.layout.cutscenes_progress_layout);
        cutscenesProgress.getWindow().getAttributes().gravity = Gravity.CENTER;
        cutscenesProgress.setCanceledOnTouchOutside(false);
        return cutscenesProgress;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (cutscenesProgress == null) {
            return;
        }
        ImageView         imageView         = (ImageView) cutscenesProgress.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    /**
     * 标题
     */
    public CutscenesProgress setTitle(String strTitle) {
        super.setTitle(strTitle);
        return cutscenesProgress;
    }

    /**
     * 提示内容
     */
    public CutscenesProgress setMessage(String strMessage) {
        TextView tvMsg = (TextView) cutscenesProgress.findViewById(R.id.loadingMsg);
        TextView loadMsg = (TextView) cutscenesProgress.findViewById(R.id.download_msg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
            if(!TextUtil.isEmpty_new(strMessage)&&strMessage.startsWith("下载中")){
                tvMsg.setVisibility(View.VISIBLE);
                loadMsg.setVisibility(View.VISIBLE);
            }else{
                tvMsg.setVisibility(View.GONE);
                loadMsg.setVisibility(View.GONE);
            }
        }
        return cutscenesProgress;
    }


    /**
     * 进度条
     */
    public CutscenesProgress setProgress(int progress,long total) {
        NumberProgressBar bar = (NumberProgressBar) cutscenesProgress.findViewById(R.id.upprogress);
        if (bar != null) {
            if(progress>0&&total>0){
                bar.setVisibility(View.VISIBLE);
                bar.setProgress(progress);
                bar.setMax((int) total);
            }else{
                bar.setVisibility(View.GONE);
            }
        }
        return cutscenesProgress;
    }
}
