package com.tcl.john.weather;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tcl.john.yahooweather.WeatherVO;

/**
 * Created by ZhangJun on 2017/9/7.
 */

public class WeatherViewModel {
    private static final String TAG = WeatherViewModel.class.getSimpleName();

    private Context mContext;
    public WeatherInfo mWeatherInfo;
    private WeatherModel mWeatherModel;

    public WeatherViewModel(Context context) {
        mContext = context;
        initWeather();
    }

    private void initWeather() {
        mWeatherModel = new WeatherModel();
        mWeatherInfo = new WeatherInfo();
        mWeatherInfo.isShow.set(false);
        mWeatherInfo.cityName.set(mContext.getString(R.string.weather_default_city));
        mWeatherInfo.weatherTemp.set(mContext.getString(R.string.weather_default_temp));
        mWeatherInfo.weatherImg.set("");
        mWeatherInfo.weatherTxt.set(mContext.getString(R.string.weather_default_txt));
    }

    @BindingAdapter({"imageUrl", "error"})
    public static void getWeatherImage(ImageView iv, String imgUrl, Drawable error) {
        Glide.with(iv.getContext()).load(imgUrl).error(error).into(iv);
    }

    public void bindService() {
        if (mWeatherModel != null) {
            mWeatherModel.bindService();
        }
    }

    public void unbindService() {
        if (mWeatherModel != null) {
            mWeatherModel.unbindService();
        }
    }

    public void updateWeather(WeatherVO weatherVO) {
        if (weatherVO != null) {
            mWeatherInfo.isShow.set(true);
            mWeatherInfo.cityName.set(weatherVO.cityName);
            mWeatherInfo.weatherTemp.set(weatherVO.temp + "°C");
            mWeatherInfo.weatherImg.set(weatherVO.img);
            mWeatherInfo.weatherTxt.set(weatherVO.weather);
            Log.d(TAG, "updateWeather: cityName = " + mWeatherInfo.cityName.get() + " temp = " +
                    mWeatherInfo.weatherTemp.get() + " img = " + mWeatherInfo.weatherImg.get() +
            " txt = " + mWeatherInfo.weatherTxt.get());
        }
    }
}
