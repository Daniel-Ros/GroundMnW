package com.rosenberg.uni.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.WithLifecycleStateKt;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.userUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewProfileFragment newInstance(String param1, String param2) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button editProfileBtn = view.findViewById(R.id.user_view_edit);

        TextView firstName = view.findViewById(R.id.view_first_name);
        TextView lastName = view.findViewById(R.id.view_last_name);
        TextView role = view.findViewById(R.id.view_Role);
        TextView phoneNum = view.findViewById(R.id.view_phone_number);
        TextView gender = view.findViewById(R.id.view_gender);
        TextView birth = view.findViewById(R.id.view_Birth);
        TextView city = view.findViewById(R.id.view_city);
        TextView detailsOnUser = view.findViewById(R.id.view_on_me);

        String uid = userUtils.getUserID();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();


        fs.collection("users").whereEqualTo("id", uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> userList = queryDocumentSnapshots.toObjects(User.class);
            if (userList.size() == 0){
                Log.e("ViewProfile","Where is my user? its connected to app but cant see its own details from db " + uid);
            }
            User user = userList.get(0);
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
        });

        // when cliecket on "edit" -> jump to new screen, which the user can edit his profile details
        editProfileBtn.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.main_fragment, EditViewProfileFragment.class, null).commit();
        });
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        View view = getView();
        Lifecycle.Event onResume = Lifecycle.Event.ON_RESUME;
        assert view != null;
        TextView firstName = view.findViewById(R.id.view_first_name);
        TextView lastName = view.findViewById(R.id.view_last_name);
        TextView role = view.findViewById(R.id.view_Role);
        TextView phoneNum = view.findViewById(R.id.view_phone_number);
        TextView gender = view.findViewById(R.id.view_gender);
        TextView birth = view.findViewById(R.id.view_Birth);
        TextView city = view.findViewById(R.id.view_city);
        TextView detailsOnUser = view.findViewById(R.id.view_on_me);

        String uid = userUtils.getUserID();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();


        fs.collection("users").whereEqualTo("id", uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> userList = queryDocumentSnapshots.toObjects(User.class);
            if (userList.size() == 0){
                Log.e("ViewProfile","Where is my user? its connected to app but cant see its own details from db " + uid);
            }
            User user = userList.get(0);
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
        });
    }
}
