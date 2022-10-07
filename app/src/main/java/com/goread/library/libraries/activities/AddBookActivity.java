package com.goread.library.libraries.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
import com.goread.library.base.BaseActivity;
import com.goread.library.models.Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


public class AddBookActivity extends BaseActivity {

    private final int PICK_IMAGE_REQUEST = 22;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ImageView add_img;
    Button btnUpload;
    FirebaseUser firebaseUser;
    String name, desc, category, price, fileLink;
    String library_id, book_id, type;
    private Uri filePath;
    ImageView back_btn;
    Button addd_btn;
    EditText book_name_et, book_description_et, book_price_et, book_quote_et;
    ArrayList<String> drop_categoriesList = new ArrayList<>();
    ArrayAdapter<String> adapter_categories;
    AutoCompleteTextView drop_menu_categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineViews();
        type = getIntent().getStringExtra("type");

        if (type.equals("edit")) {
            Book book = (Book) getIntent().getSerializableExtra("bookDetails");

            book_id = book.getId();
            book_name_et.setText(book.getName());
            book_description_et.setText(book.getDescription());
            book_price_et.setText(String.valueOf(book.getPrice()));
            Glide.with(this)
                    .load(book.getImg_url())
                    .centerCrop()
                    .into(add_img);
            drop_menu_categories.setText(book.getCategory());

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

    @Override
    public int defineLayout() {
        return R.layout.activity_add_book;
    }

    public void defineViews() {

        book_name_et = findViewById(R.id.et_book_name);
        book_description_et = findViewById(R.id.et_book_description);
        book_price_et = findViewById(R.id.et_book_price);
        back_btn = findViewById(R.id.btn_back);
        add_img = findViewById(R.id.book_img);
        btnUpload = findViewById(R.id.btn_upload);
        drop_menu_categories = findViewById(R.id.dropdown_categories);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        library_id = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Books").child(library_id);
        drop_categoriesList = new ArrayList<>();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyBooksActivity.class));
                finish();
            }
        });


        drop_categoriesList.add(getString(R.string.drame));
        drop_categoriesList.add(getString(R.string.romanntic));
        drop_categoriesList.add(getString(R.string.Philosophy_and_Psychology));
        drop_categoriesList.add(getString(R.string.General_Knowledge));
        drop_categoriesList.add(getString(R.string.Religion));
        drop_categoriesList.add(getString(R.string.Social_Science));
        drop_categoriesList.add(getString(R.string.Languages));
        drop_categoriesList.add(getString(R.string.Classic));
        drop_categoriesList.add(getString(R.string.Science));
        drop_categoriesList.add(getString(R.string.Technology));
        drop_categoriesList.add(getString(R.string.Literature));
        drop_categoriesList.add(getString(R.string.Art_and_Recreation));
        drop_categoriesList.add(getString(R.string.history_and_geography));
        drop_categoriesList.add(getString(R.string.Action));

        adapter_categories = new ArrayAdapter<String>(getApplicationContext(), com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item, drop_categoriesList);
        drop_menu_categories.setAdapter(adapter_categories);

        drop_menu_categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                category = selectedItem;

                Toast.makeText(AddBookActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });


    }


    public boolean isValidate() {
        name = book_name_et.getText().toString();
        desc = book_description_et.getText().toString();
        price = book_price_et.getText().toString();

        if (name.isEmpty()) {
            book_name_et.requestFocus();
            return false;
        }
        if (desc.isEmpty()) {
            book_description_et.requestFocus();
            return false;
        }
        if (price.isEmpty()) {
            book_price_et.requestFocus();
            return false;
        }

        if (category.isEmpty()) {
            drop_menu_categories.requestFocus();
            return false;
        }

        if (filePath == null) {
            Toast.makeText(getApplicationContext(), "Needs Photo", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
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

                                        if (book_id != null) {
                                            Book book = new Book(book_id, name, Integer.parseInt(price), desc,
                                                    0.0, fileLink, category, library_id, false);

                                            databaseReference.child(book_id).setValue(book);
                                            book_name_et.setText(" ");
                                            book_description_et.setText(" ");
                                            book_price_et.setText(" ");
                                            drop_menu_categories.setListSelection(0);
                                        } else {


                                            //next work with URL
                                            Toast.makeText(getApplicationContext(), fileLink, Toast.LENGTH_LONG).show();

                                            String key = databaseReference.push().getKey();
                                            Book book = new Book(key, name, Integer.parseInt(price), desc,
                                                    0.0, fileLink, category, library_id, false);

                                            databaseReference.child(key).setValue(book);
                                            book_name_et.setText(" ");
                                            book_description_et.setText(" ");
                                            book_price_et.setText(" ");
                                            drop_menu_categories.setListSelection(0);

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
