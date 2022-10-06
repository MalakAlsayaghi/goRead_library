package com.goread.library.admin.activities;

import static com.goread.library.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.goread.library.R;
import com.goread.library.base.BaseActivity;
import com.goread.library.models.LibraryProfile;
import com.goread.library.models.User;

import java.io.IOException;
import java.util.UUID;

public class AddLibraryActivity extends BaseActivity implements OnMapReadyCallback {
    ImageView back_btn;
    Button add_btn;
    EditText et_library_name, et_library_address, et_library_phone, et_library_email;
    ImageButton edit_pic;
    private final int PICK_IMAGE_REQUEST = 22, PICK_FILE_REQUEST = 40;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ImageView add_img, libraryImg;
    Button btnUpload;
    FirebaseUser firebaseUser;
    String name, email, phone, location, password = "123456", fileLink, locationId;
    private Uri filePath;
    FirebaseAuth firebaseAuth;
    Bitmap myBitmap;
    private MapView mMapView;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    boolean mLocationPermissionGranted = false;
    MarkerOptions markerOptions;
    LatLng selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineViews();
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        // cardView = findViewById(R.id.test);
        mMapView.getMapAsync(this);
        getLocationPermission();


        edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

    }

    @Override
    public int defineLayout() {
        return R.layout.activity_add_library;
    }


    public void upload() {
        if (isValidate()) {


            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                            new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        fileLink = task.getResult().toString();
                                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                String id = firebaseAuth.getCurrentUser().getUid();
                                                User user = new User(id, name, phone, email, "Library", "token");
                                                LibraryProfile profile = new LibraryProfile(id, name, fileLink, phone, location, true, 0.0);
                                                uploadLocation(id);
                                                databaseReference.child("Library Profile").child(id).setValue(profile);
                                                databaseReference.child("Users").child("Library").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(AddLibraryActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AddLibraryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    progressDialog.dismiss();

                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int) progress + "%");
                    System.out.println("Your progress is: " + progress);
                }
            });
        }
    }


    // Select Image method
    private void selectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);


        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                libraryImg.setImageBitmap(bitmap);
                myBitmap = bitmap;
                //saveImage(bitmap,"MyQr");
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_FILE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);

                //     saveImage(bitmap);

                System.out.println("I saved it");

            } catch (Exception e) {

            }
        }
    }

    public boolean isValidate() {
        name = et_library_name.getText().toString();
        phone = et_library_phone.getText().toString();
        email = et_library_email.getText().toString();
        location = et_library_address.getText().toString();

        if (name.isEmpty()) {
            et_library_name.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            et_library_phone.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            et_library_email.requestFocus();
            return false;
        }
        if (location.isEmpty()) {
            et_library_address.requestFocus();
            return false;
        }
        if (filePath == null) {
            Toast.makeText(this, "Should have photo", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void defineViews() {
        back_btn = findViewById(R.id.btn_back);
        add_btn = findViewById(R.id.btn_add);
        et_library_name = findViewById(R.id.et_library_name);
        et_library_address = findViewById(R.id.et_library_address);
        et_library_email = findViewById(R.id.et_library_email);
        et_library_phone = findViewById(R.id.et_library_phone);


        libraryImg = findViewById(R.id.library_img);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





        edit_pic = findViewById(R.id.edit_pic);


    }

    private void uploadLocation(String libraryId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locations").child("Libraries");
        databaseReference.child(libraryId).child("latitude").setValue(selectedPlace.latitude);
        databaseReference.child(libraryId).child("longitude").setValue(selectedPlace.longitude);
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        double latitude = 15.288740;
        double longitude = 44.2026459;


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (markerOptions != null) {
                    map.clear();
                }
                markerOptions = new MarkerOptions().position(latLng).title("");
                selectedPlace = latLng;
                System.out.println(selectedPlace);
                map.addMarker(markerOptions);


            }
        });

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("Location not enabled");
            return;
        }
        map.setMyLocationEnabled(true);


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


}