package sk.dejw.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sk.dejw.android.popularmovies.R;
import sk.dejw.android.popularmovies.models.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private final Context mContext;

    final private ReviewAdapterOnClickHandler mClickHandler;

    private ArrayList<Review> mReviews;

    /**
     * The interface that receives onTrailerClick messages.
     */
    public interface ReviewAdapterOnClickHandler {
        void onReviewClick(Review review);
    }

    public ReviewAdapter(@NonNull Context context, ArrayList<Review> reviews, ReviewAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mReviews = reviews;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, viewGroup, false);

        view.setFocusable(true);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        Review review = mReviews.get(position);

        reviewAdapterViewHolder.authorView.setText(review.getAuthor());
        reviewAdapterViewHolder.contentView.setText(review.getContent());
        reviewAdapterViewHolder.review = review;
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    public void swapReviews(ArrayList<Review> newReviews) {
        mReviews = newReviews;
        notifyDataSetChanged();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView authorView;
        final TextView contentView;

        Review review = null;

        ReviewAdapterViewHolder(View view) {
            super(view);

            authorView = (TextView) view.findViewById(R.id.tv_review_author);
            contentView = (TextView) view.findViewById(R.id.tv_review_content);

            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            mClickHandler.onReviewClick(review);
        }
    }
}