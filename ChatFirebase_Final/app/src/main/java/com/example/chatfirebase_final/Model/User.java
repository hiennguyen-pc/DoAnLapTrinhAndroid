package com.example.chatfirebase_final.Model;

public class User {
    private String email;
    private String pass;
    private String user;
    private String ImageURL;

    public User(String email, String pass, String user, String imageURL) {
        this.email = email;
        this.pass = pass;
        this.user = user;
        ImageURL = imageURL;
    }

    public  User(){}
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}


