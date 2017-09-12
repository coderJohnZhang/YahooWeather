package com.tcl.john.weather;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

/**
 * Created by ZhangJun on 2017/9/7.
 */

public class WeatherInfo {
    public final ObservableBoolean isShow = new ObservableBoolean(); // 是否显示天气
    public final ObservableField<String> cityName = new ObservableField<>(); // 当前城市名
    public final ObservableField<String> weatherTemp = new ObservableField<>(); // 气温
    public final ObservableField<String> weatherImg = new ObservableField<>(); // 天气图标
    public final ObservableField<String> weatherTxt = new ObservableField<>(); // 天气描述
}
