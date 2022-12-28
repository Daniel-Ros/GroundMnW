package com.rosenberg.uni.Renter;

import android.annotation.SuppressLint;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Models.RenterFunctions;
import com.rosenberg.uni.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * this class is the code that works with the fragment_renter_car_view.xml Window
 * here the user can see the vehicles he can rent
 */
public class RenterCarViewFragment extends Fragment {

    public List<Car> cars;
    public ListView carsView;
    RenterFunctions rf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rf = new RenterFunctions();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_car_view, container, false);
    }

    /**
     * show cars for rent
     * @param view - View object of the window (hold the objects of texts inputs that screened)
     * @param savedInstanceState -  last state of this fragment,should be null
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // start and end dates for filtering
        EditText start = view.findViewById(R.id.renter_car_view_start_date);
        EditText end = view.findViewById(R.id.renter_car_view_end_date);
        Button filter = view.findViewById(R.id.renter_car_view_filter);

        // list of cars to rent
        carsView = view.findViewById(R.id.renter_car_view_list_view);
        List<String> carStrings = new ArrayList<>();

        // show all the Active cars to req
        rf.showAllCars(null, this);

        // filtering process
        filter.setOnClickListener(v -> {
            rf.filterSearch(start.getText().toString(), end.getText().toString(), this);
        });



        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, carStrings);
        carsView.setAdapter(adapter);

        // car in index i selected
        carsView.setOnItemClickListener((adapterView, view1, i, l) -> {
            FragmentManager fm = getParentFragmentManager();
            // car details and renting fragment
            Log.d("RenterCarView","sending to this id " + cars.get(i).getDocumentId());
            fm.beginTransaction().replace(R.id.main_fragment,
                            RenterCarViewDetailsFragment.newInstance(cars.get(i).getDocumentId()),
                    null)
                    .addToBackStack("RenterCarView")
                    .commit();
        });
    }

    /**
     * put all cars on view
     * @param c list of Cars
     */
    @SuppressLint("ResourceType")
    public void refreshCars(List<Car> c){
        cars = c;
        ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),
                cars.toArray(new Car[0]));
        carsView.setAdapter(adapter);
    }

//        FirebaseFirestore fs = FirebaseFirestore.getInstance();
//        fs.collection("cars")
//                .whereEqualTo("renterID", null) // show only cars that are not reserved
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    cars = queryDocumentSnapshots.toObjects(Car.class);
//                    Log.d("CAR_VIEW Renter","ADDING CARS" + cars.size());
//                    refreshCars(cars);
//        });


//            long start_date_stamp,end_date_stamp;
//            if(start.getText().toString().isEmpty()){ // get start date for filtering
//                start_date_stamp =0;
//                Calendar calendar = new GregorianCalendar(0,1,1);
//                start_date_stamp = calendar.getTimeInMillis();
//            }else{ // parse start date for filtering
//                String [] splitdate = start.getText().toString().split("/");
//                Calendar calendar = new GregorianCalendar(Integer.parseInt(splitdate[2]),
//                        Integer.parseInt(splitdate[1]),
//                        Integer.parseInt(splitdate[0]));
//                start_date_stamp = calendar.getTimeInMillis();
//            }
//
//            if(end.getText().toString().isEmpty()){ // get end date for filtering
//                Calendar calendar = new GregorianCalendar(3000,1,1);
//                end_date_stamp = calendar.getTimeInMillis();
//            }else{ // parse end date for filtering
//                String [] splitdate = end.getText().toString().split("/");
//                Calendar calendar = new GregorianCalendar(Integer.parseInt(splitdate[2]),
//                        Integer.parseInt(splitdate[1]),
//                        Integer.parseInt(splitdate[0]));
//                end_date_stamp = calendar.getTimeInMillis();
//            }
//            // get only cars with end-of-rent time between start and end date
//            fs.collection("cars")
//                    .whereLessThan("endDateStamp",end_date_stamp)
//                    .whereGreaterThan("endDateStamp",start_date_stamp)
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        cars = queryDocumentSnapshots.toObjects(Car.class);
//                        Log.d("CAR_VIEW Renter","ADDING CARS" + cars.size());
//
//
//                        // we are doing this because we cant mnake compund queries
//                        for (int i = 0; i < cars.size(); i++) {
//                            if(cars.get(i).getRenterID() != null){
//                                cars.remove(i--);
//                            }
//                        }
//                        // show relevant cars
////                        ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),
////                                cars.toArray(new Car[0]));
////                        carsView.setAdapter(adapter);
//                        refreshCars(cars, carsView);
//
//                    });

}