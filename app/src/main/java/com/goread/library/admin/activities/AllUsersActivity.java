package com.goread.library.admin.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.admin.adapters.UsersAdapter;
import com.goread.library.libraries.adapters.QuotesAdapter;
import com.goread.library.models.Quote;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;

public class AllUsersActivity extends AppCompatActivity implements UsersAdapter.AdapterCallback {
    ImageView back_btn;
    RecyclerView allUsers_recyclerView;
    UsersAdapter usersAdapter;
    List<User> userList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        defineViews();
        getUsers();

    }

    private void defineViews() {
        back_btn = findViewById(R.id.btn_back);
        allUsers_recyclerView = findViewById(R.id.recycler_allUsers);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                finish();
            }
        });
    }

    private void getUsers() {
        allUsers_recyclerView.setHasFixedSize(true);
        allUsers_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        userList = new ArrayList<>();
        usersAdapter = new UsersAdapter(getApplicationContext());
        usersAdapter.setAdapterCallback(this);
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

                        usersAdapter.setUserList(userList);
                        allUsers_recyclerView.setAdapter(usersAdapter);


                        usersAdapter.notifyDataSetChanged();
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

    @Override
    public void phoneCall(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if (ContextCompat.checkSelfPermission(AllUsersActivity.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AllUsersActivity.this,
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
}