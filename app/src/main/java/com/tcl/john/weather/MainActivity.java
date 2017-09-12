package com.tcl.john.weather;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tcl.john.weather.databinding.ActivityMainBinding;
import com.tcl.john.yahooweather.WeatherEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private WeatherViewModel mWeatherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mWeatherViewModel = new WeatherViewModel(this);
        mWeatherViewModel.bindService();
        activityMainBinding.setWeatherViewModel(mWeatherViewModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe
    public void onUpdateWeatherEvent(WeatherEvent event) {
        Log.d(TAG, "onUpdateWeatherEvent: WeatherVO = " + event.mWeatherVO);
        mWeatherViewModel.updateWeather(event.mWeatherVO);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mWeatherViewModel.unbindService();
        super.onDestroy();
    }
}
