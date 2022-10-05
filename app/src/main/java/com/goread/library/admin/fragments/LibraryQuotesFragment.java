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
import com.goread.library.libraries.adapters.QuotesAdapter;
import com.goread.library.models.Quote;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryQuotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryQuotesFragment extends Fragment implements QuotesAdapter.AdapterCallback {

    RecyclerView allQuotes_recyclerView;
    QuotesAdapter quotesAdapter;
    List<Quote> quoteList;
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

    public LibraryQuotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryQuotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryQuotesFragment newInstance(String param1, String param2) {
        LibraryQuotesFragment fragment = new LibraryQuotesFragment();
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
        view = inflater.inflate(R.layout.fragment_library_qoutes, container, false);
        getLibrary();
        defineViews();
        getQuotes();
        return view;
    }

    private void defineViews() {
        allQuotes_recyclerView = view.findViewById(R.id.recycler_allQuotes);
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    private void getQuotes() {
        allQuotes_recyclerView.setHasFixedSize(true);
        allQuotes_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        quoteList = new ArrayList<>();
        quotesAdapter = new QuotesAdapter(getContext());
        quotesAdapter.setAdapterCallback(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Quotes").child(library.getId());

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
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void deleteBook(String quoteId) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
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

    public void getLibrary() {
        SharedPreferences mPrefs = getContext().getSharedPreferences("Library", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = mPrefs.getString("Key", "");
        library = gson.fromJson(json2, User.class);
    }

}