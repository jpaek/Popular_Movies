package android.example.popularmovies.utilities;

public class Movie {

    public String title;
    public String posterPath;
    public String releaseDate;
    public int voteCount;
    public String synopsis;

    public Movie(String title, String posterPath, String releaseDate, int voteCount, String synopsis) {
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
        this.synopsis = synopsis;
    }
}
