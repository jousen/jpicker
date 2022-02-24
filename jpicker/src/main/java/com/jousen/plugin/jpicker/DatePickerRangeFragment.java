package com.jousen.plugin.jpicker;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jousen.plugin.jpicker.model.PickerDateOption;
import com.jousen.plugin.jpicker.tool.DateFactory;
import com.jousen.plugin.jpicker.tool.DatePickResultListener;
import com.jousen.plugin.jpicker.tool.JTool;
import com.jousen.plugin.jwheel.WheelView;

import java.util.Calendar;

public class DatePickerRangeFragment extends Fragment {
    private final PickerDateOption pickOption;
    private final DatePickResultListener datePickResultListener;
    private Context context;
    private TextView date_picker_text_start;
    private TextView date_picker_text_end;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private Calendar calendar;
    private int pickType = 0;//0 正在选择开始时间 1 正在选择结束时间
    private int choiceYearStart;
    private int choiceMonthStart;
    private int choiceDayStart;
    private int choiceYearEnd;
    private int choiceMonthEnd;
    private int choiceDayEnd;

    public DatePickerRangeFragment(PickerDateOption pickOption, DatePickResultListener datePickResultListener) {
        this.pickOption = pickOption;
        this.datePickResultListener = datePickResultListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_pick_range, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        initWheel();
    }

    private void bindView(View view) {
        //初始化视图
        date_picker_text_start = view.findViewById(R.id.date_picker_text_start);
        date_picker_text_end = view.findViewById(R.id.date_picker_text_end);
        year = view.findViewById(R.id.year);
        month = view.findViewById(R.id.month);
        day = view.findViewById(R.id.day);

        calendar = Calendar.getInstance();
        choiceYearStart = (pickOption.initYear >= 0 ? pickOption.initYear : calendar.get(Calendar.YEAR));
        choiceMonthStart = (pickOption.initMonth >= 0 ? pickOption.initMonth : (calendar.get(Calendar.MONTH) + 1));
        choiceDayStart = (pickOption.initDay >= 0 ? pickOption.initDay : calendar.get(Calendar.DAY_OF_MONTH));
        choiceYearEnd = choiceYearStart;
        choiceMonthEnd = choiceMonthStart;
        choiceDayEnd = choiceDayStart;
        setDateText();

        view.findViewById(R.id.date_picker_text_start).setOnClickListener(v -> {
            pickType = 0;
            date_picker_text_start.setBackgroundResource(R.drawable.jpicker_card_border);
            date_picker_text_end.setBackgroundResource(R.drawable.jpicker_card_normal);

            year.initPosition(choiceYearStart - pickOption.wheelYearStart);
            month.initPosition(choiceMonthStart - 1);
            day.initPosition(choiceDayStart - 1);
        });
        view.findViewById(R.id.date_picker_text_end).setOnClickListener(v -> {
            pickType = 1;
            date_picker_text_start.setBackgroundResource(R.drawable.jpicker_card_normal);
            date_picker_text_end.setBackgroundResource(R.drawable.jpicker_card_border);

            year.initPosition(choiceYearEnd - pickOption.wheelYearStart);
            month.initPosition(choiceMonthEnd - 1);
            day.initPosition(choiceDayEnd - 1);
        });
        view.findViewById(R.id.date_picker_confirm).setOnClickListener(v -> {
            if (choiceYearStart == 0 || choiceMonthStart == 0 || choiceDayStart == 0) {
                Toast.makeText(context, R.string.date_choice_error, Toast.LENGTH_SHORT).show();
                return;
            }
            if (choiceYearEnd == 0 || choiceMonthEnd == 0 || choiceDayEnd == 0) {
                Toast.makeText(context, R.string.date_choice_error, Toast.LENGTH_SHORT).show();
                return;
            }
            String startText = choiceYearStart + "-" + JTool.formatNum(choiceMonthStart) + "-" + JTool.formatNum(choiceDayStart);
            String endText = choiceYearEnd + "-" + JTool.formatNum(choiceMonthEnd) + "-" + JTool.formatNum(choiceDayEnd);
            String pickText = startText + " 至 " + endText;
            String pickStart = startText + " 00:00:00";
            String pickEnd = endText + " 23:59:59";

            if (compareDate(pickStart, pickEnd)) {
                Toast.makeText(context, R.string.date_choice_range_error, Toast.LENGTH_SHORT).show();
                return;
            }
            //返回结果
            datePickResultListener.pickResult(pickText, pickStart, pickEnd);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
