package com.anderpri.openlibganizer.controllers;

import android.content.Context;
import android.util.Log;

import com.anderpri.openlibganizer.model.DBook;
import com.anderpri.openlibganizer.model.Has;
import com.anderpri.openlibganizer.model.User;
import com.goebl.david.Webb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class AppDatabase {

    private static AppDatabase INSTANCE;
    private static ConnectionController connectionController;
    private final String dbpath = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/aprieto052/WEB";

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppDatabase();
            connectionController = ConnectionController.getInstance();
        }
        return INSTANCE;
    }

    ////// DBOOKDAO //////

    public List<DBook> getAllBooks() { return null; }

    public List<DBook> getAllBooksFromUsername(String mUser) {
        String uri = dbpath + "/books/getallbooks.php";
        Map<String, Object> params = new HashMap<>();
        params.put("username", mUser);

        JSONObject result = postReturnJSON(uri,params);

        Log.d("res",result.toString());
        /*
        try {
            if (!result.toString().equals("{}")) {
                u = new User(mUser, mPass, result.getString("lang"), result.getInt("theme")!= 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        return null;
    }

    public int checkIfBookAdded(String mISBN){ return 0; }

    public void updateTitle(String mValue, String mISBN){ }

    public void updateAuthor(String mValue, String mISBN){ }

    public void updatePublisher(String mValue, String mISBN){ }

    public void updateYear(String mValue, String mISBN){ }

    public void insertBook(DBook dBook){ }

    public void delete(DBook dBook){ }

    ////// HASDAO //////

    public List<Has> getAllUsersBooks(){ return null; }

    public void insertRelation(Has relation){ }

    public int checkIfBookRelated(String mISBN, String mUser){ return 0; }

    ////// USERDAO //////

    public boolean userExists(String mUser){
        String uri = dbpath + "/user/exists.php";
        Map<String, Object> params = new HashMap<>();
        params.put("username", mUser);

        JSONObject result = postReturnJSON(uri,params);
        return !result.toString().equals("{}");
    }

    public User getUser(String mUser, String mPass /*hashed*/){
        String uri = dbpath + "/user/get.php";
        Map<String, Object> params = new HashMap<>();
        params.put("username", mUser);
        params.put("password", mPass);

        JSONObject result = postReturnJSON(uri,params);
        User u = null;
        try {
            if (!result.toString().equals("{}")) {
                u = new User(mUser, mPass, result.getString("lang"), result.getInt("theme")!= 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }

    public void insertUser(User user){
        String uri = dbpath + "/user/insert.php";
        Map<String, Object> params = new HashMap<>();
        params.put("username", user.username);
        params.put("password", user.password);
        params.put("lang", user.lang);
        params.put("theme", user.theme);

        postReturnJSON(uri,params);
    }


    ////// UTILS //////

    private JSONObject postReturnJSON(String uri, Map<String, Object> params) {
        Callable<JSONObject> callable = () -> {
            Webb webb = Webb.create();
            //webb.setDefaultHeader("Connection", "close");
            return webb
                    .post(uri)
                    .params(params)
                    .ensureSuccess()
                    .asJsonObject()
                    .getBody();
        };
        Future<JSONObject> future = Executors.newSingleThreadExecutor().submit(callable);
        JSONObject result = null;
        try {
            result = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


}
