package com.example.gdei.park.home;

/**
 * Created by gdei on 2018/4/19.
 */

public class ParkMsg {

    private String name;
    private double longitude;   //经度
    private double  latitude;   //维度
    private int canPark;    //可停车位数量
    private int totalPark;      //总车位数
    private float distance;       //距离
    public ParkMsg(String name, double longitude, double latitude, int canPark, int totalPark, float distance){
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.canPark = canPark;
        this.totalPark = totalPark;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getCanPark() {
        return canPark;
    }

    public int getTotalPark() {
        return totalPark;
    }

    public float getDistance() {
        return distance;
    }
}
