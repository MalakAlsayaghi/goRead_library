package com.goread.library.admin.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.admin.adapters.UsersAdapter;
import com.goread.library.base.BaseActivity;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllUsersActivity extends BaseActivity implements UsersAdapter.AdapterCallback {
    ImageView back_btn;
    RecyclerView allUsers_recyclerView;
    UsersAdapter usersAdapter;
    List<User> userList;
    DatabaseReference databaseReference;
    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineViews();
        getUsers();

        SearchManager searchManager =
                (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) findViewById(R.id.search_bar);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                usersAdapter.filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (usersAdapter != null)
                    usersAdapter.filter(newText);
                return true;
            }
        });


    }

    @Override
    public int defineLayout() {
        return R.layout.activity_all_users;
    }

    private void defineViews() {
        back_btn = findViewById(R.id.btn_back);
        allUsers_recyclerView = findViewById(R.id.recycler_allUsers);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers");
        shimmerFrameLayout = findViewById(R.id.shimmer_view_library);
        shimmerFrameLayout.startShimmer();

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
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        allUsers_recyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public void delete(String id) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Can't  recover this item!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Users").child("Customers").child(id).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            sDialog.setTitleText("Deleted!")
                                                    .setContentText("Your Product has been deleted!")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(null)
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        }
                                    }
                                });


                    }
                }).show();

    }
}