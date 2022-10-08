package com.goread.library.admin.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.goread.library.R;
import com.goread.library.base.BaseActivity;
import com.goread.library.models.User;

public class AddAdminActivity extends BaseActivity  {
    ImageView back_btn;
    Button add_btn;
    EditText et_admin_name, et_admin_phone, et_admin_email;

    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String name, email, phone, password = "123456";
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineViews();


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

    }
    @Override
    public int defineLayout() {
        return R.layout.activity_add_admin;
    }

    private void upload() {
        if (isValidate()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String id = firebaseAuth.getCurrentUser().getUid();
                    User user = new User(id, name, phone, email, "Admin", "token");
                    databaseReference.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddAdminActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    public boolean isValidate() {
        name = et_admin_name.getText().toString();
        phone = et_admin_phone.getText().toString();
        email = et_admin_email.getText().toString();

        if (name.isEmpty()) {
            et_admin_name.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            et_admin_phone.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            et_admin_email.requestFocus();
            return false;
        }


        return true;
    }

    private void defineViews() {
        back_btn = findViewById(R.id.btn_back);
        add_btn = findViewById(R.id.btn_add);
        et_admin_name = findViewById(R.id.et_name);
        et_admin_email = findViewById(R.id.et_email);
        et_admin_phone = findViewById(R.id.et_phone);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Admins");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}

