package com.rosenberg.uni.Renter;

import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Models.RenterFunctions;
import com.rosenberg.uni.R;
import com.rosenberg.uni.Tenant.TenantAddCarFragment;
import com.rosenberg.uni.Tenant.TenantCarViewDetailsFragment;
import com.rosenberg.uni.utils.userUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RenterMyAcceptedCarsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * this class is the code that works with the fragment_renter_my_accepted_cars.xml Window
 * here the user can see the vehicles he rents
 */
public class RenterMyAcceptedCarsFragment extends Fragment {

    public boolean canAddCar; // if more or less than 5 cars already in rent time
    public ListView carsView;
    RenterFunctions rf;
    ImageView searchCar;

    public RenterMyAcceptedCarsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RenterMyAcceptedCarsFragment.
     */
    public static RenterMyAcceptedCarsFragment newInstance(String param1, String param2) {
        return new RenterMyAcceptedCarsFragment();
    }

    /**
     * only default at "onCreate" phase
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * only default at "onCreateView" phase
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rf = new RenterFunctions();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_my_accepted_cars, container, false);
    }

    /**
     * show list of rented cars
     * @param view - View object of the window (hold the objects of texts inputs that screened)
     * @param savedInstanceState -  last state of this fragment,should be null
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // create list view and search button for new cars
        carsView = view.findViewById(R.id.renter_my_accepted_cars_list_view);
        searchCar = view.findViewById(R.id.renter_my_accepted_cars_search);

        rf.initMyCars(userUtils.getUserID(), this);

        // search button
        searchCar.setOnClickListener(v -> {
            if(canAddCar) {
                FragmentManager fm = getParentFragmentManager();
                // search for new cars
                fm.beginTransaction().replace(R.id.main_fragment, RenterCarViewFragment.class, null)
                        .addToBackStack("RenterMyAcceptedCars")
                        .commit();
            }else {
                // can't rent more than 5 cars
                Toast.makeText(getActivity(), "Max 5 Cars", Toast.LENGTH_LONG).show();
            }
        });


    }

    /**
     * get all cars and presents them
     * @param cars - list of Car objects
     */
    public void show(List<Car> cars) {

        // If we have more then 5 cars, disable this option
        if(cars.size() >= 5) {
            canAddCar = false;
            searchCar.setImageResource(R.drawable.max5cars_button_renteracceptedcars);
        }
        else{
            searchCar.setImageResource(R.drawable.searchcar_button_renteracceptedcars);
            //other wise, enable it again
            canAddCar = true;
        }

        // show cars in our format
        ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),cars.toArray(new Car[0]));
        carsView.setAdapter(adapter);

        carsView.setOnItemClickListener((adapterView, view1, i, l) -> {
            RenterMyCarDetailsViewFragment fragment = RenterMyCarDetailsViewFragment.newInstance(cars.get(i).getDocumentId());
            // show details about car
            getParentFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, null)
                    .addToBackStack("RenterMyAcceptedCars")
                    .commit();
        });

    }


//        fs.collection("cars").whereEqualTo("renterID", userUtils.getUserID())
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> { // get cars with U_ID as RenterID
//                    List<Car> cars = queryDocumentSnapshots.toObjects(Car.class);
//                    Log.d("RENTER ACCEPTED CARS","SHOWING MY CARS" + cars.size());
//
//                    // If we have more then 5 cars, disable this option
//                    if(cars.size() >= 5) {
//                        canAddCar = false;
//                        searchCar.setText("max 5 cars");
//                    }
//                    else{
//                        //other wise, enable it again
//                        canAddCar = true;
//                    }
//                    // show cars in our format
//                    ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),cars.toArray(new Car[0]));
//                    carsView.setAdapter(adapter);
//
//                    carsView.setOnItemClickListener((adapterView, view1, i, l) -> {
//                        FragmentManager fm = getParentFragmentManager();
//                        RenterMyCarDetailsViewFragment fragment = RenterMyCarDetailsViewFragment.newInstance(cars.get(i).getDocumentId());
//                        // show details about car
//                        fm.beginTransaction().replace(R.id.main_fragment, fragment, null)
//                                .addToBackStack("RenterMyAcceptedCars")
//                                .commit();
//                    });
//                });

}