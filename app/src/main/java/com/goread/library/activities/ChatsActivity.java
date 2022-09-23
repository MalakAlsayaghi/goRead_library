package com.goread.library.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.adapters.ChatsAdapter;
import com.goread.library.models.Chat;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {
    RecyclerView recycler_chats;
    List<Chat> chatsList;
    ChatsAdapter chatsAdapter;
    DatabaseReference databaseReference;
    private List<User> mUsers;
    String senderId;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (getIntent().getStringExtra("senderId") == null) {
            senderId = firebaseUser.getUid();
        } else {
            senderId = getIntent().getStringExtra("senderId");
        }
        getChats();


    }

    private void getChats() {

        recycler_chats = findViewById(R.id.recycler_chats);
        recycler_chats.setHasFixedSize(true);
        recycler_chats.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        chatsList = new ArrayList<>();

        //TODO change Id
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child("dwHPEklO03gbT50rAMWf3tBXPIy1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chatlist = snapshot.getValue(Chat.class);
                    chatsList.add(chatlist);
                    System.out.println("Found: " + chatlist.getLastMessage());
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void chatList() {
        mUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (Chat chatlist : chatsList) {
                        if (user.getId().equals(chatlist.getUserId())) {
                            mUsers.add(user);
                            System.out.println("Moha Found");
                        }
                    }
                }
                chatsAdapter = new ChatsAdapter(getApplicationContext(), mUsers, true, senderId);
                recycler_chats.setAdapter(chatsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}