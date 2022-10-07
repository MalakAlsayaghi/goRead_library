package com.goread.library.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.goread.library.R;
import com.goread.library.admin.activities.AdminMainActivity;
import com.goread.library.base.BaseActivity;
import com.goread.library.libraries.activities.LibraryMainActivity;
import com.goread.library.models.User;

public class LoginActivity extends BaseActivity {
    String email, password;
    Button loginBtn;
    EditText et_email, et_password;
    FirebaseAuth auth;
    TextView register, forget;
    DatabaseReference reference;
    ProgressBar progressBar;
    AlertDialog dialog, dialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @Override
    public int defineLayout() {
        return R.layout.activity_login;

    }

    public void signingIn(String email, String password) {
        showProgress(true);
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
                                User user1 = snapshot.getValue(User.class);
                                type = snapshot.child("user_type").getValue(String.class);
                                Toast.makeText(LoginActivity.this, type, Toast.LENGTH_SHORT).show();
                                if (user1.isBlocked()) {
                                    showProgress(false);
                                    showErrorDialog("You're Blocked By Admin");
                                    return;
                                }

                                if (user1.isNew()) {
                                    showErrorDialog("You are new you have to change password");
                                    showProgress(false);
                                    return;
                                }


                                if (type.equals("Library")) {
                                    Intent intent = new Intent(LoginActivity.this, LibraryMainActivity.class);
                                    saveObjectToSharedPreference(user1);
                                    startActivity(intent);
                                    finish();
                                }

                                if (type.equals("Admin")) {
                                    Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                    saveObjectToSharedPreference(user1);
                                    startActivity(intent);
                                    finish();
                                }


                            } else {
                                showProgress(false);
                                showErrorDialog("Invalid Information");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    if (!user.isEmailVerified()) {
                        user.sendEmailVerification();

                        //Toast.makeText(LoginActivity.this, "Verification sent..", Toast.LENGTH_LONG).show();

                    }
                } else {
                    showProgress(false);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                showProgress(false);
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
        progressBar = findViewById(R.id.progressBar);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog();
            }
        });
    }

    private void showProgress(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    public void saveObjectToSharedPreference(Object object) {
        SharedPreferences mPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString("Key", json);
        prefsEditor.commit();


    }

    public void initDialog() {
        Button btn_reset, btn_cancel;
        TextView tvMessage;
        EditText et_email;

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_driver, null);
        et_email = view.findViewById(R.id.et_email);
        btn_reset = view.findViewById(R.id.btn_reset);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        tvMessage = view.findViewById(R.id.tvResetMessage);

        btn_reset.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             FirebaseAuth.getInstance().sendPasswordResetEmail(et_email.getText().toString())
                                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if (task.isSuccessful()) {
                                                                 Toast.makeText(LoginActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
                                                                 btn_reset.setVisibility(View.INVISIBLE);
                                                                 tvMessage.setText("We Sent you an email for resetting password");
                                                             }
                                                         }
                                                     });
                                         }
                                     }
        );

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }

    public void showErrorDialog(String text) {
        MaterialButton btn_cancel;
        TextView tvMessage;

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.wrong_data_dialog, null);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(text);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.cancel();
            }
        });


        builder.setView(view);
        dialog2 = builder.create();
        dialog2.show();

    }

}