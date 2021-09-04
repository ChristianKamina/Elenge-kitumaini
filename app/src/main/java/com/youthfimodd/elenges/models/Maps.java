package com.youthfimodd.elenges.models;

public class Maps {

    private String imageUrl;
    private String place;
    private String type;
    private String address;
    private String provider;
    private String phone;
    private String latitude;
    private String longitude;
    private String timestamp;
    private String username;

    public Maps() {
    }

    public Maps(String imageUrl, String place, String type, String address, String provider, String phone, String latitude, String longitude, String timestamp, String username) {
        this.imageUrl = imageUrl;
        this.place = place;
        this.type = type;
        this.address = address;
        this.provider = provider;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
