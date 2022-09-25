package com.goread.library.admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.admin.activities.Notification;
import com.goread.library.admin.adapters.NotificationAdapter;

import java.util.ArrayList;

public class AllNotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Notification> list;
    DatabaseReference databaseReference;
    NotificationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notification);
        recyclerView=findViewById(R.id.recycler_quotes);
        databaseReference= FirebaseDatabase.getInstance().getReference("Notification");

        list=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new NotificationAdapter(this,list);
        recyclerView.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    list.add(notification);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }


    }


