package com.anderpri.openlibganizer.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HasDao {

    @Query("select * from Has")
    List<Has> getAllUsersBooks();

    @Insert
    void insertRelation(Has relation);

    @Delete
    void delete(Has relation);

    // para revisar si, tras borrar una relación, ese libro
    // sigue estando relacionado con algún usuario
    @Query("select count(*) from Has where isbn like :mISBN")
    int checkIfBookRelated(String mISBN);

}
