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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;
import com.rosenberg.uni.login.LoginFragment;

import java.util.List;

/**
 * this class represents window for specific car for the renter
 * the window shows all the details on specific car and provide option to request the car or
 * delete such request.
 */
public class RenterCarViewDetailsFragment extends Fragment {

    private static final String ARG_NAME = "CARID";
    private String car_id;

    private boolean isCarAlreadyReq = false;

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
        TextView make = view.findViewById(R.id.renter_car_view_details_make);
        TextView model = view.findViewById(R.id.renter_car_view_details_model);
        TextView mileage = view.findViewById(R.id.renter_car_view_details_mileage);
        TextView numOfSeats = view.findViewById(R.id.renter_car_view_details_num_of_seats);
        TextView fuel = view.findViewById(R.id.renter_car_view_details_fuel);
        TextView gearbox = view.findViewById(R.id.renter_car_view_details_gearbox);
        TextView startDate = view.findViewById(R.id.renter_car_view_details_start_date);
        TextView endDate = view.findViewById(R.id.renter_car_view_details_end_date);

        // init button
        Button req_car = view.findViewById(R.id.renter_car_view_details_req_car);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("cars")
                .document(car_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> { // get specific car
                    Car car = queryDocumentSnapshots.toObject(Car.class);
                    // update text to information from database
                    make.setText(car.getMake());
                    model.setText(car.getModel());
                    mileage.setText(car.getMileage().toString());
                    numOfSeats.setText(car.getNumOfSeats().toString());
                    fuel.setText(car.getFuel());
                    gearbox.setText(car.getGearbox());
                    startDate.setText(car.getStartDate());
                    endDate.setText(car.getEndDate());


                    fs.collection("cars").document(car_id)
                            .collection("request")
                            .whereEqualTo("id", FirebaseAuth.getInstance().getUid())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                if(queryDocumentSnapshots1.size() > 0) {
                                    // The user already requested the car
                                    isCarAlreadyReq = true;
                                    req_car.setText("Cancel Request");
                                }
                            });

                    // init request and cancel request button action
                    req_car.setOnClickListener(v -> {
                        if (isCarAlreadyReq) {
                            // car already requested- cancel request
                            isCarAlreadyReq = false;
                            req_car.setText("Request car");
                            fs.collection("cars").document(car_id)
                                    .collection("request")
                                    .whereEqualTo("id", FirebaseAuth.getInstance().getUid())
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots1)
                                            queryDocumentSnapshot.getReference().delete();
                                    });
                        } else {
                            // request car
                            isCarAlreadyReq = true;
                            req_car.setText("Cancel Request");
                            fs.collection("users").whereEqualTo("id",
                                            FirebaseAuth.getInstance().getUid())
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots2 -> {
                                        List<User> userList = queryDocumentSnapshots2.toObjects(User.class);
                                        Log.d("USER UTILS", "Register user " + userList.size());
                                        User u = userList.get(0);
                                        // add request to database
                                        fs.collection("cars")
                                                .document(car_id)
                                                .collection("request")
                                                .add(u);
                                    });
                        }
                    });
                })
                .addOnFailureListener(fail -> {
                    Log.d("RenterCarViewDetails", "problem loading car info" + car_id);
                });
    }
}