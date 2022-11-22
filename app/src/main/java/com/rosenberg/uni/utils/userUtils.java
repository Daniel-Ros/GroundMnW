package com.rosenberg.uni.utils;

import com.google.firebase.auth.FirebaseAuth;

public class userUtils {

    public static boolean isSignedIn(){
        return FirebaseAuth.getInstance().getCurrentUser() == null;
    }
}
