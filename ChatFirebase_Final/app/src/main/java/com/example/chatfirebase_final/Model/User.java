package com.example.chatfirebase_final.Model;

public class User {
    private String userID;
    private String email;
    private String imageInfo;
    private String pass;
    private String user;

    public User(String userID, String email, String imageURL, String pass, String user) {
        this.userID = userID;
        this.email = email;
        imageInfo = imageURL;
        this.pass = pass;
        this.user = user;
    }

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageInfo;
    }

    public void setImageURL(String imageURL) {
        imageInfo = imageURL;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}


