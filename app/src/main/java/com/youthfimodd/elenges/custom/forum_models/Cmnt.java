package com.youthfimodd.elenges.custom.forum_models;

import com.google.firebase.database.ServerValue;

public class Cmnt {
    //
    private String Comment;;
    private String uid;
    private String descrip;
    private String username;
    private String userimg;
    private Object timestamps;

    public Cmnt() {
    }

    public Cmnt(String comment, String uid, String descrip, String username ,String userimg, Object timestamps) {
        Comment = comment;
        this.uid = uid;
        this.descrip = descrip;
        this.username = username;
        this.userimg = userimg;
        this.timestamps = timestamps;
    }

    public Cmnt(String comment, String uid, String descrip, String username ,String userimg) {
        Comment = comment;
        this.uid = uid;
        this.descrip = descrip;
        this.username = username;
        this.userimg = userimg;
        this.timestamps = ServerValue.TIMESTAMP;
    }


    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setUimg(String descrip) {
        this.descrip = descrip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public Object getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Object timestamps) {
        this.timestamps = timestamps;
    }
}
