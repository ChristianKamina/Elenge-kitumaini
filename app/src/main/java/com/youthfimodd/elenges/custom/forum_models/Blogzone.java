package com.youthfimodd.elenges.custom.forum_models;

public class Blogzone {

  private String userImg, desc, imageUrl, username, status;
  private Object timestamp;

  public Blogzone() {
  }

  public Blogzone(String userImg, String desc, String imageUrl, String username, String status, Object timestamp) {
    this.userImg = userImg;
    this.desc = desc;
    this.imageUrl = imageUrl;
    this.username = username;
    this.status = status;
    this.timestamp = timestamp;
  }

  public String getUserImg() {
    return userImg;
  }

  public void setUserImg(String userImg) {
    this.userImg = userImg;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Object getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Object timestamp) {
    this.timestamp = timestamp;
  }
}
