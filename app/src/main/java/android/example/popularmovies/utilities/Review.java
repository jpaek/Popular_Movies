package android.example.popularmovies.utilities;

import java.io.Serializable;

public class Review implements Serializable {
    public String author;
    public String content;
    public String link;

    private static final String AUTHOR_LABEL = "Author: ";
    private static final String CONTENT_LABEL = "Review: ";
    private static final String LINK_LABEL = "URL: ";

    public Review(String author, String content, String link) {
        this.author = author;
        this.content = content;
        this.link = link;
    }

    public String getAuthor() {
        return AUTHOR_LABEL + author;
    }

    public String getContent() {
        return CONTENT_LABEL + content;
    }

    public String getLink() {
        return LINK_LABEL + link;
    }
}
