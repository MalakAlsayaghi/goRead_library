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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ImageViewHolder> {
    String book_id, library_id;

    Context mContext;
    List<User> userList;
    List<User> tempUserList;
    DatabaseReference databaseReference;
    int limit = 0;
    private AdapterCallback adapterCallback;


    public UsersAdapter(Context context) {
        this.mContext = context;
    }

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.users_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        User user = userList.get(position);

        holder.username.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.phone.setText(user.getPhone());
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterCallback.phoneCall(user.getPhone());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterCallback.delete(user.getId());
            }
        });


    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        tempUserList = new ArrayList();
        tempUserList.addAll(this.userList);
        notifyDataSetChanged();
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        charText = charText.replace("أ", "ا");
        charText = charText.replace("إ", "ا");
        charText = charText.replace("آ", "ا");
        charText = charText.replace("ى", "ي");
        charText = charText.replace("ئ", "ي");
        charText = charText.replace("ؤ", "و");
        charText = charText.replace("ة", "ه");


        userList.clear();

        if (charText.length() == 0) {
            userList.addAll(tempUserList);

        } else {
            System.out.println("------------------");
            System.out.println("You locks for: " + charText);
            for (User obj : tempUserList) {
                if (obj.getName().toLowerCase().contains(charText)
                        || obj.getPhone().contains(charText)) {
                    System.out.println("Name is: " + obj.getName());

                    userList.add(obj);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static interface AdapterCallback {

        void phoneCall(String phone);

        void delete(String id);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView username, email, phone;
        ImageView btnCall, btnDelete;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tv_user_name);
            email = itemView.findViewById(R.id.tv_email);
            phone = itemView.findViewById(R.id.tv_phone);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
