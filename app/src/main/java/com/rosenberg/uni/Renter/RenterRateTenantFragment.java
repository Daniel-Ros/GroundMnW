package com.rosenberg.uni.Renter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.rosenberg.uni.Models.RenterFunctions;
import com.rosenberg.uni.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RenterRateTenantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RenterRateTenantFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "CARID";
    private String carId;

    RenterFunctions rf;

    public RenterRateTenantFragment() {
        // Required empty public constructor
        rf = new RenterFunctions();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Paramet
     * @return A new instance of fragment RenterRateTenant.
     */
    public static RenterRateTenantFragment newInstance(String param1) {
        RenterRateTenantFragment fragment = new RenterRateTenantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            carId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_rate_tenant, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText comment = view.findViewById(R.id.renter_rate_comment);
        RatingBar rate = view.findViewById(R.id.renter_rate_rate);
        Button done = view.findViewById(R.id.renter_rate_done);

        done.setOnClickListener(view1 -> {
            rf.rateCar(carId,comment.getText().toString(),rate.getRating());
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.main_fragment,RenterCarViewFragment.class,null).commit();
        });
    }
}