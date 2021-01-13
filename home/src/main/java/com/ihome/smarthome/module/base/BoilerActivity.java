package com.ihome.smarthome.module.base;

import android.view.View;
import android.widget.EditText;

import com.ihome.base.base.BaseActivity;
import com.ihome.smarthome.R;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/11 11:14
 */

public class BoilerActivity extends BaseActivity {

    EditText et_input;

    @Override
    protected void bindView() {
        setContentView(R.layout.layout_boiler);
        et_input =  findViewById(R.id.et_input);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String msg = et_input.getText().toString();
                MyBluetoothManager.Instance(BoilerActivity.this).sendMessage("cooker",msg);
            }
        });


    }
}
