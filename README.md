# jpicker

**Android city and date picker easy use.** 

**Android 地址和时间选择组件**

------

## 1、Feature 特性

- Support Android 5.0+       Android 5.0以上系统版本支持
- Support Only AndroidX    只支持 AndroidX
- Support landscape and portrait    支持横屏和竖屏模式

## 2、Import 依赖

1、Add the JitPack maven repository to the list of repositories 将JitPack存储库添加到您的构建文件中(项目根目录下build.gradle文件)

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
	//用于实现沉浸式状态栏  Used to implement immersive status bar
    implementation 'com.gyf.immersionbar:immersionbar:3.0.0'

    implementation 'com.github.jousen:jpicker:2.3'
}
```

## 3、Usage 使用

##### 1、Use in Activity 在Activity文件定义如下

```
		//城市选择
        findViewById(R.id.city_pick).setOnClickListener(v -> {
            Intent intent = new Intent(this, CityPickerActivity.class);
            startActivityForResult(intent, JPicker.PICKER_CITY);
        });
        //月份选择
        findViewById(R.id.month_pick).setOnClickListener(v ->
                new DatePicker(this)
                        .setPickType(DatePicker.PICK_MONTH)
                        .setInitDate(new Date())
                        .setEnableSound()
                        .show());
        //日期选择
        findViewById(R.id.day_pick).setOnClickListener(v ->
                new DatePicker(this)
                        .setPickType(DatePicker.PICK_DAY)
                        .setInitYear(2021)
                        .setInitMonth(1)
                        .setInitDay(1)
                        .setEnableSound()
                        .show());
        //月份选择
        findViewById(R.id.time_pick).setOnClickListener(v ->
                new DatePicker(this)
                        .setPickType(DatePicker.PICK_TIME)
                        .setInitDate(new Date())
                        .setEnableSound()
                        .show());
        //区间选择
        findViewById(R.id.range_pick).setOnClickListener(v ->
                new DatePicker(this)
                        .setPickType(DatePicker.PICK_RANGE)
                        .setInitDate(new Date())
                        .setEnableSound()
                        .show());
```

------

##### 2、onActivityResult get result data 使用onActivityResult接收数据

```
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == JPicker.PICKER_CITY && data != null) {
            Toast.makeText(this, data.getStringExtra("province") + " " + data.getStringExtra("city") + " " + 			data.getStringExtra("area"), Toast.LENGTH_LONG).show();
        }
        if (resultCode == JPicker.PICKER_DATE && data != null) {
            Toast.makeText(this, data.getStringExtra("date_text") + "\n" + data.getStringExtra("date_start") + "\n" + data.getStringExtra("date_end"), Toast.LENGTH_LONG).show();
        }
    }
```



<img src="https://github.com/jousen/jpicker/blob/main/img/%E7%AB%96%E5%B1%8F%E6%A8%A1%E5%BC%8F.mp4_20210312_071918.672.jpg" alt="竖屏模式.mp4_20210312_071918.672" style="zoom: 25%;" />

<img src="https://github.com/jousen/jpicker/blob/main/img/%E7%AB%96%E5%B1%8F%E6%A8%A1%E5%BC%8F.mp4_20210312_071945.035.jpg" alt="竖屏模式.mp4_20210312_071945.035" style="zoom: 25%;" />

<img src="https://github.com/jousen/jpicker/blob/main/img/%E7%AB%96%E5%B1%8F%E6%A8%A1%E5%BC%8F.mp4_20210312_071957.844.jpg" alt="竖屏模式.mp4_20210312_071957.844" style="zoom: 25%;" />

## Project use libraries

[https://github.com/mumuy/data_location](https://github.com/mumuy/data_location)

[https://github.com/gyf-dev/ImmersionBar](https://github.com/gyf-dev/ImmersionBar)

Thanks



## Licenses

```
Copyright 2021 jousen

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