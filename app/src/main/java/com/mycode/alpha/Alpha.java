package com.mycode.alpha;

import com.google.firebase.Timestamp;

public class Alpha {

    private String email;
    private String caption;
    private String imageUrl;
    private String userId;
    private String userName;
    private Timestamp timeAdded;

    public Alpha() {
    }

    public Alpha(String caption, String imageUrl, String userId, String userName, String email, Timestamp timeAddeds) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.userName = userName;
        this.email=email;
        this.timeAdded = timeAdded;

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}
