package com.erongdu.wireless.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/22 16:05
 * <p/>
 * Description: 圆形头像
 */
public class AvatarImageView extends ImageView {
    private Paint paint;
    private Paint paint2;
    public  Paint inPaint;
    public  Paint outPaint;
    public  int inPad  = 5;
    public  int outPad = 1;
    private int pad    = 0;

    public AvatarImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvatarImageView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.setScaleType(ScaleType.FIT_XY);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint2 = new Paint();
        paint2.setXfermode(null);
        inPaint = new Paint();
        inPaint.setColor(ContextCompat.getColor(context, R.color.white));
        inPaint.setAntiAlias(true);
        outPaint = new Paint();
        outPaint.setColor(ContextCompat.getColor(context, R.color.colorGrey));
        outPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        pad = inPad + outPad;
        Bitmap bitmap  = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        drawLiftUp(canvas2);
        drawLiftDown(canvas2);
        drawRightUp(canvas2);
        drawRightDown(canvas2);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, outPaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - outPad, inPaint);
        canvas.drawBitmap(bitmap, 0, 0, paint2);
    }

    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() / 2);
        path.lineTo(0, 0);
        path.lineTo(getWidth() / 2, 0);
        path.arcTo(new RectF(pad, pad, getWidth() - pad, getHeight() - pad), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLiftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() / 2);
        path.lineTo(0, getHeight());
        path.lineTo(getWidth() / 2, getHeight());
        path.arcTo(new RectF(pad, pad, getWidth() - pad, getHeight() - pad), 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), getHeight() / 2);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() / 2, 0);
        path.arcTo(new RectF(pad, pad, getWidth() - pad, getHeight() - pad), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), getHeight() / 2);
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth() / 2, getHeight());
        path.arcTo(new RectF(pad, pad, getWidth() - pad, getHeight() - pad), 90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }
}
