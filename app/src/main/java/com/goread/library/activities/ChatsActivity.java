package com.goread.library.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.goread.library.base.BaseActivity;
import com.goread.library.models.Chat;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends BaseActivity {
    RecyclerView recycler_chats;
    ImageView back_btn;

    List<Chat> chatsList;
    ChatsAdapter chatsAdapter;
    DatabaseReference databaseReference;
    private List<User> mUsers;
    String senderId;
    FirebaseUser firebaseUser;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (getIntent().getStringExtra("senderId") == null) {
            senderId = firebaseUser.getUid();
        } else {
            senderId = getIntent().getStringExtra("senderId");
        }
        getChats();

        back_btn = findViewById(R.id.btn_back);
        linearLayout = findViewById(R.id.linearLayoutNo);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public int defineLayout() {
        return R.layout.activity_chats;
    }

    private void getChats() {

        recycler_chats = findViewById(R.id.recycler_chats);
        recycler_chats.setHasFixedSize(true);
        recycler_chats.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        chatsList = new ArrayList<>();

        //TODO change Id
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(senderId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                chatsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chatlist = snapshot.getValue(Chat.class);
                    chatsList.add(chatlist);
                    System.out.println("Found: " + chatlist.getLastMessage());
                }

                chatList();
            }
            else {
                linearLayout.setVisibility(View.VISIBLE);
                recycler_chats.setVisibility(View.GONE);
                }
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