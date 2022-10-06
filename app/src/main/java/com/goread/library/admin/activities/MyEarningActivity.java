package com.goread.library.admin.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.goread.library.admin.adapters.OrdersAdapter;
import com.goread.library.base.BaseActivity;
import com.goread.library.models.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyEarningActivity extends BaseActivity {
    ImageView back_btn;
    RecyclerView orders_recyclerView;
    TextView tv_total_order, tv_total_revenues;
    OrdersAdapter ordersAdapter;
    List<Order> orderList;
    DatabaseReference databaseReference;
    int totalOrders = 0, totalMoney = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineViews();
        getData();

    }

    @Override
    public int defineLayout() {
        return R.layout.activity_my_earning;
    }

    private void defineViews() {
        back_btn = findViewById(R.id.btn_back);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        orderList = new ArrayList<>();

        orders_recyclerView = findViewById(R.id.recycler_MyEarnings);
        tv_total_order = findViewById(R.id.tv_total_order);
        tv_total_revenues = findViewById(R.id.tv_total_revenues);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getData() {
        orders_recyclerView.setHasFixedSize(true);
        orders_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ordersAdapter = new OrdersAdapter(this);


        try {

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
                                totalMoney += order.getTotalPrice();
                                totalOrders++;


                            }
                        }
                        tv_total_order.setText(String.valueOf(totalOrders));
                        tv_total_revenues.setText(String.valueOf(totalMoney));
                    }


                    Collections.reverse(orderList);
                    ordersAdapter.setOrderList(orderList);
                    ordersAdapter.setDisable(true);
                    orders_recyclerView.setAdapter(ordersAdapter);


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

}