package com.tcl.john.yahooweather;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YahooWeatherService extends TimerTask {
    private static final String TAG = YahooWeatherService.class.getSimpleName();
    private static final String YAHOO_WEATHER_BASE_URL = "https://query.yahooapis.com/v1/public/yql";
    private static final String YAHOO_WEATHER_FORECAST = "?format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&q=select%20*%20from%20weather.forecast%20";
    private static final String YAHOO_QUERY_BY_CITY = "where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22{cityname}%22)";
    private static final String WHAT_IS_MY_IP_URL = "http://ip-api.com/json/";
    private static final String GET_MY_IP_URL = "http://www.cmyip.com/";
    private static YahooWeatherService instance;

    private Timer mUpdateTimer;

    private YahooWeatherService() {
    }

    public static YahooWeatherService getInstance() {
        if (instance == null) {
            instance = new YahooWeatherService();
        }
        return instance;
    }

    public void bindService() {
        int updateTime = 5 * 60000;
        mUpdateTimer = new Timer();
        mUpdateTimer.schedule(this, 3000, updateTime);
    }

    public void unbindService() {
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        try {
            // 设置天气
            getWeatherByCity();
            cancel();
        } catch (Exception e) {
            cancel();
        }
    }

    private String captureWeatherImg(String buf) {
        int beginIx = buf.indexOf("img src=\"");
        int endIx = buf.indexOf(".gif") + 4;
        String result = buf.substring(beginIx, endIx);
        beginIx = result.indexOf("http");
        result = result.substring(beginIx);

        return result;
    }

    /**
     * 根据城市名获取天气
     *
     * @return
     */

    public void getWeatherByCity() {
        final String cityName = obtainCityNameByIp(captureIpJavascript());

        String url = YAHOO_WEATHER_BASE_URL + YAHOO_WEATHER_FORECAST + YAHOO_QUERY_BY_CITY;
        url = url.replace("{cityname}", cityName);
        OkHttpClient client = new OkHttpClient.Builder().build();
        Log.d(TAG, "getWeatherByCity: url = " + url);
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String str = response.body().string();
                    Log.d(TAG, "onResponse: " + str);
                    JSONObject weatherLike = parseJson(str);
                    try {
                        if (weatherLike != null) {

                            JSONObject weatherCondition = weatherLike.getJSONObject("condition");
                            String weatherDescription = weatherLike.getString("description");

                            WeatherVO vo = new WeatherVO();
                            Log.d(TAG, "fetchWeather: cityName = " + cityName);
                            vo.cityName = cityName;
                            Log.d(TAG, "fetchWeather: weather = " + weatherCondition.get("text").toString());
                            vo.weather = weatherCondition.get("text").toString();
                            Log.d(TAG, "fetchWeather: img = " + weatherDescription);
                            vo.img = captureWeatherImg(weatherDescription);

                            int iTemp = Integer.parseInt(weatherCondition.get("temp").toString());
                            iTemp = (int) (((float) (iTemp - 32)) / 1.8);
                            Log.d(TAG, "fetchWeather: temp = " + String.valueOf(iTemp));
                            vo.temp = String.valueOf(iTemp);

                            EventBus.getDefault().post(new WeatherEvent(vo));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse error " + e.getMessage());
                    }
                }
            }
        });
    }

    private JSONObject parseJson(String str) {
        JSONObject re = null;
        try {
            JSONObject jsonRoot = new JSONObject(str);
            re = jsonRoot.getJSONObject("query");
            re = re.getJSONObject("results");
            re = re.getJSONObject("channel");
            re = re.getJSONObject("item");
        } catch (Exception e) {
            Log.e(TAG, "parse JSON error:" + e.getMessage());
        }
        return re;
    }

    private String obtainCityNameByIp(String ip) {
        String strURL = WHAT_IS_MY_IP_URL + ip;
        Log.d(TAG, "obtainLocationInfoByIp: strURL = " + strURL);
        try {
            URL url = new URL(strURL);
            HttpURLConnection httpConn;
            httpConn = (HttpURLConnection) url.openConnection();
            InputStreamReader input = new InputStreamReader(httpConn.getInputStream(), "utf-8");
            BufferedReader bufReader = new BufferedReader(input);
            String line;
            StringBuilder contentBuf = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                contentBuf.append(line);
            }
            String buf = contentBuf.toString();
            JSONObject jsonRoot = new JSONObject(buf);
            String result = jsonRoot.optString("city");
            Log.d(TAG, "obtainCityNameByIp: result = " + result);
            input.close();
            bufReader.close();
            return result;
        } catch (IOException | JSONException e) {
            Log.e(TAG, "obtainLocationInfoByIp: error " + e.getMessage());
            return null;
        }
    }

    private String captureIpJavascript() {
        try {
            URL url = new URL(GET_MY_IP_URL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            boolean redirect = false;
            Log.d(TAG, "captureIpJavascript: responseCode = " + responseCode + " url = " + url);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                redirect = responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                        || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                        || responseCode == HttpURLConnection.HTTP_SEE_OTHER;
            }

            if (redirect) {
                String newUrlStr = httpConn.getHeaderField("Location");
                URL newUrl = new URL(newUrlStr);
                httpConn = (HttpURLConnection) newUrl.openConnection();
            }

            InputStreamReader input = new InputStreamReader(httpConn.getInputStream(), "utf-8");
            BufferedReader bufReader = new BufferedReader(input);
            String line;
            StringBuilder contentBuf = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                contentBuf.append(line + "\n");
            }
            String buf = contentBuf.toString();
            Log.d(TAG, "captureIpJavascript: buf = " + buf);

            Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
            Matcher matcher = pattern.matcher(buf);
            String result = "";
            if (matcher.find()) {
                result = matcher.group();
            }
            input.close();
            bufReader.close();
            Log.d(TAG, " ip = " + result);
            return result;
        } catch (IOException e) {
            Log.e(TAG, "captureIpJavascript: error " + e.getMessage());
            return null;
        }
    }
}
