package android.example.popularmovies;

import android.example.popularmovies.database.AppDatabase;
import android.example.popularmovies.database.FavoriteEntry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {

    private LiveData<FavoriteEntry> favorite;

    public DetailViewModel(AppDatabase database, int movieId) {
        favorite = database.favoriteDao().loadFavoriteById(movieId);
    }

    public LiveData<FavoriteEntry> getFavorite() {
        return favorite;
    }
}
