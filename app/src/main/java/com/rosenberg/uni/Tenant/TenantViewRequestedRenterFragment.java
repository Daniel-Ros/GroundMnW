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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Adapters.ListItemHistoryViewAdapter;
import com.rosenberg.uni.Adapters.ListItemReviewViewAdapter;
import com.rosenberg.uni.Entities.Review;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Models.TenantFunctions;
import com.rosenberg.uni.R;

import java.util.List;

/**
 * this fragment goal is to show the TENANT
 *      that clicked on specific renter entry - the profile of the RENTER
 *      also, provide to TENANT option to accept or reject the request from RENTER
 */
public class TenantViewRequestedRenterFragment extends Fragment {

    private static final String USER_DATA = "USER_DATA";
    private static final String CAR_DATA = "CAR_DATA";

    // both will be used for get the curr car we talk about
    // and also the renter docId
    private String userDocId;
    private String carDocId;

    TenantFunctions tf;

    TextView firstName;
    TextView lastName;
    TextView role;
    TextView phoneNum;
    TextView gender;
    TextView birth;
    TextView city;
    TextView detailsOnUser;
    ListView reviews;


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
        args.putString(USER_DATA, userId);
        args.putString(CAR_DATA, carId);
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
            userDocId = getArguments().getString(USER_DATA);
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
        firstName = view.findViewById(R.id.view_renterProfile_firstN);
        lastName = view.findViewById(R.id.view_renterProfile_lastN);
        role = view.findViewById(R.id.view_renterProfile_role);
        phoneNum = view.findViewById(R.id.view_renterProfile_phoneNum);
        gender = view.findViewById(R.id.view_renterProfile_gender);
        birth = view.findViewById(R.id.view_renterProfile_born);
        city = view.findViewById(R.id.view_renterProfile_city);
        detailsOnUser = view.findViewById(R.id.view_renterProfile_onMe);
        reviews = view.findViewById(R.id.view_renter_reviews);

        // init buttons for window
        ImageView acceptRenterBtn = view.findViewById(R.id.view_renterProfile_AcceptBtn);
        ImageView rejectRenterBtn = view.findViewById(R.id.view_renterProfile_RejectBtn);

        // get the renter user data for show it to curr user (tenant)
        tf.getSpecificUser(userDocId, this);

        // if accepted renter, shall go back to cars list, this car "deal" is over
        acceptRenterBtn.setOnClickListener(v -> {
            tf.acceptRenter(carDocId, userDocId, this);
        });

        // if rejected, shall see more users - maybe would like to accept/reject others
        rejectRenterBtn.setOnClickListener(v -> {
            tf.rejectRenter(carDocId, null, this);
        });
    }

    /**
     * present all user obj details on view
     * @param user obj
     */
    public void show(User user) {
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

        List<Review> reviewsList = user.getReviews();
        if(reviews != null){
            ArrayAdapter adapter = new ListItemReviewViewAdapter(getActivity(),reviewsList.toArray(new Review[0]));
            reviews.setAdapter(adapter);
        }
    }

    /**
     * renter accepted
     * transact to next window
     */
    public void acceptRenterSucceed() {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
    }

    /**
     * renter rejected
     * transact to next window
     */
    public void rejectRentedSucceed() {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewDetailsFragment.newInstance(carDocId)).commit();
    }
}


//    FirebaseFirestore fs = FirebaseFirestore.getInstance();
//    FragmentManager fm = getParentFragmentManager();

//// get the renter user data and show it to curr user (tenant)
//        fs.collection("users").whereEqualTo("id", userDocId).get()
//                .addOnSuccessListener(queryDocumentSnapshots ->{
//                List<User> userList = queryDocumentSnapshots.toObjects(User.class); // returns list of items
//        User user = userList.get(0);
//        if (userList.size() == 0){
//        Log.e("TenantViewRenter",
//        "Where is my user? its connected to app but cant see its own details from db " + userDocId);
//        }
//
//        // get gender, role
//        String userGender, userRole;
//        if (user.getTenant()){
//        userRole = "Tenant";
//        }else {
//        userRole = "Renter";
//        }
//        if (user.getGender()){
//        userGender = "Male";
//        }else{
//        userGender = "Female";
//        }
//
//        // init text boxes
//        firstName.setText(user.getFirstName());
//        lastName.setText(user.getLastName());
//        role.setText(userRole);
//        gender.setText(userGender);
//        phoneNum.setText(user.getPhoneNum());
//        birth.setText(user.getBorn());
//        city.setText(user.getCity());
//        detailsOnUser.setText(user.getWritingOnMe());

//                        fs.collection("cars")
//                                .document(carDocId).update("renterID", user.getId());
//
//                                fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();

//            fs.collection("cars")
//                    .document(carDocId).update("renterID", null);
//
//                    fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewDetailsFragment.newInstance(carDocId)).commit();