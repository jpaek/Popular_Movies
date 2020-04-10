package android.example.popularmovies.database;

import android.example.popularmovies.utilities.Movie;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class FavoriteEntry {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String title;
    private String posterPath;
    private String releaseDate;
    private int voteCount;
    private String synopsis;

    public FavoriteEntry(Movie movie) {
        this(movie.id, movie.title, movie.posterPath, movie.releaseDate, movie.voteCount, movie.synopsis);
    }

    public FavoriteEntry(int id, String title, String posterPath, String releaseDate, int voteCount, String synopsis) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
        this.synopsis = synopsis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Movie getMovie() {
        return new Movie(id, title, posterPath, releaseDate, voteCount, synopsis);
    }
}
