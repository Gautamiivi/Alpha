package com.mycode.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText pwd;
    private Button login;
    private Button forgetPwd;
    private Button createAccount;


    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
   private FirebaseAuth.AuthStateListener authStateListener;

   //stay logged in process
    public static final String SHARED_PREFS = "sharedPrefs";


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
            Intent i = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(i);
        });


        //fire base auth
        firebaseAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(v->
        {
            verifyUser(email.getText().toString().trim(),pwd.getText().toString().trim());
        });


    }
    private void verifyUser(String email,String pass){
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent i = new Intent(MainActivity.this, Home.class);
                            startActivity(i);

                        }
                    });


        }

    }
}