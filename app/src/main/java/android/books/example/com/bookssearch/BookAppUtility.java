package android.books.example.com.bookssearch;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dn110 on 24.06.2017.
 */

public class BookAppUtility {
    public static final String LOG_TAG = "bookssearch";

    public static boolean checkInternet( Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnectedOrConnecting();
    }
}
