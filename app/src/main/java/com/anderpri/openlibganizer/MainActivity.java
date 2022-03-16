package com.anderpri.openlibganizer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogNewBook.DialogNewBookListener { //implements DialogNewBook2.ListenerNewBook {

    private RecyclerView mRecyclerView;
    private List<String> mTitles;
    private List<Integer> mImages;
    private MyAdapter adapter;
    FloatingActionButton fab_main;
    FloatingActionButton fab_settings;
    FloatingActionButton fab_add;


    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ------------------------------------- //
        // RecyclerView + CardView configuration

        mRecyclerView = findViewById(R.id.recyclerview);

        mTitles = new ArrayList<>();
        mImages = new ArrayList<>();

        adapter = new MyAdapter(this, mTitles,mImages);

        mImages.add(R.drawable.a1);mImages.add(R.drawable.a2);mImages.add(R.drawable.a3);mImages.add(R.drawable.a4);mImages.add(R.drawable.a5);
        mTitles.add("Uno");mTitles.add("Dos");mTitles.add("Tres");mTitles.add("Cuatro");mTitles.add("Cinco");

        int spanC;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { spanC = 4; } else { spanC = 2; }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,spanC,GridLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        // ------------------------------------- //
        // Fixed Action Buttons

        fab_main = findViewById(R.id.fab);
        fab_settings = findViewById(R.id.fab_settings);
        fab_add = findViewById(R.id.fab_add);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFABClicked();
                //String text = "Main";
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
        fab_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String text = "Settings";
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogNewBook();
            }
        });

    }

    public void openDialogNewBook() {
        DialogNewBook dialogNewBook = new DialogNewBook();
        dialogNewBook.show(getSupportFragmentManager(), "addBook");
    }

    @Override
    public void addBook(String mISBN) {

        String text = "Added new book";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        mImages.add(R.drawable.a6);
        mTitles.add(mISBN);
        adapter.notifyDataSetChanged();

        //Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT).show();
    }

    private void onFABClicked() {
        setVisibility();
        setAnimation();
        clicked = !clicked;
    }

    private void setVisibility() {

        if (!clicked){
            fab_add.setVisibility(View.VISIBLE);
            fab_settings.setVisibility(View.VISIBLE);
        } else {
            fab_add.setVisibility(View.GONE);
            fab_settings.setVisibility(View.GONE);
        }
    }

    private void setAnimation() {
        Animation rotateOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_open_anim);
        Animation rotateClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_close_anim);
        Animation toBottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.to_bottom_anim);
        Animation fromBottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.from_bottom_anim);

        if (!clicked){
            fab_add.startAnimation(fromBottom);
            fab_settings.startAnimation(fromBottom);
            fab_main.startAnimation(rotateOpen);
        } else {
            fab_add.startAnimation(toBottom);
            fab_settings.startAnimation(toBottom);
            fab_main.startAnimation(rotateClose);
        }
    }
/*
    @Override
    public void alpulsarSI() {
        String uri = "https://www.google.com/";
        Log.i("etiqueta", uri);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(i);
    }

    @Override
    public void alpulsarNO() {
        String uri = "https://www.bing.com/";
        Log.i("etiqueta", uri);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(i);
    }*/
}