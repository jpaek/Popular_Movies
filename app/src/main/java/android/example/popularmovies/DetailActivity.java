package android.example.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.example.popularmovies.database.AppDatabase;
import android.example.popularmovies.database.FavoriteEntry;
import android.example.popularmovies.utilities.Movie;
import android.example.popularmovies.utilities.NetworkUtils;
import android.example.popularmovies.utilities.Review;
import android.example.popularmovies.utilities.TMDBJsonUtils;
import android.example.popularmovies.utilities.Trailer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private Movie mMovie;
    private Switch mFavoriteSwitch;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVote;
    private TextView mSynopsis;
    private ImageView mPoster;
    private LinearLayoutManager mTrailerLayoutManager;
    private TrailerAdapter mTrailerAdapter;
    private LinearLayoutManager mReviewLayoutManager;
    private ReviewAdapter mReviewAdapter;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        mFavoriteSwitch = (Switch) findViewById(R.id.set_favorite);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mVote = (TextView) findViewById(R.id.tv_vote_average);
        mSynopsis = (TextView) findViewById(R.id.tv_synopsis);
        mPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        database = AppDatabase.getInstance(getApplicationContext());

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("Movie")) {
                mMovie = (Movie)intentThatStartedThisActivity.getSerializableExtra("Movie");
                setDetailView();
                DetailViewModelFactory factory = new DetailViewModelFactory(database, mMovie.getId());
                final DetailViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

                viewModel.getFavorite().observe(this, new Observer<FavoriteEntry>() {
                    @Override
                    public void onChanged(FavoriteEntry favoriteEntry) {
                        viewModel.getFavorite().removeObserver(this);
                        setFavoriteSwitch(favoriteEntry);
                    }
                });
                ;

                mTrailerLayoutManager = new LinearLayoutManager(this);
                mTrailerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mTrailerAdapter = new TrailerAdapter(this);
                loadTrailers(mMovie.getId());

                mReviewLayoutManager = new LinearLayoutManager(this);
                mReviewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mReviewAdapter = new ReviewAdapter();
                loadReviews(mMovie.getId());
            }
        }


    }

    private void onFavoriteChecked() {
        final FavoriteEntry favorite = new FavoriteEntry(mMovie);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "Inserted: " + favorite.toString());
                database.favoriteDao().insertFavorite(favorite);
            }
        });
    }

    private void onFavoriteUnchecked() {
        final FavoriteEntry favorite = new FavoriteEntry(mMovie);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "Deleted: " + favorite.toString());
                database.favoriteDao().deleteFavorite(favorite);
            }
        });
    }

    private void setFavoriteSwitch(final FavoriteEntry entry) {
        final FavoriteEntry favorite = new FavoriteEntry(mMovie);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (entry != null) {
                    Log.v(TAG, "Found entry: " + entry.toString());
                    mFavoriteSwitch.setChecked(true);
                }
                mFavoriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            onFavoriteChecked();
                        } else {
                            onFavoriteUnchecked();
                        }
                    }
                });
            }
        });
    }

    private void setDetailView() {
        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVote.setText(mMovie.getVote());
        mSynopsis.setText(mMovie.getSynopsis());

        setPoster();
    }

    private void loadTrailers(int movieId) {
        Log.v(TAG, "Moive ID: " + movieId);
        new FetchMovieTrailerTask().execute(Integer.valueOf(movieId));
    }


    private void loadReviews(int movieId) {
        Log.v(TAG, "Moive ID: " + movieId);
        new FetchMovieReviewTask().execute(Integer.valueOf(movieId));
    }
    @Override
    public void onClick(Trailer trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd." + trailer.site + ":" + trailer.key));
        Intent webIntent;
        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException ex) {
            switch(trailer.site) {
                case "YouTube":
                    webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + trailer.key));
                    startActivity(webIntent);
                    break;
                case "Vimeo":
                    webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.vimeo.com/" + trailer.key));
                    startActivity(webIntent);
                    break;
            }
        }
    }

    private void setPoster() {
        Picasso.get().load(mMovie.getMoviePath()).into(mPoster);
    }

    public class FetchMovieTrailerTask extends AsyncTask<Integer, Void, ArrayList<Trailer>> {

        private ProgressBar mLoadingIndicator;
        private TextView mErrorMessageDisplay;
        private RecyclerView mRecyclerView;

        FetchMovieTrailerTask() {
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers);
            mRecyclerView.setLayoutManager(mTrailerLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            Log.v(TAG, "Set Adapter");
            mRecyclerView.setAdapter(mTrailerAdapter);

            /* This TextView is used to display errors and will be hidden if there are no errors */
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_trailers);

            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_trailers);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        private boolean isOnline() {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            }
            catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }

            return false;
        }

        @Override
        protected ArrayList<Trailer> doInBackground(Integer... params) {

            if (params.length == 0) {
                return null;
            }

            int movieId = params[0];
            URL trailerListRequestUrl = NetworkUtils.buildTrailerUtrl((int)movieId);

            try {
                if (isOnline()) {
                    String jsonTrailerListResponse = NetworkUtils
                            .getResponseFromHttpUrl(trailerListRequestUrl);

                    ArrayList<Trailer> trailerData = TMDBJsonUtils
                            .getTrailersFromJson(DetailActivity.this, jsonTrailerListResponse);

                    return trailerData;
                }
                else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailerData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            Log.v(TAG, "Do Post: " + trailerData.toString());
            if (trailerData != null) {

                mTrailerAdapter.setTrailerData(trailerData);
                showTrailerView();
            } else {
                showErrorMessage();
            }
        }

        private void showTrailerView() {
            Log.v(TAG, "show trailer view");
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage() {
            /* First, hide the currently visible data */
            mRecyclerView.setVisibility(View.INVISIBLE);
            /* Then, show the error */
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }
    }


    public class FetchMovieReviewTask extends AsyncTask<Integer, Void, Review[]> {

        private ProgressBar mLoadingIndicator;
        private TextView mErrorMessageDisplay;
        private RecyclerView mRecyclerView;

        FetchMovieReviewTask() {
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
            mRecyclerView.setLayoutManager(mReviewLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mReviewAdapter);

            /* This TextView is used to display errors and will be hidden if there are no errors */
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_reviews);

            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_reviews);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        private boolean isOnline() {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            }
            catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }

            return false;
        }

        @Override
        protected Review[] doInBackground(Integer... params) {

            if (params.length == 0) {
                return null;
            }

            int movieId = params[0];
            URL trailerListRequestUrl = NetworkUtils.buildReviewUrl((int)movieId);

            try {
                if (isOnline()) {
                    String jsonReviewListResponse = NetworkUtils
                            .getResponseFromHttpUrl(trailerListRequestUrl);

                    Review[] reviewData = TMDBJsonUtils
                            .getReviewsFromJson(DetailActivity.this, jsonReviewListResponse);
                    Log.v(TAG, "Reviews: " + reviewData.length);

                    return reviewData;
                }
                else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviewData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (reviewData != null) {
                mReviewAdapter.setReviewData(reviewData);
                showReviewView();
            } else {
                showErrorMessage();
            }
        }

        private void showReviewView() {
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage() {
            /* First, hide the currently visible data */
            mRecyclerView.setVisibility(View.INVISIBLE);
            /* Then, show the error */
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }
    }

}
