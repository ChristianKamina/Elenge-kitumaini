package com.youthfimodd.elenges.custom.santesr_model;

public class SanteSr {
    //
    private String titre;
    private String apercu;
    private String img;
    private String url;

    public SanteSr() {
    }

    public SanteSr(String titre, String apercu, String img, String url) {
        this.titre = titre;
        this.apercu = apercu;
        this.img = img;
        this.url = url;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getApercu() {
        return apercu;
    }

    public void setApercu(String apercu) {
        this.apercu = apercu;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
