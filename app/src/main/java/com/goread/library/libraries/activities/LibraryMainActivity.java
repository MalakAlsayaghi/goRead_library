package com.goread.library.libraries.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.goread.library.R;
import com.goread.library.activities.ChatsActivity;
import com.goread.library.auth.LoginActivity;

public class LibraryMainActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView cvNewOrders, cvBooks, cvQuotes, cvChat;
    ImageButton btnLogout;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);
        defineViews();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(LibraryMainActivity.this,LoginActivity.class));
                finish();
            }
        });


    }

    private void defineViews() {
        cvBooks = findViewById(R.id.card_upload_book);
        cvNewOrders = findViewById(R.id.card_new_order);
        cvQuotes = findViewById(R.id.card_upload_quote);
        cvChat = findViewById(R.id.card_chat);
        btnLogout = findViewById(R.id.btnLLogOut);
        firebaseAuth = FirebaseAuth.getInstance();

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