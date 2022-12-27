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

import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Models.TenantFunctions;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.userUtils;

/**
 * this class presents the window that provide to tenant user the option to add cars for renting
 */
public class TenantAddCarFragment extends Fragment {

    TenantFunctions tf;

    EditText make;
    EditText model;
    EditText mileage;
    EditText numSeats;
    Spinner fuel;
    Spinner gearbox;
    EditText start_date;
    EditText end_date;
    EditText price;

    /**
     * we not doing anything more than default at "onCreateView" phase
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tf = new TenantFunctions();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_add_car, container, false);
    }

    /**
     * Called when fragment is inflated,
     * init all texts and buttons for curr window
     * @param view - this view object
     * @param savedInstanceState last state of this fragment,should be null
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init options
        String[] fuels = new String[]{"95", "diesel"};
        String[] gearboxes = new String[]{"automatic", "manual"};

        // init vars of the texts window
        make = view.findViewById(R.id.tenant_add_car_make);
        model = view.findViewById(R.id.tenant_add_car_model);
        mileage = view.findViewById(R.id.tenant_add_car_mileage);
        numSeats = view.findViewById(R.id.tenant_add_car_num_of_seats);
        fuel = view.findViewById(R.id.tenant_add_car_fuel);
        gearbox = view.findViewById(R.id.tenant_add_car_gearbox);
        start_date = view.findViewById(R.id.tenant_add_car_start_date);
        end_date = view.findViewById(R.id.tenant_add_car_end_date);
        price = view.findViewById(R.id.tenant_edit_car_price);

        // init buttons for window
        Button doneBtn = view.findViewById(R.id.tenant_add_car_done);

        // hold all make and model values in one array
        ArrayAdapter<String> adapterMake = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,fuels);
        fuel.setAdapter(adapterMake);

        ArrayAdapter<String> adapterModel = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,gearboxes);
        gearbox.setAdapter(adapterModel);

        doneBtn.setOnClickListener(v -> {

            String uid = userUtils.getUserID();
            Log.d("ADD CAR", "uid =" + uid);

            // set input value as car object
            Car car = new Car(
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

            System.out.println(car.getModel());

            // push to database
            tf.pushCar(car, this);

        });
    }

    /**
     * shall transact to another window
     * car added
     */
    public void carPushSucceed() {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
    }

}

//    FirebaseFirestore fs = FirebaseFirestore.getInstance();
//// add car to database
//            fs.collection("cars").add(car).addOnCompleteListener(task -> {
//                    FragmentManager fm = getParentFragmentManager();
//                    fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
//        });