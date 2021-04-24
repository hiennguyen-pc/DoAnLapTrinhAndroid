package com.example.chatfirebase_final.Model;

public class User {
    private String email;
    private String ImageURL;
    private String pass;
    private String user;

    public User(String email, String imageURL, String pass, String user) {
        this.email = email;
        ImageURL = imageURL;
        this.pass = pass;
        this.user = user;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
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


