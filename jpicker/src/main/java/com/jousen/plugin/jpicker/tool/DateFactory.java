package com.jousen.plugin.jpicker.tool;

import java.util.ArrayList;
import java.util.List;

public class DateFactory {
    public static List<String> getYearData(int startYear, int endYear) {
        List<String> yearData = new ArrayList<>();
        for (int i1 = startYear; i1 <= endYear; i1++) {
            yearData.add(i1 + "");
        }
        return yearData;
    }

    public static List<String> getMonthData() {
        List<String> monthData = new ArrayList<>();
        for (int i2 = 1; i2 <= 12; i2++) {
            if (i2 < 10) {
                monthData.add("0" + i2);
            } else {
                monthData.add(i2 + "");
            }
        }
        return monthData;
    }

    public static List<String> getDayData(int maxDayOfMonth) {
        List<String> dayData = new ArrayList<>();
        for (int i3 = 1; i3 <= maxDayOfMonth; i3++) {
            if (i3 < 10) {
                dayData.add("0" + i3);
            } else {
                dayData.add(i3 + "");
            }
        }
        return dayData;
    }

    public static List<String> getHourData() {
        List<String> hourData = new ArrayList<>();
        for (int i4 = 0; i4 <= 23; i4++) {
            if (i4 < 10) {
                hourData.add("0" + i4);
            } else {
                hourData.add(String.valueOf(i4));
            }
        }
        return hourData;
    }

    public static List<String> getMinuteData() {
        List<String> minuteData = new ArrayList<>();
        for (int i5 = 0; i5 <= 59; i5++) {
            if (i5 < 10) {
                minuteData.add("0" + i5);
            } else {
                minuteData.add(String.valueOf(i5));
            }
        }
        return minuteData;
    }
}
