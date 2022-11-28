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

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.R;
import com.rosenberg.uni.Tenant.TenantAddCarFragment;
import com.rosenberg.uni.Tenant.TenantCarViewFragment;
import com.rosenberg.uni.utils.userUtils;

import java.util.ArrayList;
import java.util.List;


public class RenterCarViewFragment extends Fragment {

    private List<Car> cars;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_car_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView car_view = view.findViewById(R.id.renter_car_view_list_view);
        List<String> carStrings = new ArrayList<>();

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("cars")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cars = queryDocumentSnapshots.toObjects(Car.class);
                    for (int i = 0; i < cars.size(); i++) {
                            cars.get(i).setDocument_ID(queryDocumentSnapshots.getDocuments()
                                    .get(i)
                                    .getId());
                    }
                    Log.d("CAR_VIEW Renter","ADDING CARS" + cars.size());

                    ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),
                            cars.toArray(new Car[0]));
                    car_view.setAdapter(adapter);
                });

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,carStrings);
        car_view.setAdapter(adapter);


        car_view.setOnItemClickListener((adapterView, view1, i, l) -> {
            FragmentManager fm = getParentFragmentManager();

            Log.d("RenterCarView","sending to this id " + cars.get(i).getDocument_ID());
            fm.beginTransaction().replace(R.id.base_fragment,
                    RenterCarViewDetailsFragment.newInstance(cars.get(i).getDocument_ID()),
                    null)
                    .addToBackStack("RenterCarView")
                    .commit();
        });
    }
}