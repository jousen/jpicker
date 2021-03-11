package com.jousen.plugin.jpicker.adapter;


import com.jousen.plugin.jpicker.model.CityData;

/**
 * @author 李易航
 * @date 2021/3/6
 */
public interface OnItemClickListener {
    void itemClick(int position, CityData data);
}