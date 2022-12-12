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

import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;

/**
 * this adapter purpose is to convert entry of user
 * to row view that we can represent
 */
public class ListItemUserViewAdapter extends ArrayAdapter {

    private User [] users;
    private Activity context;

    /**
     * constructor
     * @param context - of the activity
     * @param users - all users for the table that the adapter would present only one row in the table
     */
    public ListItemUserViewAdapter(Activity context, User[] users){
        super(context, R.layout.list_item_car_view,users);
        this.context = context;
        this.users = users;
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
        View rowView = inflater.inflate(R.layout.list_item_user_view,null,true);

        // var of text, will present the name of user
        TextView userName = rowView.findViewById(R.id.list_item_user_name);
        // init its values
        userName.setText(users[position].getFirstName()+" "+users[position].getLastName());

        return rowView;
    }
}
