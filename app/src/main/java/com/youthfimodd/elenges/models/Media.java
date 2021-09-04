package com.youthfimodd.elenges.models;

public class Media {

    private String id_media;
    private String title_media;
    private String image_media;
    private String url_media;

    public Media() {
    }

    public Media(String id_media, String title_media, String image_media, String url_media) {
        this.id_media = id_media;
        this.title_media = title_media;
        this.image_media = image_media;
        this.url_media = url_media;
    }

    public String getId_media() {
        return id_media;
    }

    public void setId_media(String id_media) {
        this.id_media = id_media;
    }

    public String getTitle_media() {
        return title_media;
    }

    public void setTitle_media(String title_media) {
        this.title_media = title_media;
    }

    public String getImage_media() {
        return image_media;
    }

    public void setImage_media(String image_media) {
        this.image_media = image_media;
    }

    public String getUrl_media() {
        return url_media;
    }

    public void setUrl_media(String url_media) {
        this.url_media = url_media;
    }
}
