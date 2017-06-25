package android.books.example.com.bookssearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import static android.books.example.com.bookssearch.BookAppUtility.LOG_TAG;

/**
 * Created by dn110 on 24.06.2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private static final String URL_TEMPLATE = "https://www.googleapis.com/books/v1/volumes?q={title}&maxResults=20";

    private final String query;

    public BookLoader(Context context, String query) {
        super(context);
        this.query = query;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        try {
            return loadBooks(query);
        } catch (IOException e) {
            Log.v(BookAppUtility.LOG_TAG, "Network Error", e);
        } catch (JSONException e) {
            Log.v(BookAppUtility.LOG_TAG, "Json Parse Error", e);
        }
        return null;
    }

    private List<Book> loadBooks(String query) throws IOException, JSONException {
        URL url = prepareUrl(query);
        String json = null;
        if(!isLoadInBackgroundCanceled()) {
            json = loadStringFromNetwork(url);
        }
        if(!isLoadInBackgroundCanceled()){
            return parseJson(json);
        }
        return null;
    }

    private List<Book> parseJson(String json) throws JSONException {
        Log.v(LOG_TAG, "json ==> "+json);

        JSONObject data = new JSONObject(json);
        JSONArray items = data.getJSONArray("items");
        List<Book> bookList = new ArrayList<>() ;
        for(int i=0; i<items.length(); i++){
            JSONObject b = items.getJSONObject(i);
            JSONObject volumeInfo = b.getJSONObject("volumeInfo");
            String id = b.getString("id");
            String title = volumeInfo.getString("title");
            List<String> authors = new ArrayList<>();
            JSONArray jsonAuthors = null;
            try {
                jsonAuthors=volumeInfo.getJSONArray("authors");
                for (int j = 0; j < jsonAuthors.length(); j++) {
                    authors.add(jsonAuthors.getString(j));
                }
            } catch (JSONException ex){

            }
            bookList.add(new Book(authors,title,id));
        }
        return bookList;
    }

    private URL prepareUrl(String query) throws UnsupportedEncodingException, MalformedURLException {
        String url = URL_TEMPLATE.replace("{title}", URLEncoder.encode(query, "utf-8"));
        Log.v(LOG_TAG, "url ==> "+url);
        return new URL(url);
    }

    private String loadStringFromNetwork(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoOutput(false);

        con.connect();
        int status = con.getResponseCode();
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        int msize = 4096;
        int s;
        byte[] buf = new byte[msize];
        try(InputStream os = status<400?con.getInputStream():con.getErrorStream();){
            while((s=os.read(buf,0,msize))>0){
                bous.write(buf, 0, s);
            }
        }

        if(status>=400){
            throw new IOException(new String(bous.toByteArray(), "utf-8"));
        }

        return new String(bous.toByteArray(), "utf-8");

    }
}
