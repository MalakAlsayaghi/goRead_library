package com.goread.library.admin.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.admin.adapters.DriverAdapter;
import com.goread.library.admin.adapters.UsersAdapter;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;

public class AllDriversActivity extends AppCompatActivity implements UsersAdapter.AdapterCallback, DriverAdapter.AdapterCallback {
    ImageView back_btn;
    ImageView add_btn;
    RecyclerView allDrivers_recyclerView;
    DriverAdapter driverAdapter;
    List<User> userList;
    DatabaseReference databaseReference;
    AlertDialog dialog;
    String name, phone;
    User driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_drivers);
        defineViews();
        getUsers();

    }

    private void getUsers() {
        allDrivers_recyclerView.setHasFixedSize(true);
        allDrivers_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        userList = new ArrayList<>();
        driverAdapter = new DriverAdapter(getApplicationContext());
        driverAdapter.setAdapterCallback(this);
        try {


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            User user = postSnapshot.getValue(User.class);
                            userList.add(user);
                        }

                        driverAdapter.setUserList(userList);
                        allDrivers_recyclerView.setAdapter(driverAdapter);


                        driverAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void defineViews() {
        back_btn = findViewById(R.id.btn_back);
        allDrivers_recyclerView = findViewById(R.id.recycler_allDrivers);
        add_btn = findViewById(R.id.btn_add);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Drivers");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                finish();
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddDriverActivity.class));
            }
        });
    }

    @Override
    public void editData(User user) {
        driver = user;
        initDialog();
    }

    @Override
    public void phoneCall(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if (ContextCompat.checkSelfPermission(AllDriversActivity.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AllDriversActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    101);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            try {
                startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public void initDialog() {
        Button btn_edit;
        EditText et_name, et_phone;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Drivers");

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_driver, null);
        et_name = view.findViewById(R.id.et_name);
        et_phone = view.findViewById(R.id.et_phone);
        btn_edit = view.findViewById(R.id.btn_edit);

        et_name.setText(driver.getName());
        et_phone.setText(driver.getPhone());

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et_name.getText().toString();
                phone = et_phone.getText().toString();
                driver.setName(name);
                driver.setPhone(phone);

                databaseReference.child(driver.getId()).setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AllDriversActivity.this, "Edited", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });


            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }

}