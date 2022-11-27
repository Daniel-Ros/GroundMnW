package com.rosenberg.uni.utils;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rosenberg.uni.Entities.User;

import java.util.List;

public class userUtils {

    private static User u = null;
    public static boolean isSignedIn(){
        return FirebaseAuth.getInstance().getCurrentUser() == null;
    }

    public static String getUserID() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static User getUser() {
        if (u != null) {
            return u;
        }
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("userID",fUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> userList = queryDocumentSnapshots.toObjects(User.class);
                    u = userList.get(0);
                });
        return u;
    }
}
