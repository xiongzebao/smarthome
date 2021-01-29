package com.ihome.smarthome.module.base;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.OrientationEventListener;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ihome.base.base.BaseActivity;
import com.ihome.smarthome.R;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.module.base.eventbusmodel.BTMessageEvent;
import com.ihome.smarthome.module.base.eventbusmodel.BaseMessageEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/28 10:51
 */

public class DHTActivity extends BaseActivity implements OnChartValueSelectedListener {
    private LineChart chart;
    protected Typeface tfRegular;
    protected Typeface tfLight;


    @Override
    protected void bindView() {
        setContentView(R.layout.activity_dht);
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        // enable description text
        chart.getDescription().setEnabled(true);
        // enable touch gestures
        chart.setTouchEnabled(true);
        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);
        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);
        Description description = new Description();
        description.setText("时间");
        chart.setDescription(description);
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);
        // add empty data
        chart.setData(data);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tfLight);
        l.setTextColor(Color.BLACK);
        l.setEnabled(false);
        XAxis xl = chart.getXAxis();
        xl.setTypeface(tfLight);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTypeface(tfLight);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.rgb(0, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));

            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(true);
        requestWriteSettingsPermission();
    }

    Pair<String, ICommunicate.Listener> listenerPair;

    private void addListener() {
        BluetoothDevice device = MyBluetoothManager.getInstance().findPairedBlueToothDeviceByName("cooker");
        listenerPair = new Pair<>(device.getAddress(), new ICommunicate.Listener() {
            @Override
            public void onMessage(BaseMessageEvent event) {
                if (event == null) {
                    return;
                }
                if (event instanceof BTMessageEvent) {
                    BTMessageEvent btMessageEvent = (BTMessageEvent) event;
                    if (btMessageEvent.getData() == null) {
                        return;
                    }
                    String json_data = btMessageEvent.getData().getJson_msg();
                    if (json_data == null) {
                        return;
                    }
                    DHT dht = GsonUtils.fromJson(json_data, DHT.class);
                    addTempEntry(dht.getTemp());
                    addHumiEntry(dht.getHumi());
                }
            }
        });
        MyBluetoothManager.getInstance().addListener(listenerPair);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyBluetoothManager.getInstance().removeListener(listenerPair);
        closeAutoRetoate();


    }

    @Override
    protected void onResume() {
        super.onResume();
        addListener();
        openAutoRetoate();

    }


    private void addTempEntry(float temp) {
        LineData data = chart.getData();
        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            set.setLabel("温度");

            data.addEntry(new Entry(set.getEntryCount(), temp), 0);
            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(120);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);
            // move to the latest entry
            chart.moveViewToX(data.getEntryCount());
            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private void addHumiEntry(float humi) {
        LineData data = chart.getData();
        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(1);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), humi), 1);
            data.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(120);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);
            // move to the latest entry
            chart.moveViewToX(data.getEntryCount());
            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }


    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "温度");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.rgb("#ff0000"));
        set.setCircleColor(Color.BLUE);
        set.setLineWidth(1f);
        set.setCircleRadius(2f);
        set.setFillAlpha(65);
        set.setFillColor(Color.BLUE);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);
        set.setDrawValues(true);

        return set;
    }


    protected void saveToGallery(Chart chart, String name) {
        if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70))
            Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                    .show();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }


}
