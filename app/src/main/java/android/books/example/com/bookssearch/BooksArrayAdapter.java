package android.books.example.com.bookssearch;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dn110 on 22.06.2017.
 */

public class BooksArrayAdapter extends ArrayAdapter<Book> {


    public BooksArrayAdapter(@NonNull Context context) {
        super(context, R.layout.list_item);
    }


    private void setText(View convertView, int id, String text) {
        ((TextView) convertView.findViewById(id)).setText(text);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        setText(convertView, R.id.authors, item.getAuthors().toString());
        setText(convertView, R.id.title, item.getTitle());
        return convertView;
    }

    public void setData(List<Book> data) {
        clear();
        if (data != null) {
            addAll(data);
        }

    }
}
