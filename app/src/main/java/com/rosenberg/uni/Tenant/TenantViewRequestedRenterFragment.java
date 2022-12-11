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
import com.rosenberg.uni.login.EditViewProfileFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TenantViewRequestedRenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantViewRequestedRenterFragment extends Fragment {

    private static final String USER_ID = "USRID";
    private static final String CAR_ID = "CARID";
    private String user_id;
    private String car_id;
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
        args.putString(USER_ID, userId);
        args.putString(CAR_ID, carId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getString(USER_ID);
            car_id = getArguments().getString(CAR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_view_requested_renter, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button acceptRenterBtn = view.findViewById(R.id.view_renterProfile_AcceptBtn);
        Button rejectRenterBtn = view.findViewById(R.id.view_renterProfile_RejectBtn);

        TextView firstName = view.findViewById(R.id.view_renterProfile_firstN);
        TextView lastName = view.findViewById(R.id.view_renterProfile_lastN);
        TextView role = view.findViewById(R.id.view_renterProfile_role);
        TextView phoneNum = view.findViewById(R.id.view_renterProfile_phoneNum);
        TextView gender = view.findViewById(R.id.view_renterProfile_gender);
        TextView birth = view.findViewById(R.id.view_renterProfile_born);
        TextView city = view.findViewById(R.id.view_renterProfile_city);
        TextView detailsOnUser = view.findViewById(R.id.view_renterProfile_onMe);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        FragmentManager fm = getParentFragmentManager();

        fs.collection("users").whereEqualTo("id", user_id).get()
                .addOnSuccessListener(queryDocumentSnapshots ->{
                    List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                    User user = userList.get(0);
                    if (userList.size() == 0){
                        Log.e("TenantViewRenter","Where is my user? its connected to app but cant see its own details from db " + user_id);
                    }
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    String userGender, userRole;
                    if (user.getTenant()){
                        userRole = "Tenant";
                    }else {
                        userRole = "Renter";
                    }
                    role.setText(userRole);
                    if (user.getGender()){
                        userGender = "Male";
                    }else{
                        userGender = "Female";
                    }
                    gender.setText(userGender);
                    phoneNum.setText(user.getPhoneNum());
                    birth.setText(user.getBorn());
                    city.setText(user.getCity());
                    detailsOnUser.setText(user.getWritingOnMe());

                    // if accepted renter, shall go back to cars list, this car "deal" is over
                    acceptRenterBtn.setOnClickListener(v -> {
                        fs.collection("cars")
                                .document(car_id).update("renterID", user.getId());

                        fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                    });
                });
        // if rejected, shall see more users - maybe would like to accept/reject others
        rejectRenterBtn.setOnClickListener(v -> {
            fs.collection("cars")
                    .document(car_id).update("renterID", null);

            fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewDetailsFragment.newInstance(car_id)).commit();
        });
    }
}