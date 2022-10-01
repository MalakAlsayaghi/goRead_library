package com.goread.library.admin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.admin.adapters.AdminOrdersAdapter;
import com.goread.library.models.Order;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllOrdersActivity extends AppCompatActivity {
    ImageView back_btn;
    RecyclerView orders_recyclerView;
    AdminOrdersAdapter adminOrdersAdapter;
    List<User> userList;
    List<Order> orderList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
        defineViews();
        getData();
    }

    private void getData() {
        orders_recyclerView.setHasFixedSize(true);
        orders_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adminOrdersAdapter = new AdminOrdersAdapter(getApplicationContext());
        userList = new ArrayList<>();
        orderList = new ArrayList<>();


        try {

            databaseReference.child("Users").addValueEventListener(new ValueEventListener() {

                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        userList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                                User user = postSnapshot2.getValue(User.class);
                                if (user.getUser_type().equals("Drivers") || user.getUser_type().equals("Library")) {
                                    userList.add(user);
                                    System.out.println("Your Order: "+user.getName());

                                }

                            }
                        }


                        adminOrdersAdapter.setUserList(userList);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                }
            });
            databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {

                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        System.out.println("New change happened");
                        orderList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                                Order order = postSnapshot2.getValue(Order.class);
                                orderList.add(order);
                                System.out.println("Your Order: "+order.getDescription());

                            }
                        }


                        Collections.reverse(orderList);
                        adminOrdersAdapter.setOrderList(orderList);
                        orders_recyclerView.setAdapter(adminOrdersAdapter);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void defineViews() {
        back_btn = findViewById(R.id.btn_back);
        orders_recyclerView = findViewById(R.id.recycler_allOrders);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                finish();
            }
        });
    }
}