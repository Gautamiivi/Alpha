package com.mycode.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNev;
    private MyAdapter myAdapter;
    private List<Alpha> alphaList;

    //firebase auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //storage
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("posts");






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNev = findViewById(R.id.bottomNev);

        setSupportActionBar(toolbar);
        
        bottomNev.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =  item.getItemId();
                if (id==R.id.add_post){
                    Intent i = new Intent(Home.this,AddPhotoActivity.class);
                    startActivity(i);
                } else if (id==R.id.user_profile) {
                    Intent i =new Intent(Home.this, ProfileActivity.class);
                    startActivity(i);
                }else {

                }
                return true;
            }
        });



        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        alphaList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot journals : queryDocumentSnapshots){
                Alpha alpha = journals.toObject(Alpha.class);
                alphaList.add(alpha);
            }
            myAdapter =new MyAdapter(this,alphaList);
            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        });
    }
}