package com.goread.library.admin.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.goread.library.admin.adapters.AdminOrdersAdapter;
import com.goread.library.admin.adapters.PreviewOrderAdapter;
import com.goread.library.models.MyCart;
import com.goread.library.models.Order;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllOrdersActivity extends AppCompatActivity implements AdminOrdersAdapter.AdapterCallback {
    ImageView back_btn;
    RecyclerView orders_recyclerView;
    AdminOrdersAdapter adminOrdersAdapter;
    List<User> userList;
    List<Order> orderList;
    DatabaseReference databaseReference;
    PreviewOrderAdapter previewOrderAdapter;
    ShimmerFrameLayout cart_shimmer;
    AlertDialog dialog;
    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
        defineViews();
        getData();

        SearchManager searchManager =
                (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) findViewById(R.id.search_bar);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setQueryHint("search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adminOrdersAdapter.filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adminOrdersAdapter != null)
                    adminOrdersAdapter.filter(newText);
                return true;
            }
        });

    }

    private void getData() {
        orders_recyclerView.setHasFixedSize(true);
        orders_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adminOrdersAdapter = new AdminOrdersAdapter(getApplicationContext());
        adminOrdersAdapter.setAdapterCallback(this);
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
                                    System.out.println("Your Order: " + user.getName());

                                }
                                System.out.println("Yours: " + postSnapshot2.getValue());

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
                                System.out.println("Your Order: " + order.getDescription());

                            }
                        }


                        Collections.reverse(orderList);
                        adminOrdersAdapter.setOrderList(orderList);
                        orders_recyclerView.setAdapter(adminOrdersAdapter);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        orders_recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(AllOrdersActivity.this, "NO admins found", Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        orders_recyclerView.setVisibility(View.VISIBLE);
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
        shimmerFrameLayout = findViewById(R.id.shimmer_view_library);
        shimmerFrameLayout.startShimmer();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                finish();
            }
        });
    }

    public void initDialog(String orderId, String userId) {
        RecyclerView cart_recyclerView;
        List<MyCart> myCartList;

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.cart_dialog, null);
        cart_recyclerView = view.findViewById(R.id.recycler_cart);
        cart_shimmer = view.findViewById(R.id.shimmer_view_container);
        cart_shimmer.startShimmer();
        cart_recyclerView.setHasFixedSize(true);
        cart_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        myCartList = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId).child(orderId).child("items");

        try {

            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    myCartList.clear();

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        MyCart cart = postSnapshot.getValue(MyCart.class);
                        myCartList.add(cart);

                    }
                    previewOrderAdapter = new PreviewOrderAdapter(getApplicationContext(), myCartList);
                    cart_recyclerView.setAdapter(previewOrderAdapter);

                    cart_shimmer.stopShimmer();
                    cart_shimmer.setVisibility(View.GONE);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
        builder.setView(view);
        dialog = builder.create();
        dialog.show();


    }

    @Override
    public void getOrderId(String orderId, String userId) {
        initDialog(orderId, userId);
    }

    @Override
    public void deleteByOrderId(String orderId, String userId) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Can't  recover this item!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId);

                        databaseReference.child(orderId).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            sDialog.setTitleText("Deleted!")
                                                    .setContentText("Your Order has been deleted!")
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