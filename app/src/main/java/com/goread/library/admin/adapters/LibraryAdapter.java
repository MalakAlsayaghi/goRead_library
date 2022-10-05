package com.goread.library.admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.goread.library.R;
import com.goread.library.admin.activities.ShowLibraryActivity;
import com.goread.library.models.LibraryProfile;
import com.goread.library.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ImageViewHolder> {
    String book_id, library_id;

    Context mContext;
    List<User> userList;
    List<LibraryProfile> profileList;
    DatabaseReference databaseReference;
    List<User> tempLibraryList;

    int limit = 0;
    String address;
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
        View v = LayoutInflater.from(mContext).inflate(R.layout.library_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        User user = userList.get(position);

        if (user != null && profileList != null) {
            for (int j = 0; j < profileList.size(); j++) {
                if (user.getId().equals(profileList.get(j).getId())) {
                    address = profileList.get(j).getLocation();
                    holder.address.setText(address);
                    Glide.with(mContext)
                            .load(profileList.get(j).getImg_url())
                            .centerCrop()
                            .into(holder.library_img);
                }
            }
        }


        holder.username.setText(user.getName());
        holder.phone.setText(user.getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = userList.get(position);
                saveObjectToSharedPreference(user);
                Intent intent = new Intent(mContext, ShowLibraryActivity.class);
                intent.putExtra("library", user);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterCallback.editData(user, profileList.get(position));
            }
        });


    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        tempLibraryList = new ArrayList();
        tempLibraryList.addAll(this.userList);
        notifyDataSetChanged();
    }

    public void setProfileList(List<LibraryProfile> profileList) {
        this.profileList = profileList;
        notifyDataSetChanged();
    }

    public void saveObjectToSharedPreference(Object object) {
        SharedPreferences mPrefs = mContext.getSharedPreferences("Library", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString("Key", json);
        prefsEditor.commit();

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
            userList.addAll(tempLibraryList);

        } else {
            for (User obj : tempLibraryList) {
                if (obj.getName().contains(charText)
                        || obj.getPhone().contains(charText)) {

                    userList.add(obj);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static interface AdapterCallback {

        void phoneCall(String phone);

        void editData(User user, LibraryProfile profile);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView username, phone, address;
        ImageView btnCall, btnEdit;
        ImageView library_img;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tv_library_name);
            phone = itemView.findViewById(R.id.tv_phone);
            //   btnCall = itemView.findViewById(R.id.btnCall);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            address = itemView.findViewById(R.id.tv_address);
            library_img = itemView.findViewById(R.id.library_img);

        }
    }
}
