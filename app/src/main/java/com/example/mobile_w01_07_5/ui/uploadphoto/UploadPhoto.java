package com.example.mobile_w01_07_5.ui.uploadphoto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.mobile_w01_07_5.MainActivity;
import com.example.mobile_w01_07_5.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadPhoto extends AppCompatActivity {

    private static final String TAG = "Photo app";
    private static final int PICK_IMAGE_REQUEST = 111;
    private static final int CAMERA_REQUEST = 101;

    private String currentPhotoPath;

    //private static final WindowManager.LayoutParams LOGGER = ;
    Photohelper photo= new Photohelper();
    private double Latitude;
    private double Longitude;
    private String time;
    Button choosePhoto, takephoto, uploadPhoto;
    ImageView imageView;
    TextView textView;
    Uri filePath;
    private static final String CACHED_FILE_NAME ="cached_data";

    ProgressDialog pd;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        choosePhoto = findViewById(R.id.choosePhoto);
        takephoto=findViewById(R.id.TakePhotoButton);
        uploadPhoto = findViewById(R.id.uploadPhoto);
        imageView = findViewById(R.id.imageView);
        textView=findViewById(R.id.DescriptionText);

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

        takephoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(UploadPhoto.this,
                                "com.example.mobile_w01_07_5",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    }
                }


            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(filePath!=null){
                    pd.show();
                    String imageName=filePath.getPath();
                    int index = imageName.lastIndexOf("/");
                    imageName=imageName.substring(index+1);
                    String desription = textView.getText().toString();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    photo.setDescription(desription);
                    photo.setHighlyRated(false);
                    photo.setLocationX(Latitude);
                    photo.setLocationY(Longitude);
                    photo.setStampID(imageName);
                    photo.setPhoto(imageName);
                    //photo.setUserID(currentUser.toString());
                    //System.out.println(currentUser);
                    TextView photoName = findViewById(R.id.PhotoName);
                    String Name = photoName.getText().toString();
                    photo.setName(Name);
                    int new_index = imageName.lastIndexOf(".");
                    String title=imageName.substring(0,new_index);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Stamps").child("stamp");
                    DatabaseReference stampRef = myRef.child(title);
                    stampRef.setValue(photo);

                    StorageReference childRef=mStorageRef.child("images/"+imageName);
                    UploadTask uploadTask=childRef.putFile(filePath);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            //Uri downloadUri=taskSnapshot.getDownloadUrl();
                            Toast.makeText(com.example.mobile_w01_07_5.ui.uploadphoto.UploadPhoto.this,"Upload successful",Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else{
                    Toast.makeText(com.example.mobile_w01_07_5.ui.uploadphoto.UploadPhoto.this,"Select an image",Toast.LENGTH_SHORT).show();
                }
            }


        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(com.example.mobile_w01_07_5.ui.uploadphoto.UploadPhoto.this, MainActivity.class);
        startActivity(intent);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        filePath=contentUri;
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic(int rotate) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        Matrix matrix = new Matrix();
        matrix.setRotate(rotate);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST&&resultCode==RESULT_OK) {
            galleryAddPic();
            PhotoAttrsUtil.PictureAttrs photo = PhotoAttrsUtil.getPhotoAttrs(currentPhotoPath );
            Latitude=photo.getLatitude();
            Longitude=photo.getLongitude();
            int orientation=Integer.parseInt(photo.getOrientation());
            int rotate = 0;

            switch (orientation) {
                default:
                    rotate=0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            setPic(rotate);
        }
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            filePath=data.getData();
            File cacheFile = null;
            try {
                cacheFile = File.createTempFile(CACHED_FILE_NAME, "", getCacheDir());
                copy(filePath,cacheFile);
                String cachePath = cacheFile.getAbsolutePath();
                PhotoAttrsUtil.PictureAttrs photo = PhotoAttrsUtil.getPhotoAttrs(cachePath );
                Latitude=photo.getLatitude();
                Longitude=photo.getLongitude();
                int orientation=Integer.parseInt(photo.getOrientation());
                int rotate = 0;

                switch (orientation) {
                    default:
                        rotate=0;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }

                // time=photo.getTime();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                Matrix matrix = new Matrix();
                matrix.setRotate(rotate);
                // 重新绘制Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
                imageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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