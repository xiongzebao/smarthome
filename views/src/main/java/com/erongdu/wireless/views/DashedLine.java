package com.erongdu.wireless.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: TaoHao
 * E-mail: taoh@erongdu.com
 * Date: 2016/11/7$ 17:58$
 * <p/>
 * Description: 画虚线
 */
public class DashedLine extends View {
    /** 虚线方向 */
    private int style;
    /** 虚线颜色 */
    private int color;

    public DashedLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.DashedLine);
        color = mTypedArray.getColor(R.styleable.DashedLine_dashedLineColor, Color.BLACK);
        style = mTypedArray.getInt(R.styleable.DashedLine_dashedLineOrientation, 0);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(5);
        Path path = new Path();
        path.moveTo(0, 0);
        if (style == 0) {
            path.lineTo(900, 0);
        } else if(style == 1) {
            path.lineTo(0, 900);
        }
        PathEffect effects = new DashPathEffect(new float[]{4, 4, 4, 4}, 4);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}
