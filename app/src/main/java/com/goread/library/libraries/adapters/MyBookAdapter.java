package com.goread.library.libraries.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.goread.library.R;
import com.goread.library.libraries.activities.AddBookActivity;
import com.goread.library.models.Book;
import com.suke.widget.SwitchButton;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.ImageViewHolder> {
    String book_id, library_id;

    Context mContext;
    List<Book> bookList;
    DatabaseReference databaseReference;

    Boolean isDisabled;
    int limit = 0;
    private AdapterCallback adapterCallback;


    public MyBookAdapter(Context context) {
        this.mContext = context;
    }

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.book_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Book bookCur = bookList.get(position);
        if (limit == 0) {
            library_id = bookCur.getLibrary_id();
            limit = 1;
        }


        Glide.with(mContext)
                .load(bookCur.getImg_url())
                .centerCrop()
                .into(holder.book_img);

        holder.name.setText(bookCur.getName());
        holder.price.setText(String.valueOf(bookCur.getPrice()) + "RY");
        holder.ratingBar.setRating((float) bookCur.getRating());

        isDisabled = bookCur.getDisabled();

        if (isDisabled) {
            holder.switchButton.setChecked(false);
        } else {
            holder.switchButton.setChecked(true);

        }

        holder.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Book book = bookList.get(position);
                String libId = book.getLibrary_id();
                String bookId = book.getId();

                if (isChecked) {
                    adapterCallback.changeStatus(libId, bookId, false);


                } else {

                    adapterCallback.changeStatus(libId, bookId, true);

                }


            }
        });


        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                library_id = bookCur.getLibrary_id();
                book_id = bookCur.getId();
                adapterCallback.deleteBook(book_id);

             }
        });
        holder.edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = bookList.get(position);

                book_id = bookCur.getId();

                Intent intent = new Intent(mContext, AddBookActivity.class);
                intent.putExtra("bookDetails", book);
                intent.putExtra("book_id", book_id);
                intent.putExtra("type", "edit");

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });


    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    public static interface AdapterCallback {
        void changeStatus(String libraryId, String bookId, Boolean status);

        void deleteBook(String bookId);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView name, brand, price;
        CardView cvProduct;
        ImageView book_img;
        RatingBar ratingBar;
        ImageButton delete_item, edit_item;
        com.suke.widget.SwitchButton switchButton;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.rating_bar);
            name = itemView.findViewById(R.id.tv_book_name);
            price = itemView.findViewById(R.id.tv_book_price);
            delete_item = itemView.findViewById(R.id.delete_item);
            edit_item = itemView.findViewById(R.id.edit_item);
            book_img = itemView.findViewById(R.id.book_img);
            switchButton = (com.suke.widget.SwitchButton) itemView.findViewById(R.id.switchButton);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");


        }
    }
}
