package com.asbozh.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asbozh.popmovies.data.Trailer;

import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onTrailerClick(Trailer selectedTrailer);
    }

    private List<Trailer> mTrailerList;
    private Context context;

    public TrailerAdapter(Context context, TrailerAdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_list_item, viewGroup, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        String trailerName = mTrailerList.get(position).getTrailerName();
        if (trailerName != null) {
            holder.trailerNameTextView.setText(trailerName);
        }
    }

    @Override
    public int getItemCount() {
        if (mTrailerList == null) {
            return 0;
        }
        return mTrailerList.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView trailerNameTextView;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            trailerNameTextView = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer selectedTrailer = mTrailerList.get(adapterPosition);
            mClickHandler.onTrailerClick(selectedTrailer);
        }
    }

    public void setTrailerData(List<Trailer> trailerList) {
        mTrailerList = trailerList;
        notifyDataSetChanged();
    }
}
