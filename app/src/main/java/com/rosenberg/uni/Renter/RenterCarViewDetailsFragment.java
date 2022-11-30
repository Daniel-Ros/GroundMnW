package com.rosenberg.uni.Renter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;

import java.util.List;

public class RenterCarViewDetailsFragment extends Fragment {

    private static final String ARG_NAME = "CARID";
    private String car_id;

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
    // TODO: Rename and change types and number of parameters
    public static RenterCarViewDetailsFragment newInstance(String param1) {
        RenterCarViewDetailsFragment fragment = new RenterCarViewDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_renter_car_view_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView make = view.findViewById(R.id.renter_car_view_details_make);
        TextView model = view.findViewById(R.id.renter_car_view_details_model);
        TextView mileage = view.findViewById(R.id.renter_car_view_details_mileage);
        Button req_car = view.findViewById(R.id.renter_car_view_details_req_car);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("cars")
                .document(car_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Car car = queryDocumentSnapshots.toObject(Car.class);

                    make.setText(car.getMake());
                    model.setText(car.getModel());
                    mileage.setText(car.getMileage());

                    req_car.setOnClickListener(v -> {
                        fs.collection("users").whereEqualTo("id",
                                        FirebaseAuth.getInstance().getUid())
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots2 -> {
                                    List<User> userList = queryDocumentSnapshots2.toObjects(User.class);
                                    Log.d("USER UTILS","Register user " + userList.size());
                                    User u = userList.get(0);

                                    fs.collection("cars")
                                            .document(car_id)
                                            .collection("request")
                                            .add(u);
                                });
                    });
                })
                .addOnFailureListener( fail -> {
                    Log.d("RenterCarViewDetails","problme loading car info" + car_id);
                });
    }
}