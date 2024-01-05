package com.mycode.alpha;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNev;
    private MyAdapter myAdapter;
    private List<Alpha> alphaList;

    // firebase auth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    // storage
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Alpha");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        bottomNev = findViewById(R.id.bottomNev);
        recyclerView = findViewById(R.id.recyclerView);

        setSupportActionBar(toolbar);

        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        alphaList = new ArrayList<>();
        myAdapter = new MyAdapter(Home.this, alphaList);
        recyclerView.setHasFixedSize(true);

        bottomNev.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.add_post) {
                    Intent i = new Intent(Home.this, AddPhotoActivity.class);
                    startActivity(i);
                } else if (id == R.id.user_profile) {
                    Intent i = new Intent(Home.this, ProfileActivity.class);
                    startActivity(i);
                } else {

                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            collectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
                alphaList.clear();
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot alphas : queryDocumentSnapshots) {
                        Alpha alpha = alphas.toObject(Alpha.class);
                        alphaList.add(alpha);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                } else {
                    // Handle the case when there are no documents in the collection.
                }
            }).addOnFailureListener(e -> {
                // Handle the failure to get data from FireStore.
                Log.e("Home", "Error getting data from FireStore", e);
            });
        } else {
            // Handle the case when the user is null (not authenticated)
            // You might want to redirect to the login screen or take appropriate action.
        }
    }
}