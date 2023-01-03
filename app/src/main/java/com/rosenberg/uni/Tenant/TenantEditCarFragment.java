package com.rosenberg.uni.Tenant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.Models.TenantFunctions;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.CustomVolleyRequestQueue;
import com.rosenberg.uni.utils.userUtils;

import java.io.IOException;
import java.util.Objects;
//aa
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TenantEditCarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantEditCarFragment extends Fragment {

    private static final String CAR_DATA = "CAR_DATA";
    private String carDocId;

    TenantFunctions tf;

    // init vars of the texts window
    EditText make;
    EditText model;
    EditText mileage;
    EditText numSeats;
    Spinner fuel;
    Spinner gearbox;
    EditText start_date;
    EditText end_date;
    EditText price;

    ImageView doneBtn;
    NetworkImageView carImage;
    ImageView editPicBtn;
    ImageLoader imageLoader;
    Bitmap bitmap;

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
        tf = new TenantFunctions();
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
        make = view.findViewById(R.id.tenant_edit_car_make);
        model = view.findViewById(R.id.tenant_edit_car_model);
        mileage = view.findViewById(R.id.tenant_edit_car_mileage);
        numSeats = view.findViewById(R.id.tenant_edit_car_num_of_seats);
        fuel = view.findViewById(R.id.tenant_edit_car_fuel);
        gearbox = view.findViewById(R.id.tenant_edit_car_gearbox);
        start_date = view.findViewById(R.id.tenant_edit_car_start_date);
        end_date = view.findViewById(R.id.tenant_edit_car_end_date);
        price = view.findViewById(R.id.tenant_edit_car_price);

        // init buttons for window
        doneBtn = view.findViewById(R.id.tenant_edit_car_done);
        carImage = view.findViewById(R.id.tenant_edit_car_image); // the image itself
        editPicBtn = view.findViewById(R.id.tenant__edit_car_change_car);

        // hold all make and model values in one array
        ArrayAdapter<String> adapterMake = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,fuels);
        fuel.setAdapter(adapterMake);

        ArrayAdapter<String> adapterModel = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,gearboxs);
        gearbox.setAdapter(adapterModel);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    carImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        editPicBtn.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });

        // data on car from fs
        tf.getSpecificCar(carDocId, this);

        // user ended to edit the car data
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

            tf.uploadMyCarData(carDocId, new_car, bitmap, this);
        });
    }

    /**
     * show car details
     * @param currCar obj
     */
    public void show(Car currCar){
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
        price.setText(currCar.getPrice().toString());
        tf.setCarImage(imageLoader, carImage, currCar, getActivity());


    }

    /**
     * move to next window
     */
    public void uploadSucceed() {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
    }

    /**
     * user tried to add car without image
     * we wont allow that
     */
    public void wontPushWithoutImage() {
        Toast.makeText(getActivity(),"cannot edit car without an Image", Toast.LENGTH_LONG).show();
    }
}


//        FirebaseFirestore fs = FirebaseFirestore.getInstance();
//                fs.collection("cars")
//                .document(carDocId)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                Car currCar = queryDocumentSnapshots.toObject(Car.class);
//
//        // get curr car data from the database and show it to user
//        // (this way user shall edit only the relevant and not type all over again)
//        make.setText(currCar.getMake());
//        model.setText(currCar.getModel());
//        mileage.setText(currCar.getMileage().toString());
//        numSeats.setText(currCar.getNumOfSeats().toString());
//        fuel.setSelection(currCar.getFuel() == "95"? 0 : 1);
//        gearbox.setSelection(currCar.getGearbox() == "automatic"? 0 : 1);
//        start_date.setText(currCar.getStartDate());
//        end_date.setText(currCar.getEndDate());
//        price.setText(currCar.getPrice());


//// put the car data at the same car obj at fs (via carDocId)
//                        fs.collection("cars").document(carDocId).set(new_car).addOnCompleteListener(task -> {
//                                FragmentManager fm = getParentFragmentManager();
//                                fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
//        });
//        });
//        })
//        .addOnFailureListener( fail -> {
//        Log.d("RenterCarViewDetails","problme loading car info" + carDocId);
//        });