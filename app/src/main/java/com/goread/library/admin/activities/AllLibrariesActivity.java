package com.goread.library.admin.activities;

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
import com.goread.library.admin.adapters.LibraryAdapter;
import com.goread.library.models.LibraryProfile;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;

public class AllLibrariesActivity extends AppCompatActivity implements LibraryAdapter.AdapterCallback {
    ImageView back_btn;
    ImageView add_btn;
    RecyclerView allLibraries_recyclerView;
    List<User> userList;
    List<LibraryProfile> profileList;
    LibraryAdapter libraryAdapter;
    DatabaseReference databaseReference,databaseReference2;
    LibraryProfile profile;
    User library;
    String name, phone, address;
    AlertDialog dialog;
    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_libraries);
        defineViews();
        getProfiles();
        getUsers();

    }

    private void getProfiles() {
        try {


            databaseReference.child("Library Profile").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        profileList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            LibraryProfile profile = postSnapshot.getValue(LibraryProfile.class);
                            profileList.add(profile);
                        }
                        libraryAdapter.setProfileList(profileList);
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

    private void getUsers() {
        allLibraries_recyclerView.setHasFixedSize(true);
        allLibraries_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        userList = new ArrayList<>();
        profileList = new ArrayList<>();
        libraryAdapter = new LibraryAdapter(getApplicationContext());
        libraryAdapter.setAdapterCallback(this);
        try {


            databaseReference.child("Users").child("Library").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            User user = postSnapshot.getValue(User.class);
                            userList.add(user);
                        }

                        libraryAdapter.setUserList(userList);
                        allLibraries_recyclerView.setAdapter(libraryAdapter);

                        libraryAdapter.notifyDataSetChanged();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        allLibraries_recyclerView.setVisibility(View.VISIBLE);
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
        databaseReference = FirebaseDatabase.getInstance().getReference();
        shimmerFrameLayout = findViewById(R.id.shimmer_view_library);
        shimmerFrameLayout.startShimmer();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                finish();
            }
        });
        add_btn = findViewById(R.id.btn_add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddLibraryActivity.class));
            }
        });
        allLibraries_recyclerView = findViewById(R.id.recycler_allLibraries);

    }

    @Override
    public void phoneCall(String phone) {

    }

    @Override
    public void editData(User user, LibraryProfile profile) {
        library =user;
        this.profile =profile;
        initDialog();
    }

    public void initDialog() {
        Button btn_edit;
        EditText et_name, et_phone, et_address;
         databaseReference2 = FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_library, null);
        et_name = view.findViewById(R.id.et_library_name);
        et_address = view.findViewById(R.id.et_library_address);
        et_phone = view.findViewById(R.id.et_library_phone);
        btn_edit = view.findViewById(R.id.btn_edit);

        et_name.setText(library.getName());
        et_phone.setText(library.getPhone());
        et_address.setText(profile.getLocation());

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et_name.getText().toString();
                phone = et_phone.getText().toString();
                address = et_address.getText().toString();
                library.setName(name);
                library.setPhone(phone);
                profile.setLocation(address);

                databaseReference2.child("Library Profile").child(library.getId()).setValue(profile);
                databaseReference2.child("Users").child("Library").child(library.getId()).setValue(library).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(AllLibrariesActivity.this, "Edited", Toast.LENGTH_SHORT).show();
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