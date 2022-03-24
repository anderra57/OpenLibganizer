package com.anderpri.openlibganizer.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DBookDao {

    @Query("select * from DBook")
    List<DBook> getAllBooks();

    @Query("select b.* from User u inner join DBook b on u.username=h.username inner join Has h on h.isbn=b.isbn where u.username like :mUser")
    List<DBook> getAllBooksFromUsername(String mUser);

    @Query("select * from DBook where isbn like :mISBN")
    DBook getBookFromISBN(String mISBN);

    @Query("select count(*) from DBook where isbn like :mISBN")
    int checkIfBookAdded(String mISBN);

    @Insert
    void insertBook(DBook dBook);

    @Delete
    void delete(DBook dBook);

}
