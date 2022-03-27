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

    @Query("update DBook set title = :mValue where isbn like :mISBN")
    void updateTitle(String mValue, String mISBN);

    @Query("update DBook set author = :mValue where isbn like :mISBN")
    void updateAuthor(String mValue, String mISBN);

    @Query("update DBook set publisher = :mValue where isbn like :mISBN")
    void updatePublisher(String mValue, String mISBN);

    @Query("update DBook set year = :mValue where isbn like :mISBN")
    void updateYear(String mValue, String mISBN);

    @Insert
    void insertBook(DBook dBook);

    @Delete
    void delete(DBook dBook);

}
