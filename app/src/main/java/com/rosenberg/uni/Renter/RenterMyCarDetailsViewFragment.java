package com.rosenberg.uni.Renter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Models.RenterFunctions;
import com.rosenberg.uni.R;

/**
 * this fragment goal is to show the RENTER the data on the car that he is already
 *      requested and got accepted its TENANT
 */
public class RenterMyCarDetailsViewFragment extends Fragment {

    private static final String CAR_DATA = "CARID";
    private String carDocId;
    public RenterFunctions rf;
    TextView make;
    TextView model;
    TextView mileage;
    TextView numOfSeats;
    TextView fuel;
    TextView gearbox;
    TextView startDate;
    TextView endDate;
    ImageView endRentBtn;

    ImageLoader imageLoader;
    NetworkImageView carImage;

    public RenterMyCarDetailsViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param carId Car Document id
     * @return A new instance of fragment RenterCarViewDetailsFragment.
     */
    public static RenterMyCarDetailsViewFragment newInstance(String carId) {
        RenterMyCarDetailsViewFragment fragment = new RenterMyCarDetailsViewFragment();
        Bundle args = new Bundle();
        args.putString(CAR_DATA, carId); // just save at CAR_DATA the data of carId
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
            carDocId = getArguments().getString(CAR_DATA);
        }
    }

    /**
     * we not doing anything more than default at "onCreateView" phase
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rf = new RenterFunctions();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_my_car_details_view, container, false);
    }

    /**
     * Called when fragment is inflated,
     * init all texts and buttons for curr window
     * @param view - curr view
     * @param savedInstanceState - last state of this fragment,should be null
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init vars of the texts window
        make = view.findViewById(R.id.renter_my_car_view_details_make);
        model = view.findViewById(R.id.renter_my_car_view_details_model);
        mileage = view.findViewById(R.id.renter_my_car_view_details_mileage);
        numOfSeats = view.findViewById(R.id.renter_my_car_view_details_num_of_seats);
        fuel = view.findViewById(R.id.renter_my_car_view_details_fuel);
        gearbox = view.findViewById(R.id.renter_my_car_view_details_gearbox);
        startDate = view.findViewById(R.id.renter_my_car_view_details_start_date);
        endDate = view.findViewById(R.id.renter_my_car_view_details_end_date);

        // image of car
        carImage = view.findViewById(R.id.renter_my_car_view_details_image);

        // init button
        endRentBtn = view.findViewById(R.id.renter_my_car_view_details_end_rent);

        // get details on my specific car
        // and then show it
        rf.getSpecificCar(carDocId, this);


        endRentBtn.setOnClickListener(view1 -> {
            FragmentManager fm = getParentFragmentManager();
            RenterRateTenantFragment f = RenterRateTenantFragment.newInstance(carDocId);
            fm.beginTransaction().replace(R.id.main_fragment, f, null).commit();
        });
    }

    /**
     * would like to show the details of the car that the renter clicked on
     * @param currentCar my car
     */
    public void show(Car currentCar) {
        // init text boxes
        make.setText(currentCar.getMake());
        model.setText(currentCar.getModel());
        mileage.setText(currentCar.getMileage().toString());
        numOfSeats.setText(currentCar.getNumOfSeats().toString());
        fuel.setText(currentCar.getFuel());
        gearbox.setText(currentCar.getGearbox());
        startDate.setText(currentCar.getStartDate());
        endDate.setText(currentCar.getEndDate());

        // update image
        rf.setCarImage(imageLoader, carImage, currentCar, getActivity());

    }
}