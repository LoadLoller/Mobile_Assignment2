package com.example.mobile_w01_07_5.ui.profile;

import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class OtherUserProfileFragment extends Fragment {

    private StorageReference mStorageRef;
    private DatabaseReference reference;
    private DatabaseReference followingsRf;
    private DatabaseReference followersRf;
    private String current_uid;
    private String img_uid;
    private Button followBtn;
    private Button unfollowBtn;

    public void getUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            current_uid = user.getUid();
            Log.d("message from profile fragment, uid", current_uid);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.other_user_profile, container, false);
        getUser();

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

        img_uid = OtherUserProfileFragmentArgs.fromBundle(getArguments()).getUserIDArgument();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(img_uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userImgStr = snapshot.child("image").getValue().toString();
                    String userNameStr = snapshot.child("name").getValue().toString();
                    String userAddrStr = snapshot.child("address").getValue().toString();

                    String userFollowerStr = snapshot.child("followers").getValue().toString();
                    String userFollowingStr = snapshot.child("following").getValue().toString();

                    String userEmailStr = snapshot.child("email").getValue().toString();
                    String userPhoneStr = snapshot.child("phone").getValue().toString();
                    String userFbStr = snapshot.child("fb").getValue().toString();

                    // Set up views with values
                    // Load User profile image from storage
                    mStorageRef = FirebaseStorage.getInstance().getReference().child("profiles/"+userImgStr);
                    mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(userImg);
                        }
                    });

                    userName.setText(userNameStr);
                    userAddr.setText(userAddrStr);

                    userFollower.setText(userFollowerStr);
                    userFollowing.setText(userFollowingStr);

                    userEmail.setText(userEmailStr);
                    userPhone.setText(userPhoneStr);
                    userFb.setText(userFbStr);

                    followBtn = root.findViewById(R.id.followBtn);
                    unfollowBtn = root.findViewById(R.id.unfollowBtn);
                    if (current_uid.equals(img_uid)){
                        Log.d("NOTIFICATION","Current User");
                    }else{
                        followingsRf = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("followingsId").child(img_uid);
                        followingsRf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue() == null){
                                    Log.d("TEST", "onDataChange: Here We Go");
                                    followBtn.setVisibility(View.VISIBLE);
                                }else{
                                    Log.d("TEST","No not a fan");
                                    unfollowBtn.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }


                    followBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            followBtn.setVisibility(View.INVISIBLE);
                            unfollowBtn.setVisibility(View.VISIBLE);
                            //todo: add user into following list
                        }
                    });

                    unfollowBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            unfollowBtn.setVisibility(View.INVISIBLE);
                            followBtn.setVisibility(View.VISIBLE);
                            //todo: Remove ...
                        }
                    });

                }else{
                    Log.d("500", "onDataChange: Error Occurs");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("500","Cancelled");
            }
        });




        return root;
    }

}
