package com.anderpri.openlibganizer.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.anderpri.openlibganizer.R;
import com.anderpri.openlibganizer.controllers.AppDatabase;
import com.anderpri.openlibganizer.model.Book;
import com.bumptech.glide.Glide;

public class CardActivity extends AppCompatActivity {

    private String mISBN;
    private ImageView thumb;
    private TextView title;
    private TextView author;
    private TextView publisher;
    private TextView year;
    private EditText title_edit;
    private EditText author_edit;
    private EditText publisher_edit;
    private EditText year_edit;
    Button share;
    Button edit;
    boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        if (getIntent().hasExtra("book")) {
            showBookInfo();
            share = findViewById(R.id.card_share);
            share.setOnClickListener(this::shareBook);
            edit = findViewById(R.id.card_edit);
            edit.setOnClickListener(this::onEditClick);
        } else {
            finish();
        }

    }

    private void showBookInfo() {
        Book book = getIntent().getParcelableExtra("book");
        Log.d("BOOK:", book.getmTitle());

        mISBN = book.getmISBN();

        ImageView thumb = findViewById(R.id.card_thumb);
        title = findViewById(R.id.card_title);
        author = findViewById(R.id.card_author);
        publisher = findViewById(R.id.card_publisher);
        year = findViewById(R.id.card_year);
        title_edit = findViewById(R.id.card_title_edit);
        author_edit = findViewById(R.id.card_author_edit);
        publisher_edit = findViewById(R.id.card_publisher_edit);
        year_edit = findViewById(R.id.card_year_edit);

        if (!book.getmThumbnail().equals(getString(R.string.default_info))) {
            // Fuente: https://devexperto.com/glide-android/
            Uri uri = Uri.parse(book.getmThumbnail());
            Glide.with(this).load(uri).into(thumb);
        } else {
            thumb.setImageResource(R.drawable.cover_not_available);
        }

        title.setText(book.getmTitle());
        author.setText(book.getmAuthor());
        publisher.setText(book.getmPublisher());
        year.setText(book.getmYear());
        title_edit.setText(book.getmTitle());
        author_edit.setText(book.getmAuthor());
        publisher_edit.setText(book.getmPublisher());
        year_edit.setText(book.getmYear());
    }

    // Fuente: https://stackoverflow.com/a/13941178
    private void shareBook(View view) {
        Book book = getIntent().getParcelableExtra("book");
        String mTitle = book.getmTitle();
        String mAuthor = book.getmAuthor();
        String mKey = book.getmKey();

        if (mTitle.length() > 50) {
            mTitle = mTitle.substring(0, 47) + "...";
        }

        String sendText = "Te recomiendo este libro: " + mTitle + " de " + mAuthor + " https://openlibrary.org" + mKey;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void onEditClick(View view){
        LinearLayout card_layout = findViewById(R.id.card_layout);
        LinearLayout card_layout_edit = findViewById(R.id.card_layout_edit);
        if (!editMode){
            editMode = true;
            card_layout_edit.setVisibility(View.VISIBLE);
            card_layout.setVisibility(View.GONE);
        } else {
            editMode = false;
            card_layout_edit.setVisibility(View.GONE);
            card_layout.setVisibility(View.VISIBLE);

            AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());

            if (!title.getText().toString().equals(title_edit.getText().toString())){
                db.updateTitle(title_edit.getText().toString(),mISBN);
                title.setText(title_edit.getText().toString());
            }
            if (!author.getText().toString().equals(author_edit.getText().toString())){
                db.updateAuthor(author_edit.getText().toString(),mISBN);
                author.setText(author_edit.getText().toString());
            }
            if (!publisher.getText().toString().equals(publisher_edit.getText().toString())){
                db.updatePublisher(publisher_edit.getText().toString(),mISBN);
                publisher.setText(publisher_edit.getText().toString());
            }
            if (!year.getText().toString().equals(year_edit.getText().toString())){
                db.updateYear(year_edit.getText().toString(),mISBN);
                year.setText(year_edit.getText().toString());
            }
        }

    }

}