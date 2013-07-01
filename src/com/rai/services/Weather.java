package com.rai.services;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 23/06/13
 * Time: 20:03
 * To change this template use File | Settings | File Templates.
 */
public class Weather {

    private final String summary;
    private final Double temperature;
    private final Double humidity;

    public Weather(String summary, Double temperature, Double humidity) {
        this.summary = summary;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getSummary() {
        return summary;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "summary='" + summary + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';
    }
}
