package android.example.popularmovies;

import android.example.popularmovies.database.AppDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDatabase;
    private final int mMovieId;

    public DetailViewModelFactory(AppDatabase database, int movieId) {
        mDatabase = database;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(mDatabase, mMovieId);
    }
}
