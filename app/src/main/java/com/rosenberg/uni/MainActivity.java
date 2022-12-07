package com.rosenberg.uni;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rosenberg.uni.Entities.User;
import com.rosenberg.uni.Renter.RenterCarViewFragment;
import com.rosenberg.uni.Tenant.TenantCarViewFragment;
import com.rosenberg.uni.login.LoginFragment;
import com.rosenberg.uni.login.ViewProfileFragment;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity","mic check");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //Setting a dynamic title at runtime. Here, it displays the current time.
            actionBar.setTitle("Dashboard");
        }
        drawerLayout  = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.main_nav);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_main_menu, R.string.close_main_menu);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            Log.d("MainActivity","open menu");
            switch (item.getItemId()) {
                case R.id.nav_home:
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    db.collection("users").whereEqualTo("id", u.getUid())
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
                                    FragmentManager fm = getSupportFragmentManager();
                                    fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                                } else {
                                    Log.d("MainActivity", "Going to renter");
                                    FragmentManager fm = getSupportFragmentManager();
                                    fm.beginTransaction().replace(R.id.main_fragment, RenterCarViewFragment.class, null).commit();
                                }
                            });
                    Log.d("MainActivity","pressed home");
                    break;
                case R.id.nav_about:
                    Log.d("MainActivity","pressed about");
                    break;
                case R.id.nav_sign_out:
                    FirebaseAuth.getInstance().signOut();
                    FragmentManager fmSignout = getSupportFragmentManager();
                    fmSignout.beginTransaction().replace(R.id.main_fragment, LoginFragment.class, null).commit();
                    break;
                case R.id.nav_view_profile:
                    Log.d("MainActivity","pressed view profile");
                    FragmentManager fmViewProfile = getSupportFragmentManager();
                    fmViewProfile.beginTransaction().replace(R.id.main_fragment, ViewProfileFragment.class, null).commit();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main_fragment, LoginFragment.class, null).commit();
        } else {
            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            Log.d(TAG, "Got user" + u.getEmail());
            Toast.makeText(MainActivity.this, "Logged In " + u.getEmail(), Toast.LENGTH_LONG).show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").whereEqualTo("id", u.getUid())
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
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment, TenantCarViewFragment.class, null).commit();
                        } else {
                            Log.d("MainActivity", "Going to renter");
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.main_fragment, RenterCarViewFragment.class, null).commit();
                        }
                    });
        }
    }

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
            super.onBackPressed();
        }
    }
}