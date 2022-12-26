package com.rosenberg.uni.Renter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Models.RenterFunctions;
import com.rosenberg.uni.R;

import java.util.List;

/**
 * this class represents window for specific car for the renter
 * the window shows all the details on specific car and provide option to request the car or
 * delete such request.
 */
public class RenterCarViewDetailsFragment extends Fragment {

    private static final String ARG_NAME = "CARID";
    private String car_id;

    public boolean isCarAlreadyReq = false;
    RenterFunctions rf;

    TextView make;
    TextView model;
    TextView mileage;
    TextView numOfSeats;
    TextView fuel;
    TextView gearbox;
    TextView startDate;
    TextView endDate;
    Button req_car;

    public RenterCarViewDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Car Document id
     * @return A new instance of fragment RenterCarViewDetailsFragment.
     */
    public static RenterCarViewDetailsFragment newInstance(String param1) {
        RenterCarViewDetailsFragment fragment = new RenterCarViewDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * do default onCreate with initializing carDocId
     * @param savedInstanceState last state of this fragment,should be null
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            car_id = getArguments().getString(ARG_NAME);
        }
    }

    /**
     * default "onCreateView" phase
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rf = new RenterFunctions();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_car_view_details, container, false);
    }

    /**
     * Called when fragment is inflated,
     * init all texts and buttons for curr window
     * @param view - this view object
     * @param savedInstanceState -  last state of this fragment,should be null
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init text vars
        make = view.findViewById(R.id.renter_car_view_details_make);
        model = view.findViewById(R.id.renter_car_view_details_model);
        mileage = view.findViewById(R.id.renter_car_view_details_mileage);
        numOfSeats = view.findViewById(R.id.renter_car_view_details_num_of_seats);
        fuel = view.findViewById(R.id.renter_car_view_details_fuel);
        gearbox = view.findViewById(R.id.renter_car_view_details_gearbox);
        startDate = view.findViewById(R.id.renter_car_view_details_start_date);
        endDate = view.findViewById(R.id.renter_car_view_details_end_date);

        // init button
        req_car = view.findViewById(R.id.renter_car_view_details_req_car);

        // get curr data on car to show it
        rf.getSpecificCar(car_id, this);

        // we shall init the name of the req_car button (cancel/not cancel)
        rf.checkAllRequests(car_id, FirebaseAuth.getInstance().getUid(), this);

        // init request and cancel request button action
        req_car.setOnClickListener(v -> {

            if (isCarAlreadyReq) {
                // car already requested- cancel request
                rf.cancelRequests(car_id, FirebaseAuth.getInstance().getUid(), this);
                isCarAlreadyReq = false;
                req_car.setText("Request car");
            } else {
                // request car
                isCarAlreadyReq = true;
                req_car.setText("Cancel Request");
                rf.sendRequest(car_id, FirebaseAuth.getInstance().getUid());

            }
        });
    }

    /**
     * present the details on car
     * @param car to show
     */
    public void show(Car car) {
        // update text to information from database
        make.setText(car.getMake());
        model.setText(car.getModel());
        mileage.setText(car.getMileage().toString());
        numOfSeats.setText(car.getNumOfSeats().toString());
        fuel.setText(car.getFuel());
        gearbox.setText(car.getGearbox());
        startDate.setText(car.getStartDate());
        endDate.setText(car.getEndDate());
    }

    /**
     * get report on how many request there is right now on car from curr user:
     *  0 requests - user can request
     *  more than 0 - user already requested
     * @param size - how many requests
     */
    public void takeRequestLength(int size) {
        if(size > 0) {
            // The user already requested the car
            isCarAlreadyReq = true;
            req_car.setText("Cancel Request");
        }else {
            req_car.setText("Request car");
        }
    }
}



//        FirebaseFirestore fs = FirebaseFirestore.getInstance();
//                fs.collection("cars")
//                .document(car_id)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> { // get specific car
//                Car car = queryDocumentSnapshots.toObject(Car.class);
//        // update text to information from database
//        make.setText(car.getMake());
//        model.setText(car.getModel());
//        mileage.setText(car.getMileage().toString());
//        numOfSeats.setText(car.getNumOfSeats().toString());
//        fuel.setText(car.getFuel());
//        gearbox.setText(car.getGearbox());
//        startDate.setText(car.getStartDate());
//        endDate.setText(car.getEndDate());

//                    fs.collection("cars").document(car_id)
//                            .collection("request")
//                            .whereEqualTo("id", FirebaseAuth.getInstance().getUid())
//                            .get()
//                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
//                            if(queryDocumentSnapshots1.size() > 0) {
//                            // The user already requested the car
//                            isCarAlreadyReq = true;
//                            req_car.setText("Cancel Request");
//                            }
//                            });