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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private StorageReference mStorageRef;
    private ImageView img;

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
        return root;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        StorageReference childRef=mStorageRef.child("images/cat.jpg");
//        childRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                img = img.findViewById(R.id.userImg);
//                img.setImageBitmap(bitmap);
//            }
//        });

        /**
         * Update User Profile with Firebase Information
         */
        // Set Up Hookers
        ImageView userImg = root.findViewById(R.id.userImg);
        TextView userName = root.findViewById(R.id.userName);
        TextView userAddr = root.findViewById(R.id.userAddr);

        TextView userFollower = root.findViewById(R.id.userFollower);
        TextView userFollowing = root.findViewById(R.id.userFollowing);

        TextView userEmail = root.findViewById(R.id.userEmail);
        TextView userPhone = root.findViewById(R.id.userPhone);
        TextView userFb = root.findViewById(R.id.userFb);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userImgStr = snapshot.child("image").getValue().toString();
                    String userAddrStr = snapshot.child("address").getValue().toString();
                    String userFollowerStr = snapshot.child("followers").getValue().toString();
                    String userFollowingStr = snapshot.child("following").getValue().toString();

                    String userEmailStr = snapshot.child("email").getValue().toString();
                    String userPhoneStr = snapshot.child("phone").getValue().toString();
                    String userNameStr = snapshot.child("name").getValue().toString();

                    // Set up views with values
                    Picasso.get().load(userImgStr).into(userImg);
                    userName.setText(userNameStr);
                    userAddr.setText(userAddrStr);

                    userFollower.setText(userFollowerStr);
                    userFollowing.setText(userFollowingStr);

                    userEmail.setText(userEmailStr);
                    userPhone.setText(userPhoneStr);
                    userFb.setText(userNameStr);

                }else{
                    Log.d("500", "onDataChange: Error Occurs");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("500","Cancelled");
            }
        });

        /**
         * Log out
         */
//        Button logoutBtn = root.findViewById(R.id.logout);
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // todo: redirect to login page
//            }
//        });
        return root;
    }


}
