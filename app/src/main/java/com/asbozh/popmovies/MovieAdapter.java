package com.asbozh.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asbozh.popmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie selectedMovie);
    }

    private List<Movie> mMovieList;
    private Context context;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.mClickHandler = mClickHandler;
    }


    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String moviePosterURL = mMovieList.get(position).getMoviePoster();
        if (moviePosterURL != null) {
            Picasso.with(context).load(moviePosterURL).into(holder.moviePosterImageView);
        }
    }


    @Override
    public int getItemCount() {
        if (mMovieList == null) {
            return 0;
        }
        return mMovieList.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView moviePosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            moviePosterImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie selectedMovie = mMovieList.get(adapterPosition);
            mClickHandler.onClick(selectedMovie);
        }
    }

    public void setMovieData(List<Movie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

}
