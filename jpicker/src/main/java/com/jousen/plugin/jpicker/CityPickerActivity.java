package com.jousen.plugin.jpicker;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.jousen.plugin.jpicker.adapter.CityPickerAdapter;
import com.jousen.plugin.jpicker.model.CityData;
import com.jousen.plugin.jpicker.model.CityItem;
import com.jousen.plugin.jpicker.model.JPicker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CityPickerActivity extends AppCompatActivity {
    private TextView city_picker_province;
    private TextView city_picker_city;
    private TextView city_picker_area;
    private TextView city_picker_choice;
    private CityPickerAdapter cityPickerAdapter;

    private int selectType = 0;//0 province 1 city 2 area

    private CityData cityData;
    private String selectCode = "0";
    private String selectProvince = "";
    private String selectCity = "";
    private String selectArea = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);
        bindView();
    }

    private void bindView() {
        //沉浸式状态栏
        ImmersionBar.with(this).statusBarColor(R.color.picker_background).statusBarDarkFont(isDayMode()).fitsSystemWindows(true).init();
        //初始化城市json数据
        initCityData();
        //绑定视图
        city_picker_province = findViewById(R.id.city_picker_province);
        city_picker_city = findViewById(R.id.city_picker_city);
        city_picker_area = findViewById(R.id.city_picker_area);
        city_picker_choice = findViewById(R.id.city_picker_choice);
        RecyclerView city_picker_list = findViewById(R.id.city_picker_list);
        //默认list数据
        city_picker_list.setLayoutManager(new LinearLayoutManager(this));
        cityPickerAdapter = new CityPickerAdapter();
        city_picker_list.setAdapter(cityPickerAdapter);
        //点击事件
        findViewById(R.id.city_picker_back).setOnClickListener(v -> finish());
        city_picker_province.setOnClickListener(v -> resetProvince());
        city_picker_city.setOnClickListener(v -> resetCity());
        city_picker_area.setOnClickListener(v -> resetArea());
        cityPickerAdapter.setOnItemClickListener((position, data) -> choiceItem(data));
        //默认展示省份数据
        setProvinceList();
    }

    private void choiceItem(CityItem data) {
        //当前选择省份
        if (selectType == 0) {
            selectCode = data.c;
            selectProvince = data.t;
            selectType = 1;//下一步选择城市
            city_picker_province.setText(data.t);
            city_picker_choice.setText(R.string.picker_choice_city);
            setCityList();
            return;
        }
        //当前选择城市
        if (selectType == 1) {
            selectCode = data.c;
            selectCity = data.t;
            selectType = 2;
            city_picker_city.setText(data.t);
            city_picker_city.setBackgroundResource(R.drawable.jpicker_card_border);
            city_picker_choice.setText(R.string.picker_choice_area);
            setAreaList();
            return;
        }
        //当前选择地区
        if (selectType == 2) {
            selectCode = data.c;
            selectArea = data.t;
            city_picker_area.setText(data.t);
            city_picker_area.setBackgroundResource(R.drawable.jpicker_card_border);
            pickComplete();
        }
    }

    private void setProvinceList() {
        cityPickerAdapter.setData(cityData.province);
    }

    private void setCityList() {
        String provinceCode = selectCode.substring(0, 2);
        List<CityItem> cities = new ArrayList<>();
        for (CityItem item : cityData.city) {
            if (item.c.startsWith(provinceCode)) {
                cities.add(item);
            }
        }
        if (cities.size() == 1) {
            choiceItem(cities.get(0));
            return;
        }
        cityPickerAdapter.setData(cities);
    }

    private void setAreaList() {
        String cityCode = selectCode.substring(0, 4);
        List<CityItem> areas = new ArrayList<>();
        for (CityItem item : cityData.area) {
            if (item.c.startsWith(cityCode)) {
                areas.add(item);
            }
        }
        if (areas.isEmpty()) {
            pickComplete();
            return;
        }
        cityPickerAdapter.setData(areas);
    }

    private void pickComplete() {
        Intent intent = new Intent();
        intent.putExtra("code", selectCode);
        intent.putExtra("province", selectProvince);
        intent.putExtra("city", selectCity);
        intent.putExtra("area", selectArea);
        setResult(JPicker.PICKER_CITY, intent);
        finish();
    }

    private void resetProvince() {
        selectCode = "0";
        selectProvince = "";
        selectCity = "";
        selectArea = "";
        selectType = 0;
        city_picker_province.setText(R.string.picker_choice_province);
        city_picker_city.setText(R.string.picker_choice_city);
        city_picker_area.setText(R.string.picker_choice_area);
        city_picker_choice.setText(R.string.picker_choice_province);
        city_picker_city.setBackgroundResource(R.drawable.jpicker_card_normal);
        city_picker_area.setBackgroundResource(R.drawable.jpicker_card_normal);
        setProvinceList();
    }

    private void resetCity() {
        if (selectProvince.equals("")) {
            Toast.makeText(this, getResources().getText(R.string.picker_choice_province), Toast.LENGTH_SHORT).show();
            return;
        }
        selectCode = (selectCode.substring(0, 2) + "0000");
        selectCity = "";
        selectArea = "";
        selectType = 1;
        city_picker_city.setText(R.string.picker_choice_city);
        city_picker_area.setText(R.string.picker_choice_area);
        city_picker_choice.setText(R.string.picker_choice_city);
        city_picker_area.setBackgroundResource(R.drawable.jpicker_card_normal);
        setCityList();
    }

    private void resetArea() {
        if (selectProvince.equals("")) {
            Toast.makeText(this, getResources().getText(R.string.picker_choice_province), Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectCity.equals("")) {
            Toast.makeText(this, getResources().getText(R.string.picker_choice_city), Toast.LENGTH_SHORT).show();
        }
    }

    private void initCityData() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = this.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open("city.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            Gson gson = new Gson();
            cityData = gson.fromJson(stringBuilder.toString(), CityData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isDayMode() {
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentMode == Configuration.UI_MODE_NIGHT_NO;
    }
}