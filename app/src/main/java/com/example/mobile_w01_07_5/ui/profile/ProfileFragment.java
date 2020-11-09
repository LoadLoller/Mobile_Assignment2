package com.example.mobile_w01_07_5.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private StorageReference mStorageRef;
    private DatabaseReference reference;
    private String uid;
    private String email;
    private View root;
    private ImageView userImg;
    private String userImgStr;

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

        root = inflater.inflate(R.layout.fragment_profile, container, false);


        /**
         * Update User Profile with Firebase Information
         */
        // Set Up Hookers
        userImg = root.findViewById(R.id.userImg);
        TextView userName = root.findViewById(R.id.userName);
        TextView userAddr = root.findViewById(R.id.userAddr);

        TextView userFollower = root.findViewById(R.id.userFollower);
        TextView userFollowing = root.findViewById(R.id.userFollowing);

        TextView userEmail = root.findViewById(R.id.userEmail);
        TextView userPhone = root.findViewById(R.id.userPhone);
        TextView userFb = root.findViewById(R.id.userFb);

        // Get Current User's ID
        getUser();

        // Set Up FIREBASE DATABASE reference
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    userImgStr = snapshot.child("image").getValue().toString();
                    String userNameStr = snapshot.child("name").getValue().toString();
                    String userAddrStr = snapshot.child("address").getValue().toString();

                    String userFollowerStr = snapshot.child("followers").getValue().toString();
                    String userFollowingStr = snapshot.child("following").getValue().toString();

                    String userPhoneStr = snapshot.child("phone").getValue().toString();
                    String userFbStr = snapshot.child("fb").getValue().toString();

                    // Load User profile image from storage
                    mStorageRef = FirebaseStorage.getInstance().getReference().child("profiles/"+userImgStr);
                    mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(userImg);
                        }
                    });

                    // Set up views with values
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
         * Update User Information
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

        /**
         * Modify User's Profile Image
         */
        Button addImgBtn = root.findViewById(R.id.addImg);
        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 200);
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

    /**
     * React to edit user profile action
     * @param requestCode 200: edit user profile request
     * @param resultCode action result code
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==200 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), selectedImage);
                userImg.setImageBitmap(bitmap);

                // Upload user's new profile image to FIREBASE STORAGE
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bytearr = baos.toByteArray();

                // Detect if this is the first time the user set up the profile image
                if (userImgStr.equals("default.jpg")){
                    mStorageRef = FirebaseStorage.getInstance().getReference().child("profiles/"+uid+".jpg");
                    reference.child("image").setValue(uid+".jpg");
                };

                // Upload user's profile image
                UploadTask uploadUserImg = mStorageRef.putBytes(bytearr);
                uploadUserImg.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", "onFailure: Fail to Update Profile Image");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Show msg when update successfully
                        Snackbar img_update_note = Snackbar.make(getView(), "Profile Image Updated", BaseTransientBottomBar.LENGTH_SHORT);
                        img_update_note.show();
                    }
                });

            } catch (FileNotFoundException e) {
                Log.d("Error", "onActivityResult: User Profile Picture Not Found");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("Error","onActivityResult: User Profile Picture Read/Write Error");
                e.printStackTrace();
            }
        }
    }

}
