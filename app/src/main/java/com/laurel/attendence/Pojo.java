package com.laurel.attendence;

/**
 * Created by SAIRAM on 12/6/2016.
 */

public class Pojo {

    private Double latitude;
    private Double longitude;
    private String time;

    @Override
    public String toString() {
        return "Pojo{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", time='" + time + '\'' +
                '}';
    }

    public Pojo() {
    }

    public Pojo(Double latitude, Double longitude, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
