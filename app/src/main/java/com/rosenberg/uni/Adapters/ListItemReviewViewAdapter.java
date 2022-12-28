package com.rosenberg.uni.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rosenberg.uni.Entities.Review;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;

public class ListItemReviewViewAdapter extends ArrayAdapter {
    Activity context;
    Review[] reviews;

    public ListItemReviewViewAdapter(Activity context, Review[] reviews){
        super(context, R.layout.list_item_car_view,reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater(); // init layout
        View rowView = inflater.inflate(R.layout.list_item_review_view,null,true);
        TextView comment = rowView.findViewById(R.id.list_item_review_view_comment);
        RatingBar rating = rowView.findViewById(R.id.list_item_review_view_rating);

        comment.setText(reviews[position].getComment());
        rating.setRating(reviews[position].getRating());
        return rowView;
    }
}
