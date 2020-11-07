package com.example.mobile_w01_07_5;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mobile_w01_07_5.data.StampData;
import com.example.mobile_w01_07_5.data.StampItem;
import com.example.mobile_w01_07_5.ui.uploadphoto.UploadPhoto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FloatingActionButton uploadPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_feed, R.id.navigation_map, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference= mDatabase.getReference("Stamps/stamp");


        uploadPhoto=findViewById(R.id.UploadPhotoButton);
        uploadPhoto.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UploadPhoto.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        https://github.com/firebase/quickstart-android/blob/adbb3cd0934f77a0338d4ba48be1eaad12cf3107/auth/app/src/main/java/com/google/firebase/quickstart/auth/java/AnonymousAuthActivity.java#L73-L79
    }

}