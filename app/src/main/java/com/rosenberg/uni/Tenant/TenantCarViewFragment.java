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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Models.TenantFunctions;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.userUtils;

import java.util.List;

/**
 *  ------------------   HOME of tenant -----------------------
 * this class represents the Home window of the tenant
 * the window shows all the current cars that this tenant offer for renting
 * also the user can add more car offers, but not more than 5 cars in parallel!
 */
public class TenantCarViewFragment extends Fragment {

    private boolean canAddCar = false; // already more than 5 ongoing offers
    TenantFunctions tf;
    ListView car_view;
    Button add_car;
    /**
     * we not doing anything more than default at "onCreateView" phase
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tf = new TenantFunctions();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_car_view, container, false);
    }

    /**
    * Called when fragment is inflated,
    * init all texts and buttons for login window
    * @param view - View object of the window (hold the objects of texts inputs that screened)
    * @param savedInstanceState -  last state of this fragment,should be null
    */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init car row entries
        car_view = view.findViewById(R.id.renter_car_view_list_view);
        // init add_car button
        add_car = view.findViewById(R.id.tenant_car_view_add_car);

        // get all cars of current user, we would like to represent this entries on screen!
        tf.getAllCarsOfOwner(userUtils.getUserID(), this);

        add_car.setOnClickListener(v -> {
            // check if possible to add car
            // canAddCar == true iff (<=>) user have 4 or less ongoing cars entry
            if(canAddCar) {
                this.addCarClicked();
            }else{ // just msg him failed
                Toast.makeText(getActivity().getApplicationContext(),"already 5 ongoing car entries",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * capacity is lower than 5, tenant can still add more cars
     */
    private void addCarClicked() {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.main_fragment, TenantAddCarFragment.class, null)
                .addToBackStack("TenantCarView")
                .commit();
    }

    /**
     * show every car from cars on screen as entery
     * also setText of button add_car (max cap = 5)
     * @param cars list
     */
    public void show(List<Car> cars) {

        ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),cars.toArray(new Car[0]));
        car_view.setAdapter(adapter);

        car_view.setOnItemClickListener((adapterView, view1, i, l) -> {
            FragmentManager fm = getParentFragmentManager();
            TenantCarViewDetailsFragment fragment = TenantCarViewDetailsFragment.newInstance(cars.get(i).getDocumentId());
            fm.beginTransaction().replace(R.id.main_fragment, fragment, null)
                    .addToBackStack("TenantCarView")
                    .commit();
        });
        // If we have more then 5 cars, disable this option
        if(cars.size() >= 5) {
            canAddCar = false;
            add_car.setText("max 5 cars");
        }
        else{
            //other wise, enable it again
            canAddCar = true;
        }
    }
}


//    FirebaseFirestore fs = FirebaseFirestore.getInstance();

//// get all cars of current user, we would like to represent this entries on screen!
//        fs.collection("cars").whereEqualTo("ownerID",userUtils.getUserID())
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                List<Car> cars = queryDocumentSnapshots.toObjects(Car.class);
//        Log.d("CAR_VIEW","ADDING CARS" + cars.size());
//
//
//        // TODO DANIEL WRITE HEREEEEEEEEEEEEEEEEEEEEE COMMENTSSSSSSSSSSSSSSSSSSSS
//        ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),cars.toArray(new Car[0]));
//        car_view.setAdapter(adapter);
//
//        // TODO MAGIC LINEEEEEEEEEEEEEEEEEEEEEEEEEe --------
//        car_view.setOnItemClickListener((adapterView, view1, i, l) -> {
//        FragmentManager fm = getParentFragmentManager();
//        TenantCarViewDetailsFragment fragment = TenantCarViewDetailsFragment.newInstance(cars.get(i).getDocumentId());
//        fm.beginTransaction().replace(R.id.main_fragment, fragment, null)
//        .addToBackStack("TenantCarView")
//        .commit();
//        });
//        // If we have more then 5 cars, disable this option
//        if(cars.size() >= 5) {
//        canAddCar = false;
//        add_car.setText("max 5 cars");
//        }
//        else{
//        //other wise, enable it again
//        canAddCar = true;
//        }
//        });