package com.jousen.example.jpicker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.gyf.immersionbar.ImmersionBar;
import com.jousen.plugin.jpicker.CityPicker;
import com.jousen.plugin.jpicker.DatePicker;
import com.jousen.plugin.jpicker.model.JPicker;


public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> cityLauncher;
    private ActivityResultLauncher<Intent> dateLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //沉浸式状态栏
        ImmersionBar.with(this).statusBarColor(R.color.picker_background).statusBarDarkFont(isDayMode()).fitsSystemWindows(true).init();

        buildLauncher();

        bindClick();
    }

    private void buildLauncher() {
        //城市选择
        cityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == JPicker.PICKER_CITY && result.getData() != null) {
                        String text = result.getData().getStringExtra("code")
                                + " " + result.getData().getStringExtra("province")
                                + " " + result.getData().getStringExtra("city")
                                + " " + result.getData().getStringExtra("area");
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    }
                });
        //日期选择
        dateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == JPicker.PICKER_DATE && result.getData() != null) {
                        String text = result.getData().getStringExtra("date_text")
                                + "\n" + result.getData().getStringExtra("date_start")
                                + "\n" + result.getData().getStringExtra("date_end");
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void bindClick() {
        //城市选择
        findViewById(R.id.city_pick).setOnClickListener(v -> {
            Intent intent = new CityPicker(this).build();
            cityLauncher.launch(intent);
        });

        //月份选择
        findViewById(R.id.month_pick).setOnClickListener(v -> {
            Intent intent = new DatePicker(this).setPickType(DatePicker.PICK_MONTH).build();
            dateLauncher.launch(intent);
        });
        //日期选择
        findViewById(R.id.day_pick).setOnClickListener(v -> {
            Intent intent = new DatePicker(this).setPickType(DatePicker.PICK_DAY).setInitYear(2021)
                    .setInitMonth(1)
                    .setInitDay(1).build();
            dateLauncher.launch(intent);
        });
        //月份选择
        findViewById(R.id.time_pick).setOnClickListener(v -> {
            Intent intent = new DatePicker(this).setPickType(DatePicker.PICK_TIME).build();
            dateLauncher.launch(intent);
        });
        //区间选择
        findViewById(R.id.range_pick).setOnClickListener(v -> {
            Intent intent = new DatePicker(this).setPickType(DatePicker.PICK_RANGE).build();
            dateLauncher.launch(intent);
        });

        //切换主题
        findViewById(R.id.switch_theme).setOnClickListener(v -> {
            com.jousen.example.jpicker.MyApp app = (com.jousen.example.jpicker.MyApp) getApplicationContext();
            if (app.day_night_mode == AppCompatDelegate.MODE_NIGHT_YES) {
                app.day_night_mode = AppCompatDelegate.MODE_NIGHT_NO;
            } else {
                app.day_night_mode = AppCompatDelegate.MODE_NIGHT_YES;
            }
            AppCompatDelegate.setDefaultNightMode(app.day_night_mode);
            recreate();
        });
    }

    private boolean isDayMode() {
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentMode == Configuration.UI_MODE_NIGHT_NO;
    }
}