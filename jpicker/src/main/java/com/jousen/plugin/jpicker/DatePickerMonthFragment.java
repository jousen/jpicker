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

public class DatePickerMonthFragment extends Fragment {
    private final PickerDateOption pickOption;
    private final DatePickResultListener datePickResultListener;
    private Context context;
    private TextView date_picker_text;
    private WheelView year;
    private WheelView month;
    private Calendar calendar;
    private int choiceYear;
    private int choiceMonth;

    public DatePickerMonthFragment(PickerDateOption pickOption, DatePickResultListener datePickResultListener) {
        this.pickOption = pickOption;
        this.datePickResultListener = datePickResultListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_pick_month, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        initWheel();
    }

    private void bindView(View view) {
        //初始化视图
        date_picker_text = view.findViewById(R.id.date_picker_text);
        year = view.findViewById(R.id.year);
        month = view.findViewById(R.id.month);

        calendar = Calendar.getInstance();
        choiceYear = (pickOption.initYear >= 0 ? pickOption.initYear : calendar.get(Calendar.YEAR));
        choiceMonth = (pickOption.initMonth >= 0 ? pickOption.initMonth : (calendar.get(Calendar.MONTH) + 1));
        setDateText();

        view.findViewById(R.id.date_picker_confirm).setOnClickListener(v -> {
            if (choiceYear == 0 || choiceMonth == 0) {
                Toast.makeText(context, R.string.date_choice_error, Toast.LENGTH_SHORT).show();
                return;
            }
            String pickText = choiceYear + "-" + JTool.formatNum(choiceMonth);
            String pickStart = pickText + "-01 00:00:00";

            calendar.clear();
            calendar.set(Calendar.YEAR, choiceYear);
            calendar.set(Calendar.MONTH, (choiceMonth - 1));
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            String pickEnd = pickText + "-" + maxDay + " 23:59:59";
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
