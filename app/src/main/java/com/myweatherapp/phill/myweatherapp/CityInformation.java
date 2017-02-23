package com.myweatherapp.phill.myweatherapp;

/**
 * Created by phill on 17/02/2017.
 */

public class CityInformation {

    private String city;
    private int temp;
    private String update;
    private String thumbnail;

    public CityInformation(String city, int temp, String update, String thumbnail) {
        this.city = city;
        this.temp = temp;
        this.update = update;
        this.thumbnail = thumbnail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
