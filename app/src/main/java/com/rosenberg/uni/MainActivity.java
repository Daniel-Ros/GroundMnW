package com.rosenberg.uni;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Renter.RenterCarViewFragment;
import com.rosenberg.uni.Tenant.TenantCarViewFragment;
import com.rosenberg.uni.login.LoginFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity", "ERROR onStart()");
        Log.w("MainActivity", "WARN onStart()");
        Log.i("MainActivity", "INFO onStart()");
        Log.d("MainActivity", "DEBUG onStart()");
        Log.v("MainActivity", "VERBOSE onStart()");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            //Setting a dynamic title at runtime. Here, it displays the current time.
            actionBar.setTitle("Dashboard");
        }
//        FloatingActionButton fab = findViewById(R.id.main_fab);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
//            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
//            MainActivity.this.startActivity(myIntent);

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.base_fragment, LoginFragment.class,null).commit();

        }else {
            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            Log.d(TAG, "Got user" + u.getEmail());
            Toast.makeText(MainActivity.this, "Logged In " + u.getEmail(), Toast.LENGTH_LONG).show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").whereEqualTo("id", u.getUid())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                        if(userList.size() == 0){
                            Log.e("MainActivity","Where is my user? " + u.getUid());
                            FirebaseAuth.getInstance().signOut();
                            return;
                        }
                        User user = userList.get(0);
                        if (user.tenant()) {
                            Log.d("MainActivity", "Going to tenant");
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.base_fragment, TenantCarViewFragment.class, null).commit();
                        } else {
                            Log.d("MainActivity", "Going to renter");
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.base_fragment, RenterCarViewFragment.class, null).commit();
                        }
                    });
        }

//        if(fab != null) {
//            fab.setOnClickListener(view -> {
//                FirebaseAuth.getInstance().signOut();
//            });
//        }
    }
}