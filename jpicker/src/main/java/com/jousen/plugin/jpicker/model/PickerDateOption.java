package com.jousen.plugin.jpicker.model;

import java.io.Serializable;
import java.util.Calendar;

public class PickerDateOption implements Serializable {
    //选择器初始类型 PICK_MONTH = 0 PICK_DAY = 1 PICK_TIME = 2 PICK_RANGE = 3
    public int pickType = 0;
    //年列表选择的范围设定
    public int wheelYearStart = 1900;
    public int wheelYearEnd = 2100;
    //初始时间
    public int initYear;
    public int initMonth;
    public int initDay;
    public int initHour;
    public int initMinute;
    public int initSecond;

    public PickerDateOption() {
        Calendar calendar = Calendar.getInstance();
        this.initYear = calendar.get(Calendar.YEAR);
        this.initMonth = calendar.get(Calendar.MONTH) + 1;
        this.initDay = calendar.get(Calendar.DAY_OF_MONTH);
        this.initHour = calendar.get(Calendar.HOUR_OF_DAY);
        this.initMinute = calendar.get(Calendar.MINUTE);
        this.initSecond = calendar.get(Calendar.SECOND);
    }
}