package com.anderpri.openlibganizer.views;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anderpri.openlibganizer.R;
import com.anderpri.openlibganizer.controllers.MyAdapter;
import com.anderpri.openlibganizer.db.AppDatabase;
import com.anderpri.openlibganizer.db.DBook;
import com.anderpri.openlibganizer.db.Has;
import com.anderpri.openlibganizer.model.Book;
import com.anderpri.openlibganizer.model.Books;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
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

    private Books mBooks = new Books();
    private MyAdapter adapter;
    private FloatingActionButton fab_main;
    private FloatingActionButton fab_settings;
    private FloatingActionButton fab_add;
    private ProgressDialog pd;
    private String mUser = "ERROR";
    AppDatabase db;//  = AppDatabase.getInstance(this.getApplicationContext());
    private boolean clicked = false;
    boolean first = false;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("books", mBooks);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mBooks = savedInstanceState.getParcelable("books");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this.getApplicationContext());

        // Para gestionar el username
        if(getIntent().getExtras().getString("username") != null) {
            mUser = getIntent().getExtras().getString("username");
            getBooksFromDB();
        }

        // Para gestionar la rotación
        // Fuente: https://stackoverflow.com/a/28155179
        if(savedInstanceState != null && savedInstanceState.getParcelable("books") != null) {
            mBooks = savedInstanceState.getParcelable("books");
        } else if (savedInstanceState == null){
            Toast.makeText(getApplicationContext(), "welcome "+ mUser, Toast.LENGTH_LONG).show();
            // TODO cambiar hardcoded text welcome
            // Si es el primer acceso importamos la información de prueba, útil para hacer los testeos
            //importTestData();
        }

        // ------------------------------------- //
        // RecyclerView + CardView configuration

        // Inicializamos las variables
        adapter = new MyAdapter(this, mBooks, this);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);

        // Configuramos el tamaño de la grilla tanto en vertical como en horizontal
        int spanC;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { spanC = 4; } else { spanC = 2; }

        // Y creamos la grilla
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanC, GridLayoutManager.VERTICAL, false);

        // Finalmente terminamos de configurar el RecyclerView
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        // ------------------------------------- //
        // Fixed Action Buttons (FAB)

        fab_main = findViewById(R.id.fab);
        fab_settings = findViewById(R.id.fab_settings);
        fab_add = findViewById(R.id.fab_add);

        fab_main.setOnClickListener(view -> onFABClicked());
        fab_settings.setOnClickListener(view -> openSettings());
        fab_add.setOnClickListener(view -> openDialogNewBook());

    }

    private void openSettings() {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    private void getBooksFromDB(){ // para cargar los libros

        gab();
        importTestData();

        System.out.println(mUser);
        List<DBook> dBookList = db.dBookDao().getAllBooksFromUsername(mUser);
        System.out.println(dBookList.toString());
        System.out.println(dBookList.size());
        List<Book> bookList = convertDBookToBook(dBookList);

        if (bookList.size()!=0){
            mBooks.addAll(bookList);
            System.out.println(mBooks.toString());
        }

    }

    private void gab() {
        System.out.println("has");
        List<Has> lh = db.hasDao().getAllUsersBooks();
        lh.stream().map(h -> h.isbn + " " + h.username).forEach(System.out::println);

        System.out.println("books");
        List<DBook> lb = db.dBookDao().getAllBooks();
        lb.stream().map(b -> b.isbn).forEach(System.out::println);
    }

    private void importTestData() {

        if((db.dBookDao().checkIfBookAdded("9780618680009") == 0)){
            DBook b1 = new DBook("9780618680009","The God Delusion","https://covers.openlibrary.org/b/id/8231555-L.jpg","","","");
            DBook b2 = new DBook("9780142000656","East of Eden","https://covers.openlibrary.org/b/id/8309140-L.jpg","","","");
            DBook b3 = new DBook("9780152052607","The Hundred Dresses","https://covers.openlibrary.org/b/id/10703295-L.jpg","","","");
            DBook b4 = new DBook("9780205609994","Influence","https://covers.openlibrary.org/b/id/8284301-L.jpg","","","");
            DBook b5 = new DBook("9780803740167","Roller Girl","https://covers.openlibrary.org/b/id/11327078-L.jpg","","","");

            db.dBookDao().insertBook(b1);
            db.dBookDao().insertBook(b2);
            db.dBookDao().insertBook(b3);
            db.dBookDao().insertBook(b4);
            db.dBookDao().insertBook(b5);

            db.hasDao().insertRelation(new Has("a","9780618680009"));
            db.hasDao().insertRelation(new Has("a","9780142000656"));
            db.hasDao().insertRelation(new Has("a","9780152052607"));
            db.hasDao().insertRelation(new Has("b","9780205609994"));
            db.hasDao().insertRelation(new Has("b","9780803740167"));

            //List<DBook> dBookList = db.dBookDao().getAllBooksFromUsername("a");
            //List<Book> bookList = convertDBookToBook(dBookList);

            //mBooks.addAll(bookList);
            //System.out.println(mBooks.toString());
        }

    }

    private List<Book> convertDBookToBook(List<DBook> lst) {
        List<Book> l = new ArrayList<>();
        for (DBook dBook : lst) {
            Book book = new Book();
            book.setmISBN(dBook.isbn);
            book.setmTitle(dBook.title);
            book.setmThumbnail(dBook.thumbnail);
            book.setmAuthor(dBook.author);
            book.setmPublisher(dBook.publisher);
            book.setmYear(dBook.year);
            l.add(book);
        }
        return l;
    }

    public void openDialogNewBook() {
        DialogNewBook dialogNewBook = new DialogNewBook();
        dialogNewBook.show(getSupportFragmentManager(), "addBook");
    }

    @Override
    public void addBook(String mISBN) {
        String uri = "https://openlibrary.org/search.json?isbn=" + mISBN;
        new JsonTask().execute(uri);
    }

    private void addBookToRV(String JSON_STRING) {

        Book mBook = new Book();
        String mThumbnail = mBook.getmThumbnail();
        String mTitle = mBook.getmTitle();
        String mAuthor = mBook.getmAuthor();
        String mPublisher = mBook.getmPublisher();
        String mYear = mBook.getmYear();
        String mISBN = mBook.getmISBN();

        try {
            // Para esquivar la mala lectura de caracteres Unicode en los objetos
            String JSON_STRING1 = forceUnicode(JSON_STRING);


            JSONObject obj_root = new JSONObject(JSON_STRING1);
            JSONArray obj_docs_pre = obj_root.getJSONArray("docs");
            JSONObject obj_docs = obj_docs_pre.getJSONObject(0);


            try {String cover_i = obj_docs.getString("cover_i");
                mThumbnail = "https://covers.openlibrary.org/b/id/"+cover_i+"-L.jpg";
                mBook.setmThumbnail(mThumbnail);
            } catch (Exception e) { e.printStackTrace(); }
            try {mTitle = obj_docs.getString("title");
                mBook.setmTitle(mTitle);
            } catch (Exception e) { e.printStackTrace(); }
            try {mISBN = obj_docs.getJSONArray("isbn").getString(0);
                mBook.setmISBN(mISBN);
            } catch (Exception e) { e.printStackTrace(); }
            try {mAuthor = obj_docs.getJSONArray("author_name").getString(0);
                mBook.setmAuthor(mAuthor);
            } catch (Exception e) { e.printStackTrace(); }
            try {mPublisher = obj_docs.getJSONArray("publisher").getString(0);
                mBook.setmPublisher(mPublisher);
            } catch (Exception e) { e.printStackTrace(); }
            try {mYear = obj_docs.getJSONArray("publish_year").getString(0);
                mBook.setmYear(mYear);
            } catch (Exception e) { e.printStackTrace(); }

        } catch (JSONException e) {
            JSON_STRING = ""; // para que no entre en el if de después
            Toast.makeText(getApplicationContext(), R.string.error_isbn, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        System.out.println(mBook);

        // Antes de nada, se mira si el ISBN no existe en la API
        if (JSON_STRING.length() < 10) { Toast.makeText(getApplicationContext(), R.string.isbn_not_found, Toast.LENGTH_LONG).show(); }
        else {
            // Si el ISBN existe hay dos comprobaciones:
            // 1) Si el libro está o no está en la base de datos
            // Tras esta comprobación, el libro está sí o sí en el sistema
            // 2) Si el libro está relacionado con nuestro usuario o no

            // 1) Se mira si el libro está REGISTRADO en la base de datos
            // Si no está registrado, se añade a la base de datos
            if (db.dBookDao().checkIfBookAdded(mISBN) == 0) {
                DBook newBook = new DBook(mISBN, mTitle, mThumbnail, mAuthor, mPublisher, mYear);
                db.dBookDao().insertBook(newBook);
            }

            // 2) Se mira si el libro está RELACIONADO a nuestro usuario en la base de datos
            if (db.hasDao().checkIfBookRelated(mISBN,mUser) != 0){
                Toast.makeText(getApplicationContext(), R.string.isbn_already, Toast.LENGTH_LONG).show();
            } else {
                // Primero se relaciona el libro
                db.hasDao().insertRelation(new Has(mUser,mISBN));

                // Y por último se añade a la vista
                int index_insert = mBooks.size();
                mBooks.add(index_insert, mBook);
                //adapter.notifyItemInserted(index_insert);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), getString(R.string.book_added, mTitle), Toast.LENGTH_LONG).show();
            }

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