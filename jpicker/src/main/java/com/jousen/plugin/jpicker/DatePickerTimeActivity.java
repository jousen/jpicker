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

public class DatePickerTimeActivity extends AppCompatActivity {
    private TextView date_picker_text;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView minute;
    private WheelView second;

    private PickerDateOption pickOption;
    private Calendar calendar;
    private int choiceYear;
    private int choiceMonth;
    private int choiceDay;
    private int choiceHour;
    private int choiceMinute;
    private int choiceSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker_time);
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
        day = findViewById(R.id.day);
        hour = findViewById(R.id.hour);
        minute = findViewById(R.id.minute);
        second = findViewById(R.id.second);

        calendar = Calendar.getInstance();
        choiceYear = (pickOption.initYear >= 0 ? pickOption.initYear : calendar.get(Calendar.YEAR));
        choiceMonth = (pickOption.initMonth >= 0 ? pickOption.initMonth : (calendar.get(Calendar.MONTH) + 1));
        choiceDay = (pickOption.initDay >= 0 ? pickOption.initDay : calendar.get(Calendar.DAY_OF_MONTH));
        choiceHour = (pickOption.initHour >= 0 ? pickOption.initHour : calendar.get(Calendar.HOUR_OF_DAY));
        choiceMinute = (pickOption.initMinute >= 0 ? pickOption.initMinute : calendar.get(Calendar.MINUTE));
        choiceSecond = (pickOption.initSecond >= 0 ? pickOption.initSecond : calendar.get(Calendar.SECOND));
        setDateText();

        findViewById(R.id.date_picker_back).setOnClickListener(v -> finish());
        findViewById(R.id.date_picker_confirm).setOnClickListener(v -> {
            if (choiceYear == 0 || choiceMonth == 0 || choiceDay == 0) {
                Toast.makeText(this, R.string.date_choice_error, Toast.LENGTH_SHORT).show();
                return;
            }
            String pickText = choiceYear + "-" + JTool.formatNum(choiceMonth) + "-" + JTool.formatNum(choiceDay)
                    + " " + JTool.formatNum(choiceHour) + ":" + JTool.formatNum(choiceMinute) + ":" + JTool.formatNum(choiceSecond);
            String pickEnd = choiceYear + "-" + JTool.formatNum(choiceMonth) + "-" + JTool.formatNum(choiceDay) + " 23:59:59";

            Intent intent = new Intent();
            intent.putExtra("date_text", pickText);
            intent.putExtra("date_start", pickText);
            intent.putExtra("date_end", pickEnd);
            setResult(JPicker.PICKER_DATE, intent);
            finish();
        });
    }

    private void initWheel() {
        year.setTextSize(14);
        year.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);
        month.setTextSize(14);
        month.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);
        day.setTextSize(14);
        day.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);
        hour.setTextSize(14);
        hour.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);
        minute.setTextSize(14);
        minute.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);
        second.setTextSize(14);
        second.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);

        year.setData(DateFactory.getYearData(pickOption.wheelYearStart, pickOption.wheelYearEnd));
        month.setData(DateFactory.getMonthData());
        day.setData(DateFactory.getDayData(JTool.getMaxDay(calendar, choiceYear, choiceMonth)));
        hour.setData(DateFactory.getHourData());
        minute.setData(DateFactory.getMinuteData());
        second.setData(DateFactory.getSecondData());

        year.initPosition(choiceYear - pickOption.wheelYearStart);
        month.initPosition(choiceMonth - 1);
        day.initPosition(choiceDay - 1);
        hour.initPosition(choiceHour);
        minute.initPosition(choiceMinute);
        second.initPosition(choiceSecond);

        year.setOnSelectListener((position, data) -> {
            choiceYear = position + pickOption.wheelYearStart;
            day.setData(DateFactory.getDayData(JTool.getMaxDay(calendar, choiceYear, choiceMonth)));
            setDateText();
        });
        month.setOnSelectListener((position, data) -> {
            choiceMonth = position + 1;
            day.setData(DateFactory.getDayData(JTool.getMaxDay(calendar, choiceYear, choiceMonth)));
            setDateText();
        });
        day.setOnSelectListener((position, data) -> {
            choiceDay = position + 1;
            setDateText();
        });
        hour.setOnSelectListener((position, data) -> {
            choiceHour = position;
            setDateText();
        });
        minute.setOnSelectListener((position, data) -> {
            choiceMinute = position;
            setDateText();
        });
        second.setOnSelectListener((position, data) -> {
            choiceSecond = position;
            setDateText();
        });
    }

    private void setDateText() {
        String text = choiceYear + "-" + JTool.formatNum(choiceMonth) + "-" + JTool.formatNum(choiceDay)
                + " " + JTool.formatNum(choiceHour) + ":" + JTool.formatNum(choiceMinute) + ":" + JTool.formatNum(choiceSecond);
        date_picker_text.setText(text);
    }
}