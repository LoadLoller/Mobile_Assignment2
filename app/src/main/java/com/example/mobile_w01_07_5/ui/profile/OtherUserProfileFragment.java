package com.example.mobile_w01_07_5.ui.profile;

import android.os.Bundle;
import android.util.Log;
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
import com.example.mobile_w01_07_5.ui.home.ProductInformationFragmentArgs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class OtherUserProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private StorageReference mStorageRef;
    private ImageView img;
    private DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.other_user_profile, container, false);

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

        String userId = OtherUserProfileFragmentArgs.fromBundle(getArguments()).getUserIDArgument();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
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

        return root;
    }
}
