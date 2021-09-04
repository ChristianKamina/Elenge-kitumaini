package com.youthfimodd.elenges.models;

public class servicemaps {

    //private String Id;
    private int icon;
    private String titre;

    public servicemaps() {
    }

    public servicemaps(int icon, String titre) {
        this.icon = icon;
        this.titre = titre;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
