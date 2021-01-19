package com.ihome.smarthome.utils;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/18 17:17
 */

public class SpanUtils {
    SpannableStringBuilder mBuilder;
   private static SpanUtils spanUtils;
    public SpanUtils( ) {
        this.mBuilder = new SpannableStringBuilder();
    }

    public    static SpanUtils newInstance() {

        return new SpanUtils();
    }

   public SpanUtils append(String text, int color) {
        int start = mBuilder.length();
        mBuilder.append(text);
        int end = mBuilder.length();
        mBuilder.setSpan(new ForegroundColorSpan(color), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public void appendLine(){
        mBuilder.append("\n");
    }


    public void setText(TextView tv){
        if(tv==null){
            return;
        }
        tv.setText(mBuilder);
        mBuilder.clear();
    }

}
