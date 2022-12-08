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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TenantEditCarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantEditCarFragment extends Fragment {

    private static final String ARG_NAME = "CARID";
    private String car_id;

    public TenantEditCarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 car id
     * @return A new instance of fragment TenantEditCarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TenantEditCarFragment newInstance(String param1) {
        TenantEditCarFragment fragment = new TenantEditCarFragment();
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
        return inflater.inflate(R.layout.fragment_tenant_edit_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] fuels = new String[]{"95", "disele"};
        String[] gearboxs = new String[]{"automatic", "manuel"};

        EditText make = view.findViewById(R.id.tenant_edit_car_make);
        EditText model = view.findViewById(R.id.tenant_edit_car_model);
        EditText mileage = view.findViewById(R.id.tenant_edit_car_mileage);
        EditText numSeats = view.findViewById(R.id.tenant_edit_car_num_of_seats);
        Spinner fuel = view.findViewById(R.id.tenant_edit_car_fuel);
        Spinner gearbox = view.findViewById(R.id.tenant_edit_car_gearbox);
        EditText start_date = view.findViewById(R.id.tenant_edit_car_start_date);
        EditText end_date = view.findViewById(R.id.tenant_edit_car_end_date);
        EditText price = view.findViewById(R.id.tenant_edit_car_price);

        Button done = view.findViewById(R.id.tenant_edit_car_done);

        ArrayAdapter<String> adapterMake = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,fuels);
        fuel.setAdapter(adapterMake);

        ArrayAdapter<String> adapterModel = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,gearboxs);
        gearbox.setAdapter(adapterModel);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("cars")
                .document(car_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Car car = queryDocumentSnapshots.toObject(Car.class);

                    make.setText(car.getMake());
                    model.setText(car.getModel());
                    mileage.setText(car.getMileage().toString());
                    numSeats.setText(car.getNumOfSeats().toString());
                    fuel.setSelection(car.getFuel() == "95"? 0 : 1);
                    gearbox.setSelection(car.getGearbox() == "automatic"? 0 : 1);
                    start_date.setText(car.getStartDate());
                    end_date.setText(car.getEndDate());
                    price.setText(car.getPrice());

                    done.setOnClickListener(v -> {
                        String uid = userUtils.getUserID();
                        Log.d("ADD CAR", "uid =" + uid);
                        Car new_car = new Car(
                                make.getText().toString(),
                                model.getText().toString(),
                                mileage.getText().toString(),
                                numSeats.getText().toString(),
                                fuel.getSelectedItem().toString(),
                                gearbox.getSelectedItem().toString(),
                                start_date.getText().toString(),
                                end_date.getText().toString(),
                                Integer.parseInt(price.getText().toString()),
                                uid);
                        fs.collection("cars").document(car_id).set(new_car).addOnCompleteListener(task -> {
                            FragmentManager fm = getParentFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                        });
                    });
                })
                .addOnFailureListener( fail -> {
                    Log.d("RenterCarViewDetails","problme loading car info" + car_id);
                });
    }
}