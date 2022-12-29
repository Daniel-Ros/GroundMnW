package com.rosenberg.uni.Login;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Models.LoginFunctions;
import com.rosenberg.uni.R;
import com.rosenberg.uni.utils.userUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * this class is the code that works with the fragment_edit_profile.xml Window
 * here the user can edit his own profile details
 */
public class EditViewProfileFragment extends Fragment {

    LoginFunctions lf;
    EditText firstName;
    EditText lastName;
    EditText born;
    EditText city;
    EditText phoneNumber;
    Spinner spinnerGender;
    EditText detailsOnUser;

    public EditViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EditViewProfileFragment.
     */
    public static EditViewProfileFragment newInstance() {
        return new EditViewProfileFragment();
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
        lf = new LoginFunctions();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_view_profile, container, false);
    }

    /**
     * initialize all fields and buttons for edit_view_profile window
     * @param view - view object
     * @param savedInstanceState .
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init vars of the texts for the window
        // edit text - user is writing here a text
        firstName = view.findViewById(R.id.edit_first_name);
        lastName = view.findViewById(R.id.edit_last_name);
        born = view.findViewById(R.id.edit_birth);
        city = view.findViewById(R.id.edit_city);
        phoneNumber = view.findViewById((R.id.edit_phone_num));
        spinnerGender = view.findViewById(R.id.edit_gender);
        detailsOnUser = view.findViewById(R.id.edit_on_me);

        // Init the spinner of gender
        String [] choicesGenders = new String[]{"Male","Female"};
        ArrayAdapter<String> adapterGenders = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,choicesGenders);
        spinnerGender.setAdapter(adapterGenders);

        // init buttons for the window
        ImageView confirmBtn = view.findViewById(R.id.confirm_button);

        // get curr details on users
        String uid = userUtils.getUserID(); // current userID
        lf.getUserDetails(uid, this);


        // user want to save his edited data
        // currentView == view from above
        confirmBtn.setOnClickListener(currentView -> {

            lf.confirmEditPressed(userUtils.getUserID(), firstName.getText().toString(), lastName.getText().toString(),
                    phoneNumber.getText().toString(), born.getText().toString(),
                    city.getText().toString(), detailsOnUser.getText().toString(), this);

        });
    }

    /**
     * gets user obj and screen its details
     * @param user obj
     */
    public void show(User user){
        // initialize the data in the fields of the EDITTEXT
        // as the current data of the users
        // this way the user dont have to write over again all his details but only EDIT
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        phoneNumber.setText(user.getPhoneNum());
        spinnerGender.setEnabled(user.getGender());
        born.setText(user.getBorn());
        city.setText(user.getCity());
        detailsOnUser.setText(user.getWritingOnMe());
    }

    /**
     * end edit go back to viewProfile
     */
    public void successfullEdit() {
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().replace(R.id.main_fragment, ViewProfileFragment.class, null).commit();
    }

    /**
     * user shall know that something is wrong
     * app is stuck, or anything else
     */
    public void failureEdit() {
        Toast.makeText(getActivity(),"cannot Edit :-(", Toast.LENGTH_LONG).show();
    }
}

//        FirebaseFirestore fs = FirebaseFirestore.getInstance();
//
//        // initialize the data in the fields of the EDITTEXT
//        // as the current data of the users
//        // this way the user dont have to write over again all his details but only EDIT
//        fs.collection("users").whereEqualTo("id", uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
//            List<User> userList = queryDocumentSnapshots.toObjects(User.class);
//            if (userList.size() == 0){
//                Log.e("EditProfile",
//                        "Where is my user? its connected to app but cant see its own details from db " + uid);
//            }
//            User user = userList.get(0);
//            firstName.setText(user.getFirstName());
//            lastName.setText(user.getLastName());
//            phoneNumber.setText(user.getPhoneNum());
//            spinnerGender.setEnabled(user.getGender());
//            born.setText(user.getBorn());
//            city.setText(user.getCity());
//            detailsOnUser.setText(user.getWritingOnMe());
//        });


//// get to the user doc at the database and edit his data
//            fs.collection("users").whereEqualTo("id", uid)
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<User> userList = queryDocumentSnapshots.toObjects(User.class);
//        if (userList.size() == 0){
//        Log.e("EditProfile",
//        "Where is my user? its connected to app but cant see its own details from db " + uid);
//        }
//
//        // edit the data on a obj
//        User user = userList.get(0);
//        user.setFirstName(firstName.getText().toString());
//        user.setLastName(lastName.getText().toString());
//        user.setPhoneNum(phoneNumber.getText().toString());
//        user.setBorn(born.getText().toString());
//        user.setCity(city.getText().toString());
//        user.setWritingOnMe(detailsOnUser.getText().toString());
//
//        // now can edit the user at the fs
//        fs.collection("users").document(user.getDocumentId()).set(user)
//        .addOnSuccessListener(new OnSuccessListener<Void>() {
//@Override
//public void onSuccess(Void unused) {
//        Log.e("EditViewProfile", "updated to fs the edit of user: "+user.getId());
//        FragmentManager fm = getParentFragmentManager();
//        fm.beginTransaction().replace(R.id.main_fragment, ViewProfileFragment.class, null).commit();
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//        Log.e("EditViewProfile", "failed to update to fs the edit of user: "+user.getId());
//        }
//        });
//        });