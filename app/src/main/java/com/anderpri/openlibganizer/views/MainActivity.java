package com.anderpri.openlibganizer.views;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anderpri.openlibganizer.R;
import com.anderpri.openlibganizer.controllers.MyAdapter;
import com.anderpri.openlibganizer.controllers.AppDatabase;
import com.anderpri.openlibganizer.model.DBook;
import com.anderpri.openlibganizer.model.Has;
import com.anderpri.openlibganizer.model.Book;
import com.anderpri.openlibganizer.model.Books;
import com.anderpri.openlibganizer.utils.Preferences;
import com.anderpri.openlibganizer.utils.Utils;
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
    private AppDatabase db;
    private Preferences pref;
    private Utils ut;
    private boolean clicked = false;

////// M??TODOS DE ANDROID //////

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
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MAIN CLOSED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this.getApplicationContext());
        ut = Utils.getInstance();
        pref = Preferences.getInstance();

        //ut.setLocale(getApplicationContext(),ut.getLocale(getApplicationContext()));
        pref.setContext(getApplicationContext());

        // Para gestionar el username
        manageUsername();

        // Para gestionar la rotaci??n
        manageRotation(savedInstanceState);

        // RecyclerView + CardView configuration
        configureView();

        // Fixed Action Buttons (FAB)
        configureFAB();

    }

////// CONFIGURACI??N DE VISTAS //////

    private void configureView() {
        // Inicializamos las variables
        adapter = new MyAdapter(this, mBooks, this);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);

        // Configuramos el tama??o de la grilla tanto en vertical como en horizontal
        int spanC;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanC = 4;
        } else {
            spanC = 2;
        }

        // Y creamos la grilla
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanC, GridLayoutManager.VERTICAL, false);

        // Finalmente terminamos de configurar el RecyclerView
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    private void configureFAB() {
        fab_main = findViewById(R.id.fab);
        fab_settings = findViewById(R.id.fab_settings);
        fab_add = findViewById(R.id.fab_add);

        fab_main.setOnClickListener(view -> onFABClicked());
        fab_settings.setOnClickListener(view -> openSettings());
        fab_add.setOnClickListener(view -> openDialogNewBook());
    }

////// GESTI??N DE LA ROTACI??N //////

    private void manageRotation(Bundle savedInstanceState) {

        // Para gestionar la rotaci??n
        // Fuente: https://stackoverflow.com/a/28155179

        if (savedInstanceState != null && savedInstanceState.getParcelable("books") != null) {
            mBooks = savedInstanceState.getParcelable("books");
        }

    }

////// GESTI??N DE LA SESI??N //////

    // Comprobar si hay sesi??n iniciada
    private void manageUsername() {
        if (!pref.checkUsernameNull()) {
            mUser = pref.getUsername();
            ut.setLocale(getApplicationContext(),pref.getLocale());
            if (!pref.isFirst()) { Toast.makeText(getApplicationContext(), getString(R.string.welcome_user, mUser), Toast.LENGTH_LONG).show(); }
            getBooksFromDB();
        } else {
            openLogin();
        }
    }

    // A) Si no hay sesi??n iniciada redirigir al login
    private void openLogin() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        finish();
        startActivity(i);
        overridePendingTransition(0, 0);
    }


    // B) Si hay sesi??n iniciada recuperar los libros de la base de datos
    private void getBooksFromDB() { // para cargar los libros

        //gab();
        //importTestData();

        System.out.println(mUser);
        List<DBook> dBookList = db.getAllBooksFromUsername(mUser);
        System.out.println(dBookList.toString());
        System.out.println(dBookList.size());
        List<Book> bookList = convertDBookToBook(dBookList);

        if (bookList.size() != 0) {
            mBooks.addAll(bookList);
            System.out.println(mBooks.toString());
        }

    }

    private void gab() {
        System.out.println("has");
        List<Has> lh = db.getAllUsersBooks();
        lh.stream().map(h -> h.isbn + " " + h.username).forEach(System.out::println);

        System.out.println("books");
        List<DBook> lb = db.getAllBooks();
        lb.stream().map(b -> b.isbn).forEach(System.out::println);
    }

    private void importTestData() {

        if ((db.checkIfBookAdded("9780618680009") == 0)) {
            DBook b1 = new DBook("9780618680009", "The God Delusion", "https://covers.openlibrary.org/b/id/8231555-L.jpg", "", "", "","");
            DBook b2 = new DBook("9780142000656", "East of Eden", "https://covers.openlibrary.org/b/id/8309140-L.jpg", "", "", "","");
            DBook b3 = new DBook("9780152052607", "The Hundred Dresses", "https://covers.openlibrary.org/b/id/10703295-L.jpg", "", "", "","");
            DBook b4 = new DBook("9780205609994", "Influence", "https://covers.openlibrary.org/b/id/8284301-L.jpg", "", "", "","");
            DBook b5 = new DBook("9780803740167", "Roller Girl", "https://covers.openlibrary.org/b/id/11327078-L.jpg", "", "", "","");

            db.insertBook(b1);
            db.insertBook(b2);
            db.insertBook(b3);
            db.insertBook(b4);
            db.insertBook(b5);

            db.insertRelation(new Has("a", "9780618680009"));
            db.insertRelation(new Has("a", "9780142000656"));
            db.insertRelation(new Has("a", "9780152052607"));
            db.insertRelation(new Has("b", "9780205609994"));
            db.insertRelation(new Has("b", "9780803740167"));

            //List<DBook> dBookList = db.getAllBooksFromUsername("a");
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
            book.setmKey(dBook.key);
            l.add(book);
        }
        return l;
    }

////// FAB: GESTI??N DE LOS FABs //////

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

////// FAB: AJUSTES //////

    private void openSettings() {
        defineReceiver();
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }
    // Hay que configurar un BroadcastReciever para poder cerrar la...
    // ...actividad main al pulsar en el log out de los Settings
    // Fuente: https://stackoverflow.com/a/10379275

    private void defineReceiver() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
    }

////// FAB: A??ADIR LIBRO //////

    // Abre el Dialog
    public void openDialogNewBook() {
        DialogNewBook dialogNewBook = new DialogNewBook();
        dialogNewBook.show(getSupportFragmentManager(), "addBook");
    }

    // Implementa el listener
    @Override
    public void addBook(String mISBN) {
        String uri = "https://openlibrary.org/search.json?isbn=" + mISBN;
        new JsonTask().execute(uri);
    }

    //Descarga la informaci??n a un JSON y lo env??a al siguiente m??todo
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

    // Recoge el JSON y lo a??ade tanto a la vista como a la base de datos
    private void addBookToRV(String JSON_STRING) {

        Book mBook = new Book();
        String mThumbnail = mBook.getmThumbnail();
        String mTitle = mBook.getmTitle();
        String mAuthor = mBook.getmAuthor();
        String mPublisher = mBook.getmPublisher();
        String mYear = mBook.getmYear();
        String mISBN = mBook.getmISBN();
        String mKey = mBook.getmKey();

        try {
            // Para esquivar la mala lectura de caracteres Unicode en los objetos
            String JSON_STRING1 = forceUnicode(JSON_STRING);


            JSONObject obj_root = new JSONObject(JSON_STRING1);
            JSONArray obj_docs_pre = obj_root.getJSONArray("docs");
            JSONObject obj_docs = obj_docs_pre.getJSONObject(0);


            try {
                String cover_i = obj_docs.getString("cover_i");
                mThumbnail = "https://covers.openlibrary.org/b/id/" + cover_i + "-L.jpg";
                mBook.setmThumbnail(mThumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mTitle = obj_docs.getString("title");
                mBook.setmTitle(mTitle);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mISBN = obj_docs.getJSONArray("isbn").getString(0);
                mBook.setmISBN(mISBN);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mAuthor = obj_docs.getJSONArray("author_name").getString(0);
                mBook.setmAuthor(mAuthor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mPublisher = obj_docs.getJSONArray("publisher").getString(0);
                mBook.setmPublisher(mPublisher);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mYear = obj_docs.getJSONArray("publish_year").getString(0);
                mBook.setmYear(mYear);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mKey = obj_docs.getString("key");
                mBook.setmKey(mKey);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            JSON_STRING = ""; // para que no entre en el if de despu??s
            Toast.makeText(getApplicationContext(), R.string.error_isbn, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        System.out.println(mBook);

        // Antes de nada, se mira si el ISBN no existe en la API
        if (JSON_STRING.length() < 10) {
            Toast.makeText(getApplicationContext(), R.string.isbn_not_found, Toast.LENGTH_LONG).show();
        } else {
            // Si el ISBN existe hay dos comprobaciones:
            // 1) Si el libro est?? o no est?? en la base de datos
            // Tras esta comprobaci??n, el libro est?? s?? o s?? en el sistema
            // 2) Si el libro est?? relacionado con nuestro usuario o no

            // 1) Se mira si el libro est?? REGISTRADO en la base de datos
            // Si no est?? registrado, se a??ade a la base de datos
            if (db.checkIfBookAdded(mISBN) == 0) {
                DBook newBook = new DBook(mISBN, mTitle, mThumbnail, mAuthor, mPublisher, mYear, mKey);
                db.insertBook(newBook);
            }

            // 2) Se mira si el libro est?? RELACIONADO a nuestro usuario en la base de datos
            if (db.checkIfBookRelated(mISBN, mUser) != 0) {
                Toast.makeText(getApplicationContext(), R.string.isbn_already, Toast.LENGTH_LONG).show();
            } else {
                // Primero se relaciona el libro
                db.insertRelation(new Has(mUser, mISBN));

                // Y por ??ltimo se a??ade a la vista
                int index_insert = mBooks.size();
                mBooks.add(index_insert, mBook);
                //adapter.notifyItemInserted(index_insert);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), getString(R.string.book_added, mTitle), Toast.LENGTH_LONG).show();
                sendNotification(mBook, getApplicationContext());
            }

        }


    }

    // Fuente: https://egela.ehu.eus/pluginfile.php/5332018/mod_resource/content/4/05_Dialogs_y_Notificaciones.pdf
    private void sendNotification(Book mBook, Context context){

        String contentTitle = getString(R.string.noti_book_added) + " " + mBook.getmTitle() +" - "+ mBook.getmAuthor();
        String contentText = getString(R.string.noti_book_action);
        String contentUri = "https://openlibrary.org" + mBook.getmKey();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contentUri));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "openlibganizer";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // String channelId = "openlibganizer";
            String channelName = "OpenLibganizer";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                //.setSubText("SubText")
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, notificationBuilder.build());
    }


    // M??todo auxiliar para
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



////// GESTIONAR LA ACCI??N AL PULSAR SOBRE UN LIBRO //////
    @Override
    public void onBookClick(int position) {
        Book b = mBooks.get(position);
        Intent i = new Intent(this, CardActivity.class);
        i.putExtra("book", b);
        startActivity(i);
    }
}