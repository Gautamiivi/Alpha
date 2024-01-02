package com.mycode.alpha.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mycode.alpha.Home;
import com.mycode.alpha.R;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText fillUserName;
    private EditText fillEmail;
    private EditText fillPwd;
    private Button createBtn;


    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    //firebase collection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createBtn = findViewById(R.id.createNewAccountBtn);
        fillPwd = findViewById(R.id.pwd);
        fillEmail = findViewById(R.id.email);
        fillUserName = findViewById(R.id.userName);

        firebaseAuth =FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();

            }
        };

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(fillUserName.getText().toString())&&
                        !TextUtils.isEmpty(fillEmail.getText().toString())&&
                        !TextUtils.isEmpty(fillPwd.getText().toString())){

                    String userName = fillUserName.getText().toString().trim();
                    String email = fillEmail.getText().toString().trim();
                    String pass = fillPwd.getText().toString().trim();
                    createAccount(userName,email,pass);
                    Intent i  =new Intent(CreateAccountActivity.this, Home.class);
                    startActivity(i);
                }else {
                    Toast.makeText(CreateAccountActivity.this,
                            "Something went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void createAccount(
            String userName,
            String email,
            String password){
        if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(CreateAccountActivity.this,
                                        "Account Created", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}