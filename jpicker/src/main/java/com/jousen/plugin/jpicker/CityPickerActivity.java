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

import com.gyf.immersionbar.ImmersionBar;
import com.jousen.plugin.jpicker.adapter.CityPickerAdapter;
import com.jousen.plugin.jpicker.model.CityData;
import com.jousen.plugin.jpicker.model.JPicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CityPickerActivity extends AppCompatActivity {
    private TextView city_picker_province;
    private TextView city_picker_city;
    private TextView city_picker_area;
    private TextView city_picker_choice;
    private CityPickerAdapter cityPickerAdapter;

    private List<CityData> cityData;
    private HashSet<String> specialCity;
    private int selectType = 0;//0 province 1 city 2 area
    private CityData selectProvince;
    private CityData selectCity;
    private CityData selectArea;

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
        cityPickerAdapter.setOnItemClickListener((position, data) -> selectAction(data));
        //默认展示的数据
        setProvinceList();
    }

    private void selectAction(CityData data) {
        //当前选择省份
        if (selectType == 0) {
            selectProvince = data;
            selectType = 1;
            city_picker_province.setText(data.t);
            setCityList();
            return;
        }
        //当前选择城市
        if (selectType == 1) {
            selectCity = data;
            selectType = 2;
            city_picker_city.setText(data.t);
            city_picker_city.setBackgroundResource(R.drawable.jpicker_card_border);
            setAreaList();
            return;
        }
        //当前选择地区
        if (selectType == 2) {
            selectArea = data;
            city_picker_area.setText(data.t);
            city_picker_area.setBackgroundResource(R.drawable.jpicker_card_border);
            pickComplete();
        }
    }

    private void setProvinceList() {
        List<CityData> provinces = new ArrayList<>();
        for (CityData item : cityData) {
            if (item.c.equals("00") && item.a.equals("00")) {
                provinces.add(item);
            }
        }
        cityPickerAdapter.setData(provinces);
    }

    private void setCityList() {
        List<CityData> cities = new ArrayList<>();
        //直辖市/自治区判断
        boolean special = specialCity.contains(selectProvince.p);
        for (CityData item : cityData) {
            if (special && item.p.equals(selectProvince.p) && !item.a.equals("00")) {
                cities.add(item);
                continue;
            }
            if (item.p.equals(selectProvince.p) && !item.c.equals("00") && item.a.equals("00")) {
                cities.add(item);
            }
        }
        cityPickerAdapter.setData(cities);
    }

    private void setAreaList() {
        boolean special = specialCity.contains(selectProvince.p);
        if (special) {
            selectArea = new CityData("", "", "", "");
            city_picker_area.setText(R.string.picker_choice_special);
            pickComplete();
            return;
        }
        List<CityData> areas = new ArrayList<>();
        for (CityData item : cityData) {
            if (item.p.equals(selectCity.p) && item.c.equals(selectCity.c) && !item.a.equals("00")) {
                areas.add(item);
            }
        }
        cityPickerAdapter.setData(areas);
    }

    private void pickComplete() {
        Intent intent = new Intent();
        intent.putExtra("province", selectProvince.t);
        intent.putExtra("city", selectCity.t);
        intent.putExtra("area", selectArea.t);
        setResult(JPicker.PICKER_CITY, intent);
        finish();
    }

    private void resetProvince() {
        selectProvince = null;
        selectCity = null;
        selectArea = null;
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
        if (selectProvince == null) {
            Toast.makeText(this, getResources().getText(R.string.picker_choice_province), Toast.LENGTH_SHORT).show();
            return;
        }
        selectCity = null;
        selectArea = null;
        selectType = 1;
        city_picker_city.setText(R.string.picker_choice_city);
        city_picker_area.setText(R.string.picker_choice_area);
        city_picker_choice.setText(R.string.picker_choice_city);
        city_picker_area.setBackgroundResource(R.drawable.jpicker_card_normal);

        setCityList();
    }

    private void resetArea() {
        if (selectCity == null) {
            Toast.makeText(this, getResources().getText(R.string.picker_choice_city), Toast.LENGTH_SHORT).show();
            return;
        }
        selectArea = null;
        selectType = 2;
        city_picker_area.setText(R.string.picker_choice_area);
        city_picker_choice.setText(R.string.picker_choice_area);

        setAreaList();
    }

    private void initCityData() {
        cityData = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = this.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open("city.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                cityData.add(new CityData(jsonObject.getString("p"), jsonObject.getString("c"), jsonObject.getString("a"), jsonObject.getString("t")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //直辖市/自治区数据
        specialCity = new HashSet<>();
        specialCity.add("11");//北京
        specialCity.add("12");//天津
        specialCity.add("31");//上海
        specialCity.add("50");//重庆
        specialCity.add("81");//香港
        specialCity.add("82");//澳门
    }

    private boolean isDayMode() {
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentMode == Configuration.UI_MODE_NIGHT_NO;
    }
}