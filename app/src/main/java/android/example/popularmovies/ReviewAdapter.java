package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.utilities.Review;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private Review[] mReviews;


    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView mAuthorView;
        private final TextView mContentView;


        public ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthorView = (TextView)view.findViewById(R.id.tv_author);
            mContentView = (TextView)view.findViewById(R.id.tv_content);
        }
    }


    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        Review review = mReviews[position];
        reviewAdapterViewHolder.mAuthorView.setText(review.getAuthor());
        reviewAdapterViewHolder.mContentView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        else Log.v(TAG, "Size: " + mReviews.length);
        return mReviews.length;
    }


    public void setReviewData(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }
}
