package com.rosenberg.uni.Tenant;

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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemUserViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;

import java.util.List;

/**
 * this class represents window for specific car for the tenant
 * the window shows all the details on specific car and provide option to remove or edit it
 * also, show all the users that requested this car
 */
public class TenantCarViewDetailsFragment extends Fragment {

    private static final String CAR_DATA = "CAR_DATA"; // get input from user
    private String carDocId;  // gets it as input from constructing

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
        TextView make = view.findViewById(R.id.tenant_car_view_details_make);
        TextView model = view.findViewById(R.id.tenant_car_view_details_model);
        TextView mileage = view.findViewById(R.id.tenant_car_view_details_mileage);
        TextView numOfSeats = view.findViewById(R.id.tenant_car_view_details_num_of_seats);
        TextView fuel = view.findViewById(R.id.tenant_car_view_details_fuel);
        TextView gearbox = view.findViewById(R.id.tenant_car_view_details_gearbox);
        TextView startDate = view.findViewById(R.id.tenant_car_view_details_start_date);
        TextView endDate = view.findViewById(R.id.tenant_car_view_details_end_date);
        ListView carView = view.findViewById(R.id.tenant_car_view_details_users_list_view);

        // init buttons
        Button editBtn = view.findViewById(R.id.tenant_car_view_details_edit);
        Button removeBtn = view.findViewById(R.id.tenant_car_view_details_remove);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();

        // get curr car data from the database and show it to user
        fs.collection("cars")
                .document(carDocId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Car currentCar = queryDocumentSnapshots.toObject(Car.class);

                    // split the data to the relevant text boxes
                    make.setText(currentCar.getMake());
                    model.setText(currentCar.getModel());
                    mileage.setText(currentCar.getMileage().toString());
                    numOfSeats.setText(currentCar.getNumOfSeats().toString());
                    fuel.setText(currentCar.getFuel());
                    gearbox.setText(currentCar.getGearbox());
                    startDate.setText(currentCar.getStartDate());
                    endDate.setText(currentCar.getEndDate());

                    // init buttons listeners
                    // edit - move to the edit window
                    editBtn.setOnClickListener(view1 -> {
                        FragmentManager fm = getParentFragmentManager();
                        fm.beginTransaction().replace(R.id.main_fragment,
                                        TenantEditCarFragment.newInstance(carDocId),null)
                                .commit();
                    });
                    // remove - delete the current car from the firebase
                    // move back to 'HOME' of tenant
                    removeBtn.setOnClickListener(view1 -> {
                        fs.collection("cars").document(carDocId).delete().addOnSuccessListener(runnable -> {
                            FragmentManager fm = getParentFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment,
                                            TenantCarViewFragment.class,null)
                                    .commit();
                        });
                    });
                })
                .addOnFailureListener( fail -> {
                    Log.d("RenterCarViewDetails","problem loading car info" + carDocId);
                });

        // get user requests and show them on screen
        fs.collection("cars")
                .document(carDocId)
                .collection("request")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = queryDocumentSnapshots.toObjects(User.class);


                    // Create the adapter and pass to it the list of users
                    ArrayAdapter adapter = new ListItemUserViewAdapter(getActivity(), users.toArray(new User[0]));
                    carView.setAdapter(adapter);


                    // take the adapter view ,  view, position and on click lisener of the item
                    // we only use the poision to pass it to the next fragment
                    carView.setOnItemClickListener((adapterView, view1, i, l) -> {
                        Log.d("tenantCarViewDetails",
                                "user want to see the details on the user: " + users.get(i).getId());
                        FragmentManager fm = getParentFragmentManager();
                        fm.beginTransaction().replace(R.id.main_fragment,
                                TenantViewRequestedRenterFragment.newInstance(users.get(i).getId(), carDocId),null)
                                .commit();
                    });
                })
                .addOnFailureListener(fail -> {
                    Log.d("tenantCarViewDetails", "problme loading car info" + carDocId);
                });
    }
}