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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Adapters.ListItemUserViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;
import com.rosenberg.uni.Renter.RenterCarViewDetailsFragment;
import com.rosenberg.uni.Renter.RenterCarViewFragment;
import com.rosenberg.uni.login.RegisterFragment;

import java.util.List;


public class TenantCarViewDetailsFragment extends Fragment {

    private static final String ARG_NAME = "CARID";
    private String car_id;

    public TenantCarViewDetailsFragment() {
        // Required empty public constructor
    }

    public static TenantCarViewDetailsFragment newInstance(String param1) {
        TenantCarViewDetailsFragment fragment = new TenantCarViewDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            car_id = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_car_view_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView make = view.findViewById(R.id.tenant_car_view_details_make);
        TextView model = view.findViewById(R.id.tenant_car_view_details_model);
        TextView mileage = view.findViewById(R.id.tenant_car_view_details_mileage);
        TextView numOfSeats = view.findViewById(R.id.tenant_car_view_details_num_of_seats);
        TextView fuel = view.findViewById(R.id.tenant_car_view_details_fuel);
        TextView gearbox = view.findViewById(R.id.tenant_car_view_details_gearbox);
        TextView startDate = view.findViewById(R.id.tenant_car_view_details_start_date);
        TextView endDate = view.findViewById(R.id.tenant_car_view_details_end_date);

        Button edit = view.findViewById(R.id.tenant_car_view_details_edit);
        Button remove = view.findViewById(R.id.tenant_car_view_details_remove);

        ListView car_view = view.findViewById(R.id.tenant_car_view_details_users_list_view);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();

        fs.collection("cars")
                .document(car_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Car car = queryDocumentSnapshots.toObject(Car.class);

                    make.setText(car.getMake());
                    model.setText(car.getModel());
                    mileage.setText(car.getMileage().toString());
                    numOfSeats.setText(car.getNumOfSeats().toString());
                    fuel.setText(car.getFuel());
                    gearbox.setText(car.getGearbox());
                    startDate.setText(car.getStartDate());
                    endDate.setText(car.getEndDate());

                    edit.setOnClickListener(view1 -> {
                        FragmentManager fm = getParentFragmentManager();
                        fm.beginTransaction().replace(R.id.main_fragment,
                                        TenantEditCarFragment.newInstance(car_id),null)
                                .commit();
                    });

                    remove.setOnClickListener(view1 -> {
                        fs.collection("cars").document(car_id).delete().addOnSuccessListener(runnable -> {
                            FragmentManager fm = getParentFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment,
                                            TenantCarViewFragment.class,null)
                                    .commit();
                        });
                    });
                })
                .addOnFailureListener( fail -> {
                    Log.d("RenterCarViewDetails","problme loading car info" + car_id);
                });

        fs.collection("cars")
                .document(car_id)
                .collection("request")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = queryDocumentSnapshots.toObjects(User.class);

                    ArrayAdapter adapter = new ListItemUserViewAdapter(getActivity(), users.toArray(new User[0]));
                    car_view.setAdapter(adapter);

                    car_view.setOnItemClickListener((adapterView, view1, i, l) -> {

                        Log.d("tenantCarViewDetails", "user want to see the details on the user: " + users.get(i).getId());
                        FragmentManager fm = getParentFragmentManager();
                        fm.beginTransaction().replace(R.id.main_fragment,
                                TenantViewRequestedRenterFragment.newInstance(users.get(i).getId(), car_id),null)
                                .commit();

//                        fs.collection("cars")
//                                .document(car_id).update("renterID",users.get(i).getId());
                    });
                })
                .addOnFailureListener(fail -> {
                    Log.d("tenantCarViewDetails", "problme loading car info" + car_id);
                });
    }
}