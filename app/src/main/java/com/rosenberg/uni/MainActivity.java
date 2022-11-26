package com.rosenberg.uni;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rosenberg.uni.login.LoginFragment;

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

//        FragmentManager fm = getSupportFragmentManager();
//
//        fm.beginTransaction().replace(R.id.base_fragment,CarViewFragment.class,null).commit();

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

        }else{
            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            Log.wtf(TAG,"Got user" + u.getEmail());
            Toast.makeText(MainActivity.this,"Logged In " + u.getEmail(), Toast.LENGTH_LONG).show();

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.base_fragment, CarViewFragment.class,null).commit();
        }


//        if(fab != null) {
//            fab.setOnClickListener(view -> {
//                FirebaseAuth.getInstance().signOut();
//            });
//        }
    }

}