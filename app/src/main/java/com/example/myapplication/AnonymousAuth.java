package com.example.myapplication;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AnonymousAuth {
    private static final String TAG = "AnonymousAuth";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    public AnonymousAuth() {
    }

    public void onCreate() {
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    public void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    public void signInAnonymously(Activity activity) {
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END signin_anonymously]
    }

//    private void linkAccount() {
//        AuthCredential credential = EmailAuthProvider.getCredential("", "");
//
//        // [START link_credential]
//        mAuth.getCurrentUser().linkWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "linkWithCredential:success");
//                            FirebaseUser user = task.getResult().getUser();
//                            updateUI(user);
//                        } else {
//                            Log.w(TAG, "linkWithCredential:failure", task.getException());
//                            Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
//        // [END link_credential]
//    }

    private void updateUI(FirebaseUser user) {

    }
}
