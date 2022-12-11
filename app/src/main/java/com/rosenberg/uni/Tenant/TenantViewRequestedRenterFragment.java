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
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;

import java.util.List;

/**
 * this fragment goal is to show the TENANT
 *      that clicked on specific renter entry - the profile of the RENTER
 *      also, provide to TENANT option to accept or reject the request from RENTER
 */
public class TenantViewRequestedRenterFragment extends Fragment {

    private static final String USER_ID = null;
    private static final String CAR_ID = null;

    // both will be used for get the curr car we talk about
    // and also the renter docId
    private String userDocId;
    private String carDocId;

    public TenantViewRequestedRenterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId - the id of user - RENTER
     * @param carId - the id of car that renter would like to request
     * @return A new instance of fragment TenantViewRequestedRenterFragment.
     */
    public static TenantViewRequestedRenterFragment newInstance(String userId, String carId) {
        TenantViewRequestedRenterFragment fragment = new TenantViewRequestedRenterFragment();
        Bundle args = new Bundle();
        // set the ids in args
        args.putString(USER_ID, userId);
        args.putString(CAR_ID, carId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * do default onCreate
     * also, extract from this obj arguments, the documentsId
     * @param savedInstanceState last state of this fragment,should be null
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userDocId = getArguments().getString(USER_ID);
            carDocId = getArguments().getString(CAR_ID);
        }
    }

    /**
     * we not doing anything more than default at "onCreateView" phase
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_view_requested_renter, container, false);
    }

    /**
     * Called when fragment is inflated,
     * init all texts and buttons for curr window
     * @param view - curr view
     * @param savedInstanceState - last state of this fragment,should be null
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init vars of the texts window
        TextView firstName = view.findViewById(R.id.view_renterProfile_firstN);
        TextView lastName = view.findViewById(R.id.view_renterProfile_lastN);
        TextView role = view.findViewById(R.id.view_renterProfile_role);
        TextView phoneNum = view.findViewById(R.id.view_renterProfile_phoneNum);
        TextView gender = view.findViewById(R.id.view_renterProfile_gender);
        TextView birth = view.findViewById(R.id.view_renterProfile_born);
        TextView city = view.findViewById(R.id.view_renterProfile_city);
        TextView detailsOnUser = view.findViewById(R.id.view_renterProfile_onMe);

        // init buttons for window
        Button acceptRenterBtn = view.findViewById(R.id.view_renterProfile_AcceptBtn);
        Button rejectRenterBtn = view.findViewById(R.id.view_renterProfile_RejectBtn);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        FragmentManager fm = getParentFragmentManager();

        // get the renter user data and show it to curr user (tenant)
        fs.collection("users").whereEqualTo("id", userDocId).get()
                .addOnSuccessListener(queryDocumentSnapshots ->{
                    List<User> userList = queryDocumentSnapshots.toObjects(User.class); // returns list of items
                    User user = userList.get(0);
                    if (userList.size() == 0){
                        Log.e("TenantViewRenter",
                                "Where is my user? its connected to app but cant see its own details from db " + userDocId);
                    }

                    // get gender, role
                    String userGender, userRole;
                    if (user.getTenant()){
                        userRole = "Tenant";
                    }else {
                        userRole = "Renter";
                    }
                    if (user.getGender()){
                        userGender = "Male";
                    }else{
                        userGender = "Female";
                    }

                    // init text boxes
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    role.setText(userRole);
                    gender.setText(userGender);
                    phoneNum.setText(user.getPhoneNum());
                    birth.setText(user.getBorn());
                    city.setText(user.getCity());
                    detailsOnUser.setText(user.getWritingOnMe());

                    // if accepted renter, shall go back to cars list, this car "deal" is over
                    acceptRenterBtn.setOnClickListener(v -> {
                        fs.collection("cars")
                                .document(carDocId).update("renterID", user.getId());

                        fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                    });
                });
        // if rejected, shall see more users - maybe would like to accept/reject others
        rejectRenterBtn.setOnClickListener(v -> {
            fs.collection("cars")
                    .document(carDocId).update("renterID", null);

            fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewDetailsFragment.newInstance(carDocId)).commit();
        });
    }
}