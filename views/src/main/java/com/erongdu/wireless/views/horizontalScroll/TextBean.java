package com.erongdu.wireless.views.horizontalScroll;

import android.graphics.Bitmap;

/**
 * Author: TaoHao
 * E-mail: taoh@erongdu.com
 * Date: 2016/11/22 17:07
 * <p/>
 * Description:
 */
public class TextBean {
    /** 文字内容 */
    private String text;
    /** 文字上面图片 */
    private Bitmap bitmap = null;
    /** 资源文件 */
    private int    resId  = 0;
    /** 网络图片资源链接 */
    private String url    = null;

    public TextBean() {
    }

    public TextBean(String text, Bitmap bitmap) {
        this.text = text;
        this.bitmap = bitmap;
    }

    public TextBean(String text, int resId) {
        this.text = text;
        this.resId = resId;
    }

    public TextBean(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
