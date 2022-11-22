package com.rosenberg.uni.login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rosenberg.uni.R;

public class RegisterActivity extends AppCompatActivity {

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

        btn.setOnClickListener(view -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"made account",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(RegisterActivity.this,"failed",Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}