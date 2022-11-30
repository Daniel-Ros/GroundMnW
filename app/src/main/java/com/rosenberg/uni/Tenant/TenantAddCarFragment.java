package com.rosenberg.uni.Tenant;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.userUtils;

import java.util.Calendar;


public class TenantAddCarFragment extends Fragment {

    EditText datePicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_add_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] fuels = new String[]{"95", "disele"};
        String[] gearboxs = new String[]{"automatic", "manuel"};

        EditText make = view.findViewById(R.id.tenant_add_car_make);
        EditText model = view.findViewById(R.id.tenant_add_car_model);
        EditText mileage = view.findViewById(R.id.tenant_add_car_mileage);
        EditText numSeats = view.findViewById(R.id.tenant_add_car_num_of_seats);
        Spinner fuel = view.findViewById(R.id.tenant_add_car_fuel);
        Spinner gearbox = view.findViewById(R.id.tenant_add_car_gearbox);
        EditText start_date = view.findViewById(R.id.tenant_add_car_start_date);
        EditText end_date = view.findViewById(R.id.tenant_add_car_end_date);
        Button done = view.findViewById(R.id.tenant_add_car_done);


        ArrayAdapter<String> adapterMake = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,fuels);
        fuel.setAdapter(adapterMake);

        ArrayAdapter<String> adapterModel = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,gearboxs);
        gearbox.setAdapter(adapterModel);

        done.setOnClickListener(v -> {
            FirebaseFirestore fs = FirebaseFirestore.getInstance();
            String uid = userUtils.getUserID();
            Log.d("ADD CAR", "uid =" + uid);
            Car car = new Car(
                    make.getText().toString(),
                    model.getText().toString(),
                    mileage.getText().toString(),
                    numSeats.getText().toString(),
                    fuel.getSelectedItem().toString(),
                    gearbox.getSelectedItem().toString(),
                    start_date.getText().toString(),
                    end_date.getText().toString(),
                    uid);
            fs.collection("cars").add(car).addOnCompleteListener(task -> {
                FragmentManager fm = getParentFragmentManager();
                fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
            });
        });
    }
}