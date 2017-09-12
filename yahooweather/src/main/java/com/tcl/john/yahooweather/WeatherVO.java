package com.tcl.john.yahooweather;

/**
 * 天气对象
 *
 * @author john
 */
public class WeatherVO {
    /**
     * 城市名称
     */
    public String cityName;
    /**
     * 所属城市名称
     */
    public String belongCity;

    /**
     * 省级代号
     */
    public String provinceCode;
    /**
     * 市级代号
     */
    public String cityCode;

    /**
     * 天气
     */
    public String weather;
    /**
     * 白天或黑夜
     */
    public String type;
    /**
     * 天气图标
     */
    public String img;
    /**
     * 气温
     */
    public String temp;
    /**
     * 最高气温
     */
    public String maxTemp;
    /**
     * 湿度
     */
    public String wetness;
    /**
     * 风速
     */
    public String airSpeed;
}
