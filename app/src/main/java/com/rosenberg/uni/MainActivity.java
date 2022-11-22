package com.rosenberg.uni;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.service.controls.actions.FloatAction;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rosenberg.uni.databinding.ActivityMainBinding;
import com.rosenberg.uni.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            //Setting a dynamic title at runtime. Here, it displays the current time.
            actionBar.setTitle("Dashboard");
        }
        FloatingActionButton fab = findViewById(R.id.main_fab);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        }else{
            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
            Toast.makeText(MainActivity.this,"Logged In " + u.getEmail(), Toast.LENGTH_LONG).show();
        }


        if(fab != null) {
            fab.setOnClickListener(view -> {
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(myIntent);
            });
        }
    }

}