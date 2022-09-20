package com.goread.library.libraries.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.goread.library.R;

public class LibraryMainActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView cvNewOrders, cvBooks, cvQuotes, cvChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);
        defineViews();

    }

    private void defineViews() {
        cvBooks = findViewById(R.id.card_upload_book);
        cvNewOrders = findViewById(R.id.card_new_order);
        cvQuotes = findViewById(R.id.card_upload_quote);
        cvChat = findViewById(R.id.card_chat);

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
                break;

            case R.id.card_chat:
                break;

            case R.id.card_upload_quote:
                startActivity(new Intent(LibraryMainActivity.this, MyQuotesActivity.class));

                break;
        }
    }
}