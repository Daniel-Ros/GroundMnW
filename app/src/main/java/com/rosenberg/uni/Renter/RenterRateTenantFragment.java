package com.rosenberg.uni.Renter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.rosenberg.uni.Models.RenterFunctions;
import com.rosenberg.uni.R;

/**
 * this class is window that the renter RATES the TENANT that gave him his car
 */
public class RenterRateTenantFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "CARID";
    private String carDocId;

    RenterFunctions rf; // the model

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

    /**
     * do default onCreate with initializing carDocId
     * @param savedInstanceState last state of this fragment,should be null
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            carDocId = getArguments().getString(ARG_PARAM1);
        }
    }

    /**
     * we not doing anything more than default at "onCreateView" phase
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_rate_tenant, container, false);
    }

    /**
     * Called when fragment is inflated,
     * init all texts and buttons for curr window
     * @param view - curr view
     * @param savedInstanceState - last state of this fragment,should be null
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText comment = view.findViewById(R.id.renter_rate_comment);
        RatingBar rate = view.findViewById(R.id.renter_rate_rate);
        ImageView done = view.findViewById(R.id.renter_rate_done);

        done.setOnClickListener(view1 -> { // done rating
            rf.rateCar(carDocId,comment.getText().toString(),rate.getRating());
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.main_fragment,RenterCarViewFragment.class,null).commit();
        });
    }
}