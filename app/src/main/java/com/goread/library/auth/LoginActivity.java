package com.goread.library.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.libraries.activities.LibraryMainActivity;

public class LoginActivity extends AppCompatActivity {
    String email, password;
    Button loginBtn;
    EditText et_email, et_password;
    FirebaseAuth auth;
    TextView register, forget;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        defineViews();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString();
                password = et_password.getText().toString();

                if (isValidate()) {
                    signingIn(email, password);
                }
            }
        });


    }

    public void signingIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String id = user.getUid();

                    reference.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String type;
                                type = snapshot.child("user_type").getValue(String.class);
                                Toast.makeText(LoginActivity.this, type, Toast.LENGTH_SHORT).show();


                                if (type.equals("Library")) {
                                    Intent intent = new Intent(LoginActivity.this, LibraryMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Not A library", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    if (!user.isEmailVerified()) {
                        //  user.sendEmailVerification();

                        //Toast.makeText(LoginActivity.this, "Verification sent..", Toast.LENGTH_LONG).show();

                    }
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public boolean isValidate() {
        if (email.isEmpty()) {
            et_email.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            et_password.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public void defineViews() {
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Library");
        auth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.btn_signIn);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        //register = findViewById(R.id.tv_signUp);
        forget = findViewById(R.id.tv_forget);


    }
}