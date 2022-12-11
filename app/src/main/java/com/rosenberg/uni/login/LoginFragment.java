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
//mail: amiramir13@gmail.com
// password: 123456

// tester login renter
//mail: amiramir14@gmail.com
// password: 123456
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(String param1, String param2) {
        return new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    /**
     * init all texts and buttons for login window
     * @param view - View object of the window (hold the objects of texts inputs that screened)
     */
    // TODO - DANIEL add comment here im not sure about
    /**
     * @param savedInstanceState - DANIEL HELP
     */
    @Override
    public void onViewCreated(@NonNull View loginView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(loginView, savedInstanceState); // start with default process

        // init vars of the texts and buttons of the window
        EditText email = loginView.findViewById(R.id.login_email);
        EditText password = loginView.findViewById(R.id.login_password);
        Button loginBtn = loginView.findViewById(R.id.login_button);
        Button RegisterBtn = loginView.findViewById(R.id.login_btn_register);

        // logic process - verify user details, then log him if match.
        loginBtn.setOnClickListener(v -> {
            FirebaseAuth fsAuth = FirebaseAuth.getInstance(); // Firebase Authorization
            // try to sign in with email & password
            fsAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())

                    .addOnCompleteListener(signInTask -> { // after the sign in try^

                        if(signInTask.isSuccessful()){ // details to sign in is correct

                            FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseFirestore fs = FirebaseFirestore.getInstance();

                            // get the user data
                            fs.collection("users").whereEqualTo("id", loginUser.getUid())
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        // the below command returns a list of the objects
                                        List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                                        if (userList.size() == 0) { // verify connection is well
                                            Log.e("MainActivity","Where is my user? " + loginUser.getUid());
                                            FirebaseAuth.getInstance().signOut();
                                            return;
                                        }

                                        User user = userList.get(0); // take user

                                        // move to Home window via role of user (tenant - renter)
                                        if (user.getTenant()) { // getTenant == isTenant (bug of fb cant denote funcs as "is..")
                                            Log.d("MainActivity", "Going to tenant");
                                            FragmentManager fm = getParentFragmentManager();
                                            fm.beginTransaction().replace(R.id.main_fragment,
                                                    TenantCarViewFragment.class, null).commit();
                                        } else {
                                            Log.d("MainActivity", "Going to renter");
                                            FragmentManager fm = getParentFragmentManager();
                                            fm.beginTransaction().replace(R.id.main_fragment,
                                                    RenterCarViewFragment.class, null).commit();
                                        }
                                    });
                           } else{ // incorrect sign in details
                            // show 'wrong' for few secs to user
                            Toast.makeText(getActivity(),"wrong", Toast.LENGTH_LONG).show();
                        }
                    });
        });

        RegisterBtn.setOnClickListener(view1 -> {
            // user clicked on register, move him to register window
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().replace(R.id.main_fragment, RegisterFragment.class,null).commit();
        });
    }


}