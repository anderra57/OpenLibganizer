package com.anderpri.openlibganizer.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DBook")
public class DBook {

    @PrimaryKey
    @NonNull
    @ColumnInfo
    public String isbn;
    @ColumnInfo
    public String thumbnail;
    @ColumnInfo
    public String title;
    @ColumnInfo
    public String author;
    @ColumnInfo
    public String publisher;
    @ColumnInfo
    public String year;

    public DBook(@NonNull String isbn, String title, String thumbnail, String author, String publisher, String year) {
        this.isbn = isbn;
        this.thumbnail = thumbnail;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
    }
}