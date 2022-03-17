package com.anderpri.openlibganizer.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    public String user;

    @ColumnInfo(name = "pass")
    public String pass;

    public User(@NonNull String user, String pass){
        this.user=user;
        this.pass=pass;
    }
}