package com.goread.library.admin.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.goread.library.R;
import com.goread.library.base.BaseActivity;

public class AddNotificationActivity extends BaseActivity {
    EditText title;
    EditText body;
    Button buttonaAddNotification;
    DatabaseReference dbnotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public int defineLayout() {
        return R.layout.dialog_add_notification;
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
