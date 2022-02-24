package com.jousen.plugin.jpicker;

import android.content.Context;
import android.content.Intent;

public class CityPicker {
    private final Context context;

    public CityPicker(Context context) {
        this.context = context;
    }

    public Intent build() {
        return new Intent(context, CityPickerActivity.class);
    }
}
