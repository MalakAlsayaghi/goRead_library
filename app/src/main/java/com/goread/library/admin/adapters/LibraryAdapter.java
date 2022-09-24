package com.goread.library.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.goread.library.R;
import com.goread.library.models.User;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ImageViewHolder> {
    String book_id, library_id;

    Context mContext;
    List<User> userList;
    DatabaseReference databaseReference;
    int limit = 0;
    private AdapterCallback adapterCallback;


    public LibraryAdapter(Context context) {
        this.mContext = context;
    }

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.driver_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        User user = userList.get(position);

        holder.username.setText(user.getName());
        holder.phone.setText(user.getPhone());
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterCallback.phoneCall(user.getPhone());
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterCallback.editData(user);
            }
        });


    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public static interface AdapterCallback {

        void phoneCall(String phone);

        void editData(User user);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView username, email, phone;
        ImageView btnCall, btnEdit;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tv_driver_name);
            phone = itemView.findViewById(R.id.tv_phone);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnEdit = itemView.findViewById(R.id.btnEdit);

        }
    }
}
