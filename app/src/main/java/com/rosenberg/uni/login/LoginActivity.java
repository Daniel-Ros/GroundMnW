package com.rosenberg.uni.login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rosenberg.uni.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            //Setting a dynamic title at runtime. Here, it displays the current time.
            actionBar.setTitle("Login");
        }


        EditText email = findViewById(R.id.login_email);
        EditText password = findViewById(R.id.login_password);
        Button btn = findViewById(R.id.login_button);
        Button RegisterBtn = findViewById(R.id.login_btn_register);

        btn.setOnClickListener(view -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                           Toast.makeText(LoginActivity.this,"Logged In",Toast.LENGTH_LONG).show();
                       }else{
                           Toast.makeText(LoginActivity.this,"wrong",Toast.LENGTH_LONG).show();
                       }
                    });
        });


        RegisterBtn.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
            LoginActivity.this.startActivity(i);
        });
    }
}