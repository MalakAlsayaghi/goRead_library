package com.goread.library.admin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.goread.library.R;
import com.goread.library.libraries.adapters.MyBookAdapter;
import com.goread.library.models.Book;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryBooksFragment extends Fragment implements MyBookAdapter.AdapterCallback {
    RecyclerView recycler_allBooks;
    List<Book> bookList;
    MyBookAdapter bookAdapter;
    DatabaseReference databaseReference;
    View view;
    User library;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibraryBooksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryBooksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryBooksFragment newInstance(String param1, String param2) {
        LibraryBooksFragment fragment = new LibraryBooksFragment();
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
        view = inflater.inflate(R.layout.fragment_library_books, container, false);
        defineViews();
        getLibrary();
        getBooks();
        return view;
    }

    private void defineViews() {
        recycler_allBooks = view.findViewById(R.id.recycler_allBooks);


    }

    public void getLibrary() {
        SharedPreferences mPrefs = getContext().getSharedPreferences("Library", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = mPrefs.getString("Key", "");
        library = gson.fromJson(json2, User.class);
    }

    private void getBooks() {
        recycler_allBooks.setHasFixedSize(true);
        recycler_allBooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        bookList = new ArrayList<>();
        bookAdapter = new MyBookAdapter(getContext());
        bookAdapter.setAdapterCallback(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Books").child(library.getId());

        try {


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        bookList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            Book book = postSnapshot.getValue(Book.class);

                            bookList.add(book);
                            System.out.println(getString(R.string.malak) + book.getName());
                        }

                        bookAdapter.setBookList(bookList);
                        recycler_allBooks.setAdapter(bookAdapter);


                        //  bookAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void changeStatus(String libraryId, String bookId, Boolean status) {
        Toast.makeText(getContext(), getString(R.string.status_changed), Toast.LENGTH_LONG).show();
        databaseReference.child(bookId).child("disabled").setValue(status);


    }

    @Override
    public void deleteBook(String bookId) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.are_you_sure))
                .setContentText(getString(R.string.Can_not_recover_this_item))
                .setConfirmText(getString(R.string.yes_delete_it))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        databaseReference.child(bookId).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            sDialog.setTitleText(getString(R.string.delete))
                                                    .setContentText(getString(R.string.our_product_has_been_deleted))
                                                    .setConfirmText(getString(R.string.ok))
                                                    .setConfirmClickListener(null)
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        }
                                    }
                                });

                    }
                }).show();

    }
}