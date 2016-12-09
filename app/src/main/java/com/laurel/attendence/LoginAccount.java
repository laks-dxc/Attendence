package com.laurel.attendence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAccount extends Activity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText user;
    EditText pass;
    Button login;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);
        session = new UserSessionManager(getApplicationContext());

        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.button);

        // pref = getSharedPreferences(Config.MAIN, MODE_PRIVATE);
        login.setOnClickListener(this);
        Firebase.setAndroidContext(this);
//        boolean res = pref.getBoolean(Config.LOG, false);
       /* if (res) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }*/


        try {

            //context=getApplicationContext();
            FirebaseApp.initializeApp(this);
            mAuth = FirebaseAuth.getInstance();
         /*   mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                     firebaseUser = firebaseAuth.getCurrentUser();
                    Toast.makeText(LoginAccount.this, user+"", Toast.LENGTH_SHORT).show();
                    Log.d("user656", "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                    if (user != null) {
                        Log.d("67","67");
                        // User is signed in
                        Log.d("user", "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                        //    editor.putString(Config.USER_ID, params[0].user.getUid());

                    } else {
                        // User is signed out
                        Log.d("user", "onAuthStateChanged:signed_out");
                    }

                }
            };*/
            Toast.makeText(getApplicationContext(),
                    "User Login Status: " + session.isUserLoggedIn(),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("fire", e.getMessage());
        }
    }

    public static String empId;

    @Override
    public void onClick(View v) {
        empId = user.getText().toString().trim();
        String empPass = pass.getText().toString().trim();
        signIn(empId, empPass);

    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("user", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginAccount.this, "Emp failed", Toast.LENGTH_SHORT).show();
                        } else {

                            Log.d("user", "signInWithEmail:onComplete:" + task.isSuccessful());
                            session.createUserLoginSession(empId);
                            Intent i = new Intent(getApplicationContext(), Attendence.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }
                    }
                });
    }


    private boolean validateForm() {
        boolean valid = true;
        String email = user.getText().toString();
        if (TextUtils.isEmpty(email)) {
            user.setError("Required.");
            valid = false;
        } else {
            user.setError(null);
        }
        String password = pass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass.setError("Required.");
            valid = false;
        } else {
            pass.setError(null);
        }
        return valid;
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            Intent intCloseApp = new Intent(Intent.ACTION_MAIN);
            intCloseApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intCloseApp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intCloseApp.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intCloseApp.addCategory(Intent.CATEGORY_HOME);
            intCloseApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intCloseApp);
        } else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
//    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d("153", "153");
//        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        Log.d("line156", "hai");
//        return super.onCreateOptionsMenu(menu);
//    }
}
