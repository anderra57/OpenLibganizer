package com.anderpri.openlibganizer;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Books implements Parcelable {
    private List<Book> books;

    public Books() {
        this.books = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeTypedList(this.books);

    }

    public Books(Parcel in) {
        books = new ArrayList<>();
        in.readTypedList(books, Book.CREATOR);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Books createFromParcel(Parcel in) {
            return new Books(in);
        }

        public Books[] newArray(int size) {
            return new Books[size];
        }
    };

    public void add(Book book) {
        books.add(book);
    }

    public void add(int index_insert, Book mBook) {
        books.add(index_insert, mBook);
    }

    public void addAll(List<Book> bookList) {
        books.addAll(bookList);
    }

    public int size() {
        return books.size();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        books.forEach(b -> s.append(b.toString()).append("\n"));
        return s.toString();
    }

    public Book get(int position) {
        return books.get(position);
    }
}
