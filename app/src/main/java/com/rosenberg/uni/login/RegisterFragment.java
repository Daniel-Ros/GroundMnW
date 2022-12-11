package com.rosenberg.uni.login;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Renter.RenterCarViewFragment;
import com.rosenberg.uni.Renter.RenterMyAcceptedCarsFragment;
import com.rosenberg.uni.Tenant.TenantCarViewFragment;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * this class is the code that works with the fragment_register.xml Window
 * responsible about taking the data from the user registration and open new User at the database
 * also, the code verify that password, phone number are legit
 */
public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment RegisterFragment.
     */
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    /**
     * we not doing anything more than default at "onCreate" phase
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    /**
     *
     * @param view - View object of the window (hold the objects of texts inputs that screened)
     * @param savedInstanceState - last state of this fragment,should be null
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        // init vars of the texts for the window
        EditText firstName = view.findViewById(R.id.register_first_name);
        EditText lastName = view.findViewById(R.id.register_last_name);
        EditText email = view.findViewById(R.id.register_email);
        EditText firstPassword = view.findViewById(R.id.register_password1);
        EditText verifyPassword = view.findViewById(R.id.register_password2);
        EditText born = view.findViewById(R.id.register_dob);
        EditText city = view.findViewById(R.id.register_city);
        Spinner spinnerRoles = view.findViewById(R.id.register_spinner);
        Spinner spinnerGender = view.findViewById(R.id.register_gender);
        EditText phoneNumber = view.findViewById((R.id.register_phoneNumber));

        // Init the spinner of roles
        String [] choicesRoles = new String[]{"Tenant","Renter"};
        ArrayAdapter<String> adapterRoles = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,choicesRoles);
        spinnerRoles.setAdapter(adapterRoles);

        // Init the spinner of gender
        String [] choicesGenders = new String[]{"Male","Female"};
        ArrayAdapter<String> adapterGenders = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,choicesGenders);
        spinnerGender.setAdapter(adapterGenders);

        // init buttons for the window
        Button registerBtn = view.findViewById(R.id.register_button);

        // currentView == view from above
        registerBtn.setOnClickListener(currentView -> {

            if (!firstPassword.getText().toString().equals(verifyPassword.getText().toString())){
                // password !=? verifyPassword
                // msg user about that
                Toast.makeText(getActivity(), "passwords is not the same", Toast.LENGTH_LONG).show();
                return;
            }

            if (firstPassword.getText().toString().length() < 6 || firstPassword.getText().toString().length() > 16){
                // password length must be at least 6 digits but not more than 14
                // msg user about that
                Toast.makeText(getActivity(), "passwords shall be 6~14 digits", Toast.LENGTH_LONG).show();
                return;
            }

            if (phoneNumber.getText().toString().charAt(0) != '0' || phoneNumber.getText().toString().charAt(5) != '5'
                    || phoneNumber.getText().toString().length() != 10){
                // phone number not start with '05...' then its for sure not phone num
                // same if the length of the phone number is more not 10 digits
                // msg user about it
                Toast.makeText(getActivity(), "phone number is not legit", Toast.LENGTH_LONG).show();
                return;
            }

            FirebaseAuth fsAuth = FirebaseAuth.getInstance(); // Firebase Authorization
            // can create user - data is reasonable
            fsAuth.createUserWithEmailAndPassword(email.getText().toString(),
                            firstPassword.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){

                            String uid = fsAuth.getCurrentUser().getUid();
                            // create user obj with all the input registration
                            User user = new User(uid,firstName.getText().toString(), lastName.getText().toString(),
                                    email.getText().toString(),born.getText().toString(),
                                    spinnerRoles.getSelectedItemPosition() == 0,
                                    spinnerGender.getSelectedItemPosition() == 0,
                                    phoneNumber.getText().toString(), city.getText().toString());

                            // add user to database
                            FirebaseFirestore fs = FirebaseFirestore.getInstance();
                            fs.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.e("ViewProfile", "added to fs new user: "+user.getId());
                                        FragmentManager fm = getParentFragmentManager();

                                        // move user to his home window via his role
                                        if(spinnerRoles.getSelectedItemPosition() == 0)
                                            fm.beginTransaction().replace(R.id.main_fragment,
                                                    TenantCarViewFragment.class,null).commit();
                                        else
                                            fm.beginTransaction().replace(R.id.main_fragment,
                                                    RenterMyAcceptedCarsFragment.class,null).commit();
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ViewProfile", "failed add to fs the user: "+user.getId());
                                        }
                                    });
                        }else{
                            Toast.makeText(getActivity(),"failed",Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}