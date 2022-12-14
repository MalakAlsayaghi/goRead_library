package com.goread.library.admin.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
import com.goread.library.activities.MapActivity;
import com.goread.library.base.BaseActivity;
import com.goread.library.models.LibraryProfile;
import com.goread.library.models.LocationUpload;
import com.goread.library.models.User;

import java.io.IOException;
import java.util.UUID;

public class AddLibraryActivity extends BaseActivity  {
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

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
    TextInputLayout textInputLayout;
    LocationUpload locationUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineViews();

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
                                                            finish();
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

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Get String data from Intent
                locationUpload = (LocationUpload) data.getSerializableExtra("selected_location");
                Toast.makeText(this, "You chose : "+locationUpload.getLatitude(), Toast.LENGTH_SHORT).show();
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
        textInputLayout = findViewById(R.id.locationInput);
        edit_pic = findViewById(R.id.edit_pic);
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

        textInputLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });


    }


    private void uploadLocation(String libraryId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locations").child("Libraries");
        databaseReference.child(libraryId).child("latitude").setValue(locationUpload.getLatitude());
        databaseReference.child(libraryId).child("longitude").setValue(locationUpload.getLongitude());
    }


}