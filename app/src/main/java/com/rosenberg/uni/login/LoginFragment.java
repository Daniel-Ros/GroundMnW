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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Renter.RenterCarViewFragment;
import com.rosenberg.uni.Tenant.TenantCarViewFragment;
import com.rosenberg.uni.R;

import java.util.List;


// tester login tenant
//mail :tenant@live.com
// password test12

// tester login renter
//mail :renter@live.com
// password test12
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText email = view.findViewById(R.id.login_email);
        EditText password = view.findViewById(R.id.login_password);
        Button btn = view.findViewById(R.id.login_button);
        Button RegisterBtn = view.findViewById(R.id.login_btn_register);

        btn.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseFirestore fs = FirebaseFirestore.getInstance();
                            fs.collection("users").whereEqualTo("id", u.getUid())
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                                        if (userList.size() == 0) {
                                            Log.e("MainActivity","Where is my user? " + u.getUid());
                                            FirebaseAuth.getInstance().signOut();
                                            return;
                                        }
                                        User user = userList.get(0);
                                        if (user.getTenant()) {
                                            Log.d("MainActivity", "Going to tenant");
                                            FragmentManager fm = getParentFragmentManager();
                                            fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                                        } else {
                                            Log.d("MainActivity", "Going to renter");
                                            FragmentManager fm = getParentFragmentManager();
                                            fm.beginTransaction().replace(R.id.main_fragment, RenterCarViewFragment.class, null).commit();
                                        }
                                    });
                           }else{
                            Toast.makeText(getActivity(),"wrong", Toast.LENGTH_LONG).show();
                        }
                    });
        });


        RegisterBtn.setOnClickListener(view1 -> {
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.main_fragment, RegisterFragment.class,null).commit();
        });
    }


}