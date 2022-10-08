package com.goread.library.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

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

import java.util.concurrent.Executor;

public class LoginActivity extends BaseActivity {
    String email, password;
    Button loginBtn;
    EditText et_email, et_password;
    FirebaseAuth auth;
    TextView register, forget;
    DatabaseReference reference;
    ProgressBar progressBar;
    AlertDialog dialog, dialog2;
    BiometricManager biometricManager;
    ImageButton btn_finger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defineViews();
        fingerPrint();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();

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

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                                        User user1 = postSnapshot2.getValue(User.class);

                                        if (user1.getId().equals(id)) {
                                            String type;
                                            //   type = snapshot.child("user_type").getValue(String.class);
                                            type = user1.getUser_type();
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
                                            setFingerprintEnabled();

                                            if (type.equals("Library")) {
                                                saveLoginData(email, password);
                                                Intent intent = new Intent(LoginActivity.this, LibraryMainActivity.class);
                                                saveObjectToSharedPreference(user1);
                                                startActivity(intent);
                                                finish();
                                            }

                                            if (type.equals("Admin")) {

                                                saveLoginData(email, password);
                                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                                saveObjectToSharedPreference(user1);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }


                                    }
                                }


                            } else {
                                showProgress(false);
                                showErrorDialog("Couldn't find data in the database");
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
                System.out.println("Moha Error" + e.getMessage());
                if (e.getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                    showProgress(false);
                    showErrorDialog(getString(R.string.you_inserted_wrong_or_invalidinformation_please_try_again));
                } else {
                    showProgress(false);
                    showErrorDialog(e.getMessage());
                }
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
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.btn_signIn);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_finger = findViewById(R.id.btn_fingerprint);
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
        View view = inflater.inflate(R.layout.reset_password_dialog, null);
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
                                                                 btn_reset.setVisibility(View.GONE);
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
        btn_cancel = view.findViewById(R.id.btnCancel);
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

    public void fingerPrint() {
        biometricManager = BiometricManager.from(this);

        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }


            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                SharedPreferences mPrefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String sharedEmail = mPrefs.getString("email", "test");
                String sharedPassword = mPrefs.getString("password", "test");

                signingIn(sharedEmail, sharedPassword);


            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Ehya")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        btn_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);


                Boolean isEnabled = sharedPreferences.getBoolean("enabled", false);
                if (isEnabled) {
                    biometricPrompt.authenticate(promptInfo);
                    switch (biometricManager.canAuthenticate()) {

                        // this means we can use biometric sensor
                        case BiometricManager.BIOMETRIC_SUCCESS:


                            break;

                        // this means that the device doesn't have fingerprint sensor
                        case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                            Toast.makeText(getApplicationContext(), "Your device doesn't has sesnor", Toast.LENGTH_LONG).show();

                            break;

                        // this means that biometric sensor is not available
                        case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:

                            Toast.makeText(getApplicationContext(), "Your device doesn't has sesnor", Toast.LENGTH_LONG).show();

                            break;

                        // this means that the device doesn't contain your fingerprint
                        case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:

                            Toast.makeText(getApplicationContext(), "Your device doesn't has sesnor", Toast.LENGTH_LONG).show();

                            break;
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "No Saved user to login by fingerprint", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void saveLoginData(String email, String password) {
        SharedPreferences mPrefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("email", email);
        prefsEditor.putString("password", password);
        prefsEditor.commit();
    }

    public void setFingerprintEnabled() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean("enabled", true);
        myEdit.commit();

    }


}