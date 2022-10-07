package com.goread.library.admin.activities;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import androidx.appcompat.widget.SearchView;
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
import com.goread.library.admin.adapters.AdminAdapter;
import com.goread.library.base.BaseActivity;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;

public class AllAdminsActivity extends BaseActivity implements AdminAdapter.AdapterCallback {
    ImageView back_btn;
    ImageView add_btn;
    RecyclerView allAdmins_recyclerView;
    AdminAdapter adminAdapter;
    List<User> userList;
    DatabaseReference databaseReference;
    AlertDialog dialog;
    String name, phone;
    User Admin;
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
                adminAdapter.filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adminAdapter != null)
                    adminAdapter.filter(newText);
                return true;
            }
        });

    }

    @Override
    public int defineLayout() {
        return R.layout.activity_all_admins;
    }

    private void getUsers() {
        allAdmins_recyclerView.setHasFixedSize(true);
        allAdmins_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        userList = new ArrayList<>();
        adminAdapter = new AdminAdapter(getApplicationContext());
        adminAdapter.setAdapterCallback(this);
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

                        adminAdapter.setUserList(userList);
                        allAdmins_recyclerView.setAdapter(adminAdapter);
                        adminAdapter.notifyDataSetChanged();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        allAdmins_recyclerView.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(AllAdminsActivity.this, "NO admins found", Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        allAdmins_recyclerView.setVisibility(View.VISIBLE);
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
        allAdmins_recyclerView = findViewById(R.id.recycler_allAdmins);
        add_btn = findViewById(R.id.btn_add);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_library);
        shimmerFrameLayout.startShimmer();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Admins");
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
                startActivity(new Intent(getApplicationContext(), AddAdminActivity.class));
            }
        });
    }

    @Override
    public void editData(User user) {
        Admin = user;
        initDialog();
    }


    public void initDialog() {
        Button btn_edit;
        EditText et_name, et_phone;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Admins");

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_driver, null);
        et_name = view.findViewById(R.id.et_name);
        et_phone = view.findViewById(R.id.et_phone);
        btn_edit = view.findViewById(R.id.btn_edit);

        et_name.setText(Admin.getName());
        et_phone.setText(Admin.getPhone());

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et_name.getText().toString();
                phone = et_phone.getText().toString();
                Admin.setName(name);
                Admin.setPhone(phone);

                databaseReference.child(Admin.getId()).setValue(Admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AllAdminsActivity.this, "Edited", Toast.LENGTH_SHORT).show();
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