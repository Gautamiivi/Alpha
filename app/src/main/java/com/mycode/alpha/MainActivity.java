package com.mycode.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
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

        currentUser = firebaseAuth.getCurrentUser();
        login.setOnClickListener(v->
        {
            verifyUser(email.getText().toString().trim(),
                    pwd.getText().toString().trim());
        });


    }
    private void verifyUser(String email,String pass){
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            firebaseAuth.signInWithEmailAndPassword(email,pass)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Intent i = new Intent(MainActivity.this, Home.class);
                        startActivity(i);
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Authentication failed. Check your email and password.", Toast.LENGTH_SHORT).show();
                    });

        }

    }
}