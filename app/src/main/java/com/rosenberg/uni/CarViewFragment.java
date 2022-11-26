package com.rosenberg.uni;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.Car;
import com.rosenberg.uni.utils.userUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button add_car = view.findViewById(R.id.car_view_add_car);


        add_car.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.base_fragment, AddCarFragment.class, null).commit();
        });
    }
}