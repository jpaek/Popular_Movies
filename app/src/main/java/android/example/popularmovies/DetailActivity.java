package android.example.popularmovies;

import android.content.Intent;
import android.example.popularmovies.utilities.Movie;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private Movie mMovie;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVote;
    private TextView mSynopsis;
    private ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);


        mTitle = (TextView) findViewById(R.id.tv_title);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mVote = (TextView) findViewById(R.id.tv_vote_average);
        mSynopsis = (TextView) findViewById(R.id.tv_synopsis);
        mPoster = (ImageView) findViewById(R.id.iv_movie_poster);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("Movie")) {
                mMovie = (Movie)intentThatStartedThisActivity.getSerializableExtra("Movie");
                Log.v(TAG, mMovie.getTitle());
                setDetailView();
            }
        }

    }

    private void setDetailView() {
        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVote.setText(mMovie.getVote());
        mSynopsis.setText(mMovie.getSynopsis());

        setPoster();
    }

    private void setPoster() {
        Picasso.get().load(mMovie.getMoviePath()).into(mPoster);
    }

}
