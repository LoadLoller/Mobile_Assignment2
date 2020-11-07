package com.example.mobile_w01_07_5.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile_w01_07_5.R;
import com.example.mobile_w01_07_5.ui.login.LoginActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private StorageReference mStorageRef;
    private DatabaseReference reference;
    private String uid;
    private String email;

    public void getUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            email = user.getEmail();
            Log.d("message from profile fragment, uid",uid);
            Log.d("message from profile fragment, email",email);
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);


//        todo: remove after testing
//        mStorageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference childRef=mStorageRef.child("images/cat.jpg");
//        childRef.getBytes(1024*1024*30).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                img = (ImageView)root.findViewById(R.id.userImg);
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
        getUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String userImgStr = snapshot.child("image").getValue().toString();
                    String userNameStr = snapshot.child("name").getValue().toString();
                    String userAddrStr = snapshot.child("address").getValue().toString();

                    String userFollowerStr = snapshot.child("followers").getValue().toString();
                    String userFollowingStr = snapshot.child("following").getValue().toString();

                    String userPhoneStr = snapshot.child("phone").getValue().toString();
                    String userFbStr = snapshot.child("fb").getValue().toString();

                    // Set up views with values
                    Picasso.get().load(userImgStr).into(userImg);
                    userName.setText(userNameStr);
                    userAddr.setText(userAddrStr);

                    userFollower.setText(userFollowerStr);
                    userFollowing.setText(userFollowingStr);

                    userEmail.setText(email);
                    userPhone.setText(userPhoneStr);
                    userFb.setText(userFbStr);

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
         * Update
         */
        Button updateBtn = root.findViewById(R.id.update);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read user's updated information
                String newUserNameStr = userName.getText().toString();
                String newUserAddrStr = userAddr.getText().toString();
                String newUserPhoneStr = userPhone.getText().toString();
                String newUserFb = userFb.getText().toString();
                writeNewProfile(newUserNameStr, newUserAddrStr, newUserPhoneStr, newUserFb);

                Snackbar mySnackbar = Snackbar.make(view, "Information Updated", BaseTransientBottomBar.LENGTH_SHORT);
                mySnackbar.show();
            }
        });

        /**
         * Log out
         */
        Button logoutBtn = root.findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    /**
     * Write User's Information into REALTIME DATABASE
     * @param name user's new nickname
     * @param addr user's new address
     * @param phone user's new phone number
     * @param fb user's new facebook account
     */
    public void writeNewProfile(String name, String addr, String phone, String fb){
        reference.child("name").setValue(name);
        reference.child("address").setValue(addr);
        reference.child("phone").setValue(phone);
        reference.child("fb").setValue(fb);
    }


}
