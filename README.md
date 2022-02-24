# jpicker

**Android 地址和时间选择组件**

**Android city and date picker easy use.** 

<img src="https://github.com/jousen/jpicker/blob/main/img/1.png"/>

<img src="https://github.com/jousen/jpicker/blob/main/img/2.png"/>

<img src="https://github.com/jousen/jpicker/blob/main/img/3.png"/>

------

## 1、特性 Feature 

- Android 5.0以上系统版本支持 Support Android 5.0+       
- 仅支持 AndroidX Support Only AndroidX
- 支持横屏和竖屏模式 Support landscape and portrait 
- 支持暗黑模式 Support dark mode

## 2、依赖 Import 

1、将JitPack存储库添加到您的构建文件中(项目根目录下build.gradle文件)

Add the JitPack maven repository to the list of repositories

**build.gradle**

[![](https://jitpack.io/v/jousen/jpicker.svg)](https://jitpack.io/#jousen/jpicker)

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2、Add dependencies 

```
dependencies {
    implementation 'com.github.jousen:jpicker:4.6'
}
```

## 3、使用 Usage

##### 1、定义ActivityResultLauncher

```
        //城市选择
        cityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == JPicker.PICKER_CITY && result.getData() != null) {
                        String text = result.getData().getStringExtra("code")
                                + " " + result.getData().getStringExtra("province")
                                + " " + result.getData().getStringExtra("city")
                                + " " + result.getData().getStringExtra("area");
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    }
                });
        //日期选择
        dateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == JPicker.PICKER_DATE && result.getData() != null) {
                        String text = result.getData().getStringExtra("date_text")
                                + "\n" + result.getData().getStringExtra("date_start")
                                + "\n" + result.getData().getStringExtra("date_end");
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    }
                });
```

##### 2、调用控件 Use in Activity

```
        //城市选择
        findViewById(R.id.city_pick).setOnClickListener(v -> {
            Intent intent = new CityPicker(this).build();
            cityLauncher.launch(intent);
        });

        //月份选择
        findViewById(R.id.month_pick).setOnClickListener(v -> {
            Intent intent = new DatePicker(this).setPickType(DatePicker.PICK_MONTH).build();
            dateLauncher.launch(intent);
        });
        //日期选择
        findViewById(R.id.day_pick).setOnClickListener(v -> {
            Intent intent = new DatePicker(this).setPickType(DatePicker.PICK_DAY).setInitYear(2021)
                    .setInitMonth(1)
                    .setInitDay(1).build();
            dateLauncher.launch(intent);
        });
        //月份选择
        findViewById(R.id.time_pick).setOnClickListener(v -> {
            Intent intent = new DatePicker(this).setPickType(DatePicker.PICK_TIME).build();
            dateLauncher.launch(intent);
        });
        //区间选择
        findViewById(R.id.range_pick).setOnClickListener(v -> {
            Intent intent = new DatePicker(this).setPickType(DatePicker.PICK_RANGE).build();
            dateLauncher.launch(intent);
        });
```



## Project use libraries

[https://github.com/gyf-dev/ImmersionBar](https://github.com/gyf-dev/ImmersionBar)

[https://github.com/youzan/vant/tree/dev/packages/vant-area-data](https://github.com/youzan/vant/tree/dev/packages/vant-area-data)

Thanks



## Licenses

```
Copyright 2022 jousen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```