package com.erongdu.wireless.views.key;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.erongdu.wireless.views.R;

import java.lang.reflect.Method;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2016/11/29 上午9:49
 * <p/>
 * Description: xml文件  定义身份证输入的键盘
 */
public class IDCardKeysUtils implements KeyboardView.OnKeyboardActionListener {
    private EditText     editText; //当前  需要添加内容的EditText
    private Keyboard     k1;  //身份证输入键盘
    private KeyboardView keyboardView;//存放键盘的  布局

    //定义  构造方法一： 传入当前activity
    public IDCardKeysUtils(Activity activity, EditText editText) {
        this.editText = editText;
        /*activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/
        editTextFocus();
        //获取 身份证输入键盘
        k1 = new Keyboard(activity.getApplicationContext(), R.xml.idcard_key_board);
        keyboardView = (KeyboardView) activity.findViewById(R.id.keyboard_view);
        //填充布局:
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(this);  //添加监听事件键盘的
    }

    //定义  构造方法一： 传入当前activity
    public IDCardKeysUtils(Context context, View view, EditText editText) {
        this.editText = editText;
        editTextFocus();
        //获取 身份证输入键盘
        k1 = new Keyboard(context, R.xml.idcard_key_board);
        keyboardView = (KeyboardView) view.findViewById(R.id.keyboard_view);
        //填充布局:
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(this);  //添加监听事件键盘的
    }

    /**
     * 使用自定义键盘后，edittext光标比显示问题解决（3.0以下有光标,只是光标不闪动）
     */
    private void editTextFocus() {
        // 利用反射技术隐藏系统自带的键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            try {
                Class<EditText> cls = EditText.class;
                Method          setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //显示键盘
    public void showKeyBoard() {
        //显示自定义键盘时，当前的EditText获取焦点
        editText.requestFocus();
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {

            //显示自定键盘时，隐藏系统键盘
            InputMethodManager imm = (InputMethodManager) keyboardView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(keyboardView.getApplicationWindowToken(), 0);
            }

            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    //隐藏键盘：
    public void hideKeyBoard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }

    //------------------以下为键盘的监听事件--------------------
    @Override
    public void onPress(int primaryCode) {//按下事件
    }

    @Override
    public void onRelease(int primaryCode) {//释放事件
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        //添加监听事件：
        Editable edittable = editText.getText();//获取   当前EditText的可编辑对象
        int      start     = editText.getSelectionStart();//
        if (primaryCode == Keyboard.KEYCODE_CANCEL) {//完成  按键
            hideKeyBoard();//隐藏键盘
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {//回退
            if (edittable != null && edittable.length() > 0) {
                if (start > 0) {
                    edittable.delete(start - 1, start);
                }
            }
        } else if (primaryCode == 57419) {  //go left
            if (start > 0) {
                editText.setSelection(start - 1);
            }
        } else if (primaryCode == 57421) { //go right
            if (start < editText.length()) {
                editText.setSelection(start + 1);
            }
        } else {
            edittable.insert(start, Character.toString((char) primaryCode));
        }
    }

}
