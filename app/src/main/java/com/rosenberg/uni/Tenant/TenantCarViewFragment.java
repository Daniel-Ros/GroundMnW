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

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemCarViewAdapter;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.userUtils;

import java.util.ArrayList;
import java.util.List;

public class TenantCarViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_car_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView car_view = view.findViewById(R.id.renter_car_view_list_view);
        Button add_car = view.findViewById(R.id.tenant_car_view_add_car);
        List<String> carStrings = new ArrayList<>();

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("cars").whereEqualTo("ownerID",userUtils.getUserID())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Car> cars = queryDocumentSnapshots.toObjects(Car.class);
                    Log.d("CAR_VIEW","ADDING CARS" + cars.size());

                    ArrayAdapter adapter = new ListItemCarViewAdapter(getActivity(),cars.toArray(new Car[0]));
                    car_view.setAdapter(adapter);
                });

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,carStrings);
        car_view.setAdapter(adapter);

        add_car.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.base_fragment, TenantAddCarFragment.class, null).commit();
        });
    }
}