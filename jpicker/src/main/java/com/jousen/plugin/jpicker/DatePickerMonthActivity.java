package com.jousen.plugin.jpicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.jousen.plugin.jpicker.model.JPicker;
import com.jousen.plugin.jpicker.model.PickerDateOption;
import com.jousen.plugin.jpicker.tool.DateFactory;
import com.jousen.plugin.jpicker.tool.JTool;
import com.jousen.plugin.jwheel.WheelView;

import java.util.Calendar;

public class DatePickerMonthActivity extends AppCompatActivity {
    private TextView date_picker_text;
    private WheelView year;
    private WheelView month;

    private PickerDateOption pickOption;
    private Calendar calendar;
    private int choiceYear;
    private int choiceMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker_month);
        bindView();
        initWheel();
    }

    private void bindView() {
        //沉浸式状态栏
        ImmersionBar.with(this).statusBarColor(R.color.picker_background).statusBarDarkFont(JTool.isDayMode(this)).fitsSystemWindows(true).init();
        //获取参数
        pickOption = (PickerDateOption) getIntent().getSerializableExtra("option");
        if (pickOption == null) {
            pickOption = new PickerDateOption();
        }
        //初始化视图
        date_picker_text = findViewById(R.id.date_picker_text);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);

        calendar = Calendar.getInstance();
        choiceYear = (pickOption.initYear >= 0 ? pickOption.initYear : calendar.get(Calendar.YEAR));
        choiceMonth = (pickOption.initMonth >= 0 ? pickOption.initMonth : (calendar.get(Calendar.MONTH) + 1));
        setDateText();

        findViewById(R.id.date_picker_back).setOnClickListener(v -> finish());
        findViewById(R.id.date_picker_confirm).setOnClickListener(v -> {
            if (choiceYear == 0 || choiceMonth == 0) {
                Toast.makeText(this, R.string.date_choice_error, Toast.LENGTH_SHORT).show();
                return;
            }
            String pickText = choiceYear + "-" + JTool.formatNum(choiceMonth);
            String pickStart = pickText + "-01 00:00:00";

            calendar.clear();
            calendar.set(Calendar.YEAR, choiceYear);
            calendar.set(Calendar.MONTH, (choiceMonth - 1));
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            String pickEnd = pickText + "-" + maxDay + " 23:59:59";

            Intent intent = new Intent();
            intent.putExtra("date_text", pickText);
            intent.putExtra("date_start", pickStart);
            intent.putExtra("date_end", pickEnd);
            setResult(JPicker.PICKER_DATE, intent);
            finish();
        });
    }

    private void initWheel() {
        year.setTextSize(18);
        year.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);
        month.setTextSize(18);
        month.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);
        if (pickOption.enableSound) {
            year.enableSound();
            month.enableSound();
        }
        year.setData(DateFactory.getYearData(pickOption.wheelYearStart, pickOption.wheelYearEnd));
        month.setData(DateFactory.getMonthData());

        year.initPosition(choiceYear - pickOption.wheelYearStart);
        month.initPosition(choiceMonth - 1);

        year.setOnSelectListener((position, data) -> {
            choiceYear = position + pickOption.wheelYearStart;
            setDateText();
        });
        month.setOnSelectListener((position, data) -> {
            choiceMonth = position + 1;
            setDateText();
        });
    }

    private void setDateText() {
        String text = choiceYear + "-" + JTool.formatNum(choiceMonth);
        date_picker_text.setText(text);
    }
}