package com.rosenberg.uni.Tenant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.rosenberg.uni.Adapters.ListItemHistoryViewAdapter;
import com.rosenberg.uni.Adapters.ListItemUserViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.History;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Models.TenantFunctions;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.CustomVolleyRequestQueue;

import java.util.List;

/**
 * this class represents window for specific car for the tenant
 * the window shows all the details on specific car and provide option to remove or edit it
 * also, show all the users that requested this car
 */
public class TenantCarViewDetailsFragment extends Fragment {

    private static final String CAR_DATA = "CAR_DATA"; // get input from user
    private String carDocId;  // gets it as input from constructing

    TenantFunctions tf;
    ImageLoader imageLoader;

    TextView make;
    TextView model;
    TextView mileage;
    TextView numOfSeats;
    TextView fuel;
    TextView gearbox;
    TextView startDate;
    TextView endDate;
    ListView carView;
    ListView historyView;
    NetworkImageView imageView;


    public TenantCarViewDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param carData - input from "outside", this way we can hold document id of a car
     * @return - the TenantCarViewDetailsFragment object instance
     */
    public static TenantCarViewDetailsFragment newInstance(String carData) {
        TenantCarViewDetailsFragment fragment = new TenantCarViewDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CAR_DATA, carData); // just save at CAR_DATA the data of carData
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
        tf = new TenantFunctions();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_car_view_details, container, false);
    }

    /**
     * Called when fragment is inflated,
     * init all texts and buttons for curr window
     * @param view - this view object
     * @param savedInstanceState .
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init text vars
        make = view.findViewById(R.id.tenant_car_view_details_make);
        model = view.findViewById(R.id.tenant_car_view_details_model);
        mileage = view.findViewById(R.id.tenant_car_view_details_mileage);
        numOfSeats = view.findViewById(R.id.tenant_car_view_details_num_of_seats);
        fuel = view.findViewById(R.id.tenant_car_view_details_fuel);
        gearbox = view.findViewById(R.id.tenant_car_view_details_gearbox);
        startDate = view.findViewById(R.id.tenant_car_view_details_start_date);
        endDate = view.findViewById(R.id.tenant_car_view_details_end_date);
        carView = view.findViewById(R.id.tenant_car_view_details_users_list_view);
        historyView = view.findViewById(R.id.tenant_car_view_details_history_list_view);
        imageView = view.findViewById(R.id.tenant_car_view_details_image);

        // init buttons
        ImageView editBtn = view.findViewById(R.id.tenant_car_view_details_edit);
        ImageView removeBtn = view.findViewById(R.id.tenant_car_view_details_remove);



        // init buttons listeners
        // edit - move to the edit window
        editBtn.setOnClickListener(view1 -> {
            this.editClicked();
        });

        // remove - delete the current car from the firebase
        // move back to 'HOME' of tenant
        removeBtn.setOnClickListener(view1 -> {
            tf.deleteCar(carDocId, this);
        });
        // pull the data on the car for showing it
        tf.getSpecificCar(carDocId, this);

        // pull all users requests
        tf.getAllRequests(carDocId, this);

    }

    /**
     * show car details
     * @param currentCar obj
     */
    public void show(Car currentCar) {
        // split the data to the relevant text boxes
        make.setText(currentCar.getMake());
        model.setText(currentCar.getModel());
        mileage.setText(currentCar.getMileage().toString());
        numOfSeats.setText(currentCar.getNumOfSeats().toString());
        fuel.setText(currentCar.getFuel());
        gearbox.setText(currentCar.getGearbox());
        startDate.setText(currentCar.getStartDate());
        endDate.setText(currentCar.getEndDate());

        List<History> histories = currentCar.getPreviousRentersID();


        if (histories != null){
            ArrayAdapter arrayAdapter = new ListItemHistoryViewAdapter(getActivity(), histories.toArray(new History[0]));
            historyView.setAdapter(arrayAdapter);
        }

        historyView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (!histories.get(i).getReviewed()) {
                FragmentManager fm = getParentFragmentManager();
                TenantRateRenterFragment fragment = TenantRateRenterFragment.newInstance(histories.get(i).getRenterID());
                fm.beginTransaction()
                        .replace(R.id.main_fragment,fragment,null)
                        .addToBackStack("review")
                        .commit();
                tf.setHistReviewed(carDocId,histories.get(i).getRenterID());
            }
        });
        if(!currentCar.getPicid().isEmpty()){
            try {
                imageLoader = CustomVolleyRequestQueue.getInstance(getActivity().getApplicationContext())
                        .getImageLoader();
                String url = "http://10.0.2.2:3000/pic/" + currentCar.getPicid();
                imageLoader.get(url, ImageLoader.getImageListener(imageView,
                        R.mipmap.ic_launcher, android.R.drawable
                                .ic_dialog_alert));
                imageView.setImageUrl(url, imageLoader);
            }catch (Exception e) {
                Log.e("IMAGE", e.getLocalizedMessage());
            }
        }

    }

    /**
     * present all users that requested the current car
     * @param users list
     */
    public void uploadUsers(List<User> users) {
        // Create the adapter and pass to it the list of users
        ArrayAdapter adapter = new ListItemUserViewAdapter(getActivity(), users.toArray(new User[0]));
        carView.setAdapter(adapter);

        // take the adapter view ,  view, position and on click lisener of the item
        // we only use the poision to pass it to the next fragment
        carView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Log.d("tenantCarViewDetails",
                    "user want to see the details on the user: " + users.get(i).getId());
            this.renterChosen(users.get(i).getId());
        });
    }

    /**
     * transaction to next window
     */
    public void carRemoveSucceed() {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.main_fragment,
                        TenantCarViewFragment.class,null)
                .commit();
    }

    /**
     * transaction to next window
     */
    private void editClicked() {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.main_fragment,
                        TenantEditCarFragment.newInstance(carDocId), null)
                .commit();
    }

    /**
     * transact it to see the details on the potential renter
     * @param uid unique user id
     */
    private void renterChosen(String uid) {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.main_fragment,
                        TenantViewRequestedRenterFragment.newInstance(uid, carDocId),null)
                .commit();
    }
}

//FirebaseFirestore fs = FirebaseFirestore.getInstance();
//
//        // get curr car data from the database and show it to user
//        fs.collection("cars")
//        .document(carDocId)
//        .get()
//        .addOnSuccessListener(queryDocumentSnapshots -> {
//        Car currentCar = queryDocumentSnapshots.toObject(Car.class);
//
//        // split the data to the relevant text boxes
//        make.setText(currentCar.getMake());
//        model.setText(currentCar.getModel());
//        mileage.setText(currentCar.getMileage().toString());
//        numOfSeats.setText(currentCar.getNumOfSeats().toString());
//        fuel.setText(currentCar.getFuel());
//        gearbox.setText(currentCar.getGearbox());
//        startDate.setText(currentCar.getStartDate());
//        endDate.setText(currentCar.getEndDate());

//                        fs.collection("cars").document(carDocId).delete().addOnSuccessListener(runnable -> {
//                                FragmentManager fm = getParentFragmentManager();
//                                fm.beginTransaction().replace(R.id.main_fragment,
//                                TenantCarViewFragment.class,null)
//        .commit();
//        });


//// get user requests and show them on screen
//        fs.collection("cars")
//                .document(carDocId)
//                .collection("request")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                List<User> users = queryDocumentSnapshots.toObjects(User.class);
//
//
//        // Create the adapter and pass to it the list of users
//        ArrayAdapter adapter = new ListItemUserViewAdapter(getActivity(), users.toArray(new User[0]));
//        carView.setAdapter(adapter);
//
//
//        // take the adapter view ,  view, position and on click lisener of the item
//        // we only use the poision to pass it to the next fragment
//        carView.setOnItemClickListener((adapterView, view1, i, l) -> {
//        Log.d("tenantCarViewDetails",
//        "user want to see the details on the user: " + users.get(i).getId());
//        FragmentManager fm = getParentFragmentManager();
//        fm.beginTransaction().replace(R.id.main_fragment,
//        TenantViewRequestedRenterFragment.newInstance(users.get(i).getId(), carDocId),null)
//        .commit();
//        });
//        })
//        .addOnFailureListener(fail -> {
//        Log.d("tenantCarViewDetails", "problme loading car info" + carDocId);
//        });