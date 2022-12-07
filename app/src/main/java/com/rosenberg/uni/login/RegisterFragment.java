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
import com.rosenberg.uni.Tenant.TenantCarViewFragment;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v,savedInstanceState);

//        EditText name = v.findViewById(R.id.register_name);
        EditText firstName = v.findViewById(R.id.register_first_name);
        EditText lastName = v.findViewById(R.id.register_last_name);

        EditText email = v.findViewById(R.id.register_email);

        // TODO: verify pass1 == pass2 - else, dont create the user
        EditText password1 = v.findViewById(R.id.register_password1);
        EditText password2 = v.findViewById(R.id.register_password2);

        EditText born = v.findViewById(R.id.register_dob);
        EditText city = v.findViewById(R.id.register_city);
        Spinner spinnerRoles = v.findViewById(R.id.register_spinner);
        Spinner spinnerGender = v.findViewById(R.id.register_gender);
        EditText phoneNumber = v.findViewById((R.id.register_phoneNumber));

        Button registerBtn = v.findViewById(R.id.register_button);


        // Init the spinner of roles
        String [] choisesRoles = new String[]{"Tenant","Renter"};
        ArrayAdapter<String> adapterRoles = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,choisesRoles);
        spinnerRoles.setAdapter(adapterRoles);

        // Init the spinner of gender
        String [] choisesGenders = new String[]{"Male","Female"};
        ArrayAdapter<String> adapterGenders = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,choisesGenders);
        spinnerGender.setAdapter(adapterGenders);



        registerBtn.setOnClickListener(view -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),
                            password1.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            FirebaseFirestore fs = FirebaseFirestore.getInstance();

                            //Toast.makeText(RegisterActivity.this,"made account",Toast.LENGTH_LONG).show();
                            String uid = mAuth.getCurrentUser().getUid();
                            User user = new User(uid,firstName.getText().toString(), lastName.getText().toString(),
                                    email.getText().toString(),born.getText().toString(),spinnerRoles.getSelectedItemPosition() == 0,
                                    spinnerGender.getSelectedItemPosition() == 0,
                                    phoneNumber.getText().toString(), city.getText().toString());
                            fs.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.e("ViewProfile", "added to fs new user: "+user.getId());
                                        FragmentManager fm = getParentFragmentManager();
                                        if(spinnerRoles.getSelectedItemPosition() == 0)
                                            fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class,null).commit();
                                        else
                                            fm.beginTransaction().replace(R.id.main_fragment, RenterCarViewFragment.class,null).commit();
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