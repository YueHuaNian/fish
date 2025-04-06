package com.example.fish.domain;

import java.sql.Timestamp;

public class Location {
    private int locationId;
    private double latitude;
    private double longitude;
    private String province;
    private String city;
    private String district;
    private String address;
    private Timestamp createdAt;

    public Location(int locationId, double latitude, double longitude, String province, String city, String district, String address, Timestamp createdAt) {
        this.locationId = locationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.createdAt = createdAt;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
