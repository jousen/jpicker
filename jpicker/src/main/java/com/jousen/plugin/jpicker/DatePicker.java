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

    public DatePicker setInitHour(int initHour) {
        this.option.initHour = initHour;
        return this;
    }

    public DatePicker setInitMinute(int initMinute) {
        this.option.initMinute = initMinute;
        return this;
    }

    public DatePicker setInitSecond(int initSecond) {
        this.option.initSecond = initSecond;
        return this;
    }

    public DatePicker setEnableSound() {
        this.option.enableSound = true;
        return this;
    }

    public void show() {
        switch (this.option.pickType) {
            case PICK_DAY:
                Intent intent1 = new Intent(activity, DatePickerDayActivity.class);
                intent1.putExtra("option", this.option);
                activity.startActivityForResult(intent1, JPicker.PICKER_DATE);
                break;
            case PICK_TIME:
                Intent intent2 = new Intent(activity, DatePickerTimeActivity.class);
                intent2.putExtra("option", this.option);
                activity.startActivityForResult(intent2, JPicker.PICKER_DATE);
                break;
            case PICK_RANGE:
                Intent intent3 = new Intent(activity, DatePickerRangeActivity.class);
                intent3.putExtra("option", this.option);
                activity.startActivityForResult(intent3, JPicker.PICKER_DATE);
                break;
            default:
                Intent intent = new Intent(activity, DatePickerMonthActivity.class);
                intent.putExtra("option", this.option);
                activity.startActivityForResult(intent, JPicker.PICKER_DATE);
                break;
        }
    }
}
