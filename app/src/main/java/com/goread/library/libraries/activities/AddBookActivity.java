package com.goread.library.libraries.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.goread.library.R;
import com.goread.library.activities.AllBooksActivity;
import com.goread.library.fragments.LibraryHomeFragment;
import com.goread.library.models.Book;

import java.io.IOException;
import java.util.UUID;


public class AddBookActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ImageView add_img;
    EditText et_name, et_desc, et_brand, et_price;
    Button btnUpload;
    FirebaseUser firebaseUser;
    String name, desc, brand, price, fileLink;
    String library_id, book_id, type;
    private Uri filePath;
    ImageView back_btn;
    Button addd_btn;
    CheckBox cb_GeneralKnowledge ,cb_Philosophy_Psychology,cb_Religion,cb_SocialScience,
            cb_classic,cb_Languages,cb_Science,cb_Technology,cb_Art_Recreation,
            cb_Literature,cb_History_Geography;
    EditText book_name_et ,book_description_et, book_price_et, book_quote_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        defineViews();
        getCategory();
        type = getIntent().getStringExtra("type");

        if (type.equals("edit")) {
            Book book = (Book) getIntent().getSerializableExtra("bookDetails");

            book_id = book.getId();
            et_name.setText(book.getName());
            et_desc.setText(book.getDescription());
            et_price.setText(String.valueOf(book.getPrice()));

        }

        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // uploadImage();
                upload();
            }
        });
    }

   public void defineViews() {
        //add_img = findViewById(R.id.add_image);
        //et_name = findViewById(R.id.upload_name);
        //et_desc = findViewById(R.id.upload_desc);
        //et_price = findViewById(R.id.upload_price);
       // btnUpload = findViewById(R.id.upload_btn);
        //cb_classic = findViewById(R.id.cb_classic);
        //cb_drama = findViewById(R.id.cb_drama);
        //cb_action = findViewById(R.id.cb_action);
        //cb_romantic = findViewById(R.id.cb_romantic);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        library_id = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Books").child(library_id);

       back_btn=findViewById(R.id.btn_back);
       back_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), AllBooksActivity.class));
               finish();
           }
       });
       addd_btn=findViewById(R.id.btn_addd);
       addd_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //startActivity(new Intent(getApplicationContext(),.class));
               finish();
           }
       });

       book_name_et=findViewById(R.id.et_book_name);
       book_name_et.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

       book_description_et=findViewById(R.id.et_book_description);
       book_description_et.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

       book_price_et=findViewById(R.id.et_book_price);
       book_price_et.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

       book_quote_et=findViewById(R.id.et_book_quote);
       book_quote_et.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });
   }

    public String getCategory() {
        String category = null;

        if (cb_GeneralKnowledge.isChecked()) {
            category = "General Knowledge";
        } else if (cb_Philosophy_Psychology.isChecked()) {
            category = "Philosophy & Psychology";
        } else if (cb_Religion.isChecked()) {
            category = "Religion";
        } else if (cb_SocialScience.isChecked()) {
            category = "SocialScience";
        } else if (cb_classic.isChecked()) {
            category = "classic";
        } else if (cb_Languages.isChecked()) {
            category = "Languages";
        } else if (cb_Science.isChecked()) {
            category = "Science";
        } else if (cb_Technology.isChecked()) {
            category = "Technology";
        } else if (cb_Art_Recreation.isChecked()) {
            category = "Art & Recreation";
        } else if (cb_Literature.isChecked()) {
            category = "Literature";
        } else if (cb_History_Geography.isChecked()) {
            category = "History & Geography";
        } else {
            Toast.makeText(this, R.string.choose_category, Toast.LENGTH_SHORT).show();
        }
        return category;
    }

    public boolean isValidate() {
        if (name.isEmpty()) {
            et_name.requestFocus();
            return false;
        }
        if (desc.isEmpty()) {
            et_desc.requestFocus();
            return false;
        }
        if (price.isEmpty()) {
            et_price.requestFocus();
            return false;
        }

        if (filePath == null) {
            Toast.makeText(getApplicationContext(), "Needs Photo", Toast.LENGTH_SHORT).show();
            return false;
        }


        String myCat = getCategory();
        if (myCat == null) {
            return false;
        } else {
            return true;
        }
    }

    public void upload() {

        name = et_name.getText().toString();
        desc = et_desc.getText().toString();
        price = et_price.getText().toString();

        if (!isValidate()) {
            return;
        }


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

                                    if (book_id != null) {
                                        Book book = new Book(book_id, name, Integer.parseInt(price), desc,
                                                0.0, fileLink, "Drama", library_id, false);

                                        databaseReference.child(book_id).setValue(book);
                                    } else {


                                        //next work with URL
                                        Toast.makeText(getApplicationContext(), fileLink, Toast.LENGTH_LONG).show();


                                        String key = databaseReference.push().getKey();
                                        Book book = new Book(key, name, Integer.parseInt(price), desc,
                                                0.0, fileLink, "Drama", library_id, false);

                                        databaseReference.child(key).setValue(book);

                                    }
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


    // Select Image method
    private void SelectImage() {

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
                add_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}
