package com.goread.library.libraries.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.goread.library.R;
import com.goread.library.models.Quote;

import java.util.List;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ImageViewHolder> {
    String book_id, library_id;

    Context mContext;
    List<Quote> quoteList;
    DatabaseReference databaseReference;
    int limit = 0;
    private AdapterCallback adapterCallback;


    public QuotesAdapter(Context context) {
        this.mContext = context;
    }

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.quote_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Quote quoteCur = quoteList.get(position);

        holder.bookName.setText(quoteCur.getBookName());
        holder.content.setText(quoteCur.getContent());


        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_id = quoteCur.getId();
                adapterCallback.deleteBook(book_id);

            }
        });


    }

    public void setQuoteList(List<Quote> quoteList) {
        this.quoteList = quoteList;
        notifyDataSetChanged();
    }

    public static interface AdapterCallback {

        void deleteBook(String quoteId);

    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView content, bookName;
        ImageView delete_item;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.tv_book_name);
            content = itemView.findViewById(R.id.quotes_content);
            delete_item = itemView.findViewById(R.id.btnDelete);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Quotes");


        }
    }
}
