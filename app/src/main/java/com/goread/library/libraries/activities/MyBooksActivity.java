package com.goread.library.libraries.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.goread.library.libraries.adapters.MyBookAdapter;
import com.goread.library.models.Book;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyBooksActivity extends AppCompatActivity implements MyBookAdapter.AdapterCallback {
    RecyclerView recycler_allBooks;
    List<Book> bookList;
    MyBookAdapter bookAdapter;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String libraryId;
    ImageView btnAdd;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_books);
        defineViews();
        getBooks();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
                intent.putExtra("type", "add");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void defineViews() {
        recycler_allBooks = findViewById(R.id.recycler_allBooks);
        btnAdd = findViewById(R.id.btn_add);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        libraryId = firebaseUser.getUid();
        back_btn = findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LibraryMainActivity.class));
                finish();
            }
        });
    }

    private void getBooks() {
        recycler_allBooks.setHasFixedSize(true);
        recycler_allBooks.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        bookList = new ArrayList<>();
        bookAdapter = new MyBookAdapter(getApplicationContext());
        bookAdapter.setAdapterCallback(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Books").child(libraryId);

        try {


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        bookList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            Book book = postSnapshot.getValue(Book.class);

                            bookList.add(book);
                            System.out.println("Moha :" + book.getName());
                        }

                        bookAdapter.setBookList(bookList);
                        recycler_allBooks.setAdapter(bookAdapter);


                        //  bookAdapter.notifyDataSetChanged();
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

    @Override
    public void changeStatus(String libraryId, String bookId, Boolean status) {

        Toast.makeText(getApplicationContext(), "Status Changed", Toast.LENGTH_LONG).show();
        databaseReference.child(bookId).child("disabled").setValue(status);


    }

    @Override
    public void deleteBook(String bookId) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Can't  recover this item!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        databaseReference.child(bookId).removeValue()
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