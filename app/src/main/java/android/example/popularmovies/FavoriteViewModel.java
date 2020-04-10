package android.example.popularmovies;

import android.app.Application;
import android.example.popularmovies.database.AppDatabase;
import android.example.popularmovies.database.FavoriteEntry;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FavoriteViewModel  extends AndroidViewModel {

    private LiveData<List<FavoriteEntry>> favorites;

    public FavoriteViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favorites = database.favoriteDao().loadAllFavorites();
    }

    public LiveData<List<FavoriteEntry>> getFavorites() {
        return favorites;
    }
}
