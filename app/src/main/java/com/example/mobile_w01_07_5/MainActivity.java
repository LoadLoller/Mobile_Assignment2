package com.example.mobile_w01_07_5;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author Jiale Zhang
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Photo app";
    private static final int PICK_IMAGE_REQUEST = 111;

    //private static final WindowManager.LayoutParams LOGGER = ;

    Button choosePhoto, uploadPhoto;
    ImageView imageView;
    Uri filePath;
    private static final String CACHED_FILE_NAME ="cached_data";

    ProgressDialog pd;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        choosePhoto = (Button)findViewById(R.id.choosePhoto);
        uploadPhoto = (Button)findViewById(R.id.uploadPhoto);
        imageView = findViewById(R.id.imageView);

        pd=new ProgressDialog(this);
        pd.setMessage("Uploading...");

        //switchBetweenActivities();

        choosePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);

            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(filePath!=null){
                    pd.show();
                    StorageReference childRef=mStorageRef.child("images/cat3.jpg");

                    UploadTask uploadTask=childRef.putFile(filePath);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            //Uri downloadUri=taskSnapshot.getDownloadUrl();
                            Toast.makeText(MainActivity.this,"Upload successful",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this,"Select an image",Toast.LENGTH_SHORT).show();
                }
            }


        });

    }

    private void switchBetweenActivities(){
        Button switchButton = (Button) findViewById(R.id.Backbutton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this,SwitchButton.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            filePath=data.getData();
            File cacheFile = null;
            try {
                cacheFile = File.createTempFile(CACHED_FILE_NAME, "", getCacheDir());
                copy(filePath,cacheFile);
                String cachePath = cacheFile.getAbsolutePath();

                PhotoAttrsUtil.PictureAttrs photo = PhotoAttrsUtil.getPhotoAttrs(cachePath );
                double latitude = photo.getLatitude();
                double longitude=photo.getLongitude();
                String time=photo.getTime();


                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);

                imageView.setImageBitmap(bitmap);
                TextView textView=(TextView)findViewById(R.id.photoAttribute);
                textView.setText("latitude = "+latitude+"\n"+"longitude = "+longitude+"\n"+"time = "+time);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }



    public void copy(Uri src, File dst) throws IOException {

        try (InputStream in = getContentResolver().openInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }


}