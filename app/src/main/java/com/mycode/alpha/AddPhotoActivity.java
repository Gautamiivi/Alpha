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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class AddPhotoActivity extends AppCompatActivity {

    private ImageButton imageButton_c , imageButton_g;
    private ImageView imageView;
    private Button upload_btn;
    private EditText editText;
    private ProgressBar progress_bar;

    //Firebase storage
    private FirebaseFirestore db  = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Alpha");
    private StorageReference storageReference;

    //firebase auth
    private String currentUserId;
    private String currentUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;


    //using activityResultLauncher
    ActivityResultLauncher<String> takePhoto;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        imageButton_c = findViewById(R.id.imageButton_c);
        imageButton_g = findViewById(R.id.imageButton_g);
        imageView = findViewById(R.id.imageView);
        upload_btn = findViewById(R.id.upload_btn);
        editText = findViewById(R.id.editText);
        progress_bar = findViewById(R.id.progress_bar);



        takePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                //showing the image
                imageView.setImageURI(o);

                //getting the image uri
                imageUri = o;
            }
        });

        progress_bar.setVisibility(View.INVISIBLE);

        //storage Reference
        storageReference = FirebaseStorage.getInstance().getReference();

        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //getting the current user;
        if (user != null){
            currentUserId = user.getUid();
            currentUserName = user.getEmail();
        }

        upload_btn.setOnClickListener(v->
        {
           uploadOnServer();
        });

        imageButton_c.setOnClickListener(v->{
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
        });
        imageButton_g.setOnClickListener(v->{
            takePhoto.launch("image/*");
            Intent intent = new Intent();
        });



    }

    private void uploadOnServer() {
        String caption = editText.getText().toString().trim();
        progress_bar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(caption)&&imageUri !=null){

           final StorageReference filePath = storageReference
                   .child("alpha_image").child("my_mage"+currentUserId+ Timestamp.now().getSeconds());
           //uploading the image
            filePath.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                      filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              String imageUrl = uri.toString();
                              Alpha alpha = new Alpha();
                              alpha.setCaption(caption);
                              alpha.setTimeAdded(new Timestamp(new Date()));
                              alpha.setImageUrl(imageUrl);
                              alpha.setUserName(currentUserName);
                              alpha.setUserId(currentUserId);

                              //after uploading we direct the user on that activity

                              collectionReference.add(alpha)
                                      .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                  @Override
                                  public void onSuccess(DocumentReference documentReference) {
                                      progress_bar.setVisibility(View.GONE);
                                      Intent i = new Intent(AddPhotoActivity.this, Home.class);
                                      startActivity(i);
                                      finish();
                                  }
                              }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(AddPhotoActivity.this,
                                              "Uploading Fail Retry"+e.getMessage(),
                                              Toast.LENGTH_SHORT).show();
                                  }
                              });

                          }
                      });

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress_bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddPhotoActivity.this,
                                    "Failed Retry"+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        }else {
            progress_bar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
    }
}