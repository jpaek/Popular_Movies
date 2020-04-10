package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.utilities.Trailer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{
    private ArrayList<Trailer> mTrailer;
    private static final String TAG = TrailerAdapter.class.getSimpleName();


    private final TrailerAdapterOnClickHandler mClickHandler;


    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }


    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTrailerTitle;


        public TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerTitle = (TextView) view.findViewById(R.id.tv_trailer_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailer.get(adapterPosition);
            mClickHandler.onClick(trailer);
        }
    }


    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        Trailer trailer = mTrailer.get(position);
        trailerAdapterViewHolder.mTrailerTitle.setText(trailer.name);
    }


    @Override
    public int getItemCount() {

        if (null == mTrailer) return 0;
        return mTrailer.size();
    }


    public void setTrailerData(ArrayList<Trailer> trailerData) {
        mTrailer = trailerData;
        notifyDataSetChanged();
    }
}
