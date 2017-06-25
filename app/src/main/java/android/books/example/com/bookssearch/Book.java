package android.books.example.com.bookssearch;

import java.util.List;

/**
 * Created by dn110 on 22.06.2017.
 */

public class Book {
    private List<String> authors;
    private String title;
    private String id;

    public Book(List<String> authors, String title, String id) {
        this.authors = authors;
        this.title = title;
        this.id = id;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}
