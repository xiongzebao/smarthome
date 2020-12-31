package com.erongdu.wireless.views;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Author: TaoHao
 * E-mail: taoh@erongdu.com
 * Date: 2016/11/10$ 10:56$
 * <p/>
 * Description: 带说明的EditText控件
 */
public class ExplainEditField extends FrameLayout {
    /** 输入框上面的文字 */
    private TextView label;
    /** 输入框 */
    private EditText editText;
    /** 文字显示的动画时间 */
    private int      labelDuration;
    /** 文字颜色 */
    private int      labelColor;
    /** 文字内容 */
    private String   labelTxt;
    /** 是否显示 */
    private boolean expanded       = false;
    private int     labelTopMargin = -1;
    private InputTextWatcherListener inputTextWatcherListener;

    public ExplainEditField(Context context) {
        super(context);
    }

    public ExplainEditField(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public ExplainEditField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    protected void initAttrs(Context context, AttributeSet attrs) {
        try {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ExplainEditField);

            labelDuration = styledAttrs.getInteger(R.styleable.ExplainEditField_labelDuration, 400);
            labelColor = styledAttrs.getColor(R.styleable.ExplainEditField_labelColor, -1);
            labelTxt = styledAttrs.getString(R.styleable.ExplainEditField_labelTxt);
            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextView createLabel() {
        label = new TextView(getContext());
        label.setText(labelTxt);
        label.setTextColor(labelColor);
        return label;
    }

    /**
     * 获取xml里面的EditText控件
     */
    private EditText findEditChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof EditText) {
            return (EditText) getChildAt(0);
        }
        return null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 得到EditText控件
        editText = findEditChild();
        if (editText == null) {
            return;
        }
        editText.addTextChangedListener(new InputTextWatcher());

        // 添加TextView控件 设置初始位置和状态
        addView(createLabel());
        ViewCompat.setPivotX(label, 0);
        ViewCompat.setPivotY(label, 0);
        ViewCompat.setScaleX(label, 0);
        ViewCompat.setScaleY(label, 0);
        ViewCompat.setAlpha(label, 0f);

        labelTopMargin = LayoutParams.class.cast(label.getLayoutParams()).topMargin;
    }

    public InputTextWatcherListener getInputTextWatcherListener() {
        return inputTextWatcherListener;
    }

    public void setInputTextWatcherListener(InputTextWatcherListener inputTextWatcherListener) {
        this.inputTextWatcherListener = inputTextWatcherListener;
    }

    private class InputTextWatcher implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {
            if (text.length() > 0 && !expanded) {
                ViewCompat.animate(label)
                        .alpha(0.5f)
                        .scaleX(0.8f)
                        .scaleY(0.8f)
                        .translationY(-labelTopMargin)
                        .setDuration(labelDuration);
                expanded = true;
            } else if (text.length() == 0 && expanded) {
                ViewCompat.animate(label)
                        .alpha(0)
                        .scaleX(0)
                        .scaleY(0)
                        .translationY(labelTopMargin)
                        .setDuration(labelDuration);
                expanded = false;
            }
            if (inputTextWatcherListener != null) {
                inputTextWatcherListener.setChange(editText, text);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence text, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable edit) {
        }
    }

    public interface InputTextWatcherListener {
        void setChange(EditText editText, CharSequence text);
    }
}
