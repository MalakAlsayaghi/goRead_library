package com.goread.library.libraries.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.admin.adapters.PreviewOrderAdapter;
import com.goread.library.libraries.adapters.MyOrdersAdapter;
import com.goread.library.models.Library;
import com.goread.library.models.MyCart;
import com.goread.library.models.Order;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOrdersFragment extends Fragment implements MyOrdersAdapter.AdapterCallback {


    RecyclerView orders_recyclerView;
    View view;
    MyOrdersAdapter myOrdersAdapter;
    List<Library> libraryList;
    List<User> userList;
    List<Order> orderList;
    DatabaseReference databaseReference;
    String libraryId;
    FirebaseAuth firebaseAuth;
    ShimmerFrameLayout cart_shimmer;
    AlertDialog dialog;
    PreviewOrderAdapter previewOrderAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewOrdersFragment newInstance(String param1, String param2) {
        NewOrdersFragment fragment = new NewOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_new_orders, container, false);
        defineViews();
        getData();
        return view;
    }

    private void defineViews() {
        orders_recyclerView = view.findViewById(R.id.recycler_newOrders);
        //  barChart = view.findViewById(R.id.barChart);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        libraryList = new ArrayList<>();
        orderList = new ArrayList<>();
        userList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        libraryId = firebaseAuth.getCurrentUser().getUid();

    }

    private void getData() {
        orders_recyclerView.setHasFixedSize(true);
        orders_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myOrdersAdapter = new MyOrdersAdapter(getContext());
        myOrdersAdapter.setAdapterCallback(this);


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
                                if (order.getLibraryId().equals(libraryId)) {
                                    if (!order.isLibraryAccepted() && !order.isRejected()) {
                                        orderList.add(order);
                                        System.out.println("Bro :" + order.getDescription());
                                        System.out.println("Bro :" + order.isRejected());
                                        System.out.println("Bro :" + order.isLibraryAccepted());
//
                                    }
                                }
                            }
                        }


                        Collections.reverse(orderList);
                        myOrdersAdapter.setOrderList(orderList);
                        orders_recyclerView.setAdapter(myOrdersAdapter);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Canceled", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    public void initDialog(String orderId, String userId) {
        RecyclerView cart_recyclerView;
        List<MyCart> myCartList;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.cart_dialog, null);
        cart_recyclerView = view.findViewById(R.id.recycler_cart);
        cart_shimmer = view.findViewById(R.id.shimmer_view_container);
        cart_shimmer.startShimmer();
        cart_recyclerView.setHasFixedSize(true);
        cart_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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
                    previewOrderAdapter = new PreviewOrderAdapter(getContext(), myCartList);
                    cart_recyclerView.setAdapter(previewOrderAdapter);

                    cart_shimmer.stopShimmer();
                    cart_shimmer.setVisibility(View.GONE);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Canceled", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
        builder.setView(view);
        dialog = builder.create();
        dialog.show();


    }


    @Override
    public void getOrderId(String orderId, String userId) {
        initDialog(orderId,userId);

    }
}
