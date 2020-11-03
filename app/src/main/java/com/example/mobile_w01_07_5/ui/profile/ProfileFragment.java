package com.example.mobile_w01_07_5.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobile_w01_07_5.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private StorageReference mStorageRef;
    private ImageView img;
    private DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


//        todo: remove after testing
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference childRef=mStorageRef.child("images/cat.jpg");
        childRef.getBytes(1024*1024*30).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                img = (ImageView)root.findViewById(R.id.userImg);
                img.setImageBitmap(bitmap);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("user");

        TextView userName = root.findViewById(R.id.userName);
        TextView userAddr = root.findViewById(R.id.userAddr);



        return root;
    }

    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
        if (dataSnapshot.exists()){
            String email = dataSnapshot.child("user").child("email").getValue(String.class);
        }
    }

}
