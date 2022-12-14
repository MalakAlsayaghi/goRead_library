package com.goread.library.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.adapters.MessageAdapter;
import com.goread.library.base.BaseActivity;
import com.goread.library.models.ChatMessage;
import com.goread.library.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChattingActivity extends BaseActivity {
    List<ChatMessage> chatMessageList;
    MessageAdapter messageAdapter;
    RecyclerView chat_recyclerView;
    DatabaseReference databaseReference;
    EditText etMessage;
    ImageView btnSend,imgeback;
    Calendar calForDate;
    TextView textName;
    FirebaseAuth firebaseAuth;
    String userId, anotherId = "test", name = "None";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chat_recyclerView = findViewById(R.id.chatRecyclerView);
        etMessage = findViewById(R.id.inputMessage);
        btnSend = findViewById(R.id.btnSend);
        textName = findViewById(R.id.textName);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        anotherId = getIntent().getStringExtra("anotherId");
        name = getIntent().getStringExtra("name");
        textName.setText(name);

        imgeback = findViewById(R.id.imageBack);
        imgeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Constants.USER_ID = userId;

        calForDate = Calendar.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chatting").child("Messages");

        chat_recyclerView.setHasFixedSize(true);
        chat_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatMessageList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessageList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ChatMessage chatMessage = postSnapshot.getValue(ChatMessage.class);
                    if (chatMessage.getSenderId().equals(userId) || chatMessage.getReceiverId().equals(userId)) {
                        if (chatMessage.getSenderId().equals(userId) || chatMessage.getReceiverId().equals(userId)) {

                            chatMessageList.add(chatMessage);
                        }
                    }
                }

                messageAdapter = new MessageAdapter(ChattingActivity.this, chatMessageList);
                chat_recyclerView.setAdapter(messageAdapter);
                chat_recyclerView.scrollToPosition(chatMessageList.size() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString();
                SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm");
                String currentDate = currentDateFormat.format(calForDate.getTime());
                ChatMessage chatMessage = new ChatMessage(userId, "user2", message, currentDate);

                String messageId = databaseReference.push().getKey();
                databaseReference.child(messageId).setValue(chatMessage);
                etMessage.setText("");
            }
        });

    }

    @Override
    public int defineLayout() {
        return R.layout.activity_chatting;
    }
}