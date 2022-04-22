package com.anderpri.openlibganizer.model;

public class User {

    public String username;

    public String password;

    public String lang;

    public boolean theme;

    public User(String username, String password, String lang, boolean theme) {
        this.username = username;
        this.password = password;
        this.lang = lang;
        this.theme = theme;
    }

}