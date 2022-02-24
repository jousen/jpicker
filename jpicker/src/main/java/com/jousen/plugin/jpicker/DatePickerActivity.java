package com.jousen.plugin.jpicker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gyf.immersionbar.ImmersionBar;
import com.jousen.plugin.jpicker.model.JPicker;
import com.jousen.plugin.jpicker.model.PickerDateOption;
import com.jousen.plugin.jpicker.tool.DatePickResultListener;
import com.jousen.plugin.jpicker.tool.JTool;

public class DatePickerActivity extends AppCompatActivity implements DatePickResultListener {
    private PickerDateOption pickOption;
    private String[] tabArray;
    private int last_orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        initView();
    }

    @Override
    public void pickResult(String pickText, String pickStart, String pickEnd) {
        Intent intent = new Intent();
        intent.putExtra("date_text", pickText);
        intent.putExtra("date_start", pickStart);
        intent.putExtra("date_end", pickEnd);
        setResult(JPicker.PICKER_DATE, intent);
        finish();
    }

    private void initView() {
        //沉浸式状态栏
        ImmersionBar.with(this).statusBarColor(R.color.picker_background).statusBarDarkFont(JTool.isDayMode(this)).fitsSystemWindows(true).init();
        //获取参数
        pickOption = (PickerDateOption) getIntent().getSerializableExtra("option");
        if (pickOption == null) {
            pickOption = new PickerDateOption();
        }
        last_orientation = getResources().getConfiguration().orientation;
        //选择方式tab
        tabArray = new String[3];
        tabArray[0] = (String) getResources().getText(R.string.date_picker_type_month);
        tabArray[1] = (String) getResources().getText(R.string.date_picker_type_day);
        tabArray[2] = (String) getResources().getText(R.string.date_picker_type_range);
        //视图
        TabLayout tabLayout = findViewById(R.id.date_picker_tab);
        ViewPager2 date_picker_view = findViewById(R.id.date_picker_view);
        //事件
        findViewById(R.id.date_picker_back).setOnClickListener(v -> finish());
        date_picker_view.setOffscreenPageLimit(2);
        date_picker_view.setUserInputEnabled(false);
        date_picker_view.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new DatePickerMonthFragment(pickOption, DatePickerActivity.this);
                    case 1:
                        return new DatePickerDayFragment(pickOption, DatePickerActivity.this);
                    default:
                        return new DatePickerRangeFragment(pickOption, DatePickerActivity.this);
                }
            }

            @Override
            public int getItemCount() {
                return tabArray.length;
            }
        });
        new TabLayoutMediator(tabLayout, date_picker_view, true, false, (tab, position) -> tab.setText(tabArray[position])).attach();
        //初始视图
        if (pickOption.pickType == DatePicker.PICK_DAY) {
            date_picker_view.setCurrentItem(1, false);
        }
        if (pickOption.pickType == DatePicker.PICK_RANGE) {
            date_picker_view.setCurrentItem(2, false);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (last_orientation != newConfig.orientation) {
            Toast.makeText(this, R.string.tip_orientation_change, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}