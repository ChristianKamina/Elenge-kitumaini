package com.youthfimodd.elenges.models;

public class EDive {

    private String title;
    private String imageBg;

    public EDive() {
    }

    public EDive(String title, String imageBg) {
        this.title = title;
        this.imageBg = imageBg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageBg() {
        return imageBg;
    }

    public void setImageBg(String imageBg) {
        this.imageBg = imageBg;
    }
}
