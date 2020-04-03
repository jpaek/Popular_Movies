package android.example.popularmovies.utilities;

import java.io.Serializable;

public class Movie implements Serializable {

    public String title;
    public String posterPath;
    public String releaseDate;
    public int voteCount;
    public String synopsis;

    private static final String IAMGE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String TITLE_LABEL = "Title: ";
    private static final String RELEASE_DATE_LABEL = "Release Date: ";
    private static final String VOTE_LABEL = "Vote Average: ";
    private static final String SYNOPSIS_LABEL = "Synopsis: ";

    public Movie(String title, String posterPath, String releaseDate, int voteCount, String synopsis) {
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
        this.synopsis = synopsis;
    }

    public String getMoviePath() {
        return IAMGE_URL + posterPath;
    }

    public String getTitle() {
        return TITLE_LABEL + title;
    }

    public String getReleaseDate() {
        return RELEASE_DATE_LABEL + releaseDate;
    }

    public String getVote() {
        return VOTE_LABEL + voteCount;
    }

    public String getSynopsis() {
        return SYNOPSIS_LABEL + synopsis;
    }
}
