package com.mycode.alpha;

import static android.app.ProgressDialog.show;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

   private ImageView profileImage;
    private TextView username;
    private RecyclerView imageGridRecyclerView;
    private ProfileAdapter adapter;

    //firebase services
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Alpha");

    private List<Alpha> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //widgets
        profileImage = findViewById(R.id.profileImage);
        username = findViewById(R.id.username);
        imageGridRecyclerView = findViewById(R.id.imageGridRecyclerView);

       firebaseAuth = FirebaseAuth.getInstance();
       user = firebaseAuth.getCurrentUser();
       arrayList = new ArrayList<>();
       adapter = new ProfileAdapter(this,arrayList);
       imageGridRecyclerView.setHasFixedSize(true);
       username.setText(user.getEmail());

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user!=null){
            collectionReference.whereEqualTo("userId",user.getUid())
            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                arrayList.clear();
                if (queryDocumentSnapshots!=null && !queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot alphas :queryDocumentSnapshots){
                        Alpha alpha  = alphas.toObject(Alpha.class);
                        arrayList.add(alpha);
                    }
                    imageGridRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
                    imageGridRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.sign_out){
            firebaseAuth.signOut();
            Intent i  = new Intent(ProfileActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}