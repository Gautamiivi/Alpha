package com.mycode.alpha;

import static android.app.ProgressDialog.show;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

   private ImageView profileImage;
    private TextView username,followerCount;
    private RecyclerView imageGridRecyclerView;


    //firebase services
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Alpha");
    private StorageReference storageReference;

    //using activitySResultLauncher
    ActivityResultLauncher<String> profileImageUrl;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //widgets
        profileImage = findViewById(R.id.profileImage);
        username = findViewById(R.id.username);
        followerCount = findViewById(R.id.followerCount);
        imageGridRecyclerView = findViewById(R.id.imageGridRecyclerView);



        //using activitySResultLauncher
        profileImageUrl = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                profileImage.setImageURI(o);
                imageUri = o;
            }
        });


        //firebaseServices
       firebaseAuth = FirebaseAuth.getInstance();
       user = firebaseAuth.getCurrentUser();
        if (imageUri !=null) {
            // Get a reference to the storage location
            storageReference = FirebaseStorage.getInstance().getReference();

            // Create a reference to the profile image file in storage
            final StorageReference file = storageReference.child("profile_img.jpj");

            //upload the image on fireStore
            file.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                               String url = uri.toString();
                                Alpha alpha = new Alpha();
                                alpha.setProfileImageUrl(url);
                                collectionReference.add(alpha);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this,
                                        "Uploading Fail Retry"+e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    });


        }

    }
}