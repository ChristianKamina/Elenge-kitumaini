package com.youthfimodd.elenges.models;

public class Magazin {

    private String id_mag;
    private String name_mag;
    private String url_mag;
    private String image_mag;

    public Magazin() {
    }

    public Magazin(String id_mag, String name_mag, String url_mag, String image_mag) {
        this.id_mag = id_mag;
        this.name_mag = name_mag;
        this.url_mag = url_mag;
        this.image_mag = image_mag;
    }

    public String getId_mag() {
        return id_mag;
    }

    public void setId_mag(String id_mag) {
        this.id_mag = id_mag;
    }

    public String getName_mag() {
        return name_mag;
    }

    public void setName_mag(String name_mag) {
        this.name_mag = name_mag;
    }

    public String getUrl_mag() {
        return url_mag;
    }

    public void setUrl_mag(String url_mag) {
        this.url_mag = url_mag;
    }

    public String getImage_mag() {
        return image_mag;
    }

    public void setImage_mag(String image_mag) {
        this.image_mag = image_mag;
    }
}
