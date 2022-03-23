package com.anderpri.openlibganizer;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    String mThumbnail;
    String mTitle;
    String mAuthor;
    String mPublisher;
    String mYear;
    String mISBN;

    public Book() {
        this.mThumbnail = "N/A";
        this.mTitle = "N/A";
        this.mAuthor = "N/A";
        this.mPublisher = "N/A";
        this.mYear = "N/A";
        this.mISBN = "N/A";
    }

    public Book(String s0, String s1, String s2) {
        this.mThumbnail = s2;
        this.mTitle = s1;
        this.mAuthor = "N/A";
        this.mPublisher = "N/A";
        this.mYear = "N/A";
        this.mISBN = s0;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public void setmPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public String getmISBN() {
        return mISBN;
    }

    public void setmISBN(String mISBN) {
        this.mISBN = mISBN;
    }

    // Fuente: https://www.vogella.com/tutorials/AndroidParcelable/article.html

    public Book(Parcel in) {
        this.mThumbnail = in.readString();
        this.mTitle = in.readString();
        this.mAuthor = in.readString();
        this.mPublisher = in.readString();
        this.mYear = in.readString();
        this.mISBN = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.mThumbnail);
        dest.writeString(this.mTitle);
        dest.writeString(this.mAuthor);
        dest.writeString(this.mPublisher);
        dest.writeString(this.mYear);
        dest.writeString(this.mISBN);
    }
}
