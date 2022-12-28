package com.rosenberg.uni;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Renter.RenterMyAcceptedCarsFragment;
import com.rosenberg.uni.Tenant.TenantCarViewFragment;
import com.rosenberg.uni.Login.LoginFragment;
import com.rosenberg.uni.Login.ViewProfileFragment;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    /**
     * blocks the event in the case the side bar was dealt
     *
     * @param item The menu that is being processed
     * @return true if event was dealt, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * create UI
     * check if user logged in, if not open login fragment
     * if yes, open relevant home fragment according to user type
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "mic check");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //Setting a dynamic title at runtime. Here, it displays the current time.
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.reg_blue)));
            actionBar.setTitle("Dashboard");
        }
        drawerLayout = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.main_nav);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_main_menu, R.string.close_main_menu);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            Log.d("MainActivity", "open menu");
            switch (item.getItemId()) {
                case R.id.nav_home:
                    // check type of user
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    db.collection("users").whereEqualTo("id", u.getUid())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> { // get all users with the same ID
                                List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                                if (userList.size() == 0) { // user not found
                                    Log.e("MainActivity", "Where is my user? " + u.getUid());
                                    FirebaseAuth.getInstance().signOut();
                                    return;
                                }
                                User user = userList.get(0);
                                if (user.getTenant()) { // send tenant to his Cars For Rent View
                                    Log.d("MainActivity", "Going to tenant");
                                    FragmentManager fm = getSupportFragmentManager();
                                    fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                                } else { // send renter to his Accepted Cars View
                                    Log.d("MainActivity", "Going to renter");
                                    FragmentManager fm = getSupportFragmentManager();
                                    fm.beginTransaction().replace(R.id.main_fragment, RenterMyAcceptedCarsFragment.class, null).commit();
                                }
                            });
                    Log.d("MainActivity", "pressed home");
                    break;
                case R.id.nav_about:
                    // TODO add information about us
                    Log.d("MainActivity", "pressed about");
                    break;
                case R.id.nav_sign_out:
                    // sign out and return to login screen
                    FirebaseAuth.getInstance().signOut();
                    FragmentManager fmSignout = getSupportFragmentManager();
                    fmSignout.beginTransaction()
                            .replace(R.id.main_fragment, LoginFragment.class, null)
                            .commit();
                    break;
                case R.id.nav_view_profile:
                    // show profile of the user
                    Log.d("MainActivity", "pressed view profile");
                    FragmentManager fmViewProfile = getSupportFragmentManager();
                    fmViewProfile.beginTransaction()
                            .addToBackStack("Home")
                            .replace(R.id.main_fragment, ViewProfileFragment.class, null)
                            .commit();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });


//        FirebaseMessaging.getInstance().setNotificationDelegationEnabled(true).addOnSuccessListener(anything -> {
//            Toast.makeText(MainActivity.this, "got popup msg!!! ", Toast.LENGTH_LONG).show();
//        });

        // disconnected from user
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main_fragment, LoginFragment.class, null).commit();
        }
        // connected to user
        else {
            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            Log.d(TAG, "Got user" + u.getEmail());
            Toast.makeText(MainActivity.this, "Logged In " + u.getEmail(), Toast.LENGTH_LONG).show();

            // get full data about user
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").whereEqualTo("id", u.getUid())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                        if (userList.size() == 0) { // user not found in database
                            Log.e("MainActivity", "Where is my user? " + u.getUid());
                            FirebaseAuth.getInstance().signOut();
                            return;
                        }
                        User user = userList.get(0);
                        if (user.getTenant()) {// send tenant to his Cars For Rent View
                            Log.d("MainActivity", "Going to tenant");
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                        } else { // send renter to his Accepted Cars View
                            Log.d("MainActivity", "Going to renter");
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment, RenterMyAcceptedCarsFragment.class, null).commit();
                        }
                    });
        }
    }

    /**
     * pick date for registration
     *
     * @param view
     */
    public void showDateDialog(View view) {
        EditText datePicked = (EditText) view;   // Store the dialog to be picked
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, y, m, d) -> {
                    String date = d + "/" + (m + 1) + "/" + y;
                    datePicked.setText(date);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            db.collection("users").whereEqualTo("id", u.getUid())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> { // get all users with the same ID
                        List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                        if (userList.size() == 0) { // user not found
                            Log.e("MainActivity", "Where is my user? " + u.getUid());
                            FirebaseAuth.getInstance().signOut();
                            return;
                        }
                        User user = userList.get(0);
                        if (user.getTenant()) { // send tenant to his Cars For Rent View
                            Log.d("MainActivity", "Going to tenant");
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                        } else { // send renter to his Accepted Cars View
                            Log.d("MainActivity", "Going to renter");
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment, RenterMyAcceptedCarsFragment.class, null).commit();
                        }
                    });
            Log.d("MainActivity", "pressed back");
            super.onBackPressed();
        }
    }
}