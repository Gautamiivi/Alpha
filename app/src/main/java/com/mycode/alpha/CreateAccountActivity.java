package com.mycode.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
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

public class CreateAccountActivity extends AppCompatActivity {
    private AutoCompleteTextView userName;
    private AutoCompleteTextView email;
    private AutoCompleteTextView pwd;
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
        pwd = findViewById(R.id.pwd);
        email = findViewById(R.id.email);
        userName = findViewById(R.id.userName);

        firebaseAuth =FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser!=null){

                }else {

                }

            }
        };

        createBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(userName.getText().toString())&&
                    !TextUtils.isEmpty(email.getText().toString())&&
                    !TextUtils.isEmpty(pwd.getText().toString())){

                String Name = userName.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String pass = pwd.getText().toString().trim();
                createAccount(Name,Email,pass);
            }else {
                Toast.makeText(CreateAccountActivity.this,
                        "Fields are empty", Toast.LENGTH_SHORT).show();
            }

        });

    }
    private void createAccount(
            String userName,
            String email,
            String password){
        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(userName)){
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(CreateAccountActivity.this,
                                        "Account Created", Toast.LENGTH_SHORT).show();
                                Intent i  =new Intent(CreateAccountActivity.this, Home.class);
                                startActivity(i);

                            }
                        }
                    });
        }
    }

}