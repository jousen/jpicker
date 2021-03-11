package com.jousen.plugin.jpicker.tool;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Calendar;

public class JTool {
    public static String formatNum(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return String.valueOf(num);
    }

    public static int getMaxDay(Calendar calendar, int choiceYear, int choiceMonth) {
        calendar.clear();
        calendar.set(Calendar.YEAR, choiceYear);
        calendar.set(Calendar.MONTH, (choiceMonth - 1));
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static boolean isDayMode(Context context) {
        int currentMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentMode == Configuration.UI_MODE_NIGHT_NO;
    }
}
