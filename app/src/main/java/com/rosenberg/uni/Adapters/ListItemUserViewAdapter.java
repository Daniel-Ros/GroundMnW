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

public class ListItemUserViewAdapter extends ArrayAdapter {
    private User [] users;
    private Activity context;
    public ListItemUserViewAdapter(Activity context, User[] users){
        super(context, R.layout.list_item_car_view,users);

        this.context=context;
        this.users = users;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item_user_view,null,true);

        TextView name = rowView.findViewById(R.id.list_item_user_name);

        name.setText(users[position].getFirstName()+" "+users[position].getLastName());
//        mail.setText(users[position].getMail());

        return rowView;
    }
}
