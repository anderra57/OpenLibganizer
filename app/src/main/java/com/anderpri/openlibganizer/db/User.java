package com.anderpri.openlibganizer.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo
    public String username;

    @ColumnInfo
    public String password;

    @ColumnInfo
    public String lang;

    @ColumnInfo
    public boolean theme;

    public User(@NonNull String username, String password, String lang, boolean theme) {
        this.username = username;
        this.password = password;
        this.lang = lang;
        this.theme = theme;
    }

}