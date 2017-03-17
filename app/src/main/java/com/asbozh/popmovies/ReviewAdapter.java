package com.asbozh.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asbozh.popmovies.data.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {


    private List<Review> mReviewList;
    private Context context;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list_item, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        String reviewAuthor = mReviewList.get(position).getReviewAuthor();
        String reviewContent = mReviewList.get(position).getReviewContent();
        if (reviewAuthor != null && reviewContent != null) {
            holder.reviewAuthorTextView.setText(reviewAuthor);
            holder.reviewContentTextView.setText(reviewContent);
        }
    }

    @Override
    public int getItemCount() {
        if (mReviewList == null) {
            return 0;
        }
        return mReviewList.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        final private TextView reviewAuthorTextView;
        final private TextView reviewContentTextView;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            reviewAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
            reviewContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }

    public void setReviewData(List<Review> reviewList) {
        mReviewList = reviewList;
        notifyDataSetChanged();
    }
}
