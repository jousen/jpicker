package com.jousen.plugin.jpicker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.jousen.plugin.jpicker.model.JPicker;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //沉浸式状态栏
        ImmersionBar.with(this).statusBarColor(R.color.picker_background).statusBarDarkFont(isDayMode()).fitsSystemWindows(true).init();
        //城市选择
        findViewById(R.id.city_pick).setOnClickListener(v -> {
            Intent intent = new Intent(this, CityPickerActivity.class);
            startActivityForResult(intent, JPicker.PICKER_CITY);
        });
        //月份选择
        findViewById(R.id.month_pick).setOnClickListener(v ->
                new DatePicker(this)
                        .setPickType(DatePicker.PICK_MONTH)
                        .setInitDate(new Date())
                        .setEnableSound()
                        .show());
        //日期选择
        findViewById(R.id.day_pick).setOnClickListener(v ->
                new DatePicker(this)
                        .setPickType(DatePicker.PICK_DAY)
                        .setInitYear(2021)
                        .setInitMonth(1)
                        .setInitDay(1)
                        .setEnableSound()
                        .show());
        //月份选择
        findViewById(R.id.time_pick).setOnClickListener(v ->
                new DatePicker(this)
                        .setPickType(DatePicker.PICK_TIME)
                        .setInitDate(new Date())
                        .setEnableSound()
                        .show());
        //区间选择
        findViewById(R.id.range_pick).setOnClickListener(v ->
                new DatePicker(this)
                        .setPickType(DatePicker.PICK_RANGE)
                        .setInitDate(new Date())
                        .setEnableSound()
                        .show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == JPicker.PICKER_CITY && data != null) {
            Toast.makeText(this, data.getStringExtra("province") + " " + data.getStringExtra("city") + " " + data.getStringExtra("area"), Toast.LENGTH_LONG).show();
        }
        if (resultCode == JPicker.PICKER_DATE && data != null) {
            Toast.makeText(this, data.getStringExtra("date_text") + "\n" + data.getStringExtra("date_start") + "\n" + data.getStringExtra("date_end"), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isDayMode() {
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentMode == Configuration.UI_MODE_NIGHT_NO;
    }
}