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
 *
 * this class is the code that works with the fragment_view_profile.xml Window
 * here the user can see his own details
 */
public class ViewProfileFragment extends Fragment {

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ViewProfileFragment.
     */
    public static ViewProfileFragment newInstance() {
        return new ViewProfileFragment();
    }

    /**
     * we not doing anything more than default at "onCreateView" phase
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * we not doing anything more than default at "onCreateView" phase
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    /**
     * initialize all fields and buttons for profile view window
     * @param viewProfileView - View object of the window (hold the objects of texts inputs that screened)
     * @param savedInstanceState -
     */
    @Override
    public void onViewCreated(@NonNull View viewProfileView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewProfileView, savedInstanceState);

        // init vars of the texts for the window
        TextView firstName = viewProfileView.findViewById(R.id.view_first_name);
        TextView lastName = viewProfileView.findViewById(R.id.view_last_name);
        TextView role = viewProfileView.findViewById(R.id.view_Role);
        TextView phoneNum = viewProfileView.findViewById(R.id.view_phone_number);
        TextView gender = viewProfileView.findViewById(R.id.view_gender);
        TextView birth = viewProfileView.findViewById(R.id.view_Birth);
        TextView city = viewProfileView.findViewById(R.id.view_city);
        TextView detailsOnUser = viewProfileView.findViewById(R.id.view_on_me);

        // init buttons for the window
        Button editProfileBtn = viewProfileView.findViewById(R.id.user_view_edit);

        String uid = userUtils.getUserID(); // current userID
        FirebaseFirestore fs = FirebaseFirestore.getInstance();

        // get from FS the data of the current user via its id (UNIQUE)
        fs.collection("users").whereEqualTo("id", uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> userList = queryDocumentSnapshots.toObjects(User.class); // return list of users
            if (userList.size() == 0){
                Log.e("ViewProfile","Where is my user? its connected to app but cant see its own details from db " + uid);
            }
            User user = userList.get(0);

            // init texts via user details
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
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            role.setText(userRole);
            gender.setText(userGender);
            phoneNum.setText(user.getPhoneNum());
            birth.setText(user.getBorn());
            city.setText(user.getCity());
            detailsOnUser.setText(user.getWritingOnMe());
        });

        // when cliecked on "edit" -> jump to new screen, which the user can edit his profile details
        // currentView == view from above
        editProfileBtn.setOnClickListener(currentView -> {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.main_fragment, EditViewProfileFragment.class, null)
                    .addToBackStack("ViewProfile")
                    .commit();
        });
    }

    /**
     * when u go back to this window, its "restored"
     * we want the details on the user a.k.a profile - will be updated via the current edits
     * @param savedInstanceState .
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d("Status","StateRestored");
        super.onViewStateRestored(savedInstanceState);
        View viewProfileView = getView();
        assert viewProfileView != null; // kinda not possible since we stand on that view?

        // init vars of the texts for the window
        TextView firstName = viewProfileView.findViewById(R.id.view_first_name);
        TextView lastName = viewProfileView.findViewById(R.id.view_last_name);
        TextView role = viewProfileView.findViewById(R.id.view_Role);
        TextView phoneNum = viewProfileView.findViewById(R.id.view_phone_number);
        TextView gender = viewProfileView.findViewById(R.id.view_gender);
        TextView birth = viewProfileView.findViewById(R.id.view_Birth);
        TextView city = viewProfileView.findViewById(R.id.view_city);
        TextView detailsOnUser = viewProfileView.findViewById(R.id.view_on_me);

        String uid = userUtils.getUserID(); // current userID
        FirebaseFirestore fs = FirebaseFirestore.getInstance();

        // get from FS the data of the current user via its id (UNIQUE)
        fs.collection("users").whereEqualTo("id", uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> userList = queryDocumentSnapshots.toObjects(User.class);
            if (userList.size() == 0){
                Log.e("ViewProfile","Where is my user? its connected to app but cant see its own details from db " + uid);
            }
            User user = userList.get(0);

            // init texts via user details
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
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            role.setText(userRole);
            gender.setText(userGender);
            phoneNum.setText(user.getPhoneNum());
            birth.setText(user.getBorn());
            city.setText(user.getCity());
            detailsOnUser.setText(user.getWritingOnMe());
        });
    }
}
