package com.rosenberg.uni.Models;

import static com.google.android.gms.tasks.Tasks.await;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.R;
import com.rosenberg.uni.Renter.RenterMyAcceptedCarsFragment;
import com.rosenberg.uni.Tenant.TenantCarViewFragment;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class LoginFunctions {

    private User user;
    private final FirebaseFirestore _fs;
    private final FirebaseAuth _fsAuth;

    public LoginFunctions(){
        _fs = FirebaseFirestore.getInstance();
        _fsAuth = FirebaseAuth.getInstance();
    }

    /**
     * logic process - verify user details, then log him if match.
     * @param email - input from user
     * @param psw - password from user
     * @param parentFm - FragmentManger (helps to move between windows)
     * @param activity - help for pop up messages
     */
    public void loginPressed(FragmentManager parentFm, FragmentActivity activity, String email, String psw){

        this.logTheUserIn(email, psw).addOnCompleteListener(signInTask -> { // after the sign in try^

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
                                parentFm.beginTransaction().replace(R.id.main_fragment,
                                        TenantCarViewFragment.class, null).commit();
                            } else {
                                Log.d("MainActivity", "Going to renter");
                                parentFm.beginTransaction().replace(R.id.main_fragment,
                                        RenterMyAcceptedCarsFragment.class, null).commit();
                            }
                        });
            } else{ // incorrect sign in details
                // show 'wrong' for few secs to user
                Toast.makeText(activity,"wrong", Toast.LENGTH_LONG).show();
            }});
    }

    /**
     * execute the registeration process
     * @param parentFm fragmentManager
     * @param activity curr..
     * @param firstName of user
     * @param lastName of user
     * @param email of user
     * @param firstPassword of user
     * @param verifyPassword of user
     * @param born date birth
     * @param city of user
     * @param phoneNumber of user
     * @param role true-tenant, false-renter
     * @param gender of user true-male, false-female
     */
    public void registerPressed(FragmentManager parentFm, FragmentActivity activity, String firstName, String lastName,
                                String email, String firstPassword, String verifyPassword, String born, String city,
                                String phoneNumber, boolean role, boolean gender){
        if (!samePsw(firstPassword, verifyPassword)){
            // password !=? verifyPassword
            // msg user about that
            Toast.makeText(activity, "passwords is not the same", Toast.LENGTH_LONG).show();
            return;
        }
        else if (!legitPsw(firstPassword)){
            // password length must be at least 6 digits but not more than 14
            // msg user about that
            Toast.makeText(activity, "passwords shall be 6~14 digits", Toast.LENGTH_LONG).show();
            return;
        }
        else if (!legitPhoneNumber(phoneNumber)){
            // phone number not start with '05...' then its for sure not phone num
            // same if the length of the phone number is more not 10 digits
            // msg user about it
            Toast.makeText(activity, "phone number is not legit", Toast.LENGTH_LONG).show();
            return;
        }

        registerTheUser(email, firstPassword).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                String uid = _fsAuth.getCurrentUser().getUid();
                // create user obj with all the input registration
                this.user = new User(uid,firstName, lastName, email, born, role, gender,
                        phoneNumber, city);
                // add user to database
                this._fs.collection("users")
                        .add(user)
                        .addOnSuccessListener(documentReference -> {
                            Log.e("ViewProfile", "added to fs new user: "+user.getId());

                            // move user to his home window via his role
                            if(role) { // true - tenant
                                parentFm.beginTransaction().replace(R.id.main_fragment,
                                        TenantCarViewFragment.class, null).commit();
                            } else { // false - renter
                                parentFm.beginTransaction().replace(R.id.main_fragment,
                                        RenterMyAcceptedCarsFragment.class, null).commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ViewProfile", "failed add to fs the user: "+user.getId());
                            }
                        });
            }else{
                Toast.makeText(activity,"failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * call to fsAuth and log via details
     * @return the loged user obj
     */
    private com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult>
        logTheUserIn(String email, String psw){
        return _fsAuth.signInWithEmailAndPassword(email, psw);
    }

    private com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult>
        registerTheUser(String email, String psw){
        return _fsAuth.createUserWithEmailAndPassword(email,
                psw);
    }

    /**
     * @param psw1 password1
     * @param psw2 password2
     * @return psw1==psw2
     */
    private boolean samePsw(String psw1, String psw2){
        return psw1.equals(psw2);
    }

    /**
     * @param psw password
     * @return 6 <= psw.len <= 16
     */
    private boolean legitPsw(String psw){
        return psw.length() >= 6 && psw.length() <= 16;
    }

    /**
     * @param phoneNumber .
     * @return looks like : 05d ddd dddd
     */
    private boolean legitPhoneNumber(String phoneNumber){
        return  phoneNumber.startsWith("05") && phoneNumber.length() == 10;
    }

}
