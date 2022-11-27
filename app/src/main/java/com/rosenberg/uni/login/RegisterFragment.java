package com.rosenberg.uni.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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

        EditText name = v.findViewById(R.id.register_name);
        EditText email = v.findViewById(R.id.register_email);
        EditText password1 = v.findViewById(R.id.register_password1);
        EditText password2 = v.findViewById(R.id.register_password2);
        EditText dob = v.findViewById(R.id.register_dob);
        Spinner spinner = v.findViewById(R.id.register_spinner);
        Button btn = v.findViewById(R.id.register_button);


        // Init the spinner
        String [] choises = new String[]{"Tenant","Renter"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,choises);
        spinner.setAdapter(adapter);


        btn.setOnClickListener(view -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),
                            password1.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            //Toast.makeText(RegisterActivity.this,"made account",Toast.LENGTH_LONG).show();
                            String uid = mAuth.getCurrentUser().getUid();
                            User user = new User(uid,name.getText().toString(),email.getText().toString(),dob.getText().toString(),spinner.getSelectedItemPosition() == 0);
                            db.collection("users")
                                    .add(user.getMap())
                                    .addOnSuccessListener(documentReference -> {
                                        FragmentManager fm = getParentFragmentManager();
                                        fm.beginTransaction().replace(R.id.base_fragment, TenantCarViewFragment.class,null).commit();
                                    });
                        }else{
//                            Toast.makeText(RegisterActivity.this,"failed",Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}