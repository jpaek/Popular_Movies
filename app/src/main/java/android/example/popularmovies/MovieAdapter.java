package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.utilities.Movie;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Movie[] mMovieData;


    private final MovieAdapterOnClickHandler mClickHandler;


    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }


    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMoviePosterView;


        public MovieAdapterViewHolder(View view) {
            super(view);
            mMoviePosterView = (ImageView) view.findViewById(R.id.iv_list_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieData[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Log.v(TAG, String.valueOf(position));
        Movie movie = mMovieData[position];
        Picasso.get().load(movie.getMoviePath()).into(movieAdapterViewHolder.mMoviePosterView);
    }

    @Override
    public int getItemCount() {
        Log.v(TAG, "Item count called");
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }


    public void setMovieData(Movie[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
