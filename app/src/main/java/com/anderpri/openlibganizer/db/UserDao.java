package com.anderpri.openlibganizer.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select count(*) from user where username like :mUser limit 1")
    boolean userExists(String mUser);

    @Query("select * from user where username like :mUser and password like :mPass limit 1")
    User getUser(String mUser, String mPass);

    @Query("select count(*) from user where username like :mUser and password like :mPass limit 1")
    int checkLogin(String mUser, String mPass);

    @Insert
    void insertUser(User user);

    //@Delete
    //void delete(User user);

}
