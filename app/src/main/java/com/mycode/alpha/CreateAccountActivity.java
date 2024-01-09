package com.mycode.alpha;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateAccountActivity extends AppCompatActivity {
    private AutoCompleteTextView userName;
    private AutoCompleteTextView email;
    private AutoCompleteTextView pwd;
    private Button createBtn;
    private ImageView profileImage;

    //firebase
    private FirebaseAuth Auth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    //firebase collection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ActivityResultLauncher<String> takePhoto;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        profileImage = findViewById(R.id.profileImage);


        takePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        //showing the image
                        profileImage.setImageURI(o);

                        //getting the image uri
                        imageUri = o;
                    }
                });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto.launch("image/*");
            }
        });

        createBtn = findViewById(R.id.createNewAccountBtn);
        pwd = findViewById(R.id.pwd);
        email = findViewById(R.id.email);
        userName = findViewById(R.id.userName);

        Auth =FirebaseAuth.getInstance();
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


    protected void onStart() {
        super.onStart();
        Auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            Auth.removeAuthStateListener(authStateListener);
        }
    }
    private void createAccount(
            String userName,
            String email,
            String password){
        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(userName)){
            Auth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(authResult -> {

                        Toast.makeText(CreateAccountActivity.this,
                                "Account Created", Toast.LENGTH_SHORT).show();
                        Intent i  =new Intent(CreateAccountActivity.this, Home.class);
                        startActivity(i);


                    }).addOnFailureListener(e -> {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            // Email is already in use
                            // Inform the user or provide a password reset option
                            Toast.makeText(CreateAccountActivity.this,
                                    "Email is already in use. Please reset your password if needed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Other registration failures
                            Toast.makeText(CreateAccountActivity.this,
                                    "Registration failed. Please try again later.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}