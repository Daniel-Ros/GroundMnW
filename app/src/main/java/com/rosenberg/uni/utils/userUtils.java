package com.rosenberg.uni.utils;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rosenberg.uni.Entities.User;

public class userUtils {

    public static boolean isSignedIn(){
        return FirebaseAuth.getInstance().getCurrentUser() == null;
    }

    public static User getUser() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User ret = new User();
        db.collection("users").whereEqualTo("userID",fUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    }
                });

        return ret;
    }
}
