package com.anderpri.openlibganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        if(getIntent().hasExtra("book")){
            showBookInfo();
        } else {
            finish();
        }

    }

    private void showBookInfo() {
        Book book = getIntent().getParcelableExtra("book");
        Log.d("BOOK:",book.getmTitle());

        ImageView thumb = findViewById(R.id.card_thumb);
        TextView title = findViewById(R.id.card_title);

        if (!book.getmThumbnail().equals("N/A")){
            // Fuente: https://devexperto.com/glide-android/
            Uri uri = Uri.parse(book.getmThumbnail());
            Glide.with(this).load(uri).into(thumb);
        } else {
            thumb.setImageResource(R.drawable.cover_not_available);
        }

        title.setText(book.getmTitle());
    }
}