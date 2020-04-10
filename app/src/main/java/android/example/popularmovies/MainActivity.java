package android.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.example.popularmovies.database.AppDatabase;
import android.example.popularmovies.database.FavoriteEntry;
import android.example.popularmovies.utilities.Movie;
import android.example.popularmovies.utilities.NetworkUtils;
import android.example.popularmovies.utilities.TMDBJsonUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private FavoriteAdapter mFavoriteAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private AppDatabase mDatabase;

    private final String POPULAR = "popular";
    private final String TOP_RATED = "top_rated";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, numberOfColumns());

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);


        mMovieAdapter = new MovieAdapter(this);
        mFavoriteAdapter = new FavoriteAdapter(this);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        loadMovieData(POPULAR);
        mDatabase = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the item
        int widthDivider = 200;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_popular:
                loadMovieData(POPULAR);
                return true;
            case R.id.action_top_rated:
                loadMovieData(TOP_RATED);
                return true;
            case R.id.action_favorite:
                loadFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMovieData(String type) {
        showMovieDataView();
        mRecyclerView.setAdapter(mMovieAdapter);
        new FetchMovieListTask().execute(type);
    }

    private void loadFavorite() {
        showMovieDataView();
        Log.v(TAG, "Show favorites");
        mRecyclerView.setAdapter(mFavoriteAdapter);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("Movie", movie);
        Log.v(TAG, "Selected a movie: " + movie.title);
        startActivity(intentToStartDetailActivity);
    }


    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void setupViewModel() {
        FavoriteViewModel viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(List<FavoriteEntry> favoriteEntries) {
                Log.v(TAG, "ID: " + favoriteEntries.get(0).getId());
                mFavoriteAdapter.setFavorites(favoriteEntries);
            }
        });
    }

    public class FetchMovieListTask extends AsyncTask<String, Void, Movie[]> {

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
        protected Movie[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String type = params[0];
            URL movieListRequestUrl = NetworkUtils.buildUrl(type);

            try {
                if (isOnline()) {
                    String jsonMovieListResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieListRequestUrl);

                    Movie[] movieData = TMDBJsonUtils
                            .getMoviesFromJson(MainActivity.this, jsonMovieListResponse);

                    return movieData;
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
        protected void onPostExecute(Movie[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
           if (movieData != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }


        }
    }
}
