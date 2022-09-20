package com.goread.library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.goread.library.R;
import com.goread.library.activities.AllBooksActivity;
import com.goread.library.activities.AllLibrariesActivity;
import com.goread.library.activities.AllQuotesActivity;
import com.goread.library.adapters.BookAdapter;
import com.goread.library.models.Book;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryHomeFragment extends Fragment implements View.OnClickListener {
    RecyclerView recycler_books;
    List<Book> bookList;
    BookAdapter bookAdapter;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FloatingActionButton fab_book, fab_quote;
    TextView tv_library;
    MaterialCardView cvNewOrder;
    MaterialCardView cvUploadBook;
    MaterialCardView cvUploadQuotes;
    MaterialCardView cvChat;
    TextView tv_number_oFsoldBook,tv_library_name;


    View v;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibraryHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryHomeFragment newInstance(String param1, String param2) {
        LibraryHomeFragment fragment = new LibraryHomeFragment();
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

        v = inflater.inflate(R.layout.fragment_home_library, container, false);
        defineViews();




        return v;
    }

    private void defineViews() {
    cvNewOrder = v.findViewById(R.id.card_new_order);
    cvNewOrder.setOnClickListener(this);

        cvUploadBook = v.findViewById(R.id.card_upload_book);
        cvUploadBook.setOnClickListener(this);

        cvUploadQuotes = v.findViewById(R.id.card_upload_quote);
        cvUploadQuotes.setOnClickListener(this);

        cvChat = v.findViewById(R.id.card_chat);
        cvChat.setOnClickListener(this);

        tv_number_oFsoldBook= v.findViewById(R.id.tv_number_oFsoldBook);
        tv_library_name = v.findViewById(R.id.tv_library_name);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_new_order:
                startActivity(new Intent(getContext(), AllLibrariesActivity.class));
                break;

            case R.id.card_upload_book:
                startActivity(new Intent(getContext(), AllBooksActivity.class));
                break;

            case R.id.card_upload_quote:
                startActivity(new Intent(getContext(), AllQuotesActivity.class));
                break;

            /*it should open the chat

            case R.id.card_chat:
                startActivity(new Intent(getContext(),.class));
                break;*/

            default:
                break;

        }
    }
}