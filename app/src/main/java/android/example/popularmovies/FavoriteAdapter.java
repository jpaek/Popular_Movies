package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.database.FavoriteEntry;
import android.example.popularmovies.utilities.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final MovieAdapter.MovieAdapterOnClickHandler mClickHandler;
    private List<FavoriteEntry> mFavorites;

    public FavoriteAdapter(MovieAdapter.MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteEntry favoriteEntry = mFavorites.get(position);
        Movie movie = favoriteEntry.getMovie();
        Picasso.get().load(movie.getMoviePath()).into(holder.mMoviePosterView);
    }

    @Override
    public int getItemCount() {
        if (mFavorites == null) {
            return 0;
        }
        return mFavorites.size();
    }

    public void setFavorites(List<FavoriteEntry> favoriteEntries) {
        mFavorites = favoriteEntries;
        notifyDataSetChanged();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mMoviePosterView;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            mMoviePosterView = (ImageView) itemView.findViewById(R.id.iv_list_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mFavorites.get(adapterPosition).getMovie();
            mClickHandler.onClick(movie);
        }
    }
}
