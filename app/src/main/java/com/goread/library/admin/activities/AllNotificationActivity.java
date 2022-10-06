package com.goread.library.admin.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.admin.adapters.NotificationAdapter;
import com.goread.library.base.BaseActivity;

import java.util.ArrayList;

public class AllNotificationActivity extends BaseActivity {
    RecyclerView recyclerView;
    List<Notification> list;
    DatabaseReference databaseReference;
    NotificationAdapter adapter;
    ImageView btnAdd ,back_btn;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.recycler_quotes);
        btnAdd = findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog();
            }
        });
        back_btn = findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Notifications");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, list);
        recyclerView.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    list.add(notification);
                }
                adapter.notifyDataSetChanged();
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int defineLayout() {
        return R.layout.activity_all_notification;
    }


    public void initDialog() {
        Button btn_send;
        EditText title;
        EditText body;
        DatabaseReference dbnotification = FirebaseDatabase.getInstance().getReference("Notifications");

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_notification, null);
        title = (EditText) view.findViewById(R.id.titleNotification);
        body = (EditText) view.findViewById(R.id.bodyNotification);
        btn_send = view.findViewById(R.id.btn_add_Notification);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String theTitle = title.getText().toString().trim();
                String theBody = body.getText().toString().trim();
                if (!TextUtils.isEmpty(theTitle)) {
                    String id = dbnotification.push().getKey();
                    Notification notification = new Notification(id, theTitle, theBody);
                    dbnotification.child(id).setValue(notification);
                    dialog.cancel();

                    Toast.makeText(getApplicationContext(), "notification added", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "you have to enter a title and a text", Toast.LENGTH_LONG).show();


            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }


}


