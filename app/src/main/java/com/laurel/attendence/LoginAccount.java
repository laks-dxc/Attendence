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
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button login;
    Context context;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);
        pref = getSharedPreferences(Config.MAIN, MODE_PRIVATE);
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

        boolean res = pref.getBoolean(Config.LOG, false);
        if (res) {
            Intent i = new Intent(this, Attendence.class);
            startActivity(i);

        }

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
                            editor = pref.edit();
                            editor.putString(Config.USER_EMAIL, empId);
                            editor.commit();
                            Log.e("share", pref.getString(Config.USER_EMAIL, ""));
                            Log.d("user", "signInWithEmail:onComplete:" + task.isSuccessful());
                            Intent intent = new Intent(getApplicationContext(), Attendence.class);
                            startActivity(intent);
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
//    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d("153", "153");
//        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        Log.d("line156", "hai");
//        return super.onCreateOptionsMenu(menu);
//    }
}
