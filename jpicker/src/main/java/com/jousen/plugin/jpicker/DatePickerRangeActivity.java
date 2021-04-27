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

public class DatePickerRangeActivity extends AppCompatActivity {
    private TextView date_picker_text_start;
    private TextView date_picker_text_end;
    private WheelView year;
    private WheelView month;
    private WheelView day;

    private PickerDateOption pickOption;
    private Calendar calendar;
    private int pickType = 0;//0 正在选择开始时间 1 正在选择结束时间
    private int choiceYearStart;
    private int choiceMonthStart;
    private int choiceDayStart;
    private int choiceYearEnd;
    private int choiceMonthEnd;
    private int choiceDayEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker_range);
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
        date_picker_text_start = findViewById(R.id.date_picker_text_start);
        date_picker_text_end = findViewById(R.id.date_picker_text_end);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        day = findViewById(R.id.day);

        calendar = Calendar.getInstance();
        choiceYearStart = (pickOption.initYear >= 0 ? pickOption.initYear : calendar.get(Calendar.YEAR));
        choiceMonthStart = (pickOption.initMonth >= 0 ? pickOption.initMonth : (calendar.get(Calendar.MONTH) + 1));
        choiceDayStart = (pickOption.initDay >= 0 ? pickOption.initDay : calendar.get(Calendar.DAY_OF_MONTH));
        choiceYearEnd = choiceYearStart;
        choiceMonthEnd = choiceMonthStart;
        choiceDayEnd = choiceDayStart;
        setDateText();

        findViewById(R.id.date_picker_back).setOnClickListener(v -> finish());
        findViewById(R.id.date_picker_switch).setOnClickListener(v -> {
            Intent intent = new Intent(this, DatePickTypeActivity.class);
            intent.putExtra("option", pickOption);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.date_picker_text_start).setOnClickListener(v -> {
            pickType = 0;
            date_picker_text_start.setBackgroundResource(R.drawable.jpicker_card_border);
            date_picker_text_end.setBackgroundResource(R.drawable.jpicker_card_normal);

            year.initPosition(choiceYearStart - pickOption.wheelYearStart);
            month.initPosition(choiceMonthStart - 1);
            day.initPosition(choiceDayStart - 1);
        });
        findViewById(R.id.date_picker_text_end).setOnClickListener(v -> {
            pickType = 1;
            date_picker_text_start.setBackgroundResource(R.drawable.jpicker_card_normal);
            date_picker_text_end.setBackgroundResource(R.drawable.jpicker_card_border);

            year.initPosition(choiceYearEnd - pickOption.wheelYearStart);
            month.initPosition(choiceMonthEnd - 1);
            day.initPosition(choiceDayEnd - 1);
        });
        findViewById(R.id.date_picker_confirm).setOnClickListener(v -> {
            if (choiceYearStart == 0 || choiceMonthStart == 0 || choiceDayStart == 0) {
                Toast.makeText(this, R.string.date_choice_error, Toast.LENGTH_SHORT).show();
                return;
            }
            if (choiceYearEnd == 0 || choiceMonthEnd == 0 || choiceDayEnd == 0) {
                Toast.makeText(this, R.string.date_choice_error, Toast.LENGTH_SHORT).show();
                return;
            }
            String startText = choiceYearStart + "-" + JTool.formatNum(choiceMonthStart) + "-" + JTool.formatNum(choiceDayStart);
            String endText = choiceYearEnd + "-" + JTool.formatNum(choiceMonthEnd) + "-" + JTool.formatNum(choiceDayEnd);
            String pickText = startText + " 至 " + endText;
            String pickStart = startText + " 00:00:00";
            String pickEnd = endText + " 23:59:59";

            if (compareDate(pickStart, pickEnd)) {
                Toast.makeText(this, R.string.date_choice_range_error, Toast.LENGTH_SHORT).show();
                return;
            }
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
        year.setSelectSuffix("年");
        month.setTextSize(18);
        month.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);
        month.setSelectSuffix("月");
        day.setTextSize(18);
        day.setTextColor(getResources().getColor(R.color.picker_text), Color.LTGRAY);
        day.setSelectSuffix("日");
        if (pickOption.enableSound) {
            year.enableSound();
            month.enableSound();
            day.enableSound();
        }
        year.setData(DateFactory.getYearData(pickOption.wheelYearStart, pickOption.wheelYearEnd));
        month.setData(DateFactory.getMonthData());
        day.setData(DateFactory.getDayData(JTool.getMaxDay(calendar, choiceYearStart, choiceMonthStart)));

        year.initPosition(choiceYearStart - pickOption.wheelYearStart);
        month.initPosition(choiceMonthStart - 1);
        day.initPosition(choiceDayStart - 1);

        year.setOnSelectListener((position, data) -> {
            if (pickType == 0) {
                choiceYearStart = position + pickOption.wheelYearStart;
                day.setData(DateFactory.getDayData(JTool.getMaxDay(calendar, choiceYearStart, choiceMonthStart)));
                setDateText();
                return;
            }
            choiceYearEnd = position + pickOption.wheelYearStart;
            day.setData(DateFactory.getDayData(JTool.getMaxDay(calendar, choiceYearEnd, choiceMonthEnd)));
            setDateText();
        });
        month.setOnSelectListener((position, data) -> {
            if (pickType == 0) {
                choiceMonthStart = position + 1;
                day.setData(DateFactory.getDayData(JTool.getMaxDay(calendar, choiceYearStart, choiceMonthStart)));
                setDateText();
                return;
            }
            choiceMonthEnd = position + 1;
            day.setData(DateFactory.getDayData(JTool.getMaxDay(calendar, choiceYearEnd, choiceMonthEnd)));
            setDateText();
        });
        day.setOnSelectListener((position, data) -> {
            if (pickType == 0) {
                choiceDayStart = position + 1;
            } else {
                choiceDayEnd = position + 1;
            }
            setDateText();
        });
    }

    private void setDateText() {
        String textStart = choiceYearStart + "-" + JTool.formatNum(choiceMonthStart) + "-" + JTool.formatNum(choiceDayStart);
        date_picker_text_start.setText(textStart);

        String textEnd = choiceYearEnd + "-" + JTool.formatNum(choiceMonthEnd) + "-" + JTool.formatNum(choiceDayEnd);
        date_picker_text_end.setText(textEnd);
    }

    /**
     * 比较时间date1和date2，若date2西小于date1返回true
     */
    private boolean compareDate(String startDate, String endDate) {
        return startDate.compareTo(endDate) > 0;
    }
}