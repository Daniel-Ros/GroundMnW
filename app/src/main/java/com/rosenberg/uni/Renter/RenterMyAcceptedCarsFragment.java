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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.R;
import com.rosenberg.uni.Tenant.TenantAddCarFragment;
import com.rosenberg.uni.Tenant.TenantCarViewDetailsFragment;
import com.rosenberg.uni.utils.userUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RenterMyAcceptedCarsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RenterMyAcceptedCarsFragment extends Fragment {

    boolean canAddCar;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RenterMyAcceptedCarsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RenterMyAcceptedCarsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RenterMyAcceptedCarsFragment newInstance(String param1, String param2) {
        RenterMyAcceptedCarsFragment fragment = new RenterMyAcceptedCarsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_my_accepted_cars, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View myAcceptedCarsView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(myAcceptedCarsView, savedInstanceState);

        ListView carsView = myAcceptedCarsView.findViewById(R.id.renter_my_accepted_cars_list_view);
        Button searchCar = myAcceptedCarsView.findViewById(R.id.renter_my_accepted_cars_search);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("cars").whereEqualTo("renterID", userUtils.getUserID())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Car> cars = queryDocumentSnapshots.toObjects(Car.class);
                    Log.d("RENTER ACCEPTED CARS","SHOWING MY CARS" + cars.size());

                    // If we have more then 5 cars, disable this option
                    if(cars.size() >= 5) {
                        canAddCar = false;
                        searchCar.setText("max 5 cars");
                    }
                    else{
                        //other wise, enable it again
                        canAddCar = true;
                    }

                    ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),cars.toArray(new Car[0]));
                    carsView.setAdapter(adapter);

                    carsView.setOnItemClickListener((adapterView, view1, i, l) -> {

                        FragmentManager fm = getParentFragmentManager();

                        RenterMyCarDetailsViewFragment fragment = RenterMyCarDetailsViewFragment.newInstance(cars.get(i).getDocumentId());
                        // show details about car
                        fm.beginTransaction().replace(R.id.main_fragment, fragment, null)
                                .addToBackStack("RenterMyAcceptedCars")
                                .commit();
                    });
                });


        searchCar.setOnClickListener(v -> {
            if(canAddCar) {
                FragmentManager fm = getParentFragmentManager();
                // search for new cars
                fm.beginTransaction().replace(R.id.main_fragment, RenterCarViewFragment.class, null)
                        .addToBackStack("TenantCarView")
                        .commit();
            }else {
                // can't rent more than 5 cars
                Toast.makeText(getActivity(), "Max 5 Cars", Toast.LENGTH_LONG).show();
            }
        });


    }
}