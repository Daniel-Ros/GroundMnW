package com.rosenberg.uni.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.R;

public class ListItemCarViewAdapter extends ArrayAdapter {
    private Car [] cars;
    private String userID;
    private Activity context;
    public ListItemCarViewAdapter(Activity context, Car[] cars){
        super(context, R.layout.list_item_car_view,cars);

        userID = FirebaseAuth.getInstance().getUid();
        this.context=context;
        this.cars = cars;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item_car_view,null,true);

        TextView make = rowView.findViewById(R.id.list_item_car_view_make);
        TextView model = rowView.findViewById(R.id.list_item_car_view_model);
        TextView mileage = rowView.findViewById(R.id.list_item_car_view_milage);
        TextView status = rowView.findViewById(R.id.list_item_car_view_status);

        if(userID.equals(cars[position].getRenterID())){
            status.setText("Reserved");
        }else{
            status.setText("Active");
        }
        make.setText(cars[position].getMake());
        model.setText(cars[position].getModel());
        mileage.setText(cars[position].getMileage().toString());

        return rowView;
    }
}
