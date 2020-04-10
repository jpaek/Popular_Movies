package android.example.popularmovies.utilities;

import java.io.Serializable;

public class Trailer implements Serializable {
    public String site;
    public String name;
    public String key;

    private static final String AUTHOR_LABEL = "Author: ";
    private static final String CONTENT_LABEL = "Review: ";
    private static final String LINK_LABEL = "URL: ";

    public Trailer(String name, String site, String key) {
        this.site = site;
        this.name = name;
        this.key = key;
    }
}
