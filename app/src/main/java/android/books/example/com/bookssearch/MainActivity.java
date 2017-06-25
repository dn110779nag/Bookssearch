package android.books.example.com.bookssearch;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import static android.books.example.com.bookssearch.BookAppUtility.LOG_TAG;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private final String infoUrlTemplate = "https://play.google.com/store/books/details?id={id}&source=gbs_api";
    private BooksArrayAdapter adapter;
    private Loader<?> loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.message));
        adapter = new BooksArrayAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) listView.getItemAtPosition(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(infoUrlTemplate.replace("{id}", book.getId())));
                startActivity(browserIntent);
            }
        });

        if(!BookAppUtility.checkInternet(this.getApplicationContext())){
            ((TextView)findViewById(R.id.message)).setText(getString(R.string.no_inet));
        }

        ((TextView)findViewById(R.id.searchQuery)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doSeacrh(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void doSeacrh(String query){
        if(loader!=null){
            loader.cancelLoad();
        }

        if(query.isEmpty()){
            Toast.makeText(this, getString(R.string.input_query), Toast.LENGTH_LONG).show();
        } else {
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
            if (BookAppUtility.checkInternet(this.getApplicationContext())) {
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
//                getLoaderManager().initLoader(0, bundle, this);
                loader = getLoaderManager().restartLoader(0, bundle, this);
            } else {
                ((TextView) findViewById(R.id.message)).setText(getString(R.string.no_inet));
            }
        }
    }

    public void search(View view){
        Log.v(LOG_TAG, "click");
        String query = ((TextView)findViewById(R.id.searchQuery)).getText().toString();
        doSeacrh(query);

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        return new BookLoader(this, args.getString("query"));
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        findViewById(R.id.progress).setVisibility(View.INVISIBLE);
        if(data==null){
            ((TextView)findViewById(R.id.message)).setText(getString(R.string.no_data));
        } else{
            this.adapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        this.adapter.setData(null);
    }
}
