package com.rosenberg.uni.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.R;

public class ListItemCarViewAdapter extends ArrayAdapter {
    private Car [] cars;
    private Activity context;
    public ListItemCarViewAdapter(Activity context, Car[] cars){
        super(context, R.layout.list_item_car_view,cars);

        this.context=context;
        this.cars = cars;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item_car_view,null,true);

        TextView make = rowView.findViewById(R.id.list_car_view_make);
        TextView model = rowView.findViewById(R.id.list_car_view_model);
        TextView mileage = rowView.findViewById(R.id.list_car_view_milage);

        make.setText(cars[position].getMake());
        model.setText(cars[position].getModel());
        mileage.setText(cars[position].getMileage());

        return rowView;
    }
}
