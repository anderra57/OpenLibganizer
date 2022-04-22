package com.anderpri.openlibganizer.model;

public class DBook {

    public String isbn;
    public String thumbnail;
    public String title;
    public String author;
    public String publisher;
    public String year;
    public String key;

    public DBook(String isbn, String title, String thumbnail, String author, String publisher, String year, String key) {
        this.isbn = isbn;
        this.thumbnail = thumbnail;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.key = key;
    }
}