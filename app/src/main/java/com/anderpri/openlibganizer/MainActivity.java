package com.anderpri.openlibganizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements DialogNewBook.DialogNewBookListener, MyAdapter.OnBookListener { //implements DialogNewBook2.ListenerNewBook {

    private RecyclerView mRecyclerView;
    //private List<String> mTitles = new ArrayList<>();
    //private List<String> mImages = new ArrayList<>();
    private List<Book> mBooks = new ArrayList<>();
    //private MyAdapter adapter = new MyAdapter(this, mTitles, mImages);
    private MyAdapter adapter = new MyAdapter(this, mBooks, this);
    private FloatingActionButton fab_main;
    private FloatingActionButton fab_settings;
    private FloatingActionButton fab_add;
    private ProgressDialog pd;

    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // ------------------------------------- //
        // RecyclerView + CardView configuration

        // Las variables están inicializadas arriba
        mRecyclerView = findViewById(R.id.recyclerview);

        // Importamos la información de prueba, útil para hacer los testeos
        importTestData();

        // Configuramos el tamaño de la grilla tanto en vertical como en horizontal
        int spanC;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { spanC = 4; } else { spanC = 2; }

        // Y finalmente
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanC, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        // ------------------------------------- //
        // Fixed Action Buttons (FAB)

        fab_main = findViewById(R.id.fab);
        fab_settings = findViewById(R.id.fab_settings);
        fab_add = findViewById(R.id.fab_add);

        fab_main.setOnClickListener(view -> {
            onFABClicked();
            // Toast.makeText(getApplicationContext(), "Main", Toast.LENGTH_SHORT).show();
        });
        fab_settings.setOnClickListener(view -> {
            //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
        });
        fab_add.setOnClickListener(view -> openDialogNewBook());

    }

    private void importTestData() {

        /*
        String[] isbns = {"9780618680009","9780142000656",
                "9780152052607","9780205609994","9780803740167"};

        for (String s:isbns) {
            addBook(s);
        }*/
        /*
        mTitles.add("");
        mImages.add("");

        mTitles.add("");
        mImages.add("");

        mTitles.add("");
        mImages.add("");

        mTitles.add("");
        mImages.add("");

        mTitles.add("");
        mImages.add("");*/

        Book b1 = new Book("9780618680009","The God Delusion","https://covers.openlibrary.org/b/id/8231555-L.jpg");
        Book b2 = new Book("9780142000656","East of Eden","https://covers.openlibrary.org/b/id/8309140-L.jpg");
        Book b3 = new Book("9780152052607","The Hundred Dresses","https://covers.openlibrary.org/b/id/10703295-L.jpg");
        Book b4 = new Book("9780205609994","Influence","https://covers.openlibrary.org/b/id/8284301-L.jpg");
        Book b5 = new Book("9780803740167","Roller Girl","https://covers.openlibrary.org/b/id/11327078-L.jpg");
        mBooks.add(b1);
        mBooks.add(b2);
        mBooks.add(b3);
        mBooks.add(b4);
        mBooks.add(b5);

    }

    public void openDialogNewBook() {
        DialogNewBook dialogNewBook = new DialogNewBook();
        dialogNewBook.show(getSupportFragmentManager(), "addBook");
    }

    @Override
    public void addBook(String mISBN) {
        String uri = "https://openlibrary.org/api/books?bibkeys=ISBN:" + mISBN + "&jscmd=details&format=json";
        new JsonTask().execute(uri);
    }

    private void addBookToRV(String JSON_STRING) {

        Book mBook = new Book();
        String mThumbnail = mBook.getmThumbnail();
        String mTitle = mBook.getmTitle();
        //String mAuthor = mBook.getmAuthor();
        //String mPublisher = mBook.getmPublisher();
        //String mYear = mBook.getmYear();
        String mISBN = mBook.getmISBN();

        try {

            // Para esquivar la mala lectura de caracteres Unicode en los objetos
            String JSON_STRING1 = forceUnicode(JSON_STRING);

            System.out.println(JSON_STRING1);
            System.out.println("-+-+-+-+-+-+");

            JSONObject obj_root = new JSONObject(JSON_STRING1);

            // En Java no es posible acceder directamente al primer elemento de un array y necesitamos
            // saber el nombre exacto del elemento (el ISBN), para ello usamos este workaround
            String isbn_search_temp = JSON_STRING1.split(": ")[0];
            String isbn_search = isbn_search_temp.substring(2, isbn_search_temp.length() - 1);


            JSONObject obj_main = obj_root.getJSONObject(isbn_search);
            JSONObject obj_details = obj_main.getJSONObject("details");


            try {mThumbnail = obj_main.getString("thumbnail_url")
                    .replace("-S.jpg","-L.jpg")
                    .replace("-M.jpg","-L.jpg");
                    mBook.setmThumbnail(mThumbnail);
            } catch (Exception e) { e.printStackTrace(); }
            try {mTitle = obj_details.getString("title");
                mBook.setmTitle(mTitle);
            } catch (Exception e) { e.printStackTrace(); }
            try {mISBN = obj_details.getJSONArray("isbn13").getString(0);
                mBook.setmISBN(mISBN);
            } catch (Exception e) { e.printStackTrace(); }

        } catch (JSONException e) {
            JSON_STRING = ""; // para que no entre en el if de después
            Toast.makeText(getApplicationContext(), R.string.error_isbn, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        if (mISBN.equals("5")){ // mirar si ya existe
            Toast.makeText(getApplicationContext(), R.string.isbn_already, Toast.LENGTH_LONG).show();
        }


        System.out.println("TITLE: " + mTitle);
        System.out.println("ISBN: " + mISBN);


        if (JSON_STRING.length() < 10) {
            //Toast.makeText(getApplicationContext(), R.string.isbn_not_found, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), R.string.isbn_not_found, Toast.LENGTH_LONG).show();
        } else {
            mBooks.add(0, mBook);
            adapter.notifyItemInserted(0);
            //adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), getString(R.string.book_added, mTitle), Toast.LENGTH_LONG).show();
        }


    }

    private String forceUnicode(String JSON_STRING) {

        // Fuente: https://stackoverflow.com/a/58455026

        String UNICODE_REGEX = "\\\\u([0-9a-f]{4})";
        Pattern UNICODE_PATTERN = Pattern.compile(UNICODE_REGEX);

        Matcher matcher = UNICODE_PATTERN.matcher(JSON_STRING);
        StringBuffer decodedMessage = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(
                    decodedMessage, String.valueOf((char) Integer.parseInt(matcher.group(1), 16)));
        }
        matcher.appendTail(decodedMessage);
        return decodedMessage.toString();
    }

    private void onFABClicked() {
        setVisibility();
        setAnimation();
        clicked = !clicked;
    }

    private void setVisibility() {

        if (!clicked) {
            fab_add.setVisibility(View.VISIBLE);
            fab_settings.setVisibility(View.VISIBLE);
        } else {
            fab_add.setVisibility(View.GONE);
            fab_settings.setVisibility(View.GONE);
        }
    }

    private void setAnimation() {
        Animation rotateOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_open_anim);
        Animation rotateClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_close_anim);
        Animation toBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_bottom_anim);
        Animation fromBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_bottom_anim);

        if (!clicked) {
            fab_add.startAnimation(fromBottom);
            fab_settings.startAnimation(fromBottom);
            fab_main.startAnimation(rotateOpen);
        } else {
            fab_add.startAnimation(toBottom);
            fab_settings.startAnimation(toBottom);
            fab_main.startAnimation(rotateClose);
        }
    }

    @Override
    public void onBookClick(int position) {

        Book b = mBooks.get(position);




        Intent i = new Intent(this, CardActivity.class);
        i.putExtra("book", b);
        startActivity(i);

        /*
        Intent i = new Intent(view.getContext(), CardActivity.class);
        i.putExtra("book", MyViewHolder.this.getAdapterPosition());
        view.getContext().startActivity(i);
        MyViewHolder.this.getAdapterPosition();*/
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        // Fuente: https://stackoverflow.com/a/37525989

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage(getString(R.string.wait));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            addBookToRV(result);
        }
    }
}