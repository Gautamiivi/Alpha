package com.mycode.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView email;
    private AutoCompleteTextView pwd;
    private Button login;
    private Button forgetPwd;
    private Button createAccount;


    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
   private FirebaseAuth.AuthStateListener authStateListener;

   //stay logged in process

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        createAccount = findViewById(R.id.createNewAccountBtn);
        forgetPwd= findViewById(R.id.forgetBtn);
        login= findViewById(R.id.loginBtn);
         pwd= findViewById(R.id.pwd);
        email= findViewById(R.id.email);

        createAccount.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this,CreateAccountActivity.class);
            startActivity(i);
        });


        //fire base auth
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is already authenticated, go to Home activity
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                    finish(); // Finish this activity to prevent going back to the login screen
                }
            }
        };
        login.setOnClickListener(v->
        {
            verifyUser(email.getText().toString().trim(),
                    pwd.getText().toString().trim());
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
    private void verifyUser(String email,String pass){
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            firebaseAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
                        if (task.isSuccessful()) {
                            // Save user login state on successful login
                            saveUserLoginState();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent i = new Intent(MainActivity.this, Home.class);
                            startActivity(i);
                            finish();
                        } else {
                            // Handle sign-in failure
                            handleSignInError(task.getException());
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                // Handle invalid credentials
                            } else if (exception instanceof FirebaseNetworkException) {
                                // Handle network-related issues
                            } else {
                                // Handle other types of errors
                            }
                        }


                    });
        }

    }


    private void saveUserLoginState() {
        // Save user login state using SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_user_logged_in", true);
        editor.apply();
    }

    private void handleSignInError(Exception exception) {
        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            // Handle invalid credentials
            Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseNetworkException) {
            // Handle network-related issues
            Toast.makeText(MainActivity.this, "Network issues", Toast.LENGTH_SHORT).show();
        } else {
            // Handle other types of errors
            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
        }
    }
}