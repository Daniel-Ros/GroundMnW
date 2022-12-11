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

    private static final String CAR_DATA = null;
    private String carDocId;

    public TenantEditCarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param carData car id
     * @return A new instance of fragment TenantEditCarFragment.
     */
    public static TenantEditCarFragment newInstance(String carData) {
        TenantEditCarFragment fragment = new TenantEditCarFragment();
        Bundle args = new Bundle();
        args.putString(CAR_DATA, carData); // just save at CAR_DATA the data of parm1
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * do default onCreate with initializing carDocId
     * @param savedInstanceState last state of this fragment,should be null
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            carDocId = getArguments().getString(CAR_DATA);
        }
    }


    /**
     * we not doing anything more than default at "onCreateView" phase
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_edit_car, container, false);
    }

    /**
     * Called when fragment is inflated,
     * init all texts and buttons for cur window
     * @param view - this view object
     * @param savedInstanceState .
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init options
        String[] fuels = new String[]{"95", "disele"};
        String[] gearboxs = new String[]{"automatic", "manuel"};

        // init vars of the texts window
        EditText make = view.findViewById(R.id.tenant_edit_car_make);
        EditText model = view.findViewById(R.id.tenant_edit_car_model);
        EditText mileage = view.findViewById(R.id.tenant_edit_car_mileage);
        EditText numSeats = view.findViewById(R.id.tenant_edit_car_num_of_seats);
        Spinner fuel = view.findViewById(R.id.tenant_edit_car_fuel);
        Spinner gearbox = view.findViewById(R.id.tenant_edit_car_gearbox);
        EditText start_date = view.findViewById(R.id.tenant_edit_car_start_date);
        EditText end_date = view.findViewById(R.id.tenant_edit_car_end_date);
        EditText price = view.findViewById(R.id.tenant_edit_car_price);

        // init buttons for window
        Button doneBtn = view.findViewById(R.id.tenant_edit_car_done);

        // hold all make and model values in one array
        ArrayAdapter<String> adapterMake = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,fuels);
        fuel.setAdapter(adapterMake);

        ArrayAdapter<String> adapterModel = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,gearboxs);
        gearbox.setAdapter(adapterModel);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("cars")
                .document(carDocId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Car currCar = queryDocumentSnapshots.toObject(Car.class);

                    // get curr car data from the database and show it to user
                    // (this way user shall edit only the relevant and not type all over again)
                    make.setText(currCar.getMake());
                    model.setText(currCar.getModel());
                    mileage.setText(currCar.getMileage().toString());
                    numSeats.setText(currCar.getNumOfSeats().toString());
                    fuel.setSelection(currCar.getFuel() == "95"? 0 : 1);
                    gearbox.setSelection(currCar.getGearbox() == "automatic"? 0 : 1);
                    start_date.setText(currCar.getStartDate());
                    end_date.setText(currCar.getEndDate());
                    price.setText(currCar.getPrice());

                    doneBtn.setOnClickListener(v -> {
                        String uid = userUtils.getUserID();
                        Log.d("ADD CAR", "uid =" + uid);

                        // save on Car obj all the input data from user
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

                        // put the car data at the same car obj at fs (via carDocId)
                        fs.collection("cars").document(carDocId).set(new_car).addOnCompleteListener(task -> {
                            FragmentManager fm = getParentFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                        });
                    });
                })
                .addOnFailureListener( fail -> {
                    Log.d("RenterCarViewDetails","problme loading car info" + carDocId);
                });
    }
}