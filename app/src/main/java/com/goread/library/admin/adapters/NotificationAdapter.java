package com.goread.library.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goread.library.R;
import com.goread.library.admin.activities.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;
    ArrayList<Notification> list;

    public NotificationAdapter(Context context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new MyViewHolder(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView NotificationTitle, NotificationBody;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            NotificationTitle = itemView.findViewById(R.id.title_tv_notification);
            NotificationBody = itemView.findViewById(R.id.body_tv_notification);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notification notification = list.get(position);
        System.out.println(notification.getNotificationBody());
        holder.NotificationTitle.setText(notification.getNotificationTitle());
        holder.NotificationBody.setText(notification.getNotificationBody());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}