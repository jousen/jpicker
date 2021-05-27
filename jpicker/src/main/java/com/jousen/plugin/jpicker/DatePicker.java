package com.jousen.plugin.jpicker;


import android.app.Activity;
import android.content.Intent;

import com.jousen.plugin.jpicker.model.JPicker;
import com.jousen.plugin.jpicker.model.PickerDateOption;

import java.util.Calendar;
import java.util.Date;

public class DatePicker {
    public static final int PICK_MONTH = 0;
    public static final int PICK_DAY = 1;
    public static final int PICK_TIME = 2;
    public static final int PICK_RANGE = 3;

    private Activity activity;
    private PickerDateOption option;

    private DatePicker() {
    }

    public DatePicker(Activity activity) {
        this.activity = activity;
        this.option = new PickerDateOption();
    }

    public DatePicker setPickType(int pickType) {
        this.option.pickType = pickType;
        return this;
    }

    public DatePicker setWheelYearStart(int wheelYearStart) {
        this.option.wheelYearStart = wheelYearStart;
        return this;
    }

    public DatePicker setWheelYearEnd(int wheelYearEnd) {
        this.option.wheelYearEnd = wheelYearEnd;
        return this;
    }

    public DatePicker setInitDate(Date initDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initDate);

        this.option.initYear = calendar.get(Calendar.YEAR);
        this.option.initMonth = calendar.get(Calendar.MONTH) + 1;
        this.option.initDay = calendar.get(Calendar.DAY_OF_MONTH);
        this.option.initHour = calendar.get(Calendar.HOUR_OF_DAY);
        this.option.initMinute = calendar.get(Calendar.MINUTE);
        this.option.initSecond = calendar.get(Calendar.SECOND);
        return this;
    }

    public DatePicker setInitYear(int initYear) {
        this.option.initYear = initYear;
        return this;
    }

    public DatePicker setInitMonth(int initMonth) {
        this.option.initMonth = initMonth;
        return this;
    }

    public DatePicker setInitDay(int initDay) {
        this.option.initDay = initDay;
        return this;
    }

    public void show() {
        //时间选择单独跳转
        if (this.option.pickType == PICK_TIME) {
            Intent intentTime = new Intent(activity, DatePickerTimeActivity.class);
            intentTime.putExtra("option", this.option);
            activity.startActivityForResult(intentTime, JPicker.PICKER_DATE);
            return;
        }
        //日期选择组件
        Intent intent = new Intent(activity, DatePickerActivity.class);
        intent.putExtra("option", this.option);
        activity.startActivityForResult(intent, JPicker.PICKER_DATE);
    }

    @Deprecated
    public DatePicker setEnableSound() {
        return this;
    }
}
