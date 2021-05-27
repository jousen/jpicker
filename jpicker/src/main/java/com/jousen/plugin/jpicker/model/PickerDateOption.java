package com.jousen.plugin.jpicker.model;

import java.io.Serializable;

public class PickerDateOption implements Serializable {
    //选择器初始类型 PICK_MONTH = 0 PICK_DAY = 1 PICK_TIME = 2 PICK_RANGE = 3
    public int pickType = 0;
    //年列表选择的范围设定
    public int wheelYearStart = 1900;
    public int wheelYearEnd = 2100;
    //初始时间
    public int initYear = -1;
    public int initMonth = -1;
    public int initDay = -1;
    public int initHour = -1;
    public int initMinute = -1;
    public int initSecond = -1;
}