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
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.userUtils;


public class TenantAddCarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_add_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String []  makes = new String[]{"audi", "toyota"};
        String []  models = new String[]{"a5", "yaris"};

        Spinner make  = view.findViewById(R.id.tenant_add_car_make);
        Spinner model  = view.findViewById(R.id.tenant_add_car_model);
        EditText mileage = view.findViewById(R.id.tenant_add_car_mileage);
        Button done = view.findViewById(R.id.tenant_add_car_done);

        ArrayAdapter<String> adapterMake = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,makes);
        make.setAdapter(adapterMake);

        ArrayAdapter<String> adapterModel = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,models);
        model.setAdapter(adapterModel);

        done.setOnClickListener(v -> {
            FirebaseFirestore fs = FirebaseFirestore.getInstance();
            String uid = userUtils.getUserID();
            Log.d("ADD CAR","uid =" + uid);
            Car car = new Car(make.getSelectedItem().toString(),
                              model.getSelectedItem().toString(),
                              mileage.getText().toString(),
                              uid);
            fs.collection("cars").add(car.getMap()).addOnCompleteListener(task -> {
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().replace(R.id.base_fragment, TenantCarViewFragment.class,null).commit();
            });
        });

    }
}