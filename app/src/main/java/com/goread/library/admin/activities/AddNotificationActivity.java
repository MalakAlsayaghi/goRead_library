package com.goread.library.admin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DatabaseReference;
import com.goread.library.R;

public class AddNotificationActivity extends AppCompatActivity {
    EditText title;
    EditText body;
    Button buttonaAddNotification;
    DatabaseReference dbnotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);
        dbnotification = FirebaseDatabase.getInstance().getReference("Notification");
        title = (EditText) findViewById(R.id.titleNotification);
        body = (EditText) findViewById(R.id.bodyNotification);
        buttonaAddNotification = (Button) findViewById(R.id.btn_add_Notification);
        buttonaAddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });

    }

    private void addNotification() {
        String theTitle = title.getText().toString().trim();
        String theBody = body.getText().toString().trim();
        if (!TextUtils.isEmpty(theTitle)) {
            String id = dbnotification.push().getKey();
            Notification notification = new Notification(id, theTitle, theBody);
            dbnotification.child(id).setValue(notification);

            Toast.makeText(this, "notification added", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "you have to enter a title and a text", Toast.LENGTH_LONG).show();
    }

}
