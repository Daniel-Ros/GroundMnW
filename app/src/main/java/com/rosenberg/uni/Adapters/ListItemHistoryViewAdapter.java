package com.rosenberg.uni.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.History;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;

import java.util.List;

/**
 * this adapter purpose is to convert entry of user
 * to row view that we can represent
 */
public class ListItemHistoryViewAdapter extends ArrayAdapter {

    private History[] histories;
    private Activity context;

    /**
     * constructor
     * @param context - of the activity
     * @param histories - all users for the table that the adapter would present only one row in the table
     */
    public ListItemHistoryViewAdapter(Activity context, History[] histories){
        super(context, R.layout.list_item_car_view,histories);
        this.context = context;
        this.histories = histories;
    }

    /**
     *
     * @param position - for a user in a row in users[]
     * @param convertView - built in, we wont use
     * @param parent - same above
     * @return the row view object of a spesific entry of user
     */
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater(); // init layout

        // make that row be exist
        View rowView = inflater.inflate(R.layout.list_item_history_view,null,true);

        // var of text, will present the name of user
        TextView name = rowView.findViewById(R.id.list_item_history_view_name);
        TextView reviewd = rowView.findViewById(R.id.list_item_history_view_reviewed);
        // init its values


        name.setText(histories[position].getName());
        reviewd.setText(histories[position].getReviewed().toString());

        return rowView;
    }
}
