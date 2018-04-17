package sk.dejw.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sk.dejw.android.popularmovies.R;
import sk.dejw.android.popularmovies.models.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final Context mContext;

    final private TrailerAdapterOnClickHandler mClickHandler;

    private ArrayList<Trailer> mTrailers;

    /**
     * The interface that receives onTrailerClick messages.
     */
    public interface TrailerAdapterOnClickHandler {
        void onTrailerClick(Trailer trailer);
    }

    public TrailerAdapter(@NonNull Context context, ArrayList<Trailer> trailers, TrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mTrailers = trailers;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, viewGroup, false);

        view.setFocusable(true);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        Trailer trailer = mTrailers.get(position);

        trailerAdapterViewHolder.iconView.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        trailerAdapterViewHolder.nameView.setText(trailer.getName());
        trailerAdapterViewHolder.trailer = trailer;
    }

    @Override
    public int getItemCount() {
        if (null == mTrailers) return 0;
        return mTrailers.size();
    }

    public void swapTrailers(ArrayList<Trailer> newTrailers) {
        mTrailers = newTrailers;
        notifyDataSetChanged();
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconView;

        final TextView nameView;

        Trailer trailer = null;

        TrailerAdapterViewHolder(View view) {
            super(view);

            iconView = (ImageView) view.findViewById(R.id.iv_trailer_play_icon);
            nameView = (TextView) view.findViewById(R.id.tv_trailer_name);

            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            mClickHandler.onTrailerClick(trailer);
        }
    }
}