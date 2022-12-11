package com.rosenberg.uni.Renter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class RenterCarViewFragment extends Fragment {

    private List<Car> cars;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_car_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText start = view.findViewById(R.id.renter_car_view_start_date);
        EditText end = view.findViewById(R.id.renter_car_view_end_date);
        Button filter = view.findViewById(R.id.renter_car_view_filter);

        ListView car_view = view.findViewById(R.id.renter_car_view_list_view);
        List<String> carStrings = new ArrayList<>();

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("cars")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cars = queryDocumentSnapshots.toObjects(Car.class);
                    Log.d("CAR_VIEW Renter","ADDING CARS" + cars.size());

                    ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),
                            cars.toArray(new Car[0]));
                    car_view.setAdapter(adapter);
                });

        filter.setOnClickListener(v -> {
            long start_date_stamp,end_date_stamp;
            if(start.getText().toString().isEmpty()){
                start_date_stamp =0;
                Calendar calendar = new GregorianCalendar(0,1,1);
                start_date_stamp = calendar.getTimeInMillis();
            }else{
                String [] splitdate = start.getText().toString().split("/");
                Calendar calendar = new GregorianCalendar(Integer.parseInt(splitdate[2]),
                        Integer.parseInt(splitdate[1]),
                        Integer.parseInt(splitdate[0]));
                start_date_stamp = calendar.getTimeInMillis();
            }

            if(end.getText().toString().isEmpty()){
                Calendar calendar = new GregorianCalendar(3000,1,1);
                end_date_stamp = calendar.getTimeInMillis();
            }else{
                String [] splitdate = end.getText().toString().split("/");
                Calendar calendar = new GregorianCalendar(Integer.parseInt(splitdate[2]),
                        Integer.parseInt(splitdate[1]),
                        Integer.parseInt(splitdate[0]));
                end_date_stamp = calendar.getTimeInMillis();
            }
            fs.collection("cars")
                    .whereLessThan("endDateStamp",end_date_stamp)
                    .whereGreaterThan("endDateStamp",start_date_stamp)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        cars = queryDocumentSnapshots.toObjects(Car.class);
                        Log.d("CAR_VIEW Renter","ADDING CARS" + cars.size());



                        ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),
                                cars.toArray(new Car[0]));
                        car_view.setAdapter(adapter);
                    });
        });

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,carStrings);
        car_view.setAdapter(adapter);


        car_view.setOnItemClickListener((adapterView, view1, i, l) -> {
            FragmentManager fm = getParentFragmentManager();

            Log.d("RenterCarView","sending to this id " + cars.get(i).getDocumentId());
            fm.beginTransaction().replace(R.id.main_fragment,
                            RenterCarViewDetailsFragment.newInstance(cars.get(i).getDocumentId()),
                    null)
                    .addToBackStack("RenterCarView")
                    .commit();
        });
    }
}