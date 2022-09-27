package com.goread.library.libraries.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.activities.ChatsActivity;
import com.goread.library.auth.LoginActivity;
import com.goread.library.models.Order;

import java.util.ArrayList;
import java.util.List;

public class LibraryMainActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView cvNewOrders, cvBooks, cvQuotes, cvChat;
    ImageButton btnLogout;
    FirebaseAuth firebaseAuth;
    ArrayList<PieEntry> pieEntries;
    PieChart pieChart;
    DatabaseReference databaseReference;
    int myTotalOrders = 0, othersTotalOrders = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);
        defineViews();
       // initCharts();
drawChart();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(LibraryMainActivity.this, LoginActivity.class));
                finish();
            }
        });


    }


    private void drawChart() {
        PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieEntries = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();

        final int[] MY_COLORS = { Color.rgb(223, 153, 102),Color.rgb(82, 54, 32)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : MY_COLORS) colors.add(c);


        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pieEntries.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                        Order order = postSnapshot2.getValue(Order.class);
                        if (order.getLibraryId().equals(firebaseAuth.getCurrentUser().getUid())) {
                            orderList.add(order);
                            myTotalOrders += order.getTotalPrice();
                        } else {
                            othersTotalOrders += order.getTotalPrice();

                        }
                    }

                }

                ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
                yvalues.add(new PieEntry(myTotalOrders, "Mine", 0));
                yvalues.add(new PieEntry(othersTotalOrders, "Others", 1));

                PieDataSet dataSet = new PieDataSet(yvalues, "Election Result");
                PieData data = new PieData(dataSet);

                data.setValueFormatter(new PercentFormatter());
                pieChart.setData(data);
                Description description = new Description();
                description.setText("Pie Chart");
                pieChart.setDescription(description);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setTransparentCircleRadius(58f);
                pieChart.setHoleRadius(58f);
                pieChart.animateXY(500, 500);
                dataSet.setColors(MY_COLORS);
                data.setValueTextSize(13f);
                data.setValueTextColor(Color.WHITE);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void initCharts() {
        pieEntries = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();

        final int[] MY_COLORS = {Color.rgb(82, 54, 32), Color.rgb(223, 153, 102)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : MY_COLORS) colors.add(c);


        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pieEntries.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                        Order order = postSnapshot2.getValue(Order.class);
                        if (order.getLibraryId().equals(firebaseAuth.getCurrentUser().getUid())) {
                            orderList.add(order);
                            myTotalOrders += order.getTotalPrice();
                        } else {
                            othersTotalOrders += order.getTotalPrice();

                        }
                    }

                }


                PieEntry pieEntry = new PieEntry(0, "Mine: " + myTotalOrders + " RY");
                PieEntry pieEntry2 = new PieEntry(1, "Others " + othersTotalOrders + " RY");
                pieEntries.add(pieEntry);
                pieEntries.add(pieEntry2);


                PieDataSet pieDataSet = new PieDataSet(pieEntries, "Total Money");
                pieDataSet.setColors(colors);

                pieDataSet.setDrawValues(false);
                pieChart.setData(new PieData(pieDataSet));
                pieChart.animateXY(500, 500);
                pieChart.getDescription().setEnabled(false);
                pieChart.setDrawSlicesUnderHole(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void defineViews() {
        cvBooks = findViewById(R.id.card_upload_book);
        cvNewOrders = findViewById(R.id.card_new_order);
        cvQuotes = findViewById(R.id.card_upload_quote);
        cvChat = findViewById(R.id.card_chat);
        btnLogout = findViewById(R.id.btnLLogOut);
        pieChart = findViewById(R.id.pieChart);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        cvBooks.setOnClickListener(this);
        cvNewOrders.setOnClickListener(this);
        cvQuotes.setOnClickListener(this);
        cvChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_upload_book:
                startActivity(new Intent(LibraryMainActivity.this, MyBooksActivity.class));
                break;

            case R.id.card_new_order:
                startActivity(new Intent(LibraryMainActivity.this, LibraryOrdersActivity.class));
                break;

            case R.id.card_upload_quote:
                startActivity(new Intent(LibraryMainActivity.this, MyQuotesActivity.class));
                break;

            case R.id.card_chat:
                startActivity(new Intent(LibraryMainActivity.this, ChatsActivity.class));
                break;
        }
    }
}