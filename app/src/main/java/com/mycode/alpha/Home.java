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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BottomNavigationView bottomNev;
    private MyAdapter myAdapter;
    private List<Alpha> alphaList;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private boolean isToolbarVisible = true;

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



        bottomNev = findViewById(R.id.bottomNev);
        recyclerView = findViewById(R.id.recyclerView);




        // Toolbar code
         appBarLayout = findViewById(R.id.appbar);
         toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);


        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        // Adapter
        alphaList = new ArrayList<>();
        myAdapter = new MyAdapter(Home.this, alphaList);
        recyclerView.setHasFixedSize(false);


        //handling toolbar
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy>0){
                    // Scrolling up
                    hideToolbar();
                }else {
                    // Scrolling down
                    showToolbar();
                }
            }
        });


        // Bottom nev code
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
    private void hideToolbar() {
        if (isToolbarVisible) {
            // Animate the Toolbar to go up with the RecyclerView
            toolbar.animate().translationY(-toolbar.getHeight()).setDuration(100).start();
            isToolbarVisible = false;
        }
    }

    private void showToolbar() {
        if (!isToolbarVisible) {
            // Animate the Toolbar to scroll down first
            toolbar.animate().translationY(0).setDuration(100).start();
            isToolbarVisible = true;
        }
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