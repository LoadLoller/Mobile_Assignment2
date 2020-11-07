package com.example.mobile_w01_07_5.ui.dashboard;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobile_w01_07_5.R;

// note: this is actually the stamp on map
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.mobile_w01_07_5.data.StampData;
import com.example.mobile_w01_07_5.data.StampItem;
import com.example.mobile_w01_07_5.ui.Adapters.StampsAdapter;
import com.example.mobile_w01_07_5.ui.home.HomeFragmentDirections;
import com.example.mobile_w01_07_5.ui.home.ProductInformationFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DashboardFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    private MapView mMapView;
    private View root;
    private ArrayList<StampItem> stampList = new ArrayList<StampItem>();
    private static final int LOCATION_REQUEST = 9158;
    private final String TAG = "DashboardFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        String stampBucket = "gs://mobile-assignment2.appspot.com";
        StorageReference mStoRef = mStorage.getReferenceFromUrl(stampBucket).child("images");

        root = inflater.inflate(R.layout.activity_maps, container, false);

        mMapView = (MapView) root.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        MapsInitializer.initialize(getActivity().getApplicationContext());

//        stampList = new ArrayList<StampItem>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Stamps/stamp");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Structure: HashMap -> HashMap -> userID -> value: String...

                HashMap<Object, Object> stampsList = (HashMap<Object, Object>)snapshot.getValue();
                Object[] keys = stampsList.keySet().toArray();
                stampList.clear();
                if (mMap != null){
                    mMap.clear();
                }

                for (int i = 0; i < stampsList.size(); i++)
                {
                    //the key is acturally the user id
                    //but it's not filled in the user_id section in the firebase?
                    Object key = keys[i];
//                    Log.d(TAG, key.toString());
                    HashMap<String, Object> stamp = (HashMap<String, Object>) stampsList.get(key);
                    String stampID = stamp.get("stampID").toString();
                    String userID = stamp.get("userID").toString();
                    String name = stamp.get("name").toString();
                    int rate = Integer.parseInt(stamp.get("rate").toString());
                    String description = stamp.get("description").toString();
                    double locationX = Double.parseDouble(stamp.get("locationX").toString());
//                    Log.d(TAG, "x: " + locationX);
                    double locationY = Double.parseDouble(stamp.get("locationY").toString());
                    String photo = stamp.get("photo").toString();
                    StorageReference photoUrl = mStoRef.child(photo);
                    int index = i;
                    photoUrl.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            boolean isHighlyRated = Boolean.parseBoolean(stamp.get("highlyRated").toString());
                            StampItem item = new StampItem(stampID, userID, name, rate, description,
                                    locationX, locationY, uri, isHighlyRated);
                            stampList.add(item);

                            if (mMap != null)
                            {
                                //add stamp to map
                                LatLng stamp_pos = new LatLng(item.getLocationX(), item.getLocationY());
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(stamp_pos)
                                        .title(item.getName())
                                        .snippet(item.getDescription()));
                                marker.setTag(stampID);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Log.d(TAG, "firebase fail in dashboard fragment");
                        }
                    });

                }

                if (mMap != null){
                    //Add marker for melb uni to say hi:)
                    LatLng uni_postion = new LatLng(-37.7983459, 144.9598797);
                    mMap.addMarker(new MarkerOptions()
                            .position(uni_postion)
                            .title("The_Dev_Team @ UniMelb")
                            .snippet("Hi from University of Melbourne!"));

                    LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location current_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    //Add a marker at the user's current position based on GPS information
//                    if (current_location != null) {
//                        LatLng current_position = new LatLng(current_location.getLatitude(), current_location.getLongitude());
//                        mMap.addMarker(new MarkerOptions().position(current_position).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_position));
//                        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "firebase cancelled in dashboard fragment");
            }
        };

        ref.addValueEventListener(postListener);

        mMapView.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    LOCATION_REQUEST);
        }

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location current_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Add marker for melb uni to say hi:)
        LatLng uni_postion = new LatLng(-37.7983459, 144.9598797);
        mMap.addMarker(new MarkerOptions()
                .position(uni_postion)
                .title("The_Dev_Team @ UniMelb")
                .snippet("Hi from University of Melbourne!"));

        // Add markers in the list
        for (int i = 0; i < stampList.size(); i++)
        {
            StampItem stamp = stampList.get(i);
            LatLng stamp_pos = new LatLng(stamp.getLocationX(), stamp.getLocationY());
//            Log.d(TAG, "reading 2 pos: " + stamp_pos.toString());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(stamp_pos)
                    .title(stamp.getName())
                    .snippet(stamp.getDescription()));
            marker.setTag(stamp.getStampID());
        }

        //Add a marker at the user's current position based on GPS information
//        if (current_location != null) {
//            LatLng current_position = new LatLng(current_location.getLatitude(), current_location.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(current_position).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(current_position));
//            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //jump to stamp info page
                if (marker.getTag() != null)
                {
                    String stampID = marker.getTag().toString();
                    NavDirections action = new NavDirections() {
                        @Override
                        public int getActionId() {
                            return R.id.action_navigation_map_to_productInfo;
                        }

                        @NonNull
                        @Override
                        public Bundle getArguments() {
                            Bundle b =  new Bundle();
                            b.putString("productCodeArgument", stampID);
                            return b;
                        }
                    };
                    Navigation.findNavController(getView()).navigate(action);
                }
            }
        });
    }

}