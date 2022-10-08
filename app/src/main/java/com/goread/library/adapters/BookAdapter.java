package com.goread.library.adapters;

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

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ImageViewHolder> {
    String book_id, library_id;

    Context mContext;
    List<Book> bookList;
    DatabaseReference databaseReference;


    public BookAdapter(Context context, List<Book> bookList) {
        this.mContext = context;
        this.bookList = bookList;
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


        Glide.with(mContext)
                .load(bookCur.getImg_url())
                .centerCrop()
                .into(holder.pro_img);

        holder.name.setText(bookCur.getName());
        holder.price.setText(String.valueOf(bookCur.getPrice()) + " " + mContext.getString(R.string.yr));
        holder.ratingBar.setRating((float) bookCur.getRating());
//        holder.brand.setText(bookCur.getBrand());

        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                library_id = bookCur.getLibrary_id();
                book_id = bookCur.getId();
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Can't  recover this item!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {


                                databaseReference.child(library_id).child(book_id).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    sDialog.setTitleText("Deleted!")
                                                            .setContentText("Your item has been deleted!")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(null)
                                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                                }
                                            }
                                        });


                            }
                        }).show();
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


     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id = bookCur.getId();
                name = bookCur.getName();
                description = bookCur.getDescription();
                rating = bookCur.getRating();
                img_url = bookCur.getImg_url();
                type = bookCur.getType();
                brand = bookCur.getBrand();
                price = bookCur.getPrice();

                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("desc", description);
                intent.putExtra("rating", rating);
                intent.putExtra("img_url", img_url);
                intent.putExtra("type", type);
                intent.putExtra("brand", brand);
                intent.putExtra("price", price);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);
            }
        });

*/
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView name, brand, price;
        CardView cvProduct;
        ImageView pro_img;
        RatingBar ratingBar;
        ImageButton delete_item, edit_item;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.rating_bar);
            delete_item = itemView.findViewById(R.id.delete_item);
            edit_item = itemView.findViewById(R.id.edit_item);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");


        }
    }
}
