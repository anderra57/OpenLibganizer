package com.anderpri.openlibganizer.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"username", "isbn"})
public class Has {

    @NonNull
    @ColumnInfo
    public String username;

    @NonNull
    @ColumnInfo
    public String isbn;

    public Has(@NonNull String username, @NonNull String isbn) {
        this.username = username;
        this.isbn = isbn;
    }
}
