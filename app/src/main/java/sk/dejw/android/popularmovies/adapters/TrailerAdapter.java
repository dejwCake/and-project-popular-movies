package sk.dejw.android.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sk.dejw.android.popularmovies.R;
import sk.dejw.android.popularmovies.models.Trailer;

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final Context mContext;

    final private TrailerAdapterOnClickHandler mClickHandler;

    private Trailer[] mTrailers;

    /**
     * The interface that receives onTrailerClick messages.
     */
    public interface TrailerAdapterOnClickHandler {
        void onTrailerClick(Trailer trailer);
    }

    public TrailerAdapter(@NonNull Context context, Trailer[] trailers, TrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mTrailers = trailers;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, viewGroup, false);

        view.setFocusable(true);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        //TODO finish
    }

    @Override
    public int getItemCount() {
        if (null == mTrailers) return 0;
        return mTrailers.length;
    }

    void swapTrailers(Trailer[] newTreilers) {
        mTrailers = newTreilers;
        notifyDataSetChanged();
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconView;

        final TextView nameView;

        TrailerAdapterViewHolder(View view) {
            super(view);

            iconView = (ImageView) view.findViewById(R.id.iv_trailer_play_icon);
            nameView = (TextView) view.findViewById(R.id.tv_trailer_name);

            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            //TODO fill on click, get trailer
//            mClickHandler.onClick(trailer);
        }
    }
}