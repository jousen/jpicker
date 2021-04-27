package com.jousen.plugin.jpicker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.jousen.plugin.jpicker.model.PickerDateOption;
import com.jousen.plugin.jpicker.tool.JTool;

public class DatePickTypeActivity extends AppCompatActivity {
    private PickerDateOption pickOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_pick_type);
        //沉浸式状态栏
        ImmersionBar.with(this).statusBarColor(R.color.picker_background).statusBarDarkFont(JTool.isDayMode(this)).fitsSystemWindows(true).init();

        Intent intentData = getIntent();
        pickOption = (PickerDateOption) intentData.getSerializableExtra("option");
        //返回
        findViewById(R.id.date_picker_back).setOnClickListener(v -> finish());
        //月份
        findViewById(R.id.date_picker_type_month).setOnClickListener(v -> {
            Intent intent = new Intent(this, DatePickerMonthActivity.class);
            intent.putExtra("option", pickOption);
            startActivity(intent);
            finish();
        });
        //日期
        findViewById(R.id.date_picker_type_day).setOnClickListener(v -> {
            Intent intent = new Intent(this, DatePickerDayActivity.class);
            intent.putExtra("option", pickOption);
            startActivity(intent);
            finish();
        });
        //范围
        findViewById(R.id.date_picker_type_range).setOnClickListener(v -> {
            Intent intent = new Intent(this, DatePickerRangeActivity.class);
            intent.putExtra("option", pickOption);
            startActivity(intent);
            finish();
        });
        //时间
        findViewById(R.id.date_picker_type_time).setOnClickListener(v -> {
            Intent intent = new Intent(this, DatePickerTimeActivity.class);
            intent.putExtra("option", pickOption);
            startActivity(intent);
            finish();
        });
    }
}