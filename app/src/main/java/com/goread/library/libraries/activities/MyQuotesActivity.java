package com.goread.library.libraries.activities;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.admin.activities.AdminMainActivity;
import com.goread.library.libraries.adapters.QuotesAdapter;
import com.goread.library.models.Quote;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyQuotesActivity extends AppCompatActivity implements QuotesAdapter.AdapterCallback {
    ImageView back_btn;
    ImageView add_btn;
    RecyclerView allQuotes_recyclerView;
    QuotesAdapter quotesAdapter;
    List<Quote> quoteList;
    DatabaseReference databaseReference;
    String quote, bookName;
    AlertDialog dialog;

    FirebaseUser firebaseUser;
    String libraryId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quotes);
        defineViews();
        initDialog();
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MyQuotesActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                dialog.show();


            }
        });
        getQuotes();
    }

    private void getQuotes() {
        allQuotes_recyclerView.setHasFixedSize(true);
        allQuotes_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        quoteList = new ArrayList<>();
        quotesAdapter = new QuotesAdapter(getApplicationContext());
        quotesAdapter.setAdapterCallback(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Quotes").child(libraryId);

        try {


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        quoteList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            Quote quote = postSnapshot.getValue(Quote.class);
                            quoteList.add(quote);
                        }

                        quotesAdapter.setQuoteList(quoteList);
                        allQuotes_recyclerView.setAdapter(quotesAdapter);


                        quotesAdapter.notifyDataSetChanged();
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


    public void initDialog() {
        Button btn_add_quote;
        EditText et_quote, et_quote_bookName;

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_quote, null);

        btn_add_quote = view.findViewById(R.id.btn_add_quote);
        et_quote = view.findViewById(R.id.et_quote);
        et_quote_bookName = view.findViewById(R.id.et_quote_bookName);

        btn_add_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quote = et_quote.getText().toString();
                bookName = et_quote_bookName.getText().toString();
                String id = databaseReference.push().getKey();
                if (!quote.isEmpty() && !bookName.isEmpty()) {
                    Quote myQuote = new Quote(id, bookName, quote);
                    databaseReference.child(id).setValue(myQuote).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MyQuotesActivity.this, "Quote Added", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MyQuotesActivity.this, "Fill Data", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setView(view);
        dialog = builder.create();


    }

    private void defineViews() {
        back_btn = findViewById(R.id.btn_back);
        add_btn = findViewById(R.id.btn_add);
        allQuotes_recyclerView = findViewById(R.id.recycler_allQuotes);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        libraryId = firebaseUser.getUid();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                finish();
            }
        });

    }

    @Override
    public void deleteBook(String quoteId) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Can't  recover this item!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        databaseReference.child(quoteId).removeValue()
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