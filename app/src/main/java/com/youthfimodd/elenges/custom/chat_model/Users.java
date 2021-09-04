package com.youthfimodd.elenges.custom.chat_model;

public class Users {
    //
    private String Id;
    private String name;
    private String sexe;
    private String age;
    private String image;
    private String phone;
    private String ville;
    private String status;
    private String timeStatus;
    private String typingTo;
    private String typeusesr;
    //
    public Users() {
    }

    public Users(String id, String name, String sexe, String age, String image, String phone, String ville, String status, String timeStatus, String typingTo, String typeusesr) {
        Id = id;
        this.name = name;
        this.sexe = sexe;
        this.age = age;
        this.image = image;
        this.phone = phone;
        this.ville = ville;
        this.status = status;
        this.timeStatus = timeStatus;
        this.typingTo = typingTo;
        this.typeusesr = typeusesr;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(String timeStatus) {
        this.timeStatus = timeStatus;
    }

    public String getTypingTo() {
        return typingTo;
    }

    public void setTypingTo(String typingTo) {
        this.typingTo = typingTo;
    }

    public String getTypeusesr() {
        return typeusesr;
    }

    public void setTypeusesr(String typeusesr) {
        this.typeusesr = typeusesr;
    }
}

