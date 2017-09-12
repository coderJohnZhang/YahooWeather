package com.tcl.john.weather;

import com.tcl.john.yahooweather.YahooWeatherService;

/**
 * Created by ZhangJun on 2017/9/7.
 */

public class WeatherModel {

    private YahooWeatherService mYahooWeatherService;
    public WeatherModel() {
        mYahooWeatherService = YahooWeatherService.getInstance();
    }

    public void bindService() {
        if (mYahooWeatherService != null) {
            mYahooWeatherService.bindService();
        }
    }

    public void unbindService() {
        if (mYahooWeatherService != null) {
            mYahooWeatherService.unbindService();
        }
    }
}
